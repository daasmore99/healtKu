package com.healthku.app.ui.asclepios

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(
    go: (String) -> Unit,
    onSyncHealthConnect: () -> Unit = {},
    onOpenHealthConnect: () -> Unit = {}
) {
    val ctx = LocalContext.current
    val score by animateIntAsState(88, tween(1200, easing = FastOutSlowInEasing), label = "score")
    var scoreDialog by remember { mutableStateOf(false) }
    val query = Store.query.trim()
    val results = Store.metrics.filter { it.name.contains(query, true) }
    val rows = Store.metrics.drop(4)

    if (scoreDialog) AlertDialog(
        onDismissRequest = { scoreDialog = false },
        title = { Text("Asclepios Score: 88") },
        text = { Text("Skor dihitung dari detak jantung, tekanan darah, oksigen, kualitas tidur, dan aktivitas harian Anda. Skor 88 berarti kondisi di atas rata-rata.") },
        confirmButton = { TextButton({ scoreDialog = false; openUrl(ctx, "https://www.who.int/health-topics/cardiovascular-diseases") }) { Text("Pelajari lebih lanjut") } },
        dismissButton = { TextButton({ scoreDialog = false }) { Text("Tutup") } },
    )

    LazyColumn(
        Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color(0xFFF6EDE1), Color(0xFFEFE3D3)))),
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item {
            Column(
                Modifier.fillMaxWidth().shadow(2.dp, RoundedCornerShape(bottomStart = 36.dp, bottomEnd = 36.dp))
                    .background(Color.White, RoundedCornerShape(bottomStart = 36.dp, bottomEnd = 36.dp))
                    .statusBarsPadding().padding(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 20.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier.size(52.dp).clip(RoundedCornerShape(14.dp)).clickable { go("profile") }
                            .background(Brush.linearGradient(listOf(Color(0xFFFDE68A), Color(0xFFF59E0B)))),
                        contentAlignment = Alignment.Center,
                    ) { Icon(Icons.Default.Person, null, tint = Color.White, modifier = Modifier.size(34.dp)) }
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f).clickable { go("profile") }) {
                        Text("Hey, ${Store.userName} \uD83D\uDC4B", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.height(2.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.MonitorHeart, null, Modifier.size(11.dp), tint = Muted)
                            Text(if (Store.hasHealthConnect) " Sensor Aktif  |  " else " 88%  |  ", fontSize = 10.sp, color = Muted)
                            Icon(Icons.Default.Shield, null, Modifier.size(11.dp), tint = Muted)
                            Text(" Pro Member", fontSize = 10.sp, color = Muted)
                        }
                    }
                    MoreButton(
                        listOf(
                            "Sync Smartwatch" to onSyncHealthConnect,
                            "Izin Health Connect" to onOpenHealthConnect,
                            "Profil" to { go("profile") },
                            "Semua metrik" to { go("metrics") },
                            "Tidur" to { go("sleep") },
                            "Pusat bantuan" to { openUrl(ctx, "https://www.who.int/health-topics") },
                        ), size = 48.dp, bg = CreamDeep,
                    )
                }
                Spacer(Modifier.height(18.dp))
                Row(
                    Modifier.fillMaxWidth().height(46.dp).clip(RoundedCornerShape(16.dp)).background(CreamDeep).padding(start = 14.dp, end = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    BasicTextField(
                        value = Store.query, onValueChange = { Store.query = it }, singleLine = true,
                        textStyle = TextStyle(fontSize = 13.sp, color = Ink), cursorBrush = SolidColor(Forest),
                        keyboardOptions = KeyboardOptions(imeAction = androidx.compose.ui.text.input.ImeAction.Search),
                        modifier = Modifier.weight(1f),
                        decorationBox = { inner -> if (Store.query.isEmpty()) Text("Search aklepios...", fontSize = 12.sp, color = Muted); inner() },
                    )
                    Box(
                        Modifier.size(34.dp).clip(RoundedCornerShape(12.dp)).background(Color.White).clickable { Store.query = "" },
                        contentAlignment = Alignment.Center,
                    ) { Icon(if (query.isEmpty()) Icons.Default.Search else Icons.Default.Close, null, Modifier.size(17.dp)) }
                }

                if (!Store.hasHealthConnect) {
                    Spacer(Modifier.height(12.dp))
                    Row(
                        Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(Color(0xFFE3EBDC)).clickable { onOpenHealthConnect() }.padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.HealthAndSafety, null, tint = Forest, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Hubungkan Smartwatch (Health Connect)", fontSize = 11.sp, color = Forest, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                        Text("Buka Izin", fontSize = 11.sp, color = Forest, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        if (query.isNotEmpty()) {
            item { Text("${results.size} hasil untuk \"$query\"", fontSize = 12.sp, color = Muted, modifier = Modifier.padding(horizontal = 20.dp)) }
            items(results) { m -> MetricRow(m) { go(m.route) } }
        } else {
            item {
                Reveal {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        SectionHeader("Health Score") {
                            MoreButton(listOf("Lihat detail skor" to { scoreDialog = true }, "Bagikan skor" to { shareText(ctx, "Asclepios Score saya: 88") }), 28.dp, Color.Transparent)
                        }
                        Row(
                            Modifier.padding(horizontal = 20.dp).fillMaxWidth().height(100.dp).clip(RoundedCornerShape(26.dp))
                                .background(Color(0xFFFFFDF5)).clickable { scoreDialog = true }.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Column(Modifier.weight(1f)) {
                                Text("Asclepios Score", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                                Spacer(Modifier.height(4.dp))
                                Text("Based on your data, we think your health status is above average", fontSize = 9.sp, color = Color(0xFF555555), lineHeight = 12.sp)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("87", fontSize = 10.sp, color = Color.LightGray)
                                Box(Modifier.size(52.dp).clip(RoundedCornerShape(14.dp)).background(Color(0xFFE3EBDC)), contentAlignment = Alignment.Center) {
                                    Text("$score", fontSize = 24.sp, fontWeight = FontWeight.SemiBold, color = Forest)
                                }
                                Text("89", fontSize = 10.sp, color = Color.LightGray)
                            }
                        }
                    }
                }
            }
            item {
                Reveal {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        SectionHeader("Smart Health Metrics") {
                            Text("See All", fontSize = 11.sp, color = Muted, modifier = Modifier.clickable { go("metrics") }.padding(4.dp))
                        }
                        val state = rememberLazyListState()
                        LazyRow(
                            state = state, flingBehavior = rememberSnapFlingBehavior(state),
                            contentPadding = PaddingValues(horizontal = 20.dp), horizontalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            items(Store.metrics.take(4)) { m -> MetricCard(m, green = m.name != "Blood Pressure") { go(m.route) } }
                        }
                    }
                }
            }
            item {
                Reveal {
                    SectionHeader("Daily Activity") {
                        MoreButton(listOf("Semua metrik" to { go("metrics") }, "Catatan tidur" to { go("sleep") }))
                    }
                }
            }
            items(rows) { m -> Reveal(40) { MetricRow(m) { go(m.route) } } }
            item { Reveal { MetricRow(Store.metrics[0].copy(name = "Sleep Log", value = "6.4", unit = "h", icon = Icons.Default.Bedtime, tint = Forest, bg = Color(0xFFD6E8CC))) { go("sleep") } } }
        }
    }
}

@Composable
fun MetricRow(m: Metric, onClick: () -> Unit) {
    Row(
        Modifier.padding(horizontal = 20.dp).fillMaxWidth().clip(RoundedCornerShape(26.dp))
            .background(Brush.horizontalGradient(listOf(CardWhite, Color(0xFFF0F4D9)))).clickable(onClick = onClick).padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CircleIcon(m.icon, m.tint, m.bg, 44.dp)
        Text(m.name, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f).padding(start = 12.dp))
        Text("${m.value} ${m.unit}", fontSize = 12.sp)
        Icon(Icons.Default.ChevronRight, null, tint = Muted, modifier = Modifier.padding(start = 4.dp))
    }
}

@Composable
fun MetricCard(m: Metric, green: Boolean, modifier: Modifier = Modifier.width(176.dp), onClick: () -> Unit) {
    val float by rememberInfiniteTransition(label = "f").animateFloat(
        0f, -6f, infiniteRepeatable(tween(1500, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "y",
    )
    Box(modifier.height(176.dp).clip(RoundedCornerShape(30.dp)).background(CardWhite).clickable(onClick = onClick).padding(14.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CircleIcon(m.icon, m.tint, m.bg)
            Text(m.name, fontSize = 12.sp, modifier = Modifier.padding(start = 8.dp), maxLines = 1)
        }
        Heart3D(86.dp, green, Modifier.align(Alignment.Center).offset(y = float.dp + 8.dp))
        Row(
            Modifier.align(Alignment.BottomStart).clip(RoundedCornerShape(14.dp)).background(CreamDeep).padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.Bottom,
        ) {
            Text(m.value.substringBefore('/'), fontSize = 26.sp, fontWeight = FontWeight.SemiBold)
            Text(m.unit, fontSize = 9.sp, modifier = Modifier.padding(start = 2.dp, bottom = 5.dp))
        }
    }
}
