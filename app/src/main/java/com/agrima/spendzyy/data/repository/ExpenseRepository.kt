package com.agrima.spendzyy.data.repository

import com.agrima.spendzyy.data.local.dao.ExpenseDao
import com.agrima.spendzyy.data.local.entity.ExpenseEntity
import kotlinx.coroutines.flow.Flow

class ExpenseRepository(
    private val expenseDao: ExpenseDao
) {

    // 🔹 Get all expenses
    fun getAllExpenses(): Flow<List<ExpenseEntity>> {
        return expenseDao.getAllExpenses()
    }

    // 🔹 Get expenses by month + year
    fun getExpensesByMonth(year: Int, month: Int): Flow<List<ExpenseEntity>> {
        return expenseDao.getExpensesByMonth(year, month)
    }

    // 🔹 Get single expense (for edit)
    suspend fun getExpenseById(id: Long): ExpenseEntity? {
        return expenseDao.getExpenseById(id)
    }

    // 🔹 Insert
    suspend fun insertExpense(expense: ExpenseEntity) {
        expenseDao.insertExpense(expense)
    }

    // 🔹 Update
    suspend fun updateExpense(expense: ExpenseEntity) {
        expenseDao.updateExpense(expense)
    }

    // 🔹 Delete
    suspend fun deleteExpense(expense: ExpenseEntity) {
        expenseDao.deleteExpense(expense)
    }
}

