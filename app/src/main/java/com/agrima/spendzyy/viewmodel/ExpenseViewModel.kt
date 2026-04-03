package com.agrima.spendzyy.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agrima.spendzyy.data.local.entity.ExpenseEntity
import com.agrima.spendzyy.data.repository.ExpenseRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import kotlin.collections.emptyList

class ExpenseViewModel(
    private val repository: ExpenseRepository
) : ViewModel() {

    /* -------------------- BUDGET -------------------- */

    var monthlyBudget by mutableStateOf(10_000)
        private set

    fun updateMonthlyBudget(newBudget: Int) {
        monthlyBudget = newBudget
    }

    /* -------------------- ALL EXPENSES -------------------- */

    // 🔥 Room → Flow → Compose State
    val allExpenses = repository.getAllExpenses()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    /* -------------------- ADD / UPDATE / DELETE -------------------- */

    fun addExpense(expense: ExpenseEntity) {
        viewModelScope.launch {
            repository.insertExpense(expense)
        }
    }

    fun updateExpense(expense: ExpenseEntity) {
        viewModelScope.launch {
            repository.updateExpense(expense)
        }
    }

    fun deleteExpense(expense: ExpenseEntity) {
        viewModelScope.launch {
            repository.deleteExpense(expense)
        }
    }

    /* -------------------- GET BY ID (EDIT) -------------------- */

    suspend fun getExpenseById(id: Long): ExpenseEntity? {
        return repository.getExpenseById(id)
    }

    /* -------------------- MONTH / YEAR -------------------- */

    fun getExpensesForMonth(year: Int, month: Int) =
        repository.getExpensesByMonth(year, month)

    /* -------------------- CALCULATIONS (UI HELPERS) -------------------- */

    fun getCategoryTotals(expenses: List<ExpenseEntity>): Map<String, Int> {
        return expenses
            .groupBy { it.category }
            .mapValues { it.value.sumOf { e -> e.amount } }
    }

//    fun getCategoryPercentages(expenses: List<ExpenseEntity>): Map<String, Float> {
//        val total = expenses.sumOf { it.amount }.toFloat()
//        if (total == 0f) return emptyMap()
//
//        return getCategoryTotals(expenses).mapValues { (, amount) ->
//            (amount / total) * 100f
//        }
//    }

    fun getCategoryPercentages(
        expenses: List<ExpenseEntity>
    ): Map<String, Float> {

        val total = expenses.sumOf { it.amount }.toFloat()
        if (total == 0f) return emptyMap()

        val categoryTotals = getCategoryTotals(expenses)

        return categoryTotals.mapValues { entry ->
            (entry.value / total) * 100f
        }
    }


    fun getAvailableYears(expenses: List<ExpenseEntity>): List<Int> {
        return expenses
            .map {
                Calendar.getInstance().apply {
                    timeInMillis = it.timestamp
                }.get(Calendar.YEAR)
            }
            .distinct()
            .sortedDescending()
    }

    fun getMonthsForYear(expenses: List<ExpenseEntity>, year: Int): List<Int> {
        return expenses
            .filter {
                Calendar.getInstance().apply {
                    timeInMillis = it.timestamp
                }.get(Calendar.YEAR) == year
            }
            .map {
                Calendar.getInstance().apply {
                    timeInMillis = it.timestamp
                }.get(Calendar.MONTH)
            }
            .distinct()
            .sorted()
    }
}