package com.agrima.spendzyy.model

data class ExpenseFirestoreModel(
    val id: String = "",
    val amount: Int = 0,
    val category: String = "",
    val note: String = "",
    val timestamp: Long = 0
)