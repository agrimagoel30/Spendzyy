package com.agrima.spendzyy.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem("home", "Home", Icons.Filled.Home)
    object Expenses : BottomNavItem("expenses", "Expenses", Icons.Filled.List)
    object Add : BottomNavItem("add", "Add", Icons.Filled.Add)
    object Notes : BottomNavItem("notes", "Notes", Icons.Filled.Description    )
    object Profile : BottomNavItem("profile", "Profile", Icons.Filled.Person)
}
