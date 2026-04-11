package com.agrima.spendzyy.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.agrima.spendzyy.components.PieChartLegend
import com.agrima.spendzyy.components.RealPieChart
import com.agrima.spendzyy.data.local.entity.ExpenseEntity
import com.agrima.spendzyy.model.Expense
import com.agrima.spendzyy.ui.theme.SubTextColor
import com.agrima.spendzyy.utils.getFriendlyDate
import com.agrima.spendzyy.utils.getMonthYear
import com.agrima.spendzyy.viewmodel.ExpenseViewModel
import com.google.firebase.auth.FirebaseAuth
import java.util.Calendar


@Composable
fun HomeScreen(
    navController: NavController,
    expenseViewModel: ExpenseViewModel
) {
    val allExpenses by expenseViewModel.allExpenses.collectAsState(emptyList())
    val calendar = Calendar.getInstance()
    val currentMonth = calendar.get(Calendar.MONTH)
    val currentYear = calendar.get(Calendar.YEAR)
    val expenses = allExpenses.filter { expense ->
        val cal = Calendar.getInstance()
        cal.timeInMillis = expense.timestamp

        cal.get(Calendar.MONTH) == currentMonth &&
                cal.get(Calendar.YEAR) == currentYear
    }
    val categoryTotals = expenseViewModel.getCategoryTotals(expenses)

    val totalSpent = expenses.sumOf { it.amount }

    val monthlyBudget = expenseViewModel.monthlyBudget

    val remainingBalance = monthlyBudget - totalSpent

    val recentExpenses = expenses
        .sortedByDescending { it.timestamp }
        .take(5)
    val categoryPercentages = expenseViewModel.getCategoryPercentages(expenses)


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {

        TopHeader(navController)

        Spacer(modifier = Modifier.height(16.dp))

        BudgetSummaryCard(
            monthlyBudget = monthlyBudget,
            totalSpent = totalSpent,
            remainingBalance = remainingBalance
        )

        Spacer(modifier = Modifier.height(16.dp))

//        PieChartSection(expenseViewModel)

        PieChartSection(     categoryData = categoryTotals
        ) // dummy for now

        Spacer(modifier = Modifier.height(16.dp))

        RecentExpensesSection(recentExpenses)
    }
}


/* ---------------- TOP HEADER ---------------- */



@Composable
fun TopHeader(navController: NavController) {
    val user = FirebaseAuth.getInstance().currentUser
    val name = user?.displayName ?: "Guest User"
    val firstLetter = name.first().uppercase()

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Spendzyy",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = getMonthYear(System.currentTimeMillis()),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }

//        Icon(
//            imageVector = Icons.Default.AccountCircle,
//            contentDescription = "Profile",
//            modifier = Modifier
//                .size(36.dp)
//                .clickable {
//                    navController.navigate("profile")
//                },
//            tint = Color.DarkGray
//        )
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color(0xFF4CAF50), shape = CircleShape)
                .clickable{navController.navigate("profile")},
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = firstLetter,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


/* ---------------- SUMMARY CARD ---------------- */

@Composable
fun BudgetSummaryCard(
    monthlyBudget: Int,
    totalSpent: Int,
    remainingBalance: Int
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            BudgetRow(
                label = "Monthly Budget",
                value = "₹$monthlyBudget",
                valueColor = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            BudgetRow(
                label = "Total Spent",
                value = "₹$totalSpent",
                valueColor = Color.Red
            )

            Spacer(modifier = Modifier.height(8.dp))

            BudgetRow(
                label = "Remaining Balance",
                value = "₹$remainingBalance",
                valueColor = Color(0xFF2E7D32)
            )
        }
    }
}

@Composable
fun BudgetRow(
    label: String,
    value: String,
    valueColor: Color
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label,             color = SubTextColor   // 👈 ADD THIS
        )
        Text(
            text = value,
            fontWeight = FontWeight.Bold,
            color = valueColor
        )
    }
}

/* ---------------- PIE CHART PLACEHOLDER ---------------- */


//@Composable
//fun PieChartSection(
////    expenseViewModel: ExpenseViewModel
//    expenses: List<Expense>
//) {
//
//    val categoryData = expenseViewModel.getCategoryTotals()
//    val categoryPercentages = expenseViewModel.getCategoryPercentages()
//
//    if (categoryData.isEmpty()) return   // no chart if no data
//
//    Card(
//        modifier = Modifier.fillMaxWidth(),
//        shape = RoundedCornerShape(18.dp),
//        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface
//        ),
//        elevation = CardDefaults.cardElevation(3.dp)
//    ) {
//        Column(
//            modifier = Modifier.padding(16.dp)
//        ) {
//
//            Text(
//                text = "Expense Breakdown",
//                fontWeight = FontWeight.SemiBold,
//                color = MaterialTheme.colorScheme.onSurface
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//
//                // 🔵 PIE CHART (LEFT)
//                RealPieChart(
//                    data = categoryData,
//                    modifier = Modifier.size(180.dp)
//                )
//
//                Spacer(modifier = Modifier.width(16.dp))
//
//                // 🟢 LEGEND + PERCENTAGE (RIGHT)
//                PieChartLegend(
//                    data = categoryData
//                )
//            }
//        }
//    }
//}

//@Composable
//fun PieChartSection(
//    expenses: List<Expense>
//) {
//    // 🔥 category wise totals from expenses
//    val categoryData = expenses
//        .groupBy { it.category }
//        .mapValues { entry ->
//            entry.value.sumOf { it.amount }
//        }
//
//    if (categoryData.isEmpty()) return
//
//    Card(
//        modifier = Modifier.fillMaxWidth(),
//        shape = RoundedCornerShape(18.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = MaterialTheme.colorScheme.surface
//        ),
//        elevation = CardDefaults.cardElevation(3.dp)
//    ) {
//        Column(modifier = Modifier.padding(16.dp)) {
//
//            Text(
//                text = "Expense Breakdown",
//                fontWeight = FontWeight.SemiBold,
//                color = MaterialTheme.colorScheme.onSurface
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//
//                RealPieChart(
//                    data = categoryData,
//                    modifier = Modifier.size(180.dp)
//                )
//
//                Spacer(modifier = Modifier.width(16.dp))
//
//                PieChartLegend(
//                    data = categoryData
//                )
//            }
//        }
//    }
//}

@Composable
fun PieChartSection(
    categoryData: Map<String, Int>
) {

    if (categoryData.isEmpty()) return

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = "Expense Breakdown",
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {

                RealPieChart(
                    data = categoryData,
                    modifier = Modifier.size(180.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                PieChartLegend(data = categoryData)
            }
        }
    }
}


/* ---------------- RECENT EXPENSES ---------------- */

//@Composable
//fun RecentExpensesSection(expenses: List<com.agrima.spendzyy.model.Expense>) {
//
//    Column {
//
//        Text(
//            text = "Recent Expenses",
//            fontSize = 18.sp,
//            fontWeight = FontWeight.Bold,
//            color = MaterialTheme.colorScheme.onSurface
//        )
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        if (expenses.isEmpty()) {
//            Text(
//                text = "No expenses yet",
//                color  = MaterialTheme.colorScheme.onSurface
//            )
//        } else {
//            expenses.forEach { expense ->
//                ExpenseItem(
//                    title = expense.title,
//                    amount = "₹${expense.amount}"
//                )
//            }
//        }
//    }
//}

@Composable
fun RecentExpensesSection(
//    expenses: List<Expense>
    expenses: List<ExpenseEntity>

) {

    Column {

        Text(
            text = "Recent Expenses",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (expenses.isEmpty()) {

            Text(
                text = "No expenses yet",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )

        } else {

            // 🔥 safety: only show latest 3
            expenses
                .take(3)
                .forEach { expense ->

                    ExpenseItem(
                        title = expense.title,
                        amount = "₹${expense.amount}",
                        category = expense.category,
//                        date = expense.date
                        date = getFriendlyDate(expense.timestamp)

                    )
                }
        }
    }
}

//@Composable
//fun ExpenseItem(
//    title: String,
//    amount: String
//) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 8.dp),
//        horizontalArrangement = Arrangement.SpaceBetween
//    ) {
//        Text(text = title)
//        Text(
//            text = amount,
//            color = Color.Red,
//            fontWeight = FontWeight.SemiBold
//        )
//    }
//}


@Composable
fun ExpenseItem(
    title: String,
    amount: String,
    category: String,
    date: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Column {
            Text(
                text = title,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "$category • $date",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }

        Text(
            text = amount,
            color = MaterialTheme.colorScheme.error,
            fontWeight = FontWeight.SemiBold
        )
    }
}
