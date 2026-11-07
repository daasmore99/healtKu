package com.healthku.app

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.health.connect.client.PermissionController
import com.healthku.app.data.HealthConnectManager
import com.healthku.app.ui.asclepios.*
import kotlinx.coroutines.launch

private data class Tab(val route: String, val label: String, val icon: ImageVector)

private val tabs = listOf(
    Tab("home", "Home", Icons.Default.Home),
    Tab("metrics", "Metrik", Icons.Default.MonitorHeart),
    Tab("sleep", "Tidur", Icons.Default.Bedtime),
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

        setContent {
            AsclepiosTheme {
                val nav = rememberNavController()
                val route = nav.currentBackStackEntryAsState().value?.destination?.route
                val go: (String) -> Unit = { dest -> nav.navigate(dest) { launchSingleTop = true } }
                val back: () -> Unit = { nav.popBackStack() }
                val scope = rememberCoroutineScope()

                val syncRealData: () -> Unit = {
                    scope.launch {
                        if (healthConnectManager.isHealthConnectAvailable()) {
                            if (!healthConnectManager.hasAllPermissions()) {
                                requestPermissions.launch(healthConnectManager.permissions)
                            }
                            val realData = healthConnectManager.readRealHealthData()
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
                            NavigationBar(containerColor = Color.White) {
                                tabs.forEach { t ->
                                    NavigationBarItem(
                                        selected = route == t.route,
                                        onClick = { switchTab(nav, t.route) },
                                        icon = { Icon(t.icon, null) },
                                        label = { Text(t.label) },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = Color.White,
                                            indicatorColor = Forest,
                                            selectedTextColor = Forest,
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
