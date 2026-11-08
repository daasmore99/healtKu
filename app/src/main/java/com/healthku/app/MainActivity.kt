package com.healthku.app

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.health.connect.client.PermissionController
import com.healthku.app.data.DailyHealthSummary
import com.healthku.app.data.HealthConnectManager
import com.healthku.app.ui.asclepios.*
import kotlinx.coroutines.launch

private data class Tab(val route: String, val label: String, val icon: ImageVector)

private val tabs = listOf(
    Tab("home", "Home", Icons.Default.Home),
    Tab("metrics", "Metrik", Icons.Default.MonitorHeart),
    Tab("sleep", "Tidur", Icons.Default.Bedtime),
    Tab("ai", "AI Coach", Icons.Default.AutoAwesome),
    Tab("profile", "Profil", Icons.Default.Person),
)

class MainActivity : ComponentActivity() {

    private lateinit var healthConnectManager: HealthConnectManager

    private val requestPermissions = registerForActivityResult(
        PermissionController.createRequestPermissionResultContract()
    ) { granted ->
        if (granted.containsAll(healthConnectManager.permissions)) {
            Toast.makeText(this, "Izin Health Connect Diberikan! Mengambil data sensor...", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Izin Health Connect belum lengkap", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        healthConnectManager = HealthConnectManager(this)

        val sharedPref = getSharedPreferences("healthku_prefs", Context.MODE_PRIVATE)
        val initialApiKey = sharedPref.getString("gemini_api_key", "") ?: ""

        setContent {
            AsclepiosTheme {
                val nav = rememberNavController()
                val route = nav.currentBackStackEntryAsState().value?.destination?.route
                val go: (String) -> Unit = { dest -> nav.navigate(dest) { launchSingleTop = true } }
                val back: () -> Unit = { nav.popBackStack() }
                val scope = rememberCoroutineScope()

                var currentSummary by remember { mutableStateOf(DailyHealthSummary()) }
                var apiKey by remember { mutableStateOf(initialApiKey) }

                val syncRealData: () -> Unit = {
                    scope.launch {
                        if (healthConnectManager.isHealthConnectAvailable()) {
                            if (!healthConnectManager.hasAllPermissions()) {
                                requestPermissions.launch(healthConnectManager.permissions)
                            }
                            val realData = healthConnectManager.readRealHealthData()
                            currentSummary = realData
                            Store.updateWithRealData(realData)
                            if (realData.hasHealthConnectPermission) {
                                Toast.makeText(this@MainActivity, "Data Real Tersinkron", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this@MainActivity, "Health Connect belum terpasang di HP Anda", Toast.LENGTH_LONG).show()
                        }
                    }
                }

                val openHealthConnectSettings: () -> Unit = {
                    healthConnectManager.openHealthConnectSettings(this@MainActivity)
                }

                LaunchedEffect(Unit) {
                    syncRealData()
                }

                Scaffold(
                    containerColor = Cream,
                    bottomBar = {
                        if (route in tabs.map { it.route }) {
                            NavigationBar(containerColor = CardWhite) {
                                tabs.forEach { t ->
                                    val isSelected = route == t.route
                                    NavigationBarItem(
                                        selected = isSelected,
                                        onClick = { switchTab(nav, t.route) },
                                        icon = { Icon(t.icon, null, modifier = Modifier.size(22.dp)) },
                                        label = { Text(t.label, fontSize = 10.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = SageGreen,
                                            indicatorColor = SagePillBg,
                                            selectedTextColor = SageGreen,
                                            unselectedIconColor = Muted,
                                            unselectedTextColor = Muted
                                        ),
                                    )
                                }
                            }
                        }
                    },
                ) { inner ->
                    NavHost(
                        navController = nav,
                        startDestination = "home",
                        modifier = Modifier.padding(bottom = inner.calculateBottomPadding())
                    ) {
                        composable("home") {
                            HomeScreen(
                                go = go,
                                onSyncHealthConnect = syncRealData,
                                onOpenHealthConnect = openHealthConnectSettings
                            )
                        }
                        composable("metrics") { MetricsScreen(go) }
                        composable("bp") { BloodPressureScreen(back, go) }
                        composable("sleep") { SleepScreen(back) }
                        composable("ai") {
                            AISummaryScreen(
                                summary = currentSummary,
                                apiKey = apiKey,
                                onSaveApiKey = { newKey ->
                                    apiKey = newKey
                                    sharedPref.edit().putString("gemini_api_key", newKey).apply()
                                    Toast.makeText(this@MainActivity, "Gemini API Key Disimpan!", Toast.LENGTH_SHORT).show()
                                },
                                onSyncHealthConnect = syncRealData,
                                onOpenHealthConnect = openHealthConnectSettings
                            )
                        }
                        composable("profile") {
                            ProfileScreen(
                                onOpenHealthConnect = openHealthConnectSettings,
                                onSyncHealthConnect = syncRealData
                            )
                        }
                        composable("detail/{name}") { e ->
                            DetailScreen(e.arguments?.getString("name") ?: "Heart Rate", back)
                        }
                    }
                }
            }
        }
    }
}

private fun switchTab(nav: NavHostController, route: String) {
    nav.navigate(route) {
        popUpTo(nav.graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
