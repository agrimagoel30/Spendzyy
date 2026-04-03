package com.agrima.spendzyy.model

import java.util.UUID

data class Expense(
    val id: Long = System.currentTimeMillis(), // 🔥 IMPORTANT
    val title: String,
    val amount: Int,
    val category: String,
    val date: String,
    val description: String="",
    val timestamp: Long = System.currentTimeMillis()
)
