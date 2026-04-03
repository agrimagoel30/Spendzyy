package com.agrima.spendzyy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import com.agrima.spendzyy.navigation.BottomBar
import com.agrima.spendzyy.navigation.NavGraph
import com.agrima.spendzyy.ui.theme.SpendzyyTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.agrima.spendzyy.data.local.database.ExpenseDatabase
import com.agrima.spendzyy.data.repository.ExpenseRepository
import com.agrima.spendzyy.navigation.BottomBar
import com.agrima.spendzyy.navigation.NavGraph
import com.agrima.spendzyy.viewmodel.ExpenseViewModel
import com.agrima.spendzyy.viewmodel.ExpenseViewModelFactory


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = ExpenseDatabase.getInstance(applicationContext)
        val repository = ExpenseRepository(database.expenseDao())
        setContent {
            val expenseViewModel: ExpenseViewModel = viewModel(
                factory = ExpenseViewModelFactory(repository)
            )
            var isDarkMode by rememberSaveable { mutableStateOf(false) }

            SpendzyyTheme(
                darkTheme = isDarkMode

            ) {
                val navController = rememberNavController()
                val currentBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = currentBackStackEntry?.destination?.route

                // 👇 jin screens pe bottom bar chahiye
                val showBottomBar = currentRoute in listOf(
                    "home",
                    "expenses",
                    "add",
                    "notes",
                    "profile"

                )

                Scaffold(
                    bottomBar = {
                        if (showBottomBar) {
                            BottomBar(navController = navController)
                        }
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        NavGraph(navController = navController,
                            isDarkMode = isDarkMode,
                            onDarkModeToggle = { newValue ->
                                isDarkMode = newValue })
                    }
                }
            }
        }
    }
}
