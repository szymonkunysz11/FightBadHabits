package com.example.fightbadhabits.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fightbadhabits.data.model.Habit
import com.example.fightbadhabits.ui.habits.HabitViewModel
import com.example.fightbadhabits.util.DateUtils
import com.example.fightbadhabits.util.NotificationHelper
import java.util.*

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StatsScreen(viewModel: HabitViewModel) {
    val habits = viewModel.habits
    var selectedHabit by remember { mutableStateOf<Habit?>(null) }
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Your Progress", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(habits) { habit ->
                FilterChip(
                    selected = selectedHabit == habit,
                    onClick = { selectedHabit = habit },
                    label = { Text(habit.name) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF4CAF50),
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (selectedHabit != null) {
            val habit = selectedHabit!!
            val bestStreak = DateUtils.getBestStreak(habit)
            val currentStreak = ((System.currentTimeMillis() - habit.lastUsed) / (1000 * 60 * 60 * 24)).toInt().coerceAtLeast(0)


            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Stats for: ${habit.name}", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Current: $currentStreak days", color = Color(0xFF2E7D32))
                        Text(text = "Best: $bestStreak days", color = Color(0xFFFBC02D), fontWeight = FontWeight.Bold)
                    }
                }
            }

            val calendar = Calendar.getInstance()
            val currentYear = calendar.get(Calendar.YEAR)
            val currentMonth = calendar.get(Calendar.MONTH)
            val maxDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

            Text("Monthly Calendar", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                maxItemsInEachRow = 7,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                for (day in 1..maxDaysInMonth) {
                    val dayCal = Calendar.getInstance().apply { set(currentYear, currentMonth, day, 0, 0, 0); set(Calendar.MILLISECOND, 0) }
                    val dayTimestamp = dayCal.timeInMillis
                    val habitStart = DateUtils.getStartOfDay(habit.createdAt)
                    val isResetDay = habit.resets.any { DateUtils.isSameDay(dayTimestamp, it) }

                    val boxColor = when {
                        isResetDay -> Color(0xFFE91E63)
                        dayTimestamp < habitStart || dayTimestamp > System.currentTimeMillis() -> Color.LightGray.copy(alpha = 0.3f)
                        else -> Color(0xFF4CAF50)
                    }

                    Surface(
                        modifier = Modifier.size(42.dp).padding(2.dp).clickable {
                            if (dayTimestamp in habitStart..System.currentTimeMillis()) {
                                if (isResetDay) viewModel.removeReset(habit, dayTimestamp)
                                else viewModel.addReset(habit, dayTimestamp)
                            }
                        },
                        shape = RoundedCornerShape(8.dp),
                        color = boxColor
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(text = day.toString(), color = Color.White, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}