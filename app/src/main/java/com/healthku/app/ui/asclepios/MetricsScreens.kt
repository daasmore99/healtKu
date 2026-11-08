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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.healthku.app.ui.asclepios.Store

/** "See All" destination: every metric as a tappable card. */
@Composable
fun MetricsScreen(go: (String) -> Unit) {
    Column(Modifier.fillMaxSize().background(Cream).statusBarsPadding().padding(top = 16.dp)) {
        Text("Smart Health Metrics", fontSize = 22.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(horizontal = 20.dp))
        Text("Ketuk kartu untuk melihat detail", fontSize = 12.sp, color = Muted, modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp))
        LazyVerticalGrid(
            GridCells.Fixed(2), contentPadding = PaddingValues(20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(Store.metrics) { m -> MetricCard(m, green = m.name != "Blood Pressure", modifier = Modifier.fillMaxWidth()) { go(m.route) } }
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
    val max = m.series.max(); val min = m.series.min()

    LazyColumn(
        Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color(0xFFF3E8DC), Color(0xFFF0E2D3)))),
        contentPadding = PaddingValues(bottom = 32.dp), verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Row(Modifier.fillMaxWidth().statusBarsPadding().padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                SqButton(Icons.AutoMirrored.Filled.ArrowBack, 48.dp, onClick = onBack)
                Text(m.name, fontSize = 17.sp, fontWeight = FontWeight.Medium, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
                SqButton(Icons.Default.Share, 48.dp) { shareText(ctx, "${m.name}: ${m.value} ${m.unit} (${m.status})") }
            }
        }
        item {
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Reveal { Heart3D(120.dp, green = m.name != "Blood Pressure") }
                Text("${m.value} ${m.unit}", fontSize = 40.sp, fontWeight = FontWeight.SemiBold)
                Text(m.status, fontSize = 13.sp, color = Muted)
            }
        }
        item {
            Reveal {
                Column(Modifier.padding(horizontal = 20.dp).fillMaxWidth().clip(RoundedCornerShape(28.dp)).background(CardWhite).padding(18.dp)) {
                    Text("7 hari terakhir", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(14.dp))
                    Row(Modifier.fillMaxWidth().height(140.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
                        m.series.forEachIndexed { i, v ->
                            val frac = (0.3f + 0.7f * (v - min) / (max - min).coerceAtLeast(0.001f)) * grow.value
                            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Bottom, modifier = Modifier.fillMaxHeight()) {
                                Box(
                                    Modifier.width(26.dp).fillMaxHeight(frac * 0.85f).clip(RoundedCornerShape(10.dp))
                                        .background(if (i == 6) Forest else Color(0xFFCFE0C5)),
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
                Row(Modifier.padding(horizontal = 20.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    listOf("Rata-rata" to m.series.average(), "Tertinggi" to m.series.max().toDouble(), "Terendah" to m.series.min().toDouble()).forEach { (l, v) ->
                        Column(Modifier.weight(1f).clip(RoundedCornerShape(22.dp)).background(CardWhite).padding(14.dp)) {
                            Text(l, fontSize = 11.sp, color = Muted)
                            Text(if (v >= 100) "${v.toInt()}" else "%.1f".format(v), fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }
        item {
            Reveal {
                Row(
                    Modifier.padding(horizontal = 20.dp).fillMaxWidth().clip(RoundedCornerShape(50)).background(Ink)
                        .clickable { openUrl(ctx, m.url) }.padding(horizontal = 24.dp, vertical = 18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("Pelajari tentang ${m.name}", color = Color.White, fontSize = 14.sp, modifier = Modifier.weight(1f))
                    Icon(Icons.Default.OpenInNew, null, tint = Color.White)
                }
            }
        }
    }
}
