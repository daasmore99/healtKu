package com.healthku.app.ai

import com.healthku.app.data.AICoachReport
import com.healthku.app.data.DailyHealthSummary
import com.healthku.app.data.WorkoutCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class AICoachEngine {

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    fun generateLocalBriefing(
        summary: DailyHealthSummary,
        preferredWorkout: WorkoutCategory = WorkoutCategory.WEIGHTLIFTING
    ): AICoachReport {
        val score = summary.readinessScore ?: 75

        // Dynamic step goal based on sport science
        val dynamicStepGoal = when (preferredWorkout) {
            WorkoutCategory.WEIGHTLIFTING -> 7000
            WorkoutCategory.RUNNING -> 11000
            WorkoutCategory.BRISK_WALKING -> 8500
        }

        val cnsStatus = when {
            score >= 85 -> "Pulih Total (CNS Optimal)"
            score >= 65 -> "Stabil (Volume Latihan Standar)"
            else -> "Kelelahan Terdeteksi (Perlu Recovery)"
        }

        val morningBriefing = buildString {
            if (summary.readinessScore != null) {
                append("Skor Kesiapan Fisik hari ini: $score/100 ($cnsStatus). ")
                if (summary.sleepHours > 0f) {
                    append("Durasi tidur semalam: ${String.format("%.1f", summary.sleepHours)} jam. ")
                }
                if (summary.restingHeartRateBpm != null) {
                    append("Resting Heart Rate: ${summary.restingHeartRateBpm} bpm. ")
                }
                if (score >= 85) {
                    append("Sistem saraf dan otot siap untuk beban maksimal. Target langkah dinamis: $dynamicStepGoal langkah!")
                } else if (score >= 65) {
                    append("Kondisi tubuh stabil. Jalankan program $preferredWorkout dengan volume terukur.")
                } else {
                    append("Tubuh membutuhkan fase pemulihan. Prioritaskan peregangan atau jalan santai Zone 2.")
                }
            } else {
                append("Menunggu sinkronisasi data dari Health Connect. Buka aplikasi Health Connect dan izinkan pembacaan data.")
            }
        }

        val eveningRecap = buildString {
            val stepDiff = summary.steps - dynamicStepGoal
            append("Evaluasi Disiplin Malam: ")
            if (summary.steps > 0) {
                if (stepDiff >= 0) {
                    append("Target $dynamicStepGoal langkah berhasil tercapai (${summary.steps} langkah). ")
                } else {
                    append("Tercatat ${summary.steps} langkah dari target $dynamicStepGoal langkah. ")
                }
                if (summary.caloriesBurned > 0) {
                    append("Pembakaran kalori aktif: ${summary.caloriesBurned} kcal. ")
                }
            } else {
                append("Belum ada aktivitas langkah yang tercatat di Health Connect hari ini. ")
            }
            append("Jaga jadwal tidur konsisten malam ini.")
        }

        return AICoachReport(
            readinessScore = score,
            status = summary.readinessStatus,
            morningBriefing = morningBriefing,
            eveningRecap = eveningRecap,
            dynamicStepGoal = dynamicStepGoal,
            recommendedWorkout = when (preferredWorkout) {
                WorkoutCategory.WEIGHTLIFTING -> "Compound Lifts (Push/Pull) - 45-60 menit"
                WorkoutCategory.RUNNING -> "Interval Run / Zone 2 Cardio - 30-45 menit"
                WorkoutCategory.BRISK_WALKING -> "Brisk Walk - 40 menit continuous tempo"
            },
            cnsRecoveryStatus = cnsStatus
        )
    }

    suspend fun queryGeminiCoach(
        apiKey: String,
        summary: DailyHealthSummary,
        workoutCategory: WorkoutCategory
    ): AICoachReport = withContext(Dispatchers.IO) {
        if (apiKey.isBlank()) {
            return@withContext generateLocalBriefing(summary, workoutCategory)
        }

        val prompt = """
            Kamu adalah AI Health & Athletic Coach pribadi bernama "HealthKu Coach".
            Karaktermu: TEGAS, DISIPLIN, namun SANGAT SAINTIFIK dan berbasis data fisiologi olahraga (sport science).
            
            Data atlet hari ini dari Health Connect (DATA ASLI):
            - Langkah hari ini: ${summary.steps} langkah
            - Detak jantung terkini: ${summary.heartRateBpm ?: "Belum ada"} bpm
            - Resting Heart Rate: ${summary.restingHeartRateBpm ?: "Belum ada"} bpm
            - Total tidur semalam: ${summary.sleepHours} jam
            - Kalori terbakar: ${summary.caloriesBurned} kcal
            - Jarak tempuh: ${summary.distanceKm} km
            - Kategori latihan: ${workoutCategory.displayName}
            
            Berikan respon JSON valid:
            {
               "readinessScore": 88,
               "status": "Prime Zone",
               "morningBriefing": "teks briefing...",
               "eveningRecap": "teks rekap...",
               "dynamicStepGoal": 7500,
               "recommendedWorkout": "rekomendasi latihan...",
               "cnsRecoveryStatus": "Pulih Optimal"
            }
        """.trimIndent()

        try {
            val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=$apiKey"
            val jsonBody = JSONObject().apply {
                val contents = org.json.JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", org.json.JSONArray().apply {
                            put(JSONObject().apply { put("text", prompt) })
                        })
                    })
                }
                put("contents", contents)
            }

            val request = Request.Builder()
                .url(url)
                .post(jsonBody.toString().toRequestBody("application/json".toMediaType()))
                .build()

            val response = httpClient.newCall(request).execute()
            val responseString = response.body?.string() ?: ""

            val jsonResponse = JSONObject(responseString)
            val candidates = jsonResponse.getJSONArray("candidates")
            val rawText = candidates.getJSONObject(0)
                .getJSONObject("content")
                .getJSONArray("parts")
                .getJSONObject(0)
                .getString("text")
                .trim()
                .replace("```json", "")
                .replace("```", "")
                .trim()

            val parsed = JSONObject(rawText)
            AICoachReport(
                readinessScore = parsed.optInt("readinessScore", 85),
                status = parsed.optString("status", "Optimal"),
                morningBriefing = parsed.optString("morningBriefing", "Tetap disiplin jalankan protokol latihan."),
                eveningRecap = parsed.optString("eveningRecap", "Tuntaskan target harian dan tidur tepat waktu."),
                dynamicStepGoal = parsed.optInt("dynamicStepGoal", 8000),
                recommendedWorkout = parsed.optString("recommendedWorkout", "Sesi latihan terukur"),
                cnsRecoveryStatus = parsed.optString("cnsRecoveryStatus", "Stabil")
            )
        } catch (e: Exception) {
            e.printStackTrace()
            generateLocalBriefing(summary, workoutCategory)
        }
    }
}
