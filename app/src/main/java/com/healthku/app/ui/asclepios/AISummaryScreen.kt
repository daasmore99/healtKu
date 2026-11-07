package com.healthku.app.ui.asclepios

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.healthku.app.ai.AICoachEngine
import com.healthku.app.data.AICoachReport
import com.healthku.app.data.DailyHealthSummary
import com.healthku.app.data.WorkoutCategory
import kotlinx.coroutines.launch

@Composable
fun AISummaryScreen(
    summary: DailyHealthSummary,
    apiKey: String,
    onSaveApiKey: (String) -> Unit,
    onSyncHealthConnect: () -> Unit,
    onOpenHealthConnect: () -> Unit
) {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()
    val aiEngine = remember { AICoachEngine() }
    var selectedWorkout by remember { mutableStateOf(WorkoutCategory.WEIGHTLIFTING) }
    var report by remember { mutableStateOf(aiEngine.generateLocalBriefing(summary, selectedWorkout)) }
    var isLoading by remember { mutableStateOf(false) }
    var inputKey by remember { mutableStateOf(apiKey) }
    var showKeyDialog by remember { mutableStateOf(false) }

    fun refreshAnalysis() {
        scope.launch {
            isLoading = true
            report = if (inputKey.isNotBlank()) {
                aiEngine.queryGeminiCoach(inputKey, summary, selectedWorkout)
            } else {
                aiEngine.generateLocalBriefing(summary, selectedWorkout)
            }
            isLoading = false
        }
    }

    LaunchedEffect(summary, selectedWorkout) {
        refreshAnalysis()
    }

    if (showKeyDialog) {
        AlertDialog(
            onDismissRequest = { showKeyDialog = false },
            title = { Text("Gemini AI API Key") },
            text = {
                Column {
                    Text("Masukkan Gemini API Key gratis dari Google AI Studio untuk analisis medis mendalam:", fontSize = 12.sp, color = Muted)
                    Spacer(Modifier.height(10.dp))
                    OutlinedTextField(
                        value = inputKey,
                        onValueChange = { inputKey = it },
                        placeholder = { Text("AIzaSy...", fontSize = 12.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton({
                    onSaveApiKey(inputKey.trim())
                    showKeyDialog = false
                    refreshAnalysis()
                }) { Text("Simpan") }
            },
            dismissButton = {
                TextButton({ showKeyDialog = false }) { Text("Batal") }
            }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFFF6EDE1), Color(0xFFEFE3D3)))),
        contentPadding = PaddingValues(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                    .background(Color.White)
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("AI Health Coach", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Ink)
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = if (inputKey.isNotBlank()) "Didukung Google Gemini AI ✦" else "Mode Analisis Offline Cerdas ✦",
                            fontSize = 11.sp,
                            color = Forest,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        SqButton(
                            icon = Icons.Default.Key,
                            size = 44.dp,
                            bg = CreamDeep,
                            onClick = { showKeyDialog = true }
                        )
                        SqButton(
                            icon = Icons.Default.Sync,
                            size = 44.dp,
                            bg = CreamDeep,
                            onClick = {
                                onSyncHealthConnect()
                                refreshAnalysis()
                            }
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Readiness & Status Banner
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(CreamDeep)
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(Forest),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${report.readinessScore}",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Status: ${report.readinessStatus}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Ink
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = "Rekomendasi Beban: ${report.recommendedIntensity}",
                            fontSize = 11.sp,
                            color = Forest,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // Workout Target Selector
        item {
            Reveal {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Text("Fokus Aktivitas / Olahraga", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Ink)
                    Spacer(Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        WorkoutCategory.values().forEach { cat ->
                            val isSel = cat == selectedWorkout
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(if (isSel) Forest else CardWhite)
                                    .clickable { selectedWorkout = cat }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = when (cat) {
                                        WorkoutCategory.WEIGHTLIFTING -> "Gym / Beban"
                                        WorkoutCategory.RUNNING -> "Lari / Kardio"
                                        WorkoutCategory.HYBRID_CROSSFIT -> "Crossfit"
                                        WorkoutCategory.RECOVERY_YOGA -> "Yoga / Santai"
                                    },
                                    fontSize = 11.sp,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSel) Color.White else Ink
                                )
                            }
                        }
                    }
                }
            }
        }

        // Main Briefing Card
        item {
            Reveal {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(26.dp))
                        .background(CardWhite)
                        .padding(18.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CircleIcon(Icons.Default.AutoAwesome, Forest, Color(0xFFE3EBDC), 38.dp)
                        Spacer(Modifier.width(10.dp))
                        Text("Ringkasan Pemulihan & Biometrik", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Ink)
                    }

                    Spacer(Modifier.height(14.dp))

                    if (isLoading) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Forest, strokeWidth = 2.dp)
                            Spacer(Modifier.width(10.dp))
                            Text("Gemini sedang menganalisis data tubuh Anda...", fontSize = 12.sp, color = Muted)
                        }
                    } else {
                        Text(
                            text = report.briefingText,
                            fontSize = 13.sp,
                            lineHeight = 19.sp,
                            color = Ink
                        )
                    }

                    Spacer(Modifier.height(16.dp))
                    Text("Poin Kunci & Tindakan Hari Ini:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Forest)
                    Spacer(Modifier.height(8.dp))

                    report.keyTakeaways.forEach { takeaway ->
                        Row(modifier = Modifier.padding(vertical = 3.dp), verticalAlignment = Alignment.Top) {
                            Text("✦ ", fontSize = 12.sp, color = Forest, fontWeight = FontWeight.Bold)
                            Text(takeaway, fontSize = 12.sp, color = Ink, lineHeight = 17.sp)
                        }
                    }
                }
            }
        }

        // Biological Metrics Snapshot
        item {
            Reveal {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(26.dp))
                        .background(CardWhite)
                        .padding(18.dp)
                ) {
                    Text("Snapshot Data Sensor Smartwatch", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Ink)
                    Spacer(Modifier.height(12.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        MetricSmallBox("Detak Jantung", if (summary.heartRateBpm != null) "${summary.heartRateBpm} bpm" else "70 bpm", Modifier.weight(1f))
                        MetricSmallBox("Tensi Darah", if (summary.bloodPressureSys != null) "${summary.bloodPressureSys}/${summary.bloodPressureDia}" else "121/80", Modifier.weight(1f))
                    }
                    Spacer(Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        MetricSmallBox("Oksigen (SpO2)", if (summary.oxygenSaturationPercent != null) "${summary.oxygenSaturationPercent}%" else "98%", Modifier.weight(1f))
                        MetricSmallBox("Waktu Tidur", if (summary.sleepHours > 0) "${summary.sleepHours} jam" else "7.2 jam", Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun MetricSmallBox(label: String, value: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(CreamDeep)
            .padding(12.dp)
    ) {
        Column {
            Text(label, fontSize = 10.sp, color = Muted)
            Spacer(Modifier.height(4.dp))
            Text(value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Ink)
        }
    }
}
