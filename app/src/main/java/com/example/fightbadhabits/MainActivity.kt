package com.example.fightbadhabits

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.fightbadhabits.navigation.Screen
import com.example.fightbadhabits.ui.habits.HabitViewModel
import com.example.fightbadhabits.ui.screens.*
import com.example.fightbadhabits.ui.theme.FIghtBadHabitsTheme
import com.example.fightbadhabits.util.NotificationHelper

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NotificationHelper.createChannel(this)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            androidx.core.app.ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 101)
        }

        val habitViewModel = androidx.lifecycle.ViewModelProvider(
            this,
            androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[HabitViewModel::class.java]

        setContent {
            FIghtBadHabitsTheme(dynamicColor = false) {
                val navController = rememberNavController()

                Scaffold(
                    bottomBar = {
                        NavigationBar(containerColor = Color.White) {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentRoute = navBackStackEntry?.destination?.route

                            val items = listOf(Screen.Habits, Screen.Stats, Screen.Support, Screen.Profile)

                            items.forEach { screen ->
                                NavigationBarItem(
                                    icon = { Icon(screen.icon, contentDescription = screen.title) },
                                    label = { Text(screen.title) },
                                    selected = currentRoute == screen.route,
                                    onClick = {
                                        navController.navigate(screen.route) {
                                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Habits.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Screen.Habits.route) { HabitListScreen(habitViewModel) }
                        composable(Screen.Stats.route) { StatsScreen(habitViewModel) }
                        composable(Screen.Support.route) { SupportMapScreen() }

                        composable(Screen.Profile.route) {
                            ProfileScreen(onLogout = {
                                val intent = android.content.Intent(this@MainActivity, LoginActivity::class.java)
                                intent.flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK or android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                                finish()
                            })
                        }
                    }
                }
            }
        }
    }
}