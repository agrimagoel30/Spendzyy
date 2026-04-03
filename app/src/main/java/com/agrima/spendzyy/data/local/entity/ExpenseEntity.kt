package com.agrima.spendzyy.data.local.entity


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class ExpenseEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val title: String,

    val amount: Int,

    val category: String,

    val description: String,

    val timestamp: Long,
    val year: Int,
    val month: Int

)
