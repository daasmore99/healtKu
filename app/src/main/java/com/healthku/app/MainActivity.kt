package com.healthku.app

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.filled.Settings
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
import com.healthku.app.data.AICoachReport
import com.healthku.app.data.DailyHealthSummary
import com.healthku.app.data.HealthConnectManager
import com.healthku.app.data.WorkoutCategory
import com.healthku.app.ui.screens.BloodPressureLiveScreen
import com.healthku.app.ui.screens.DashboardScreen
import com.healthku.app.ui.screens.SettingsScreen
import com.healthku.app.ui.screens.SleepOverviewScreen
import com.healthku.app.ui.theme.AsclepiosCard
import com.healthku.app.ui.theme.HealthKuTheme
import com.healthku.app.ui.theme.SageDark
import com.healthku.app.ui.theme.SagePillBg
import com.healthku.app.ui.theme.TextMuted
import com.healthku.app.ui.theme.TextPrimary
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var healthConnectManager: HealthConnectManager
    private val aiCoachEngine = AICoachEngine()

    private val requestPermissions = registerForActivityResult(
        PermissionController.createRequestPermissionResultContract()
    ) { granted ->
        if (granted.containsAll(healthConnectManager.permissions)) {
            Toast.makeText(this, "Izin Health Connect Diberikan! Mengambil data real...", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Izin Health Connect belum lengkap", Toast.LENGTH_SHORT).show()
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
                var selectedWorkout by remember { mutableStateOf(WorkoutCategory.WEIGHTLIFTING) }
                var aiReport by remember {
                    mutableStateOf(aiCoachEngine.generateLocalBriefing(summary, selectedWorkout))
                }
                var apiKey by remember { mutableStateOf(initialApiKey) }
                val scope = rememberCoroutineScope()

                // Function to pull real data from Health Connect
                val syncRealData: () -> Unit = {
                    scope.launch {
                        if (healthConnectManager.isHealthConnectAvailable()) {
                            if (!healthConnectManager.hasAllPermissions()) {
                                requestPermissions.launch(healthConnectManager.permissions)
                            }
                            val realData = healthConnectManager.readRealHealthData()
                            summary = realData
                            aiReport = if (apiKey.isNotBlank()) {
                                aiCoachEngine.queryGeminiCoach(apiKey, realData, selectedWorkout)
                            } else {
                                aiCoachEngine.generateLocalBriefing(realData, selectedWorkout)
                            }
                            if (realData.hasHealthConnectPermission) {
                                Toast.makeText(this@MainActivity, "Data Real Berhasil Diperbarui dari Health Connect", Toast.LENGTH_SHORT).show()
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
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        NavigationBar(
                            containerColor = AsclepiosCard,
                            contentColor = TextPrimary,
                            tonalElevation = 0.dp
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
                                label = { Text("Health Score", fontSize = 11.sp) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = SageDark,
                                    selectedTextColor = SageDark,
                                    indicatorColor = SagePillBg,
                                    unselectedIconColor = TextMuted,
                                    unselectedTextColor = TextMuted
                                )
                            )

                            NavigationBarItem(
                                selected = selectedTab == 1,
                                onClick = { selectedTab = 1 },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.MonitorHeart,
                                        contentDescription = "Blood Pressure",
                                        modifier = Modifier.size(22.dp)
                                    )
                                },
                                label = { Text("Pressure", fontSize = 11.sp) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = SageDark,
                                    selectedTextColor = SageDark,
                                    indicatorColor = SagePillBg,
                                    unselectedIconColor = TextMuted,
                                    unselectedTextColor = TextMuted
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
                                label = { Text("Sleep Log", fontSize = 11.sp) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = SageDark,
                                    selectedTextColor = SageDark,
                                    indicatorColor = SagePillBg,
                                    unselectedIconColor = TextMuted,
                                    unselectedTextColor = TextMuted
                                )
                            )

                            NavigationBarItem(
                                selected = selectedTab == 3,
                                onClick = { selectedTab = 3 },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.Settings,
                                        contentDescription = "Settings",
                                        modifier = Modifier.size(22.dp)
                                    )
                                },
                                label = { Text("Settings", fontSize = 11.sp) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = SageDark,
                                    selectedTextColor = SageDark,
                                    indicatorColor = SagePillBg,
                                    unselectedIconColor = TextMuted,
                                    unselectedTextColor = TextMuted
                                )
                            )
                        }
                    }
                ) { innerPadding ->
                    when (selectedTab) {
                        0 -> DashboardScreen(
                            summary = summary,
                            aiReport = aiReport,
                            selectedWorkout = selectedWorkout,
                            onWorkoutChange = { newCat ->
                                selectedWorkout = newCat
                                aiReport = aiCoachEngine.generateLocalBriefing(summary, newCat)
                            },
                            onOpenLiveBp = { selectedTab = 1 },
                            onOpenSleepOverview = { selectedTab = 2 },
                            onSyncHealthConnect = syncRealData,
                            modifier = Modifier.padding(innerPadding)
                        )
                        1 -> BloodPressureLiveScreen(
                            summary = summary,
                            onBack = { selectedTab = 0 },
                            onTriggerReading = syncRealData,
                            modifier = Modifier.padding(innerPadding)
                        )
                        2 -> SleepOverviewScreen(
                            summary = summary,
                            onBack = { selectedTab = 0 },
                            modifier = Modifier.padding(innerPadding)
                        )
                        3 -> SettingsScreen(
                            apiKey = apiKey,
                            onSaveApiKey = { newKey ->
                                apiKey = newKey
                                sharedPref.edit().putString("gemini_api_key", newKey).apply()
                                Toast.makeText(this@MainActivity, "API Key Tersimpan!", Toast.LENGTH_SHORT).show()
                            },
                            onOpenHealthConnectSettings = {
                                try {
                                    val intent = Intent("androidx.health.ACTION_HEALTH_CONNECT_SETTINGS")
                                    startActivity(intent)
                                } catch (e: Exception) {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.apps.healthdata"))
                                    startActivity(intent)
                                }
                            },
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}
