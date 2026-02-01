package com.example.fightbadhabits.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fightbadhabits.ui.profile.ProfileViewModel
import com.example.fightbadhabits.util.NotificationHelper

@Composable
fun ProfileScreen(
    onLogout: () -> Unit,
    viewModel: ProfileViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val user = viewModel.userData.value
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Hello, ${user?.name ?: "User"}!", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = { NotificationHelper.scheduleNotification(context, "Test", "Works", 5) }) {
            Text("Test notification (5s)")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            viewModel.logout()
            onLogout()
        }) {
            Text("Log out")
        }
    }
}