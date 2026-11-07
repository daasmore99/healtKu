package com.healthku.app.data

enum class WorkoutCategory(val displayName: String, val baseStepImpact: Int) {
    WEIGHTLIFTING("Angkat Beban", 2000),
    RUNNING("Lari", 6000),
    BRISK_WALKING("Jalan Cepat", 4000)
}

data class DailyHealthSummary(
    val steps: Int = 0,
    val stepGoal: Int = 8000,
    val heartRateBpm: Int? = null,
    val restingHeartRateBpm: Int? = null,
    val heartRateHistory: List<Float> = emptyList(),
    val bloodPressureSys: Int? = null,
    val bloodPressureDia: Int? = null,
    val oxygenSaturationPercent: Int? = null,
    val sleepHours: Float = 0f,
    val deepSleepHours: Float = 0f,
    val sleepIrregularities: Int = 0,
    val caloriesBurned: Int = 0,
    val distanceKm: Float = 0f,
    val readinessScore: Int? = null,
    val readinessStatus: String = "Menunggu Data",
    val hasHealthConnectPermission: Boolean = false,
    val isHealthConnectAvailable: Boolean = true,
    val lastSyncTime: String = "Belum Tersinkron",
    val sleepHistory: List<SleepLogEntry> = emptyList()
)

data class SleepLogEntry(
    val dateText: String,
    val dayNumber: String,
    val durationHours: Float,
    val tagText: String, // "Optimal", "Low", "Rem", "Insomniac"
    val tagType: SleepTagType,
    val suggestionCount: Int = 0
)

enum class SleepTagType {
    OPTIMAL, LOW, REM, WARNING
}

data class AICoachReport(
    val readinessScore: Int,
    val status: String,
    val morningBriefing: String,
    val eveningRecap: String,
    val dynamicStepGoal: Int,
    val recommendedWorkout: String,
    val cnsRecoveryStatus: String
)
