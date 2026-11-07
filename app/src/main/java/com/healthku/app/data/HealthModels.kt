package com.healthku.app.data

data class DailyHealthSummary(
    val steps: Int = 7420,
    val stepGoal: Int = 8000,
    val heartRateBpm: Int = 70,
    val restingHeartRateBpm: Int = 62,
    val heartRateHistory: List<Float> = listOf(62f, 65f, 78f, 72f, 85f, 70f, 68f, 74f, 70f),
    val bloodPressureSys: Int = 120,
    val bloodPressureDia: Int = 80,
    val bloodPressureHistory: List<Float> = listOf(118f, 122f, 120f, 124f, 120f),
    val sleepHours: Float = 6.8f,
    val deepSleepHours: Float = 1.9f,
    val sleepIrregularities: Int = 1,
    val sleepHistory: List<Float> = listOf(6.2f, 7.0f, 6.5f, 5.8f, 6.8f),
    val caloriesBurned: Int = 1840,
    val distanceKm: Float = 5.6f,
    val readinessScore: Int = 88,
    val readinessStatus: String = "Prime Zone",
    val isSyncedWithHealthConnect: Boolean = false,
    val lastSyncTime: String = "Baru Saja"
)

enum class WorkoutCategory(val displayName: String, val baseStepImpact: Int) {
    WEIGHTLIFTING("Angkat Beban", 2000),
    RUNNING("Lari", 6000),
    BRISK_WALKING("Jalan Cepat", 4000)
}

data class WorkoutSession(
    val id: String,
    val category: WorkoutCategory,
    val title: String,
    val durationMinutes: Int,
    val calories: Int,
    val avgHeartRate: Int,
    val dateText: String
)

data class AICoachReport(
    val readinessScore: Int,
    val status: String,
    val morningBriefing: String,
    val eveningRecap: String,
    val dynamicStepGoal: Int,
    val recommendedWorkout: String,
    val cnsRecoveryStatus: String
)
