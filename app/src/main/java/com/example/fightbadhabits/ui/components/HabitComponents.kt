package com.example.fightbadhabits.ui.components

import android.app.DatePickerDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fightbadhabits.R
import com.example.fightbadhabits.data.model.Habit
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HabitCard(habit: Habit, onDelete: () -> Unit, onEditDate: () -> Unit, onReset: () -> Unit) {
    val daysCount = ((System.currentTimeMillis() - habit.lastUsed) / (1000 * 60 * 60 * 24)).toInt().coerceAtLeast(0)
    val imageResource = if (habit.imageResId != 0) habit.imageResId else R.drawable.login

    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(painter = painterResource(id = imageResource), contentDescription = null, modifier = Modifier.size(45.dp))
            Column(modifier = Modifier.weight(1f).padding(horizontal = 12.dp)) {
                Text(text = habit.name, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                Text(text = "Days: $daysCount", color = Color(0xFF4CAF50))
            }
            IconButton(onClick = onReset) { Icon(Icons.Default.Refresh, contentDescription = null, tint = Color(0xFFE91E63)) }
            IconButton(onClick = onEditDate) { Icon(Icons.Default.DateRange, contentDescription = null, tint = Color(0xFF4CAF50)) }
            IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red.copy(alpha = 0.7f)) }
        }
    }
}

@Composable
fun AddHabitDialog(onDismiss: () -> Unit, onConfirm: (String, Long) -> Unit) {
    var text by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(System.currentTimeMillis()) }
    val context = LocalContext.current
    val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Habit") },
        text = {
            Column {
                OutlinedTextField(value = text, onValueChange = { text = it }, label = { Text("Habit Name") })
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    val cal = Calendar.getInstance().apply { timeInMillis = selectedDate }
                    DatePickerDialog(context, { _, y, m, d ->
                        val newCal = Calendar.getInstance().apply { set(y, m, d) }
                        selectedDate = newCal.timeInMillis
                    }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
                }, modifier = Modifier.fillMaxWidth()) {
                    Text(sdf.format(Date(selectedDate)))
                }
            }
        },
        confirmButton = { Button(onClick = { if (text.isNotBlank()) onConfirm(text, selectedDate) }) { Text("OK") } }
    )
}