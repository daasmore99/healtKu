package com.healthku.app.ui.asclepios

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.healthku.app.R

@Composable
fun HomeScreen(
    go: (String) -> Unit,
    onSyncHealthConnect: () -> Unit = {},
    onOpenHealthConnect: () -> Unit = {}
) {
    val ctx = LocalContext.current
    val score by animateIntAsState(88, tween(1000, easing = FastOutSlowInEasing), label = "score")
    var scoreDialog by remember { mutableStateOf(false) }
    val query = Store.query.trim()
    val results = Store.metrics.filter { it.name.contains(query, true) }

    val hrMetric = Store.metrics.firstOrNull { it.name == "Heart Rate" }
    val bpMetric = Store.metrics.firstOrNull { it.name == "Blood Pressure" }
    val calMetric = Store.metrics.firstOrNull { it.name == "Calories Burned" }
    val stepsMetric = Store.metrics.firstOrNull { it.name == "Steps Walked" }

    if (scoreDialog) {
        AlertDialog(
            onDismissRequest = { scoreDialog = false },
            title = { Text("Asclepios Score: 88", fontWeight = FontWeight.Bold) },
            text = { Text("Skor dihitung dari detak jantung, tekanan darah, saturasi oksigen, kualitas tidur, dan aktivitas harian. Skor 88 menunjukkan kondisi di atas rata-rata.") },
            confirmButton = {
                TextButton(onClick = {
                    scoreDialog = false
                    openUrl(ctx, "https://www.who.int/health-topics/cardiovascular-diseases")
                }) { Text("Pelajari Lebih Lanjut", color = SageGreen) }
            },
            dismissButton = {
                TextButton(onClick = { scoreDialog = false }) { Text("Tutup", color = Muted) }
            },
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AsclepiosBg),
        contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // 1. Profile Header & Search Bar
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Profile Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.avatar_user),
                        contentDescription = "User Avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .border(1.dp, CardBorder, CircleShape)
                            .clickable { go("profile") }
                    )
                    Spacer(Modifier.width(12.dp))
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { go("profile") }
                    ) {
                        Text(
                            text = "Hey, ${Store.userName} 👋",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Ink
                        )
                        Spacer(Modifier.height(2.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AutoAwesome, null, Modifier.size(11.dp), tint = SageGreen)
                            Text(" 88%  |  ", fontSize = 11.sp, color = Muted)
                            Icon(Icons.Default.VerifiedUser, null, Modifier.size(11.dp), tint = Muted)
                            Text(" Pro Member", fontSize = 11.sp, color = Muted)
                        }
                    }
                    MoreButton(
                        items = listOf(
                            "Sync Smartwatch" to onSyncHealthConnect,
                            "Izin Health Connect" to onOpenHealthConnect,
                            "Profil" to { go("profile") },
                            "Semua Metrik" to { go("metrics") },
                            "Tidur" to { go("sleep") },
                            "Pusat Bantuan" to { openUrl(ctx, "https://www.who.int/health-topics") },
                        ),
                        size = 44.dp
                    )
                }

                // Search Capsule
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .clip(RoundedCornerShape(23.dp))
                        .background(CardWhite)
                        .border(1.dp, CardBorder, RoundedCornerShape(23.dp))
                        .padding(start = 16.dp, end = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    BasicTextField(
                        value = Store.query,
                        onValueChange = { Store.query = it },
                        singleLine = true,
                        textStyle = TextStyle(fontSize = 13.sp, color = Ink),
                        cursorBrush = SolidColor(SageGreen),
                        keyboardOptions = KeyboardOptions(imeAction = androidx.compose.ui.text.input.ImeAction.Search),
                        modifier = Modifier.weight(1f),
                        decorationBox = { inner ->
                            if (Store.query.isEmpty()) {
                                Text("Search aklepios...", fontSize = 13.sp, color = Muted)
                            }
                            inner()
                        },
                    )
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(AsclepiosBg)
                            .clickable { if (Store.query.isNotEmpty()) Store.query = "" },
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = if (query.isEmpty()) Icons.Default.Search else Icons.Default.Close,
                            contentDescription = "Search",
                            tint = Ink,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                // Health Connect Integration Banner (if not connected)
                if (!Store.hasHealthConnect) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(SagePillBg)
                            .clickable { onOpenHealthConnect() }
                            .padding(horizontal = 14.dp, vertical = 9.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(painter = painterResource(id = R.drawable.ic_smartwatch), null, tint = SageGreen, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "Hubungkan Smartwatch (Health Connect)",
                            fontSize = 11.sp,
                            color = SageGreen,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.weight(1f)
                        )
                        Text("Buka Izin", fontSize = 11.sp, color = SageGreen, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        if (query.isNotEmpty()) {
            item {
                Text(
                    "${results.size} hasil untuk \"$query\"",
                    fontSize = 12.sp,
                    color = Muted,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }
            items(results) { m ->
                MetricRow(m) { go(m.route) }
            }
        } else {
            // 2. Health Score Section
            item {
                Reveal {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        SectionHeader("Health Score") {
                            MoreButton(
                                items = listOf(
                                    "Lihat detail skor" to { scoreDialog = true },
                                    "Bagikan skor" to { shareText(ctx, "Asclepios Score saya: 88") }
                                ),
                                size = 32.dp
                            )
                        }
                        // Asclepios Score Card
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                                .fillMaxWidth()
                                .height(104.dp)
                                .clip(RoundedCornerShape(26.dp))
                                .background(CardWhite)
                                .border(1.dp, CardBorder, RoundedCornerShape(26.dp))
                                .clickable { scoreDialog = true }
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Asclepios Score",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Ink
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    text = "Based on your data, we think your health status is above average",
                                    fontSize = 11.sp,
                                    color = Color(0xFF7C7871),
                                    lineHeight = 15.sp
                                )
                            }
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(start = 12.dp)
                            ) {
                                Text("87", fontSize = 11.sp, color = Color(0xFFB8B2A6), fontWeight = FontWeight.Medium)
                                Spacer(Modifier.height(2.dp))
                                Box(
                                    modifier = Modifier
                                        .size(width = 52.dp, height = 44.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(SagePillBg),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "$score",
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SageGreen
                                    )
                                }
                                Spacer(Modifier.height(2.dp))
                                Text("89", fontSize = 11.sp, color = Color(0xFFB8B2A6), fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                }
            }

            // 3. Smart Health Metrics (2-COLUMN SIDE-BY-SIDE GRID)
            item {
                Reveal {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        SectionHeader("Smart Health Metrics") {
                            Text(
                                text = "See All",
                                fontSize = 13.sp,
                                color = Muted,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier
                                    .clickable { go("metrics") }
                                    .padding(vertical = 4.dp)
                            )
                        }

                        // Exact 2-Column Row matching reference
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            // Column 1: Heart Rate Card
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(174.dp)
                                    .clip(RoundedCornerShape(26.dp))
                                    .background(CardWhite)
                                    .border(1.dp, CardBorder, RoundedCornerShape(26.dp))
                                    .clickable { go(hrMetric?.route ?: "detail/Heart Rate") }
                                    .padding(14.dp)
                            ) {
                                Column(modifier = Modifier.fillMaxSize()) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        CircleIcon(
                                            painter = painterResource(id = R.drawable.ic_heart_pulse),
                                            tint = Color(0xFF4B8E9D),
                                            bg = Color(0xFFE0EFF2),
                                            size = 30.dp
                                        )
                                        Spacer(Modifier.width(8.dp))
                                        Text(
                                            text = "Heart Rate",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Ink
                                        )
                                    }

                                    // Center graphic: 3D Heart with sparkline indicator
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxWidth(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Heart3D(size = 70.dp)
                                        // Floating sparkline tag
                                        Row(
                                            modifier = Modifier
                                                .align(Alignment.BottomEnd)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(CardWhite.copy(alpha = 0.9f))
                                                .border(0.5.dp, CardBorder, RoundedCornerShape(8.dp))
                                                .padding(horizontal = 5.dp, vertical = 2.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text("bpm", fontSize = 8.sp, color = Muted)
                                            Spacer(Modifier.width(3.dp))
                                            SparklineWave(color = Color(0xFF4B8E9D), modifier = Modifier.size(28.dp, 10.dp))
                                        }
                                    }

                                    // Bottom row: Value
                                    Row(verticalAlignment = Alignment.Bottom) {
                                        Text(
                                            text = hrMetric?.value ?: "70",
                                            fontSize = 22.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Ink
                                        )
                                        Spacer(Modifier.width(3.dp))
                                        Text(
                                            text = "bpm",
                                            fontSize = 12.sp,
                                            color = Muted,
                                            modifier = Modifier.padding(bottom = 2.dp)
                                        )
                                    }
                                }
                            }

                            // Column 2: Blood Pressure Card
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(174.dp)
                                    .clip(RoundedCornerShape(26.dp))
                                    .background(CardWhite)
                                    .border(1.dp, CardBorder, RoundedCornerShape(26.dp))
                                    .clickable { go("bp") }
                                    .padding(14.dp)
                            ) {
                                Column(modifier = Modifier.fillMaxSize()) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        CircleIcon(
                                            painter = painterResource(id = R.drawable.ic_stethoscope),
                                            tint = Color(0xFFC85B7A),
                                            bg = Color(0xFFF7E4EA),
                                            size = 30.dp
                                        )
                                        Spacer(Modifier.width(8.dp))
                                        Text(
                                            text = "Blood Pressure",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Ink,
                                            maxLines = 1
                                        )
                                    }

                                    // Center graphic: Stethoscope wrapping around 3D heart
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxWidth(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.stethoscope_heart),
                                            contentDescription = "Blood Pressure Graphic",
                                            contentScale = ContentScale.Fit,
                                            modifier = Modifier.size(76.dp)
                                        )
                                    }

                                    // Bottom row: Value
                                    Row(verticalAlignment = Alignment.Bottom) {
                                        Text(
                                            text = bpMetric?.value ?: "120/80",
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Ink
                                        )
                                        Spacer(Modifier.width(3.dp))
                                        Text(
                                            text = "mmHg",
                                            fontSize = 11.sp,
                                            color = Muted,
                                            modifier = Modifier.padding(bottom = 2.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 4. Secondary Section: Smart Health Metrics (Calories Burned & Activity)
            item {
                Reveal {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        SectionHeader("Smart Health Metrics") {
                            MoreButton(
                                items = listOf(
                                    "Semua metrik" to { go("metrics") },
                                    "Catatan tidur" to { go("sleep") }
                                ),
                                size = 32.dp
                            )
                        }

                        // Horizontal Card: Calories Burned
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                                .fillMaxWidth()
                                .height(78.dp)
                                .clip(RoundedCornerShape(24.dp))
                                .background(CardWhite)
                                .border(1.dp, CardBorder, RoundedCornerShape(24.dp))
                                .clickable { go(calMetric?.route ?: "detail/Calories Burned") }
                                .padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            CircleIcon(
                                painter = painterResource(id = R.drawable.ic_flame),
                                tint = Color(0xFFE27B30),
                                bg = Color(0xFFFDF0E2),
                                size = 42.dp
                            )
                            Spacer(Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Calories Burned",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Ink
                                )
                                Spacer(Modifier.height(2.dp))
                                Text(
                                    text = "${calMetric?.value ?: "2000"} kcal • No Suggestion",
                                    fontSize = 11.sp,
                                    color = Muted
                                )
                            }
                            Text(
                                text = "${calMetric?.value ?: "2000"} kcal",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Ink
                            )
                        }

                        // Horizontal Card: Steps Walked
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                                .fillMaxWidth()
                                .height(78.dp)
                                .clip(RoundedCornerShape(24.dp))
                                .background(CardWhite)
                                .border(1.dp, CardBorder, RoundedCornerShape(24.dp))
                                .clickable { go(stepsMetric?.route ?: "detail/Steps Walked") }
                                .padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            CircleIcon(
                                painter = painterResource(id = R.drawable.ic_footprints),
                                tint = Color(0xFF4B8E9D),
                                bg = Color(0xFFE0EFF2),
                                size = 42.dp
                            )
                            Spacer(Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Steps Walked",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Ink
                                )
                                Spacer(Modifier.height(2.dp))
                                Text(
                                    text = "${stepsMetric?.value ?: "8420"} steps • Target 10,000",
                                    fontSize = 11.sp,
                                    color = Muted
                                )
                            }
                            Text(
                                text = "${stepsMetric?.value ?: "8420"}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Ink
                            )
                        }

                        // Horizontal Card: Sleep Overview Quick Link
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                                .fillMaxWidth()
                                .height(78.dp)
                                .clip(RoundedCornerShape(24.dp))
                                .background(CardWhite)
                                .border(1.dp, CardBorder, RoundedCornerShape(24.dp))
                                .clickable { go("sleep") }
                                .padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            CircleIcon(
                                painter = painterResource(id = R.drawable.ic_moon),
                                tint = SageGreen,
                                bg = SagePillBg,
                                size = 42.dp
                            )
                            Spacer(Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Sleep Analysis",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Ink
                                )
                                Spacer(Modifier.height(2.dp))
                                Text(
                                    text = "Deep REM • 126+ irregularities",
                                    fontSize = 11.sp,
                                    color = Muted
                                )
                            }
                            Text(
                                text = "8.2h",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Ink
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MetricRow(m: Metric, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(CardWhite)
            .border(1.dp, CardBorder, RoundedCornerShape(22.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CircleIcon(painter = painterResource(id = m.iconRes), m.tint, m.bg, 40.dp)
        Text(
            text = m.name,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Ink,
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp)
        )
        Text(
            text = "${m.value} ${m.unit}",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Ink
        )
        Icon(Icons.Default.ChevronRight, null, tint = Muted, modifier = Modifier.padding(start = 4.dp))
    }
}
