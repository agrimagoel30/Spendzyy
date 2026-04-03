package com.agrima.spendzyy.utils

import androidx.compose.ui.graphics.Color

fun getCategoryColor(category: String): Color {
    return when (category) {
        "Food" -> Color(0xFF4CAF50)       // Green
        "Shopping" -> Color(0xFF9C27B0)   // Purple
        "Other" -> Color(0xFF9E9E9E)      // Grey
        "Bills" -> Color(0xFFFF9800)     // Orange
        "Travel" -> Color(0xFF03A9F4)    // Blue
        else -> Color(0xFFBDBDBD)
    }
}
fun getCategoryBackground(category: String): Color {
    return getCategoryColor(category).copy(alpha = 0.12f)
}
