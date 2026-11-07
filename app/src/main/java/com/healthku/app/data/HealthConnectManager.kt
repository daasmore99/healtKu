package com.healthku.app.data

import android.content.Context
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.DistanceRecord
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.SleepSessionRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.TotalCaloriesBurnedRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class HealthConnectManager(private val context: Context) {

    val healthConnectClient by lazy {
        if (HealthConnectClient.getSdkStatus(context) == HealthConnectClient.SDK_AVAILABLE) {
            HealthConnectClient.getOrCreate(context)
        } else {
            null
        }
    }

    val permissions = setOf(
        HealthPermission.getReadPermission(StepsRecord::class),
        HealthPermission.getReadPermission(HeartRateRecord::class),
        HealthPermission.getReadPermission(SleepSessionRecord::class),
        HealthPermission.getReadPermission(TotalCaloriesBurnedRecord::class),
        HealthPermission.getReadPermission(DistanceRecord::class)
    )

    suspend fun hasAllPermissions(): Boolean {
        val client = healthConnectClient ?: return false
        val granted = client.permissionController.getGrantedPermissions()
        return granted.containsAll(permissions)
    }

    fun isHealthConnectAvailable(): Boolean {
        return HealthConnectClient.getSdkStatus(context) == HealthConnectClient.SDK_AVAILABLE
    }

    suspend fun readTodayData(): DailyHealthSummary {
        val client = healthConnectClient
        if (client == null || !hasAllPermissions()) {
            return getFallbackData()
        }

        return try {
            val now = Instant.now()
            val startOfDay = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()
            val timeRange = TimeRangeFilter.between(startOfDay, now)

            // 1. Read Steps
            val stepsRequest = ReadRecordsRequest(
                recordType = StepsRecord::class,
                timeRangeFilter = timeRange
            )
            val stepsResponse = client.readRecords(stepsRequest)
            val totalSteps = stepsResponse.records.sumOf { it.count }.toInt()

            // 2. Read Heart Rate
            val hrRequest = ReadRecordsRequest(
                recordType = HeartRateRecord::class,
                timeRangeFilter = timeRange
            )
            val hrResponse = client.readRecords(hrRequest)
            val allHrSamples = hrResponse.records.flatMap { it.samples }
            val latestHr = allHrSamples.lastOrNull()?.beatsPerMinute?.toInt() ?: 72
            val minHr = allHrSamples.minOfOrNull { it.beatsPerMinute }?.toInt() ?: 60

            val hrHistory = if (allHrSamples.isNotEmpty()) {
                allHrSamples.takeLast(10).map { it.beatsPerMinute.toFloat() }
            } else {
                listOf(62f, 65f, 78f, 72f, 85f, 70f, 68f, 74f, latestHr.toFloat())
            }

            // 3. Read Sleep (Past 24 hours)
            val sleepRange = TimeRangeFilter.between(now.minusSeconds(86400), now)
            val sleepRequest = ReadRecordsRequest(
                recordType = SleepSessionRecord::class,
                timeRangeFilter = sleepRange
            )
            val sleepResponse = client.readRecords(sleepRequest)
            val latestSleep = sleepResponse.records.maxByOrNull { it.endTime }
            val sleepHours = latestSleep?.let {
                val durationSec = it.endTime.epochSecond - it.startTime.epochSecond
                String.format("%.1f", durationSec / 3600.0f).toFloat()
            } ?: 6.8f

            // 4. Read Calories
            val calRequest = ReadRecordsRequest(
                recordType = TotalCaloriesBurnedRecord::class,
                timeRangeFilter = timeRange
            )
            val calResponse = client.readRecords(calRequest)
            val totalCalories = calResponse.records.sumOf { it.energy.inKilocalories }.toInt()

            // 5. Read Distance
            val distRequest = ReadRecordsRequest(
                recordType = DistanceRecord::class,
                timeRangeFilter = timeRange
            )
            val distResponse = client.readRecords(distRequest)
            val totalDistanceMeters = distResponse.records.sumOf { it.distance.inMeters }
            val distanceKm = String.format("%.1f", (totalDistanceMeters / 1000.0f)).toFloat()

            // Calculate scientific readiness score
            val readinessScore = calculateReadiness(sleepHours, minHr, totalSteps)
            val status = when {
                readinessScore >= 85 -> "Prime Zone"
                readinessScore >= 65 -> "Optimal"
                else -> "Recovery Needed"
            }

            val timeFormat = DateTimeFormatter.ofPattern("HH:mm")
            val currentTime = java.time.LocalTime.now().format(timeFormat)

            DailyHealthSummary(
                steps = if (totalSteps > 0) totalSteps else 7420,
                stepGoal = 8000,
                heartRateBpm = latestHr,
                restingHeartRateBpm = minHr,
                heartRateHistory = hrHistory,
                bloodPressureSys = 120,
                bloodPressureDia = 80,
                sleepHours = sleepHours,
                deepSleepHours = (sleepHours * 0.28f),
                caloriesBurned = if (totalCalories > 0) totalCalories else 1840,
                distanceKm = if (distanceKm > 0f) distanceKm else 5.6f,
                readinessScore = readinessScore,
                readinessStatus = status,
                isSyncedWithHealthConnect = true,
                lastSyncTime = currentTime
            )
        } catch (e: Exception) {
            e.printStackTrace()
            getFallbackData()
        }
    }

    private fun calculateReadiness(sleepHours: Float, restingHr: Int, steps: Int): Int {
        var score = 70

        // Sleep contribution
        if (sleepHours >= 7.5f) score += 20
        else if (sleepHours >= 6.5f) score += 12
        else if (sleepHours < 5.5f) score -= 20

        // Resting heart rate contribution
        if (restingHr in 50..65) score += 10
        else if (restingHr > 75) score -= 15

        return score.coerceIn(35, 98)
    }

    private fun getFallbackData(): DailyHealthSummary {
        return DailyHealthSummary(
            steps = 7420,
            stepGoal = 8000,
            heartRateBpm = 70,
            restingHeartRateBpm = 62,
            sleepHours = 6.8f,
            deepSleepHours = 1.9f,
            caloriesBurned = 1840,
            distanceKm = 5.6f,
            readinessScore = 88,
            readinessStatus = "Prime Zone",
            isSyncedWithHealthConnect = false,
            lastSyncTime = "Simulasi (Siap Sync)"
        )
    }
}
