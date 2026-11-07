package com.healthku.app.ui.asclepios

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
            onDismissRequest = { editName = false }, title = { Text("Ubah nama") },
            text = { OutlinedTextField(t, { t = it }, singleLine = true) },
            confirmButton = { TextButton({ if (t.isNotBlank()) Store.userName = t.trim(); editName = false }) { Text("Simpan") } },
            dismissButton = { TextButton({ editName = false }) { Text("Batal") } },
        )
    }
    if (about) AlertDialog(
        onDismissRequest = { about = false }, title = { Text("Asclepios Health") },
        text = { Text("Versi 1.3.0\nAplikasi pemantau kesehatan: detak jantung, tekanan darah, tidur, dan aktivitas harian dengan integrasi resmi Android Health Connect.") },
        confirmButton = { TextButton({ about = false }) { Text("OK") } },
    )

    LazyColumn(
        Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color(0xFFF6EDE1), Color(0xFFEFE3D3)))),
        contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Column(Modifier.fillMaxWidth().statusBarsPadding(), horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    Modifier.size(96.dp).clip(RoundedCornerShape(28.dp)).background(Brush.linearGradient(listOf(Color(0xFFFDE68A), Color(0xFFF59E0B)))),
                    contentAlignment = Alignment.Center,
                ) { Icon(Icons.Default.Person, null, tint = Color.White, modifier = Modifier.size(64.dp)) }
                Spacer(Modifier.height(12.dp))
                Text(Store.userName, fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
                Text(if (Store.hasHealthConnect) "Health Connect Aktif  |  Skor 88" else "Menunggu Izin Sensor  |  Skor 88", fontSize = 12.sp, color = Muted)
            }
        }

        item { Text("Health Connect & Sensor", fontSize = 13.sp, color = Muted, modifier = Modifier.padding(top = 12.dp)) }
        item {
            LinkRow(
                Icons.Default.HealthAndSafety,
                if (Store.hasHealthConnect) "Health Connect: Terhubung" else "Buka Izin Health Connect (Allow All)"
            ) {
                onOpenHealthConnect()
            }
        }
        item {
            LinkRow(Icons.Default.Sync, "Sinkronisasi Smartwatch Sekarang") {
                onSyncHealthConnect()
            }
        }

        item { Text("Pengaturan", fontSize = 13.sp, color = Muted, modifier = Modifier.padding(top = 12.dp)) }
        item {
            Row(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(22.dp)).background(CardWhite).padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CircleIcon(Icons.Default.Notifications, Forest, Color(0xFFE3EBDC))
                Text("Notifikasi", fontSize = 14.sp, modifier = Modifier.weight(1f).padding(start = 12.dp))
                Switch(Store.notifications, { Store.notifications = it }, colors = SwitchDefaults.colors(checkedTrackColor = Forest))
            }
        }
        item { LinkRow(Icons.Default.Edit, "Ubah nama") { editName = true } }
        item { Text("Bantuan & tautan", fontSize = 13.sp, color = Muted, modifier = Modifier.padding(top = 12.dp)) }
        item { LinkRow(Icons.Default.Language, "Situs web WHO Kesehatan") { openUrl(ctx, "https://www.who.int/health-topics") } }
        item { LinkRow(Icons.Default.PrivacyTip, "Kebijakan privasi") { openUrl(ctx, "https://www.who.int/about/policies/privacy") } }
        item { LinkRow(Icons.Default.Email, "Hubungi dukungan") { sendEmail(ctx, "support@asclepios.app", "Bantuan Asclepios Health") } }
        item { LinkRow(Icons.Default.Share, "Bagikan aplikasi") { shareText(ctx, "Coba Asclepios Health: https://asclepios.app") } }
        item { LinkRow(Icons.Default.Info, "Tentang aplikasi") { about = true } }
    }
}

@Composable
private fun LinkRow(icon: ImageVector, label: String, onClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(22.dp)).background(CardWhite).clickable(onClick = onClick).padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CircleIcon(icon, Forest, Color(0xFFE3EBDC))
        Text(label, fontSize = 14.sp, modifier = Modifier.weight(1f).padding(start = 12.dp))
        Icon(Icons.Default.ChevronRight, null, tint = Muted)
    }
}
