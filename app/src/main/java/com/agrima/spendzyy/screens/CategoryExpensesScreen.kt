
package com.agrima.spendzyy.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.agrima.spendzyy.utils.getFriendlyDate
import com.agrima.spendzyy.utils.getMonthName
import com.agrima.spendzyy.viewmodel.ExpenseViewModel
import java.util.Calendar

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun CategoryExpensesScreen(
//    navController: NavController,
//    viewModel: ExpenseViewModel,
//    year: Int,
//    month: Int
//) {
//
//
//    val expenses = viewModel
//        .getExpensesForMonth(year, month)
//        .sortedByDescending { it.timestamp }
//
//    val categoryWithSortedExpenses = expenses
//        .groupBy { it.category }
//        .mapValues { (_, list) ->
//            list.sortedByDescending { it.timestamp }
//        }
//
//    val sortedCategories = categoryWithSortedExpenses
//        .toList()
//        .sortedByDescending { (_, list) ->
//            list.first().timestamp
//        }
//
//    val categoryTotals = viewModel.getCategoryTotals(expenses)
//
//    var expandedCategory by remember { mutableStateOf<String?>(null) }
//
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = {
//                    Text(
//                        text = "${getMonthName(month+1)} $year",
//                        fontWeight = FontWeight.Bold
//                    )
//                },
//                navigationIcon = {
//                    IconButton(onClick = { navController.popBackStack() }) {
//                        Icon(
//                            imageVector = Icons.Default.ArrowBack,
//                            contentDescription = "Back"
//                        )
//                    }
//                }
//            )
//        }
//    ) { padding ->
//
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(padding)
//                .background(MaterialTheme.colorScheme.background)
//                .padding(16.dp)
//        ) {
//
//
//            if (categoryTotals.isNotEmpty()) {
//                Card(
//                    modifier = Modifier.fillMaxWidth(),
//                    shape = RoundedCornerShape(18.dp),
//                    colors = CardDefaults.cardColors(
//                        containerColor = MaterialTheme.colorScheme.surface
//                    ),
//                    elevation = CardDefaults.cardElevation(4.dp)
//                ) {
//                    Row(
//                        modifier = Modifier.padding(16.dp),
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        RealPieChart(
//                            data = categoryTotals,
//                            modifier = Modifier.size(180.dp)
//                        )
//
//                        Spacer(modifier = Modifier.width(16.dp))
//
//                        PieChartLegend(data = categoryTotals)
//                    }
//                }
//
//                Spacer(modifier = Modifier.height(24.dp))
//            }
//
//
//            if (sortedCategories.isEmpty()) {
//                Text(
//                    text = "No expenses for this month",
//                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
//                )
//            } else {
//                LazyColumn {
//                    items(sortedCategories,
//                        key = { it.first } // category name
//                    ) { (category, expenseList) ->
//
//                        val isExpanded = expandedCategory == category
//
//                        CategoryExpandableCard(
//                            category = category,
//                            expenses = expenseList,
//                            expanded = isExpanded,
//                            onClick = {
//                                expandedCategory =
//                                    if (isExpanded) null else category
//                            },
//                            navController,
//                                    onDeleteExpense = { expense ->
//                                viewModel.deleteExpense(expense) // ✅ ONLY HERE
//                            }
//                        )
//
//                        Spacer(modifier = Modifier.height(12.dp))
//                    }
//                }
//            }
//        }
//    }
//}





@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryExpensesScreen(
    navController: NavController,
    viewModel: ExpenseViewModel,
    year: Int,
    month: Int
) {

    /* ---------------- DATA (ROOM) ---------------- */

    // ✅ STEP 1: Room se saare expenses
    val allExpenses by viewModel.allExpenses.collectAsState(initial = emptyList())

    // ✅ STEP 2: Filter by selected year & month
    val expenses: List<ExpenseEntity> = allExpenses.filter { expense ->
        val cal = Calendar.getInstance().apply {
            timeInMillis = expense.timestamp
        }
        cal.get(Calendar.YEAR) == year &&
                cal.get(Calendar.MONTH) == month
    }.sortedByDescending { it.timestamp }

    // ✅ STEP 3: Group by category
    val categoryWithSortedExpenses = expenses
        .groupBy { it.category }
        .mapValues { (_, list) ->
            list.sortedByDescending { it.timestamp }
        }

    // ✅ STEP 4: Sort categories by latest expense
    val sortedCategories = categoryWithSortedExpenses
        .toList()
        .sortedByDescending { (_, list) ->
            list.first().timestamp
        }

    // ✅ STEP 5: Pie chart data
    val categoryTotals = viewModel.getCategoryTotals(expenses)

    // Expand / collapse state
    var expandedCategory by remember { mutableStateOf<String?>(null) }

    /* ---------------- UI ---------------- */

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "${getMonthName(month + 1)} $year",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {

            /* ---------- PIE CHART ---------- */

            if (categoryTotals.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RealPieChart(
                            data = categoryTotals,
                            modifier = Modifier.size(180.dp)
                        )

                        Spacer(modifier = Modifier.width(16.dp))
                        PieChartLegend(data = categoryTotals)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            /* ---------- CATEGORY LIST ---------- */

            if (sortedCategories.isEmpty()) {
                Text(
                    text = "No expenses for this month",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            } else {
                LazyColumn {
                    items(
                        sortedCategories,
                        key = { it.first }
                    ) { (category, expenseList) ->

                        val isExpanded = expandedCategory == category

                        CategoryExpandableCard(
                            category = category,
                            expenses = expenseList,
                            expanded = isExpanded,
                            onClick = {
                                expandedCategory =
                                    if (isExpanded) null else category
                            },
                            navController = navController,
                            onDeleteExpense = { expense ->
                                viewModel.deleteExpense(expense)
                            }
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}







/* ---------------- CATEGORY CARD ---------------- */

@Composable
fun CategoryExpandableCard(
    category: String,
//    expenses: List<Expense>,
    expenses: List<ExpenseEntity>,
    expanded: Boolean,
    onClick: () -> Unit,
    navController: NavController,
//    onDeleteExpense: (Expense) -> Unit,
    onDeleteExpense: (ExpenseEntity) -> Unit,



    ) {

    val total = expenses.sumOf { it.amount }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
//            containerColor = getCategoryBackground(category)
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = category,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "₹$total",
                        color = Color.Red,
//                        containerColor = getCategoryBackground(category),
                                fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = if (expanded)
                            Icons.Filled.ExpandLess
                        else
                            Icons.Filled.ExpandMore,
                        contentDescription = null
                    )
                }
            }

            // Expandable expenses
            AnimatedVisibility(visible = expanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()) {
                Column {
                    Spacer(modifier = Modifier.height(12.dp))
                    expenses.forEach { expense ->
                        ExpenseCard(expense,
                            onEdit = {
                                navController.navigate(
                                    "add?expenseId=${expense.id}"
                                )
                            },
                            onDelete = {
                                onDeleteExpense(expense)
                            })
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

/* ---------------- EXPENSE CARD ---------------- */


@Composable
fun ExpenseCard(
// expense: Expense,
//                onEdit: (Expense) -> Unit,
//                onDelete: (Expense) -> Unit
    expense: ExpenseEntity,
    onEdit: (ExpenseEntity) -> Unit,
    onDelete: (ExpenseEntity) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = expense.title,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
//                        text = "${expense.category} • ${expense.date}",
                        text = "${expense.category} • ${getFriendlyDate(expense.timestamp)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "₹${expense.amount}",
                        color = Color.Red,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = { onEdit(expense) }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(
                        onClick = { showDeleteDialog = true}
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color.Red
                        )
                    }
                    Icon(
                        imageVector = if (expanded)
                            Icons.Filled.ExpandLess
                        else
                            Icons.Filled.ExpandMore,
                        contentDescription = null
                    )
                }
            }
            AnimatedVisibility(
                visible = expanded
            ) {
                Column {
                    Spacer(modifier = Modifier.height(12.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(8.dp))

                    // Description (agar hai to)
                    if (expense.description.isNotBlank()) {
                        Text(
                            text = expense.description,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

        }
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = {
                    Text("Delete Expense?")
                },
                text = {
                    Text("Are you sure you want to delete this expense?")
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            // delete call yahan aayega (step 4)
                            onDelete(expense)   // ✅ ONLY DELETE CALL
                            showDeleteDialog = false
                        }
                    ) {
                        Text("Delete", color = Color.Red)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showDeleteDialog = false }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }

    }
}
