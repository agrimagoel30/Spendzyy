package com.agrima.spendzyy.screens


//class ExpenseViewModel : ViewModel() {
//
//    private val _expenseList = mutableStateListOf<Expense>()
//    val expenseList: List<Expense> get() = _expenseList
//
//    var monthlyBudget by mutableStateOf(10_000)
//        private set
//
//    fun addExpense(expense: Expense) {
//        _expenseList.add(expense)
//    }
//
//
//    fun deleteExpense(expense: Expense) {
//        _expenseList.remove(expense)
//    }
//
//    fun getCurrentMonthExpenses(): List<Expense> {
//        val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
//        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
//
//        return _expenseList.filter {
//            val cal = Calendar.getInstance().apply {
//                timeInMillis = it.timestamp
//            }
//            cal.get(Calendar.MONTH) == currentMonth &&
//                    cal.get(Calendar.YEAR) == currentYear
//        }
//    }
//
//    // ✅ FIXED
//    fun getCategoryTotals(expenses: List<Expense>): Map<String, Int> {
//        return expenses
//            .groupBy { it.category }
//            .mapValues { entry ->
//                entry.value.sumOf { it.amount }
//            }
//    }
//
//    // ✅ FIXED
//    fun getCategoryPercentages(expenses: List<Expense>): Map<String, Float> {
//        val total = expenses.sumOf { it.amount }.toFloat()
//        if (total == 0f) return emptyMap()
//
//        return getCategoryTotals(expenses).mapValues { (_, amount) ->
//            (amount / total) * 100f
//        }
//    }
//
//    fun getAvailableYears(): List<Int> {
//        return expenseList
//            .map {
//                Calendar.getInstance().apply {
//                    timeInMillis = it.timestamp
//                }.get(Calendar.YEAR)
//            }
//            .distinct()
//            .sortedDescending()
//    }
//
//    fun getMonthsForYear(year: Int): List<Int> {
//        return expenseList
//            .filter {
//                val cal = Calendar.getInstance().apply {
//                    timeInMillis = it.timestamp
//                }
//                cal.get(Calendar.YEAR) == year
//            }
//            .map {
//                Calendar.getInstance().apply {
//                    timeInMillis = it.timestamp
//                }.get(Calendar.MONTH)
//            }
//            .distinct()
//            .sorted()
//    }
//
//
//
//
//    fun getExpensesForMonth(year: Int, month: Int): List<Expense> {
//        return expenseList.filter { expense ->
//            val cal = Calendar.getInstance().apply {
//                timeInMillis = expense.timestamp
//            }
//
//            cal.get(Calendar.YEAR) == year &&
//                    cal.get(Calendar.MONTH)== month   // ⚠️ month is 0-based
//        }
//    }
//
//
//
//
//
//    fun updateMonthlyBudget(newBudget: Int) {
//        monthlyBudget = newBudget
//    }
//
//
//    fun getExpenseById(id: Long): Expense? {
//        return _expenseList.find { it.id == id }
//    }
//
//    fun updateExpense(updated: Expense) {
//        val index = _expenseList.indexOfFirst { it.id == updated.id }
//        if (index != -1) {
//            _expenseList[index] = updated
//        }
//    }
//
//
//}




