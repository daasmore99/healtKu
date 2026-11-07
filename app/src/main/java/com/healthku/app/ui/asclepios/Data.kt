package com.healthku.app.ui.asclepios

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.*
import com.healthku.app.data.DailyHealthSummary
import java.time.LocalDate

data class Metric(
    val name: String, val value: String, val unit: String, val status: String,
    val icon: ImageVector, val tint: Color, val bg: Color, val url: String,
    val series: List<Float>, val route: String = "detail/$name",
)

data class Reading(val label: String, val sys: Int, val dia: Int)

data class SleepLog(val date: LocalDate, val hours: Float) {
    val tag get() = if (hours < 3f) "Insomniac" else if (hours < 6.5f) "Low" else "Rem"
    val note get() = when (tag) { "Insomniac" -> "78 AI Suggestions"; "Rem" -> "8 AI Suggestions"; else -> "No Suggestion" }
}

object Store {
    val metrics = mutableStateListOf(
        Metric("Heart Rate", "70", "bpm", "Normal resting range", Icons.Default.MonitorHeart, Color(0xFF5B8C9D), Color(0xFFDFE9EC),
            "https://www.who.int/health-topics/cardiovascular-diseases", listOf(68f, 72f, 70f, 75f, 69f, 71f, 70f)),
        Metric("Blood Pressure", "121/80", "mmHg", "Slightly elevated", Icons.Default.Bloodtype, Color(0xFFC06B86), Color(0xFFF6DFE6),
            "https://www.heart.org/en/health-topics/high-blood-pressure", listOf(118f, 122f, 120f, 125f, 119f, 121f, 121f), route = "bp"),
        Metric("Oxygen", "98", "%", "Healthy SpO2", Icons.Default.Air, Color(0xFFEC4899), Color(0xFFF6DFF0),
            "https://www.who.int/health-topics/oxygen", listOf(97f, 98f, 98f, 96f, 98f, 99f, 98f)),
        Metric("Cholesterol", "172", "mg/dL", "Desirable (< 200)", Icons.Default.Science, Color(0xFF64748B), Color(0xFFE1E8E4),
            "https://www.cdc.gov/cholesterol/about/index.html", listOf(180f, 176f, 174f, 175f, 173f, 172f, 172f)),
        Metric("Calories Burned", "2000", "kcal", "On target for today", Icons.Default.LocalFireDepartment, Color(0xFFF57C00), Color(0xFFF3E6B8),
            "https://www.cdc.gov/healthy-weight-growth/physical-activity/index.html", listOf(1800f, 2100f, 1950f, 2200f, 1700f, 2050f, 2000f)),
        Metric("Steps Walked", "8420", "steps", "84% of daily goal", Icons.AutoMirrored.Filled.DirectionsWalk, Color(0xFF5B8C9D), Color(0xFFDFE9EC),
            "https://www.who.int/news-room/fact-sheets/detail/physical-activity", listOf(6500f, 9200f, 8000f, 10100f, 7400f, 8800f, 8420f)),
        Metric("Water Intake", "1.8", "L", "Drink 0.7 L more", Icons.Default.WaterDrop, Color(0xFF3B82F6), Color(0xFFDFE9F6),
            "https://www.cdc.gov/healthy-weight-growth/water-consumption/index.html", listOf(1.5f, 2f, 1.8f, 2.2f, 1.6f, 1.9f, 1.8f)),
        Metric("Glucose", "92", "mg/dL", "Normal fasting level", Icons.Default.Opacity, Color(0xFFE11D48), Color(0xFFF6DFE6),
            "https://www.cdc.gov/diabetes/about/index.html", listOf(90f, 95f, 92f, 98f, 91f, 93f, 92f)),
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
