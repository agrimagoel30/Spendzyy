package com.agrima.spendzyy.data.local.dao

import androidx.room.*
import com.agrima.spendzyy.data.local.entity.ExpenseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    // 1️⃣ Insert (Add Expense)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: ExpenseEntity)

    // 2️⃣ Update (Edit Expense)
    @Update
    suspend fun updateExpense(expense: ExpenseEntity)

    // 3️⃣ Delete
    @Delete
    suspend fun deleteExpense(expense: ExpenseEntity)

    // 4️⃣ Get ALL expenses (latest first)
    @Query("SELECT * FROM expenses ORDER BY timestamp DESC")
    fun getAllExpenses(): Flow<List<ExpenseEntity>>

    // 5️⃣ Get expense by ID (EDIT ke liye)
    @Query("SELECT * FROM expenses WHERE id = :id")
    suspend fun getExpenseById(id: Long): ExpenseEntity?

    // 6️⃣ Get expenses by month + year
    @Query("""
        SELECT * FROM expenses 
        WHERE year = :year AND month = :month 
        ORDER BY timestamp DESC
    """)
    fun getExpensesByMonth(
        year: Int,
        month: Int
    ): Flow<List<ExpenseEntity>>
}
