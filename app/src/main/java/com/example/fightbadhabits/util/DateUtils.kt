package com.example.fightbadhabits.util

import com.example.fightbadhabits.data.model.Habit
import java.util.*

object DateUtils {
    fun getBestStreak(habit: Habit): Int {
        val dayMillis = 1000 * 60 * 60 * 24L
        val startDay = getStartOfDay(habit.createdAt)
        val validResets = habit.resets.filter { it >= startDay }.sorted()
        if (validResets.isEmpty()) return ((getStartOfDay(System.currentTimeMillis()) - startDay) / dayMillis).toInt().coerceAtLeast(0)
        var maxDiff = validResets.first() - startDay
        for (i in 0 until validResets.size - 1) {
            val diff = validResets[i+1] - validResets[i]
            if (diff > maxDiff) maxDiff = diff
        }
        val currentDiff = System.currentTimeMillis() - validResets.last()
        if (currentDiff > maxDiff) maxDiff = currentDiff
        return (maxDiff / dayMillis).toInt().coerceAtLeast(0)
    }

    fun isSameDay(t1: Long, t2: Long): Boolean {
        val cal1 = Calendar.getInstance().apply { timeInMillis = t1 }
        val cal2 = Calendar.getInstance().apply { timeInMillis = t2 }
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }

    fun getStartOfDay(timestamp: Long): Long {
        val cal = Calendar.getInstance().apply {
            timeInMillis = timestamp
            set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
        }
        return cal.timeInMillis
    }
}