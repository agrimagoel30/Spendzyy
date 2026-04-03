package com.agrima.spendzyy.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.agrima.spendzyy.viewmodel.ExpenseViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavHostController,
    expenseViewModel: ExpenseViewModel,
    isDarkMode: Boolean,
    onDarkModeToggle: (Boolean) -> Unit
) {

    var showBudgetDialog by remember { mutableStateOf(false) }
    var notificationsEnabled by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {

        /* ---------------- USER INFO ---------------- */

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Profile",
                modifier = Modifier.size(64.dp),
                tint = Color.DarkGray
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = "Guest User",
                    fontSize = 18.sp,
                    color=MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Not logged in",
                    color =  MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        /* ---------------- BUDGET CARD ---------------- */

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface

            ),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Monthly Budget",     color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Text(
                        text = "₹${expenseViewModel.monthlyBudget}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Budget",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { showBudgetDialog = true }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        /* ---------------- PREFERENCES ---------------- */

        Text(
            text = "Preferences",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        PreferenceRow(
            title = "Dark Mode",
            checked = isDarkMode,
            onCheckedChange = { onDarkModeToggle(it) }
        )

        Spacer(modifier = Modifier.height(32.dp))

        /* ---------------- LOGIN BUTTON ---------------- */

        Button(
            onClick = {
                // 🔥 Firebase auth later
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Login to Sync Data")
        }
        val auth = FirebaseAuth.getInstance()

        Button(onClick = {
            auth.signOut()
            navController.navigate("auth") {
                popUpTo("home") { inclusive = true }
            }
        }) {
            Text("Logout")
        }
    }

    /* ---------------- EDIT BUDGET DIALOG ---------------- */

    if (showBudgetDialog) {
        EditBudgetDialog(
            currentBudget = expenseViewModel.monthlyBudget,
            onDismiss = { showBudgetDialog = false },
            onSave = {
                expenseViewModel.updateMonthlyBudget(it)
                showBudgetDialog = false
            }
        )
    }
}


/* ---------------- PREFERENCE ROW ---------------- */

@Composable
fun PreferenceRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title,     color = MaterialTheme.colorScheme.onSurface
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

/* ---------------- EDIT BUDGET DIALOG ---------------- */

@Composable
fun EditBudgetDialog(
    currentBudget: Int,
    onDismiss: () -> Unit,
    onSave: (Int) -> Unit
) {

    var budgetText by remember { mutableStateOf(currentBudget.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Edit Monthly Budget", fontWeight = FontWeight.Bold)
        },
        text = {
            OutlinedTextField(
                value = budgetText,
                onValueChange = { budgetText = it },
                label = { Text("Budget Amount") },
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    val value = budgetText.toIntOrNull()
                    if (value != null && value > 0) {
                        onSave(value)
                    }
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
