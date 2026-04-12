package com.agrima.spendzyy.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.agrima.spendzyy.data.local.entity.ExpenseEntity
import com.agrima.spendzyy.model.Expense
import com.agrima.spendzyy.ui.theme.AccentBlue
import com.agrima.spendzyy.ui.theme.SubTextColor
import com.agrima.spendzyy.utils.getFormattedDate
import com.agrima.spendzyy.viewmodel.ExpenseViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(viewModel: ExpenseViewModel,
                     navController: NavController,
                     expenseId: Long

) {

    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Food") }
    var expanded by remember { mutableStateOf(false) }

    val categories = listOf("Food", "Travel", "Shopping", "Bills", "Other")


    var existingExpense by remember { mutableStateOf<ExpenseEntity?>(null) }

    LaunchedEffect(expenseId) {
        if (expenseId != -1L) {
            existingExpense = viewModel.getExpenseById(expenseId)
        }
    }

    LaunchedEffect(existingExpense) {
        existingExpense?.let { expense ->
            title = expense.title
            amount = expense.amount.toString()
            description = expense.description
            selectedCategory = expense.category
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {

        Text(
            text = "Add Expense",
            style = MaterialTheme.typography.headlineSmall,
            color= MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        // ---------- TITLE ----------
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Expense Title",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            ) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AccentBlue,
                unfocusedBorderColor = SubTextColor,
                focusedLabelColor = AccentBlue,
                unfocusedLabelColor = SubTextColor
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // ---------- AMOUNT ----------
        OutlinedTextField(
            value = amount,
            onValueChange = { input ->
                // ✅ Sirf digits allow
                if (input.all { it.isDigit() }) {
                    amount = input
                }
            },
            label = { Text("Amount (₹)") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AccentBlue,
                unfocusedBorderColor = SubTextColor,
                focusedLabelColor = AccentBlue,
                unfocusedLabelColor = SubTextColor
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // ---------- CATEGORY DROPDOWN ----------
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedCategory,
                onValueChange = {},
                readOnly = true,
                label = { Text("Category") },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AccentBlue,
                    unfocusedBorderColor = SubTextColor,
                    focusedLabelColor = AccentBlue,
                    unfocusedLabelColor = SubTextColor
                )
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                categories.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category) },
                        onClick = {
                            selectedCategory = category
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ---------- DESCRIPTION ----------
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description (optional)") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AccentBlue,
                unfocusedBorderColor = SubTextColor,
                focusedLabelColor = AccentBlue,
                unfocusedLabelColor = SubTextColor
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val amountInt = amount.toIntOrNull()
                val calendar = Calendar.getInstance().apply {
                    timeInMillis = System.currentTimeMillis()
                }

                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)

                when {
                    title.isBlank() -> return@Button
                    amountInt == null -> return@Button
                    amountInt <= 0 -> return@Button

                    else -> {
                        if (existingExpense == null) {

                            viewModel.addExpense(
                                ExpenseEntity(
                                    id = 0, // Room auto-generate karega
                                    title = title.trim(),
                                    amount = amountInt,
                                    category = selectedCategory,
                                    description = description.trim(),
                                    timestamp = System.currentTimeMillis(),
                                    year = year,
                                    month = month
                                )
                            )

                        } else {


                            existingExpense?.let { expense ->
                                viewModel.updateExpense(
                                    expense.copy(
                                        title = title.trim(),
                                        amount = amountInt,
                                        category = selectedCategory,
                                        description = description.trim()
                                        // year & month same rahenge
                                    )
                                )
                            }

                        }

                        navController.popBackStack()
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AccentBlue
            )
        ) {
            Icon(Icons.Default.Check, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (existingExpense == null) "Save Expense" else "Update Expense"
            )
        }



    }
}
