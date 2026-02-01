package com.example.fightbadhabits.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.filled.LocationOn

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Habits : Screen(
        route = "habits",
        title = "Habits",
        icon = Icons.Default.List
    )
    object Stats : Screen(
        route = "stats",
        title = "Stats",
        icon = Icons.Default.Info
    )
    object Support : Screen(
        route = "support",
        title = "Find Help",
        icon = Icons.Default.LocationOn
    )
    object Profile : Screen(
        route = "profile",
        title = "Profile",
        icon = Icons.Default.Person
    )

    object Login : Screen(
        route = "login",
        title = "Login",
        icon = Icons.Default.Person
    )
}