package com.example.fightbadhabits.data.model

data class Habit(
    val id: String = "",
    val name: String = "",
    val imageResId: Int = 0,
    val lastUsed: Long = System.currentTimeMillis(),
    val createdAt: Long = System.currentTimeMillis(),
    val resets: List<Long> = emptyList()
)