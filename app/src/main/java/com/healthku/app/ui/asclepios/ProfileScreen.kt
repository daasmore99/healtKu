package com.healthku.app.ui.asclepios

import androidx.compose.foundation.*
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.healthku.app.R

@Composable
fun ProfileScreen(
    onOpenHealthConnect: () -> Unit = {},
    onSyncHealthConnect: () -> Unit = {}
) {
    val ctx = LocalContext.current
    var editName by remember { mutableStateOf(false) }
    var about by remember { mutableStateOf(false) }

    if (editName) {
        var t by remember { mutableStateOf(Store.userName) }
        AlertDialog(
            onDismissRequest = { editName = false },
            title = { Text("Ubah Nama Pengguna", fontWeight = FontWeight.Bold) },
            text = { OutlinedTextField(t, { t = it }, singleLine = true) },
            confirmButton = {
                TextButton(onClick = {
                    if (t.isNotBlank()) Store.userName = t.trim()
                    editName = false
                }) { Text("Simpan", color = SageGreen, fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { editName = false }) { Text("Batal", color = Muted) }
            },
        )
    }

    if (about) {
        AlertDialog(
            onDismissRequest = { about = false },
            title = { Text("Asclepios Health AI", fontWeight = FontWeight.Bold) },
            text = {
                Text("Versi 1.3.0\nAplikasi pemantau kesehatan cerdas dengan integrasi Android Health Connect dan Gemini AI Coach.")
            },
            confirmButton = {
                TextButton(onClick = { about = false }) { Text("OK", color = SageGreen) }
            },
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AsclepiosBg),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.avatar_user),
                    contentDescription = "User Avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(92.dp)
                        .clip(CircleShape)
                        .border(2.dp, CardBorder, CircleShape)
                        .clickable { editName = true }
                )
                Spacer(Modifier.height(14.dp))
                Text(
                    text = Store.userName,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Ink
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = if (Store.hasHealthConnect) "Health Connect Aktif  |  Skor 88" else "Menunggu Izin Sensor  |  Skor 88",
                    fontSize = 12.sp,
                    color = Muted
                )
            }
        }

        item {
            Text(
                text = "Health Connect & Sensor Smartwatch",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = Muted,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        item {
            LinkRow(
                icon = Icons.Default.HealthAndSafety,
                label = if (Store.hasHealthConnect) "Health Connect: Terhubung" else "Buka Izin Health Connect (Allow All)"
            ) {
                onOpenHealthConnect()
            }
        }
        item {
            LinkRow(Icons.Default.Sync, "Sinkronisasi Smartwatch Sekarang") {
                onSyncHealthConnect()
            }
        }

        item {
            Text(
                text = "Pengaturan Aplikasi",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = Muted,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(22.dp))
                    .background(CardWhite)
                    .border(1.dp, CardBorder, RoundedCornerShape(22.dp))
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CircleIcon(Icons.Default.Notifications, SageGreen, SagePillBg, size = 38.dp)
                Text(
                    text = "Notifikasi Pengingat",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Ink,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp)
                )
                Switch(
                    checked = Store.notifications,
                    onCheckedChange = { Store.notifications = it },
                    colors = SwitchDefaults.colors(checkedTrackColor = SageGreen)
                )
            }
        }
        item {
            LinkRow(Icons.Default.Edit, "Ubah Nama Profil") { editName = true }
        }

        item {
            Text(
                text = "Bantuan & Informasi",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = Muted,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        item { LinkRow(Icons.Default.Language, "Situs Web WHO Kesehatan") { openUrl(ctx, "https://www.who.int/health-topics") } }
        item { LinkRow(Icons.Default.PrivacyTip, "Kebijakan Privasi") { openUrl(ctx, "https://www.who.int/about/policies/privacy") } }
        item { LinkRow(Icons.Default.Email, "Hubungi Dukungan") { sendEmail(ctx, "support@asclepios.app", "Bantuan Asclepios Health") } }
        item { LinkRow(Icons.Default.Share, "Bagikan Aplikasi") { shareText(ctx, "Coba Asclepios Health: https://asclepios.app") } }
        item { LinkRow(Icons.Default.Info, "Tentang Asclepios") { about = true } }
    }
}

@Composable
private fun LinkRow(icon: ImageVector, label: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(CardWhite)
            .border(1.dp, CardBorder, RoundedCornerShape(22.dp))
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CircleIcon(icon, SageGreen, SagePillBg, size = 38.dp)
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Ink,
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp)
        )
        Icon(Icons.Default.ChevronRight, null, tint = Muted)
    }
}
