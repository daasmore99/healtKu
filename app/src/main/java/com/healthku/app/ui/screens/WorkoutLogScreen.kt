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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import com.healthku.app.data.WorkoutCategory
import com.healthku.app.data.WorkoutSession
import com.healthku.app.ui.theme.BgCream
import com.healthku.app.ui.theme.BorderSubtle
import com.healthku.app.ui.theme.CardWhite
import com.healthku.app.ui.theme.PeachCalories
import com.healthku.app.ui.theme.SageGreenDark
import com.healthku.app.ui.theme.SageGreenMedium
import com.healthku.app.ui.theme.TextMuted
import com.healthku.app.ui.theme.TextPrimary
import com.healthku.app.ui.theme.TextSecondary

@Composable
fun WorkoutLogScreen(
    modifier: Modifier = Modifier
) {
    val workouts = remember {
        mutableStateListOf(
            WorkoutSession(
                id = "1",
                category = WorkoutCategory.WEIGHTLIFTING,
                title = "Push Day (Chest, Shoulders, Triceps)",
                durationMinutes = 55,
                calories = 380,
                avgHeartRate = 124,
                dateText = "Hari ini • 07:30"
            ),
            WorkoutSession(
                id = "2",
                category = WorkoutCategory.BRISK_WALKING,
                title = "Jalan Cepat - Zone 2 Recovery",
                durationMinutes = 35,
                calories = 190,
                avgHeartRate = 108,
                dateText = "Kemarin • 17:15"
            ),
            WorkoutSession(
                id = "3",
                category = WorkoutCategory.RUNNING,
                title = "Interval 5K Run",
                durationMinutes = 28,
                calories = 320,
                avgHeartRate = 152,
                dateText = "2 hari lalu • 06:15"
            )
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BgCream)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        // Screen Header
        Text(
            text = "Workout & Challenges",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Text(
            text = "Track angkat beban, lari, & jalan cepat",
            fontSize = 13.sp,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Discipline Streak Banner
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, BorderSubtle, RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = CardWhite)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFCF0EB)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = null,
                            tint = PeachCalories,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "7 Hari Beruntun",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Disiplin target langkah & latihan tercapai",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFE4EFE7))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "Streak 🔥",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = SageGreenDark
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Quick Log Buttons
        Text(
            text = "Catat Sesi Baru",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            QuickLogButton(
                title = "Beban",
                icon = Icons.Default.FitnessCenter,
                modifier = Modifier.weight(1f)
            ) {
                workouts.add(
                    0,
                    WorkoutSession(
                        id = System.currentTimeMillis().toString(),
                        category = WorkoutCategory.WEIGHTLIFTING,
                        title = "Sesi Angkat Beban Baru",
                        durationMinutes = 45,
                        calories = 300,
                        avgHeartRate = 120,
                        dateText = "Baru saja"
                    )
                )
            }

            QuickLogButton(
                title = "Lari",
                icon = Icons.Default.DirectionsRun,
                modifier = Modifier.weight(1f)
            ) {
                workouts.add(
                    0,
                    WorkoutSession(
                        id = System.currentTimeMillis().toString(),
                        category = WorkoutCategory.RUNNING,
                        title = "Sesi Lari Outdoor",
                        durationMinutes = 30,
                        calories = 280,
                        avgHeartRate = 145,
                        dateText = "Baru saja"
                    )
                )
            }

            QuickLogButton(
                title = "Jalan Cepat",
                icon = Icons.Default.DirectionsWalk,
                modifier = Modifier.weight(1f)
            ) {
                workouts.add(
                    0,
                    WorkoutSession(
                        id = System.currentTimeMillis().toString(),
                        category = WorkoutCategory.BRISK_WALKING,
                        title = "Jalan Cepat - Zone 2",
                        durationMinutes = 35,
                        calories = 180,
                        avgHeartRate = 105,
                        dateText = "Baru saja"
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Riwayat Latihan",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(workouts) { session ->
                WorkoutItemCard(session = session)
            }
        }
    }
}

@Composable
fun QuickLogButton(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .border(1.dp, BorderSubtle, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = SageGreenDark,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
        }
    }
}

@Composable
fun WorkoutItemCard(
    session: WorkoutSession
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, BorderSubtle, RoundedCornerShape(18.dp)),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEBF3EE)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when (session.category) {
                            WorkoutCategory.WEIGHTLIFTING -> Icons.Default.FitnessCenter
                            WorkoutCategory.RUNNING -> Icons.Default.DirectionsRun
                            WorkoutCategory.BRISK_WALKING -> Icons.Default.DirectionsWalk
                        },
                        contentDescription = null,
                        tint = SageGreenDark,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = session.title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "${session.durationMinutes} mnt • ${session.calories} kcal • HR ${session.avgHeartRate} bpm",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }

            Text(
                text = session.dateText,
                fontSize = 11.sp,
                color = TextMuted
            )
        }
    }
}
