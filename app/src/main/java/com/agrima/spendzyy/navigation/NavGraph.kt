package com.agrima.spendzyy.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.agrima.spendzyy.data.local.database.ExpenseDatabase
import com.agrima.spendzyy.data.repository.NoteRepository
import com.agrima.spendzyy.screens.*
import com.agrima.spendzyy.viewmodel.ExpenseViewModel
import com.agrima.spendzyy.viewmodel.NotesViewModel
import com.agrima.spendzyy.viewmodel.NotesViewModelFactory
import com.google.firebase.auth.FirebaseAuth


@Composable
fun NavGraph(
    navController: NavHostController,
    isDarkMode: Boolean,
    onDarkModeToggle: (Boolean) -> Unit
) {

    val expenseViewModel: ExpenseViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {

        /* ---------------- SPLASH ---------------- */
        composable("splash") {
            SplashScreen(navController)
        }

        /* ---------------- HOME ---------------- */
        composable("home") {
            HomeScreen(
                navController = navController,
                expenseViewModel = expenseViewModel
            )
        }

        /* ---------------- YEARLY EXPENSES ---------------- */
        composable("expenses") {
            YearlyExpensesScreen(
                navController = navController,
                viewModel = expenseViewModel
            )
        }

        /* ---------------- MONTHLY EXPENSES ---------------- */
        composable(
            route = "months/{year}",
            arguments = listOf(
                navArgument("year") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val year = backStackEntry.arguments?.getInt("year")!!

            MonthlyExpensesScreen(
                navController = navController,
                year = year,
                viewModel = expenseViewModel
            )
        }

        /* ---------------- CATEGORY EXPENSES ---------------- */
        composable(
            route = "categories/{year}/{month}",
            arguments = listOf(
                navArgument("year") { type = NavType.IntType },
                navArgument("month") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val year = backStackEntry.arguments?.getInt("year")!!
            val month = backStackEntry.arguments?.getInt("month")!!

            CategoryExpensesScreen(
                navController = navController,   // 👈 ADD THIS
                viewModel = expenseViewModel,
                year = year,
                month = month
            )
        }

        /* ---------------- ADD EXPENSE ---------------- */
//        composable("add") {
//            AddExpenseScreen(
//                viewModel = expenseViewModel,
//                navController = navController
//            )
//        }

        composable(
            route = "add?expenseId={expenseId}",
            arguments = listOf(
                navArgument("expenseId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) { backStackEntry ->

            val expenseId = backStackEntry.arguments?.getLong("expenseId") ?: -1L

            AddExpenseScreen(
                viewModel = expenseViewModel,
                navController = navController,
                expenseId = expenseId
            )
        }


        /* ---------------- NOTES ---------------- */
        composable("notes") {
            val context = LocalContext.current

            val db = ExpenseDatabase.getInstance(context)
            val noteDao = db.noteDao()
            val noteRepository = NoteRepository(noteDao)

            val notesViewModel: NotesViewModel = viewModel(
                factory = NotesViewModelFactory(noteRepository)
            )
            NotesScreen(viewModel = notesViewModel)
        }

        /* ---------------- PROFILE ---------------- */
        composable("profile") {
            ProfileScreen(
                navController=navController,
                expenseViewModel = expenseViewModel,
                isDarkMode = isDarkMode,
                onDarkModeToggle = onDarkModeToggle
            )
        }
        composable("auth") {
            val auth = FirebaseAuth.getInstance()
            AuthScreen(
                onLoginClick = { email, password ->
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener {
                            navController.navigate("home") {
                                popUpTo("auth") { inclusive = true }
                            }
                        }
                        .addOnFailureListener {
                            Log.e("AUTH", "Login failed", it)
                        }
                },
                onSignupClick = { email, password ->
                    if(email.isBlank() || password.isBlank()){
                        Log.e("AUTH","Email or Password empty")
                        return@AuthScreen
                    }
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener {
                            navController.navigate("home") {
                                popUpTo("auth") { inclusive = true }
                            }
                        }
                        .addOnFailureListener {
                            Log.e("AUTH", "Signup failed", it)
                        }
                }
            )
        }
    }
}
