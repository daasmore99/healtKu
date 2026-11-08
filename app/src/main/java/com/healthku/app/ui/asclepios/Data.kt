package com.healthku.app.ui.asclepios

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.healthku.app.data.DailyHealthSummary
import java.time.LocalDate

data class Metric(
    val name: String,
    val value: String,
    val unit: String,
    val status: String,
    val iconType: HealthIconType,
    val tint: Color,
    val bg: Color,
    val url: String,
    val series: List<Float>,
    val route: String = "detail/$name",
)

data class Reading(val label: String, val sys: Int, val dia: Int)

data class SleepLog(val date: LocalDate, val hours: Float) {
    val tag get() = if (hours < 3f) "Insomniac" else if (hours < 6.5f) "Low" else "Rem"
    val note get() = when (tag) { "Insomniac" -> "78 AI Suggestions"; "Rem" -> "8 AI Suggestions"; else -> "No Suggestion" }
}

object Store {
    val metrics = mutableStateListOf(
        Metric(
            name = "Heart Rate",
            value = "70",
            unit = "bpm",
            status = "Normal resting range",
            iconType = HealthIconType.HEART_RATE,
            tint = Color(0xFF4B8E9D),
            bg = Color(0xFFE0EFF2),
            url = "https://www.who.int/health-topics/cardiovascular-diseases",
            series = listOf(68f, 72f, 70f, 75f, 69f, 71f, 70f)
        ),
        Metric(
            name = "Blood Pressure",
            value = "121/80",
            unit = "mmHg",
            status = "Slightly elevated",
            iconType = HealthIconType.BLOOD_PRESSURE,
            tint = Color(0xFFC85B7A),
            bg = Color(0xFFF7E4EA),
            url = "https://www.heart.org/en/health-topics/high-blood-pressure",
            series = listOf(118f, 122f, 120f, 125f, 119f, 121f, 121f),
            route = "bp"
        ),
        Metric(
            name = "Oxygen",
            value = "98",
            unit = "%",
            status = "Healthy SpO2",
            iconType = HealthIconType.OXYGEN,
            tint = Color(0xFFC85B7A),
            bg = Color(0xFFF7E4EA),
            url = "https://www.who.int/health-topics/oxygen",
            series = listOf(97f, 98f, 98f, 96f, 98f, 99f, 98f)
        ),
        Metric(
            name = "Cholesterol",
            value = "172",
            unit = "mg/dL",
            status = "Desirable (< 200)",
            iconType = HealthIconType.CHOLESTEROL,
            tint = Color(0xFF4B8E9D),
            bg = Color(0xFFE0EFF2),
            url = "https://www.cdc.gov/cholesterol/about/index.html",
            series = listOf(180f, 176f, 174f, 175f, 173f, 172f, 172f)
        ),
        Metric(
            name = "Calories Burned",
            value = "2000",
            unit = "kcal",
            status = "On target for today",
            iconType = HealthIconType.CALORIES,
            tint = Color(0xFFE27B30),
            bg = Color(0xFFFDF0E2),
            url = "https://www.cdc.gov/healthy-weight-growth/physical-activity/index.html",
            series = listOf(1800f, 2100f, 1950f, 2200f, 1700f, 2050f, 2000f)
        ),
        Metric(
            name = "Steps Walked",
            value = "8420",
            unit = "steps",
            status = "84% of daily goal",
            iconType = HealthIconType.STEPS,
            tint = Color(0xFF4B8E9D),
            bg = Color(0xFFE0EFF2),
            url = "https://www.who.int/news-room/fact-sheets/detail/physical-activity",
            series = listOf(6500f, 9200f, 8000f, 10100f, 7400f, 8800f, 8420f)
        ),
        Metric(
            name = "Water Intake",
            value = "1.8",
            unit = "L",
            status = "Drink 0.7 L more",
            iconType = HealthIconType.WATER,
            tint = Color(0xFF3B82F6),
            bg = Color(0xFFDFE9F6),
            url = "https://www.cdc.gov/healthy-weight-growth/water-consumption/index.html",
            series = listOf(1.5f, 2f, 1.8f, 2.2f, 1.6f, 1.9f, 1.8f)
        )
    )
    fun metric(name: String) = metrics.first { it.name == name }

    val readings = mutableStateListOf(Reading("Yesterday", 119, 79), Reading("2 days ago", 124, 82), Reading("3 days ago", 118, 78))
    val sleepLogs = mutableStateListOf(
        SleepLog(LocalDate.of(2025, 10, 17), 5.2f), SleepLog(LocalDate.of(2025, 10, 16), 8.2f), SleepLog(LocalDate.of(2025, 10, 15), 2.2f),
        SleepLog(LocalDate.of(2025, 10, 14), 6.4f), SleepLog(LocalDate.of(2025, 10, 13), 7.8f), SleepLog(LocalDate.of(2025, 10, 12), 3.1f),
    )
    var notifications by mutableStateOf(true)
    var userName by mutableStateOf("Michelle")
    var query by mutableStateOf("")

    var hasHealthConnect by mutableStateOf(false)
    var healthConnectStatus by mutableStateOf("Menunggu izin Health Connect")

    fun updateWithRealData(summary: DailyHealthSummary) {
        hasHealthConnect = summary.hasHealthConnectPermission
        healthConnectStatus = summary.lastSyncTime

        summary.heartRateBpm?.let { hr ->
            val idx = metrics.indexOfFirst { it.name == "Heart Rate" }
            if (idx >= 0) {
                metrics[idx] = metrics[idx].copy(
                    value = "$hr",
                    status = "Tersinkron Health Connect"
                )
            }
        }

        if (summary.bloodPressureSys != null && summary.bloodPressureDia != null) {
            val idx = metrics.indexOfFirst { it.name == "Blood Pressure" }
            if (idx >= 0) {
                metrics[idx] = metrics[idx].copy(
                    value = "${summary.bloodPressureSys}/${summary.bloodPressureDia}",
                    status = "Tersinkron Health Connect"
                )
            }
        }

        summary.oxygenSaturationPercent?.let { o2 ->
            val idx = metrics.indexOfFirst { it.name == "Oxygen" }
            if (idx >= 0) {
                metrics[idx] = metrics[idx].copy(
                    value = "$o2",
                    status = "Tersinkron Health Connect"
                )
            }
        }

        if (summary.caloriesBurned > 0) {
            val idx = metrics.indexOfFirst { it.name == "Calories Burned" }
            if (idx >= 0) {
                metrics[idx] = metrics[idx].copy(
                    value = "${summary.caloriesBurned}",
                    status = "Aktif terbakar hari ini"
                )
            }
        }

        if (summary.steps > 0) {
            val idx = metrics.indexOfFirst { it.name == "Steps Walked" }
            if (idx >= 0) {
                metrics[idx] = metrics[idx].copy(
                    value = "${summary.steps}",
                    status = "${summary.distanceKm} km jarak tempuh"
                )
            }
        }

        if (summary.sleepHistory.isNotEmpty()) {
            sleepLogs.clear()
            summary.sleepHistory.forEach { entry ->
                val d = try {
                    val dayNum = entry.dayNumber.toIntOrNull() ?: 17
                    LocalDate.of(2025, 10, dayNum.coerceIn(1, 31))
                } catch (e: Exception) {
                    LocalDate.now()
                }
                sleepLogs.add(SleepLog(d, entry.durationHours))
            }
        }
    }
}
