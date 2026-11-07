package com.healthku.app.data

import android.content.Context
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.BloodPressureRecord
import androidx.health.connect.client.records.DistanceRecord
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.OxygenSaturationRecord
import androidx.health.connect.client.records.SleepSessionRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.TotalCaloriesBurnedRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

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
        HealthPermission.getReadPermission(BloodPressureRecord::class),
        HealthPermission.getReadPermission(OxygenSaturationRecord::class),
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

    suspend fun readRealHealthData(): DailyHealthSummary {
        val client = healthConnectClient
        val isAvailable = isHealthConnectAvailable()

        if (client == null || !isAvailable) {
            return DailyHealthSummary(
                isHealthConnectAvailable = false,
                hasHealthConnectPermission = false,
                lastSyncTime = "Health Connect Belum Terinstal di HP"
            )
        }

        val hasPerms = hasAllPermissions()
        if (!hasPerms) {
            return DailyHealthSummary(
                isHealthConnectAvailable = true,
                hasHealthConnectPermission = false,
                lastSyncTime = "Perlu Izin Akses Health Connect"
            )
        }

        return try {
            val now = Instant.now()
            val startOfDay = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()
            val todayRange = TimeRangeFilter.between(startOfDay, now)

            // 1. REAL Steps
            val stepsResponse = client.readRecords(
                ReadRecordsRequest(recordType = StepsRecord::class, timeRangeFilter = todayRange)
            )
            val realSteps = stepsResponse.records.sumOf { it.count }.toInt()

            // 2. REAL Heart Rate
            val hrResponse = client.readRecords(
                ReadRecordsRequest(recordType = HeartRateRecord::class, timeRangeFilter = todayRange)
            )
            val allHrSamples = hrResponse.records.flatMap { it.samples }
            val latestHr = allHrSamples.lastOrNull()?.beatsPerMinute?.toInt()
            val restingHr = if (allHrSamples.isNotEmpty()) {
                allHrSamples.minOf { it.beatsPerMinute }.toInt()
            } else null

            val hrHistory = allHrSamples.takeLast(10).map { it.beatsPerMinute.toFloat() }

            // 3. REAL Blood Pressure
            val bpResponse = client.readRecords(
                ReadRecordsRequest(recordType = BloodPressureRecord::class, timeRangeFilter = todayRange)
            )
            val latestBp = bpResponse.records.lastOrNull()
            val sys = latestBp?.systolic?.inMillimetersOfMercury?.toInt()
            val dia = latestBp?.diastolic?.inMillimetersOfMercury?.toInt()

            // 4. REAL Oxygen Saturation (SpO2)
            val o2Response = client.readRecords(
                ReadRecordsRequest(recordType = OxygenSaturationRecord::class, timeRangeFilter = todayRange)
            )
            val latestO2 = o2Response.records.lastOrNull()?.percentage?.value?.toInt()

            // 5. REAL Sleep (Last 7 days for history and last 24h for today)
            val pastWeekRange = TimeRangeFilter.between(now.minusSeconds(7 * 86400), now)
            val sleepResponse = client.readRecords(
                ReadRecordsRequest(recordType = SleepSessionRecord::class, timeRangeFilter = pastWeekRange)
            )

            // Today's latest sleep
            val todaySleep = sleepResponse.records.filter {
                it.endTime.isAfter(now.minusSeconds(86400))
            }.maxByOrNull { it.endTime }

            val sleepHours = todaySleep?.let {
                val sec = it.endTime.epochSecond - it.startTime.epochSecond
                (sec / 3600.0f)
            } ?: 0f

            // Real Sleep History List
            val dateFormatter = DateTimeFormatter.ofPattern("MMM dd", Locale.ENGLISH)
            val dayFormatter = DateTimeFormatter.ofPattern("dd", Locale.ENGLISH)
            val sleepHistoryList = sleepResponse.records
                .sortedByDescending { it.startTime }
                .take(5)
                .map { record ->
                    val sec = record.endTime.epochSecond - record.startTime.epochSecond
                    val hrs = String.format(Locale.US, "%.1f", sec / 3600.0f).toFloat()
                    val zdt = record.startTime.atZone(ZoneId.systemDefault())

                    val tagType = when {
                        hrs >= 7.0f -> SleepTagType.OPTIMAL
                        hrs in 5.0f..6.9f -> SleepTagType.LOW
                        hrs in 4.0f..4.9f -> SleepTagType.REM
                        else -> SleepTagType.WARNING
                    }
                    val tagText = when (tagType) {
                        SleepTagType.OPTIMAL -> "Optimal"
                        SleepTagType.LOW -> "Low"
                        SleepTagType.REM -> "Rem"
                        SleepTagType.WARNING -> "Insomniac"
                    }

                    SleepLogEntry(
                        dateText = zdt.format(dateFormatter),
                        dayNumber = zdt.format(dayFormatter),
                        durationHours = hrs,
                        tagText = tagText,
                        tagType = tagType,
                        suggestionCount = if (hrs < 6.0f) 8 else 0
                    )
                }

            // 6. REAL Calories Burned
            val calResponse = client.readRecords(
                ReadRecordsRequest(recordType = TotalCaloriesBurnedRecord::class, timeRangeFilter = todayRange)
            )
            val realCalories = calResponse.records.sumOf { it.energy.inKilocalories }.toInt()

            // 7. REAL Distance
            val distResponse = client.readRecords(
                ReadRecordsRequest(recordType = DistanceRecord::class, timeRangeFilter = todayRange)
            )
            val totalDistanceMeters = distResponse.records.sumOf { it.distance.inMeters }
            val realDistanceKm = (totalDistanceMeters / 1000.0f).toFloat()

            // Calculate Readiness Score based on REAL data
            val calculatedScore = if (sleepHours > 0f || latestHr != null || realSteps > 0) {
                var score = 70
                if (sleepHours >= 7.0f) score += 15 else if (sleepHours in 5.0f..6.9f) score += 5 else if (sleepHours > 0f) score -= 15
                if (restingHr != null) {
                    if (restingHr in 55..68) score += 10 else if (restingHr > 75) score -= 10
                }
                score.coerceIn(40, 96)
            } else {
                null
            }

            val statusText = when {
                calculatedScore == null -> "Menunggu Data"
                calculatedScore >= 85 -> "Prime Zone"
                calculatedScore >= 65 -> "Normal"
                else -> "Recovery Needed"
            }

            val timeFormat = DateTimeFormatter.ofPattern("HH:mm")
            val currentTime = java.time.LocalTime.now().format(timeFormat)

            DailyHealthSummary(
                steps = realSteps,
                stepGoal = 8000,
                heartRateBpm = latestHr,
                restingHeartRateBpm = restingHr,
                heartRateHistory = hrHistory,
                bloodPressureSys = sys,
                bloodPressureDia = dia,
                oxygenSaturationPercent = latestO2,
                sleepHours = sleepHours,
                deepSleepHours = if (sleepHours > 0f) (sleepHours * 0.25f) else 0f,
                sleepIrregularities = if (sleepHours in 0.1f..5.5f) 1 else 0,
                caloriesBurned = realCalories,
                distanceKm = realDistanceKm,
                readinessScore = calculatedScore,
                readinessStatus = statusText,
                hasHealthConnectPermission = true,
                isHealthConnectAvailable = true,
                lastSyncTime = "Tersinkron pukul $currentTime",
                sleepHistory = sleepHistoryList
            )
        } catch (e: Exception) {
            e.printStackTrace()
            DailyHealthSummary(
                isHealthConnectAvailable = true,
                hasHealthConnectPermission = true,
                lastSyncTime = "Gagal membaca: ${e.localizedMessage}"
            )
        }
    }
}
