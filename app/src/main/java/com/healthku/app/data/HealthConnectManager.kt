package com.healthku.app.data

import android.content.Context
import android.content.Intent
import android.net.Uri
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

    fun getClient(): HealthConnectClient? {
        val status = HealthConnectClient.getSdkStatus(context)
        return if (status == HealthConnectClient.SDK_AVAILABLE) {
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
        val client = getClient() ?: return false
        return try {
            val granted = client.permissionController.getGrantedPermissions()
            granted.containsAll(permissions)
        } catch (e: Exception) {
            false
        }
    }

    fun isHealthConnectAvailable(): Boolean {
        return HealthConnectClient.getSdkStatus(context) == HealthConnectClient.SDK_AVAILABLE
    }

    fun openHealthConnectSettings(ctx: Context) {
        val actionManage = "android.health.connect.action.MANAGE_HEALTH_PERMISSIONS"
        val intentWithPkg = Intent(actionManage).apply {
            putExtra(Intent.EXTRA_PACKAGE_NAME, ctx.packageName)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        try {
            ctx.startActivity(intentWithPkg)
            return
        } catch (e: Exception) {
            // Fallback 1: Standard Health Connect Settings
            try {
                val settingsIntent = Intent(HealthConnectClient.ACTION_HEALTH_CONNECT_SETTINGS).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                ctx.startActivity(settingsIntent)
                return
            } catch (e2: Exception) {
                // Fallback 2: Google Play Store
                try {
                    val playStoreIntent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.apps.healthdata")).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                    ctx.startActivity(playStoreIntent)
                } catch (e3: Exception) {
                    val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.healthdata")).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                    ctx.startActivity(webIntent)
                }
            }
        }
    }

    suspend fun readRealHealthData(): DailyHealthSummary {
        val client = getClient()
        val isAvailable = isHealthConnectAvailable()

        if (client == null || !isAvailable) {
            return DailyHealthSummary(
                isHealthConnectAvailable = false,
                hasHealthConnectPermission = false,
                lastSyncTime = "Health Connect Belum Siap di HP"
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

            // 1. Steps
            val stepsResponse = client.readRecords(
                ReadRecordsRequest(recordType = StepsRecord::class, timeRangeFilter = todayRange)
            )
            val realSteps = stepsResponse.records.sumOf { it.count }.toInt()

            // 2. Heart Rate
            val hrResponse = client.readRecords(
                ReadRecordsRequest(recordType = HeartRateRecord::class, timeRangeFilter = todayRange)
            )
            val allHrSamples = hrResponse.records.flatMap { it.samples }
            val latestHr = allHrSamples.lastOrNull()?.beatsPerMinute?.toInt()
            val restingHr = if (allHrSamples.isNotEmpty()) {
                allHrSamples.minOf { it.beatsPerMinute }.toInt()
            } else null

            val hrHistory = allHrSamples.takeLast(12).map { it.beatsPerMinute.toFloat() }

            // 3. Blood Pressure
            val bpResponse = client.readRecords(
                ReadRecordsRequest(recordType = BloodPressureRecord::class, timeRangeFilter = todayRange)
            )
            val latestBp = bpResponse.records.lastOrNull()
            val sys = latestBp?.systolic?.inMillimetersOfMercury?.toInt()
            val dia = latestBp?.diastolic?.inMillimetersOfMercury?.toInt()

            // 4. Oxygen (SpO2)
            val o2Response = client.readRecords(
                ReadRecordsRequest(recordType = OxygenSaturationRecord::class, timeRangeFilter = todayRange)
            )
            val latestO2 = o2Response.records.lastOrNull()?.percentage?.value?.toInt()

            // 5. Sleep
            val pastWeekRange = TimeRangeFilter.between(now.minusSeconds(7 * 86400), now)
            val sleepResponse = client.readRecords(
                ReadRecordsRequest(recordType = SleepSessionRecord::class, timeRangeFilter = pastWeekRange)
            )
            val todaySleep = sleepResponse.records.filter {
                it.endTime.isAfter(now.minusSeconds(86400))
            }.maxByOrNull { it.endTime }

            val sleepHours = todaySleep?.let {
                val sec = it.endTime.epochSecond - it.startTime.epochSecond
                (sec / 3600.0f)
            } ?: 0f

            // Sleep History
            val dateFormatter = DateTimeFormatter.ofPattern("MMM dd", Locale.ENGLISH)
            val dayFormatter = DateTimeFormatter.ofPattern("dd", Locale.ENGLISH)
            val sleepHistoryList = sleepResponse.records
                .sortedByDescending { it.startTime }
                .take(6)
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
                        suggestionCount = if (hrs < 6.0f) 5 else 0
                    )
                }

            // 6. Calories
            val calResponse = client.readRecords(
                ReadRecordsRequest(recordType = TotalCaloriesBurnedRecord::class, timeRangeFilter = todayRange)
            )
            val realCalories = calResponse.records.sumOf { it.energy.inKilocalories }.toInt()

            // 7. Distance
            val distResponse = client.readRecords(
                ReadRecordsRequest(recordType = DistanceRecord::class, timeRangeFilter = todayRange)
            )
            val totalDistanceMeters = distResponse.records.sumOf { it.distance.inMeters }
            val realDistanceKm = (totalDistanceMeters / 1000.0f).toFloat()

            // Calculate Readiness Score purely from REAL biometric inputs
            val calculatedScore = if (sleepHours > 0f || latestHr != null || realSteps > 0) {
                var score = 70
                if (sleepHours >= 7.5f) score += 18 else if (sleepHours in 6.0f..7.4f) score += 10 else if (sleepHours > 0f) score -= 14
                if (restingHr != null) {
                    if (restingHr in 52..68) score += 10 else if (restingHr > 78) score -= 12
                }
                score.coerceIn(38, 98)
            } else {
                null
            }

            val statusText = when {
                calculatedScore == null -> "Menunggu Data"
                calculatedScore >= 85 -> "Peak Recovery"
                calculatedScore >= 65 -> "Optimal"
                else -> "Needs Rest"
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
                deepSleepHours = if (sleepHours > 0f) (sleepHours * 0.28f) else 0f,
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
                lastSyncTime = "Error baca data: ${e.localizedMessage}"
            )
        }
    }
}
