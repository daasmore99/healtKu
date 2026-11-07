package com.healthku.app

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.health.connect.client.PermissionController
import com.healthku.app.ai.AICoachEngine
import com.healthku.app.data.DailyHealthSummary
import com.healthku.app.data.HealthConnectManager
import com.healthku.app.data.WorkoutCategory
import com.healthku.app.ui.screens.AsclepiosAICoachScreen
import com.healthku.app.ui.screens.AsclepiosBloodPressureScreen
import com.healthku.app.ui.screens.AsclepiosHomeScreen
import com.healthku.app.ui.screens.AsclepiosSleepScreen
import com.healthku.app.ui.theme.AsclepiosBg
import com.healthku.app.ui.theme.AsclepiosCard
import com.healthku.app.ui.theme.AsclepiosCardBorder
import com.healthku.app.ui.theme.AsclepiosTextMuted
import com.healthku.app.ui.theme.HealthKuTheme
import com.healthku.app.ui.theme.SagePillBg
import com.healthku.app.ui.theme.SagePillGreen
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var healthConnectManager: HealthConnectManager
    private val aiCoachEngine = AICoachEngine()

    private val requestPermissions = registerForActivityResult(
        PermissionController.createRequestPermissionResultContract()
    ) { granted ->
        if (granted.containsAll(healthConnectManager.permissions)) {
            Toast.makeText(this, "Izin Health Connect Diberikan! Mengambil data sensor...", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Izin belum lengkap. Anda juga bisa membukanya via Buka Izin di HP.", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        healthConnectManager = HealthConnectManager(this)

        val sharedPref = getSharedPreferences("healthku_prefs", Context.MODE_PRIVATE)
        val initialApiKey = sharedPref.getString("gemini_api_key", "") ?: ""

        setContent {
            HealthKuTheme {
                var selectedTab by remember { mutableIntStateOf(0) }
                var summary by remember { mutableStateOf(DailyHealthSummary()) }
                var apiKey by remember { mutableStateOf(initialApiKey) }
                val scope = rememberCoroutineScope()

                var aiReport by remember {
                    mutableStateOf(aiCoachEngine.generateLocalBriefing(summary, WorkoutCategory.WEIGHTLIFTING))
                }

                // Sync data real from Health Connect
                val syncRealData: () -> Unit = {
                    scope.launch {
                        if (healthConnectManager.isHealthConnectAvailable()) {
                            if (!healthConnectManager.hasAllPermissions()) {
                                requestPermissions.launch(healthConnectManager.permissions)
                            }
                            val realData = healthConnectManager.readRealHealthData()
                            summary = realData
                            aiReport = if (apiKey.isNotBlank()) {
                                aiCoachEngine.queryGeminiCoach(apiKey, realData, WorkoutCategory.WEIGHTLIFTING)
                            } else {
                                aiCoachEngine.generateLocalBriefing(realData, WorkoutCategory.WEIGHTLIFTING)
                            }
                            if (realData.hasHealthConnectPermission) {
                                Toast.makeText(this@MainActivity, "Data Real Tersinkron", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this@MainActivity, "Health Connect belum terpasang di HP Anda", Toast.LENGTH_LONG).show()
                        }
                    }
                }

                LaunchedEffect(Unit) {
                    syncRealData()
                }

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AsclepiosBg),
                    bottomBar = {
                        NavigationBar(
                            containerColor = AsclepiosCard,
                            tonalElevation = 0.dp,
                            modifier = Modifier.border(0.5.dp, AsclepiosCardBorder)
                        ) {
                            NavigationBarItem(
                                selected = selectedTab == 0,
                                onClick = { selectedTab = 0 },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.Home,
                                        contentDescription = "Home",
                                        modifier = Modifier.size(22.dp)
                                    )
                                },
                                label = { Text("Home", fontSize = 10.sp) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = SagePillGreen,
                                    selectedTextColor = SagePillGreen,
                                    indicatorColor = SagePillBg,
                                    unselectedIconColor = AsclepiosTextMuted,
                                    unselectedTextColor = AsclepiosTextMuted
                                )
                            )

                            NavigationBarItem(
                                selected = selectedTab == 1,
                                onClick = { selectedTab = 1 },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.MonitorHeart,
                                        contentDescription = "Vitals",
                                        modifier = Modifier.size(22.dp)
                                    )
                                },
                                label = { Text("Pressure", fontSize = 10.sp) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = SagePillGreen,
                                    selectedTextColor = SagePillGreen,
                                    indicatorColor = SagePillBg,
                                    unselectedIconColor = AsclepiosTextMuted,
                                    unselectedTextColor = AsclepiosTextMuted
                                )
                            )

                            NavigationBarItem(
                                selected = selectedTab == 2,
                                onClick = { selectedTab = 2 },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.Bedtime,
                                        contentDescription = "Sleep",
                                        modifier = Modifier.size(22.dp)
                                    )
                                },
                                label = { Text("Sleep", fontSize = 10.sp) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = SagePillGreen,
                                    selectedTextColor = SagePillGreen,
                                    indicatorColor = SagePillBg,
                                    unselectedIconColor = AsclepiosTextMuted,
                                    unselectedTextColor = AsclepiosTextMuted
                                )
                            )

                            NavigationBarItem(
                                selected = selectedTab == 3,
                                onClick = { selectedTab = 3 },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = "Assistant",
                                        modifier = Modifier.size(22.dp)
                                    )
                                },
                                label = { Text("Assistant", fontSize = 10.sp) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = SagePillGreen,
                                    selectedTextColor = SagePillGreen,
                                    indicatorColor = SagePillBg,
                                    unselectedIconColor = AsclepiosTextMuted,
                                    unselectedTextColor = AsclepiosTextMuted
                                )
                            )
                        }
                    }
                ) { innerPadding ->
                    when (selectedTab) {
                        0 -> AsclepiosHomeScreen(
                            summary = summary,
                            onOpenLiveBp = { selectedTab = 1 },
                            onSyncHealthConnect = syncRealData,
                            onOpenHealthConnectSettings = {
                                healthConnectManager.openHealthConnectSettings(this@MainActivity)
                            },
                            modifier = Modifier.padding(innerPadding)
                        )
                        1 -> AsclepiosBloodPressureScreen(
                            summary = summary,
                            onBack = { selectedTab = 0 },
                            modifier = Modifier.padding(innerPadding)
                        )
                        2 -> AsclepiosSleepScreen(
                            summary = summary,
                            onBack = { selectedTab = 0 },
                            modifier = Modifier.padding(innerPadding)
                        )
                        3 -> AsclepiosAICoachScreen(
                            summary = summary,
                            aiReport = aiReport,
                            apiKey = apiKey,
                            onSaveApiKey = { newKey ->
                                apiKey = newKey
                                sharedPref.edit().putString("gemini_api_key", newKey).apply()
                                Toast.makeText(this@MainActivity, "API Key Disimpan!", Toast.LENGTH_SHORT).show()
                            },
                            onSyncHealthConnect = syncRealData,
                            onOpenHealthConnectSettings = {
                                healthConnectManager.openHealthConnectSettings(this@MainActivity)
                            },
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}
