package com.example.fightbadhabits.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.fightbadhabits.R
import com.example.fightbadhabits.ui.components.AddHabitDialog
import com.example.fightbadhabits.ui.components.HabitCard
import com.example.fightbadhabits.ui.habits.HabitViewModel
import java.util.*

@Composable
fun HabitListScreen(viewModel: HabitViewModel) {
    val habits = viewModel.habits
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AddHabitDialog(
            onDismiss = { showDialog = false },
            onConfirm = { name, startDate ->
                viewModel.addHabit(name, R.drawable.habiticon, startDate)
                showDialog = false
            }
        )
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }, containerColor = Color(0xFF4CAF50)) {
                Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            Image(
                painter = painterResource(id = R.drawable.background),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(habits) { habit ->
                    HabitCard(
                        habit = habit,
                        onDelete = { viewModel.removeHabit(habit) },
                        onReset = { viewModel.addReset(habit, System.currentTimeMillis()) },
                        onEditDate = {
                            val cal = Calendar.getInstance()
                            DatePickerDialog(context, { _, y, m, d ->
                                val selectedCal = Calendar.getInstance().apply { set(y, m, d) }
                                viewModel.addPastReset(habit, selectedCal.timeInMillis)
                            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
                        }
                    )
                }
            }
        }
    }
}