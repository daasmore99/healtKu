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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.healthku.app.data.DailyHealthSummary
import com.healthku.app.ui.components.LumiOrb
import com.healthku.app.ui.theme.LumiAmber
import com.healthku.app.ui.theme.LumiCanvasDark
import com.healthku.app.ui.theme.LumiCapsule
import com.healthku.app.ui.theme.LumiCardBorderDark
import com.healthku.app.ui.theme.LumiCardDark
import com.healthku.app.ui.theme.LumiPeriwinkle
import com.healthku.app.ui.theme.LumiSage
import com.healthku.app.ui.theme.LumiTextMuted
import com.healthku.app.ui.theme.LumiTextPrimary
import com.healthku.app.ui.theme.LumiTextSecondary
import kotlinx.coroutines.launch

data class LumiChatMessage(
    val id: String,
    val sender: String, // "lumi" or "user"
    val text: String,
    val time: String
)

@Composable
fun LumiChatScreen(
    summary: DailyHealthSummary,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var inputText by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    val messages = remember {
        mutableStateListOf(
            LumiChatMessage(
                id = "1",
                sender = "lumi",
                text = "Halo! Saya LUMI, asisten kesehatan pribadi Anda. Berdasarkan data Health Connect terkini, tubuh Anda dalam fase ${summary.readinessStatus} (${summary.readinessScore ?: 88}% kesiapan). Ada yang ingin Anda tanyakan seputar pemulihan atau latihan hari ini?",
                time = "Baru saja"
            )
        )
    }

    val quickQuestions = listOf(
        "Bagaimana pemulihan tidurku?",
        "Boleh angkat beban hari ini?",
        "Evaluasi detak jantungku",
        "Target jalan cepat sore ini"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(LumiCanvasDark)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(LumiCardDark)
                    .border(1.dp, LumiCardBorderDark, RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = LumiTextPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                LumiOrb(sizeDp = 34.dp)
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "LUMI Assistant",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = LumiTextPrimary
                    )
                    Text(
                        text = "Real-time Biometrics AI",
                        fontSize = 11.sp,
                        color = LumiSage
                    )
                }
            }

            Box(modifier = Modifier.size(42.dp))
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Quick Suggestion Chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            quickQuestions.take(2).forEach { q ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(LumiCardDark)
                        .border(1.dp, LumiCardBorderDark, RoundedCornerShape(12.dp))
                        .clickable {
                            messages.add(LumiChatMessage(System.currentTimeMillis().toString(), "user", q, "12:00"))
                            val reply = when {
                                q.contains("tidur") -> "Tidur Anda tercatat ${summary.sleepHours} jam dengan deep sleep ${String.format("%.1f", summary.deepSleepHours)} jam. Fase regenerasi sel otot sudah cukup optimal."
                                q.contains("beban") -> "Kesiapan sistem saraf pusat Anda berada pada skor ${summary.readinessScore ?: 85}%. Aman untuk compound lifts intensitas terukur!"
                                else -> "Aktivitas langkah harian: ${summary.steps} langkah (${summary.distanceKm} km). Pertahankan konsistensi jalan cepat Zone 2."
                            }
                            messages.add(LumiChatMessage((System.currentTimeMillis() + 1).toString(), "lumi", reply, "12:00"))
                        }
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = q,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = LumiTextSecondary,
                        maxLines = 1
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Chat Message List
        LazyColumn(
            state = listState,
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(messages) { msg ->
                val isLumi = msg.sender == "lumi"
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (isLumi) Arrangement.Start else Arrangement.End
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(if (isLumi) 0.88f else 0.75f)
                            .clip(
                                RoundedCornerShape(
                                    topStart = 20.dp,
                                    topEnd = 20.dp,
                                    bottomStart = if (isLumi) 4.dp else 20.dp,
                                    bottomEnd = if (isLumi) 20.dp else 4.dp
                                )
                            )
                            .background(if (isLumi) LumiCardDark else LumiPeriwinkle)
                            .border(
                                1.dp,
                                if (isLumi) LumiCardBorderDark else Color.Transparent,
                                RoundedCornerShape(20.dp)
                            )
                            .padding(14.dp)
                    ) {
                        Column {
                            if (isLumi) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = LumiPeriwinkle,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "LUMI",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = LumiPeriwinkle
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                            }
                            Text(
                                text = msg.text,
                                fontSize = 13.sp,
                                lineHeight = 19.sp,
                                color = if (isLumi) LumiTextPrimary else Color.White
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Bottom Input Capsule
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(LumiCardDark)
                .border(1.dp, LumiCardBorderDark, RoundedCornerShape(24.dp))
                .padding(horizontal = 14.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            androidx.compose.foundation.text.BasicTextField(
                value = inputText,
                onValueChange = { inputText = it },
                textStyle = androidx.compose.ui.text.TextStyle(
                    color = LumiTextPrimary,
                    fontSize = 13.sp
                ),
                modifier = Modifier.weight(1f),
                decorationBox = { innerTextField ->
                    if (inputText.isEmpty()) {
                        Text(
                            text = "Tanyakan apapun pada LUMI...",
                            fontSize = 13.sp,
                            color = LumiTextMuted
                        )
                    }
                    innerTextField()
                }
            )

            IconButton(
                onClick = {
                    if (inputText.isNotBlank()) {
                        val textToSend = inputText
                        inputText = ""
                        messages.add(LumiChatMessage(System.currentTimeMillis().toString(), "user", textToSend, "12:01"))
                        messages.add(
                            LumiChatMessage(
                                (System.currentTimeMillis() + 1).toString(),
                                "lumi",
                                "Analisa data untuk '$textToSend': Denyut jantung Anda stabil di ${summary.heartRateBpm ?: 70} bpm dan pemulihan berjalan baik.",
                                "12:01"
                            )
                        )
                    }
                }
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(LumiPeriwinkle),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Kirim",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
