package com.healthku.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import com.healthku.app.ui.theme.LumiCanvasDark
import com.healthku.app.ui.theme.LumiCardBorderDark
import com.healthku.app.ui.theme.LumiCardDark
import com.healthku.app.ui.theme.LumiPeriwinkle
import com.healthku.app.ui.theme.LumiSage
import com.healthku.app.ui.theme.LumiTextMuted
import com.healthku.app.ui.theme.LumiTextPrimary
import com.healthku.app.ui.theme.LumiTextSecondary

@Composable
fun LumiSettingsScreen(
    hasPermission: Boolean,
    apiKey: String,
    onSaveApiKey: (String) -> Unit,
    onOpenHealthConnectSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    var keyInput by remember { mutableStateOf(apiKey) }
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(LumiCanvasDark)
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 18.dp)
    ) {
        Text(
            text = "Settings & Integrations",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = LumiTextPrimary,
            letterSpacing = (-0.5).sp
        )
        Text(
            text = "Koneksi data real Android Health Connect",
            fontSize = 13.sp,
            color = LumiTextSecondary
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Health Connect Status & Action Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, LumiCardBorderDark, RoundedCornerShape(26.dp)),
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(containerColor = LumiCardDark)
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(if (hasPermission) Color(0x225E8C4A) else Color(0x22E2A55E)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (hasPermission) Icons.Default.CheckCircle else Icons.Default.Sensors,
                                contentDescription = null,
                                tint = if (hasPermission) LumiSage else Color(0xFFE2A55E),
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "Health Connect",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = LumiTextPrimary
                            )
                            Text(
                                text = if (hasPermission) "Akses Data Aktif (Real)" else "Izin Belum Aktif",
                                fontSize = 12.sp,
                                color = if (hasPermission) LumiSage else Color(0xFFE2A55E),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Jika popup 'Allow All' tidak muncul otomatis di HP, buka pengaturan Health Connect secara langsung melalui tombol di bawah ini:",
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    color = LumiTextSecondary
                )

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = onOpenHealthConnectSettings,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LumiPeriwinkle,
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.OpenInNew,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Buka Izin di Health Connect Langsung",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Gemini AI Engine Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, LumiCardBorderDark, RoundedCornerShape(26.dp)),
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(containerColor = LumiCardDark)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Key,
                        contentDescription = null,
                        tint = LumiPeriwinkle,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Google Gemini AI API Key (Opsional)",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = LumiTextPrimary
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "LUMI AI Assistant sudah bekerja secara lokal tanpa API key. Masukkan API key jika ingin respon Gemini interaktif kustom.",
                    fontSize = 12.sp,
                    lineHeight = 17.sp,
                    color = LumiTextSecondary
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = keyInput,
                    onValueChange = { keyInput = it },
                    label = { Text("API Key Gemini", color = LumiTextMuted) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = LumiTextPrimary,
                        unfocusedTextColor = LumiTextPrimary,
                        focusedContainerColor = Color(0xFF0F1118),
                        unfocusedContainerColor = Color(0xFF0F1118),
                        focusedIndicatorColor = LumiPeriwinkle,
                        unfocusedIndicatorColor = LumiCardBorderDark
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { onSaveApiKey(keyInput) },
                    modifier = Modifier.align(Alignment.End),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = LumiPeriwinkle)
                ) {
                    Text("Simpan Key", color = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}
