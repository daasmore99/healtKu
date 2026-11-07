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
        // Calculate dynamic step recommendation
        val dynamicStepGoal = when (preferredWorkout) {
            WorkoutCategory.WEIGHTLIFTING -> 7000
            WorkoutCategory.RUNNING -> 11000
            WorkoutCategory.BRISK_WALKING -> 8500
        }

        val cnsStatus = when {
            summary.readinessScore >= 85 -> "Pulih Total (Sistem Saraf Optimal)"
            summary.readinessScore >= 65 -> "Stabil (Volume Latihan Standar)"
            else -> "Kelelahan Terdeteksi (Perlu Deload / Active Recovery)"
        }

        val morningBriefing = buildString {
            append("Pagi, Atlet! Skor Kesiapan Fisikmu hari ini: ${summary.readinessScore}/100. ")
            if (summary.readinessScore >= 85) {
                append("Tidur semalam ${summary.sleepHours} jam dengan deep sleep ${String.format("%.1f", summary.deepSleepHours)} jam. ")
                append("Resting Heart Rate kamu stabil di ${summary.restingHeartRateBpm} bpm—indikasi pemulihan sistem saraf pusat (CNS) dan otot berjalan prima. ")
                append("Hari ini waktu yang tepat untuk push limit pada sesi ${preferredWorkout.displayName}. Target langkah dinamis: $dynamicStepGoal langkah!")
            } else if (summary.readinessScore >= 65) {
                append("Kondisi tubuhmu berada di batas aman (${summary.sleepHours} jam tidur). ")
                append("Pertahankan konsistensi latihan dengan intensitas terukur. Jangan memaksakan PR hari ini. ")
                append("Kunci target langkah di $dynamicStepGoal langkah untuk menjaga active metabolism.")
            } else {
                append("PERINGATAN FISIOLOGIS: Tidurmu hanya ${summary.sleepHours} jam. ")
                append("Sintesis protein terhambat dan hormon kortisol berpotensi naik jika dipaksa angkat beban berat. ")
                append("Saran disiplin: Turunkan beban 30% atau alihkan ke jalan cepat 30 menit (Zone 2) untuk sirkulasi pembuangan asam laktat.")
            }
        }

        val eveningRecap = buildString {
            val stepDiff = summary.steps - dynamicStepGoal
            append("Evaluasi Disiplin Malam: ")
            if (stepDiff >= 0) {
                append("Target $dynamicStepGoal langkah tuntas (${summary.steps} langkah tercapai). ")
                append("Estimasi pembakaran kalori aktif ${summary.caloriesBurned} kcal. Eksekusi hari ini solid! ")
            } else {
                append("Kamu masih kurang ${-stepDiff} langkah dari target $dynamicStepGoal. ")
                append("Jangan jadikan alasan. Luangkan 15 menit jalan santai sekarang sebelum tidur. ")
            }
            append("Untuk menjaga regenerasi sel otot maksimal, kunci jam tidurmu paling lambat pukul 22.30 malam ini.")
        }

        return AICoachReport(
            readinessScore = summary.readinessScore,
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
            
            Data atlet hari ini dari Smartwatch & Health Connect:
            - Langkah hari ini: ${summary.steps} langkah
            - Detak jantung terkini: ${summary.heartRateBpm} bpm
            - Resting Heart Rate: ${summary.restingHeartRateBpm} bpm
            - Total tidur semalam: ${summary.sleepHours} jam (Deep sleep: ${summary.deepSleepHours} jam)
            - Kalori terbakar: ${summary.caloriesBurned} kcal
            - Jarak tempuh: ${summary.distanceKm} km
            - Kategori latihan yang direncanakan: ${workoutCategory.displayName}
            
            Tugasmu:
            1. Berikan Skor Kesiapan (Readiness Score 1-100).
            2. Buat Morning Readiness Briefing (analisa deep sleep, resting heart rate, rekomendasi intensitas).
            3. Buat Evening Recap & Sleep Lockdown (evaluasi pencapaian, jam tidur yang wajib dipatuhi).
            4. Tentukan target langkah dinamis yang optimal.
            
            Kirimkan respon HANYA dalam format JSON valid berikut tanpa markdown codeblock:
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
