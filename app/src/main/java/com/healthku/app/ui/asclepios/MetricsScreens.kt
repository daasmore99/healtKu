package com.healthku.app.ui.asclepios

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.healthku.app.R

/** "See All" destination: every metric displayed with Asclepios design standards. */
@Composable
fun MetricsScreen(go: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AsclepiosBg)
            .statusBarsPadding()
            .padding(top = 16.dp)
    ) {
        Text(
            text = "Smart Health Metrics",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Ink,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        Text(
            text = "Ketuk kartu untuk melihat detail historis dan analisis",
            fontSize = 12.sp,
            color = Muted,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            items(Store.metrics) { m ->
                MetricCard(m, modifier = Modifier.fillMaxWidth()) { go(m.route) }
            }
        }
    }
}

@Composable
fun MetricCard(m: Metric, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .height(174.dp)
            .clip(RoundedCornerShape(26.dp))
            .background(CardWhite)
            .border(1.dp, CardBorder, RoundedCornerShape(26.dp))
            .clickable(onClick = onClick)
            .padding(14.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CircleIcon(painter = painterResource(id = m.iconRes), m.tint, m.bg, size = 30.dp)
                Spacer(Modifier.width(8.dp))
                Text(
                    text = m.name,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Ink,
                    maxLines = 1
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                when (m.name) {
                    "Blood Pressure" -> {
                        Image(
                            painter = painterResource(id = R.drawable.stethoscope_heart),
                            contentDescription = "Blood Pressure Graphic",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(72.dp)
                        )
                    }
                    "Heart Rate" -> {
                        Heart3D(size = 68.dp)
                    }
                    else -> {
                        CircleIcon(painter = painterResource(id = m.iconRes), m.tint, m.bg, size = 52.dp)
                    }
                }
            }
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = m.value.substringBefore('/'),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Ink
                )
                Spacer(Modifier.width(3.dp))
                Text(
                    text = m.unit,
                    fontSize = 11.sp,
                    color = Muted,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            }
        }
    }
}

@Composable
fun DetailScreen(name: String, onBack: () -> Unit) {
    val ctx = LocalContext.current
    val m = Store.metric(name)
    val days = listOf("Sen", "Sel", "Rab", "Kam", "Jum", "Sab", "Min")
    val grow = remember { Animatable(0f) }
    LaunchedEffect(Unit) { grow.animateTo(1f, tween(900, easing = FastOutSlowInEasing)) }
    val max = m.series.max()
    val min = m.series.min()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AsclepiosBg),
        contentPadding = PaddingValues(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SqButton(Icons.AutoMirrored.Filled.ArrowBack, 44.dp, onClick = onBack)
                Text(
                    text = m.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Ink,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
                SqButton(Icons.Default.Share, 44.dp) {
                    shareText(ctx, "${m.name}: ${m.value} ${m.unit} (${m.status})")
                }
            }
        }
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Reveal {
                    if (m.name == "Blood Pressure") {
                        Image(
                            painter = painterResource(id = R.drawable.stethoscope_heart),
                            contentDescription = "Blood Pressure",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(110.dp)
                        )
                    } else {
                        Heart3D(110.dp)
                    }
                }
                Spacer(Modifier.height(12.dp))
                Text(
                    text = "${m.value} ${m.unit}",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = Ink
                )
                Text(
                    text = m.status,
                    fontSize = 13.sp,
                    color = Muted
                )
            }
        }
        item {
            Reveal {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(26.dp))
                        .background(CardWhite)
                        .border(1.dp, CardBorder, RoundedCornerShape(26.dp))
                        .padding(18.dp)
                ) {
                    Text(
                        text = "7 Hari Terakhir",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Ink
                    )
                    Spacer(Modifier.height(16.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        m.series.forEachIndexed { i, v ->
                            val frac = (0.3f + 0.7f * (v - min) / (max - min).coerceAtLeast(0.001f)) * grow.value
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Bottom,
                                modifier = Modifier.fillMaxHeight()
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(26.dp)
                                        .fillMaxHeight(frac * 0.85f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (i == 6) SageGreen else Color(0xFFCFE0C5)),
                                )
                                Spacer(Modifier.height(6.dp))
                                Text(days[i], fontSize = 10.sp, color = Muted)
                            }
                        }
                    }
                }
            }
        }
        item {
            Reveal {
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    listOf(
                        "Rata-rata" to m.series.average(),
                        "Tertinggi" to m.series.max().toDouble(),
                        "Terendah" to m.series.min().toDouble()
                    ).forEach { (label, value) ->
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(22.dp))
                                .background(CardWhite)
                                .border(1.dp, CardBorder, RoundedCornerShape(22.dp))
                                .padding(14.dp)
                        ) {
                            Text(label, fontSize = 11.sp, color = Muted)
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = if (value >= 100) "${value.toInt()}" else "%.1f".format(value),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Ink
                            )
                        }
                    }
                }
            }
        }
        item {
            Reveal {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth()
                        .height(54.dp)
                        .clip(RoundedCornerShape(27.dp))
                        .background(Ink)
                        .clickable { openUrl(ctx, m.url) }
                        .padding(horizontal = 22.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Pelajari tentang ${m.name}",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(Icons.Default.OpenInNew, null, tint = Color.White, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}
