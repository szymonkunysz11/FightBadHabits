package com.example.fightbadhabits.ui.habits

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import com.example.fightbadhabits.data.model.Habit
import com.example.fightbadhabits.util.DateUtils
import com.example.fightbadhabits.util.NotificationHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HabitViewModel(application: Application) : AndroidViewModel(application) {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val userId get() = auth.currentUser?.uid

    var habits = mutableStateListOf<Habit>()
        private set

    init {
        loadHabits()
    }

    private fun loadHabits() {
        val uid = userId ?: return
        db.collection("users").document(uid).collection("habits")
            .addSnapshotListener { value, error ->
                if (error != null) return@addSnapshotListener

                val items = value?.documents?.mapNotNull { doc ->
                    doc.toObject(Habit::class.java)?.copy(id = doc.id)
                } ?: emptyList()

                habits.clear()
                habits.addAll(items)
            }
    }

    fun addHabit(name: String, imageResId: Int, startDate: Long) {
        val uid = userId ?: return
        val habitRef = db.collection("users").document(uid).collection("habits").document()

        val startOfDay = DateUtils.getStartOfDay(startDate)
        val newHabit = Habit(
            id = habitRef.id,
            name = name,
            imageResId = imageResId,
            createdAt = startDate,
            lastUsed = startDate,
            resets = listOf(startOfDay)
        )
        habitRef.set(newHabit)
    }

    fun removeHabit(habit: Habit) {
        val uid = userId ?: return
        db.collection("users").document(uid).collection("habits").document(habit.id).delete()
    }

    fun addPastReset(habit: Habit, pastDate: Long) {
        val uid = userId ?: return
        val dateToAdd = DateUtils.getStartOfDay(pastDate)

        //prevents adding a reset from before the habit was created
        if (dateToAdd < DateUtils.getStartOfDay(habit.createdAt)) return

        val updatedResets = (habit.resets + dateToAdd).distinct().sorted()
        val updatedHabit = habit.copy(
            resets = updatedResets,
            lastUsed = updatedResets.maxOrNull() ?: habit.createdAt
        )

        updateHabitInFirestore(uid, updatedHabit)
    }

    fun addReset(habit: Habit, timestamp: Long) {
        addPastReset(habit, timestamp)

        NotificationHelper.scheduleNotification(
            getApplication(),
            "Do not give up",
            "A day has passed since your last failure regarding ${habit.name}.",
            86400 // 24h
        )
    }

    fun removeReset(habit: Habit, timestamp: Long) {
        val uid = userId ?: return
        val updatedResets = habit.resets.filterNot { DateUtils.isSameDay(it, timestamp) }.sorted()
        val updatedHabit = habit.copy(
            resets = updatedResets,
            lastUsed = updatedResets.maxOrNull() ?: habit.createdAt
        )
        updateHabitInFirestore(uid, updatedHabit)
    }

    private fun updateHabitInFirestore(uid: String, habit: Habit) {
        db.collection("users").document(uid).collection("habits").document(habit.id).set(habit)
    }
}