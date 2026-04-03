package com.agrima.spendzyy.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.agrima.spendzyy.utils.getMonthName
import com.agrima.spendzyy.viewmodel.ExpenseViewModel
import java.util.Calendar

//@Composable
//fun MonthlyExpensesScreen(
//    navController: NavController,
//    year: Int,
//    viewModel: ExpenseViewModel
//) {
//    val months = viewModel.getMonthsForYear(year)
//
//    Column(Modifier.padding(16.dp)) {
//        Text("Select Month", fontSize = 20.sp)
//
//        months.forEach { month ->
//            Card(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 6.dp)
//                    .clickable {
//                        navController.navigate("categories/$year/$month")
//                    }
//            ) {
//                Text(
//                    text = getMonthName(month),
//                    modifier = Modifier.padding(16.dp)
//                )
//            }
//        }
//    }
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthlyExpensesScreen(
    navController: NavController,
    year: Int,
    viewModel: ExpenseViewModel
) {
    val allExpenses by viewModel.allExpenses.collectAsState(initial = emptyList())

//    val months = viewModel.getMonthsForYear(year)

    val months = allExpenses
        .filter {
            val cal = Calendar.getInstance().apply {
                timeInMillis = it.timestamp
            }
            cal.get(Calendar.YEAR) == year
        }
        .map {
            Calendar.getInstance().apply {
                timeInMillis = it.timestamp
            }.get(Calendar.MONTH)
        }
        .distinct()
        .sorted()


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Select Month")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            months.forEach { month ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable {
                            navController.navigate("categories/$year/$month")
                        },
                    shape = RoundedCornerShape(14.dp),
                    elevation = CardDefaults.cardElevation(3.dp)
                ) {
                    Text(
                        text = getMonthName(month+1),
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}
