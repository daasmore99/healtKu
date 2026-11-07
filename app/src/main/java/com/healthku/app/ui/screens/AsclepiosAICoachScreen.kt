package com.healthku.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.healthku.app.data.AICoachReport
import com.healthku.app.data.DailyHealthSummary
import com.healthku.app.ui.theme.AsclepiosBg
import com.healthku.app.ui.theme.AsclepiosCard
import com.healthku.app.ui.theme.AsclepiosCardBorder
import com.healthku.app.ui.theme.AsclepiosTextMuted
import com.healthku.app.ui.theme.AsclepiosTextPrimary
import com.healthku.app.ui.theme.AsclepiosTextSecondary
import com.healthku.app.ui.theme.SagePillBg
import com.healthku.app.ui.theme.SagePillGreen

@Composable
fun AsclepiosAICoachScreen(
    summary: DailyHealthSummary,
    aiReport: AICoachReport,
    apiKey: String,
    onSaveApiKey: (String) -> Unit,
    onSyncHealthConnect: () -> Unit,
    onOpenHealthConnectSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    var tempKey by remember { mutableStateOf(apiKey) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AsclepiosBg)
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        // Top Title
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "HealthKu AI Assistant",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = AsclepiosTextPrimary
            )

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(AsclepiosCard)
                    .border(1.dp, AsclepiosCardBorder, CircleShape)
                    .clickable { onSyncHealthConnect() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Sync",
                    tint = AsclepiosTextPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Health Connect Status Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, AsclepiosCardBorder, RoundedCornerShape(26.dp)),
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(containerColor = AsclepiosCard),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Android Health Connect",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = AsclepiosTextPrimary
                    )

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (summary.hasHealthConnectPermission) SagePillBg else Color(0xFFFFECE5))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (summary.hasHealthConnectPermission) "Terhubung" else "Belum Diizinkan",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (summary.hasHealthConnectPermission) SagePillGreen else Color(0xFFD64527)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Aplikasi membaca data biologis riil langsung dari Health Connect (detak jantung, tensi, tidur, langkah) dari Smartwatch Anda.",
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    color = AsclepiosTextSecondary
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onOpenHealthConnectSettings,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E2127))
                    ) {
                        Text("Buka Izin di HP", fontSize = 12.sp, color = Color.White)
                    }

                    Button(
                        onClick = onSyncHealthConnect,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SagePillGreen)
                    ) {
                        Text("Sync Ulang", fontSize = 12.sp, color = Color.White)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // AI Coach Advice Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, AsclepiosCardBorder, RoundedCornerShape(26.dp)),
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(containerColor = AsclepiosCard),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(SagePillBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = SagePillGreen,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = "Rekomendasi Pemulihan",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = AsclepiosTextPrimary
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = aiReport.morningBriefing,
                    fontSize = 13.sp,
                    lineHeight = 19.sp,
                    color = AsclepiosTextPrimary
                )

                Spacer(modifier = Modifier.height(12.dp))

                listOf(
                    "Rekomendasi Latihan: ${aiReport.recommendedWorkout}",
                    "Target Langkah Dinamis: ${aiReport.dynamicStepGoal} langkah",
                    "Status CNS & Pemulihan: ${aiReport.cnsRecoveryStatus}"
                ).forEach { takeaway ->
                    Row(
                        modifier = Modifier.padding(vertical = 3.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(text = "• ", fontSize = 14.sp, color = SagePillGreen, fontWeight = FontWeight.Bold)
                        Text(text = takeaway, fontSize = 12.sp, color = AsclepiosTextSecondary)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Optional Gemini API Key
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, AsclepiosCardBorder, RoundedCornerShape(26.dp)),
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(containerColor = AsclepiosCard),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "Gemini AI Key (Opsional)",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = AsclepiosTextPrimary
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Jika diisi, analisis kesehatan diproses langsung oleh Gemini AI terbaru.",
                    fontSize = 11.sp,
                    color = AsclepiosTextSecondary
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = tempKey,
                    onValueChange = { tempKey = it },
                    placeholder = { Text("AIzaSy...", fontSize = 12.sp, color = AsclepiosTextMuted) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SagePillGreen,
                        unfocusedBorderColor = AsclepiosCardBorder
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = { onSaveApiKey(tempKey) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E2127))
                ) {
                    Text("Simpan Key", fontSize = 12.sp, color = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}
