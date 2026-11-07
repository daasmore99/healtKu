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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
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
import com.healthku.app.ui.theme.AsclepiosBg
import com.healthku.app.ui.theme.AsclepiosCard
import com.healthku.app.ui.theme.AsclepiosCardBorder
import com.healthku.app.ui.theme.AsclepiosTextPrimary
import com.healthku.app.ui.theme.AsclepiosTextSecondary
import com.healthku.app.ui.theme.SageDark
import com.healthku.app.ui.theme.SagePillBg
import com.healthku.app.ui.theme.SagePillGreen

@Composable
fun SettingsScreen(
    apiKey: String,
    onSaveApiKey: (String) -> Unit,
    onOpenHealthConnectSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    var keyInput by remember { mutableStateOf(apiKey) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AsclepiosBg)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Text(
            text = "Pengaturan & Integrasi",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = AsclepiosTextPrimary
        )
        Text(
            text = "Hubungkan data real langsung ke Android Health Connect",
            fontSize = 13.sp,
            color = AsclepiosTextSecondary
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Health Connect Direct Integration Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, AsclepiosCardBorder, RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = AsclepiosCard)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
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
                                .background(SagePillBg),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Sensors,
                                contentDescription = null,
                                tint = SagePillGreen,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "Android Health Connect",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = AsclepiosTextPrimary
                            )
                            Text(
                                text = "Pusat Data Kesehatan Resmi Google",
                                fontSize = 12.sp,
                                color = SagePillGreen
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = null,
                        tint = SagePillGreen,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Aplikasi ini membaca data asli (real-time) langsung dari Health Connect di HP Anda tanpa data rekayasa/dummy:\n• Langkah & Jarak Harian\n• Detak Jantung & Resting HR\n• Tekanan Darah (SYS/DIA)\n• Saturasi Oksigen (SpO2)\n• Sesi Tidur & Durasi",
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    color = AsclepiosTextSecondary
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onOpenHealthConnectSettings,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SagePillBg,
                        contentColor = SagePillGreen
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Sensors,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Kelola Izin di Health Connect",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Gemini AI API Key Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, AsclepiosCardBorder, RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = AsclepiosCard)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Key,
                        contentDescription = null,
                        tint = SageDark,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Google Gemini API Key (Opsional)",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = AsclepiosTextPrimary
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Jika dikosongkan, AI Coach tetap aktif offline menggunakan mesin sport science bawaan. Jika diisi, AI akan menghasilkan rekomendasi dinamis via Google Gemini.",
                    fontSize = 12.sp,
                    lineHeight = 17.sp,
                    color = AsclepiosTextSecondary
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = keyInput,
                    onValueChange = { keyInput = it },
                    label = { Text("Masukkan API Key Gemini") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { onSaveApiKey(keyInput) },
                    modifier = Modifier.align(Alignment.End),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SageDark)
                ) {
                    Text("Simpan Key")
                }
            }
        }
    }
}
