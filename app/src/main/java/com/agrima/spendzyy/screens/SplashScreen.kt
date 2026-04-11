package com.agrima.spendzyy.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import com.agrima.spendzyy.R
import com.google.firebase.auth.FirebaseAuth

val splashGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF1F2937),
        Color(0xFF374151),
        Color(0xFF4B5563)
    )
)


@Composable
fun SplashScreen(navController: NavController) {
    val calligraphyFont = FontFamily(
        Font(R.font.pacifico_regular, FontWeight.Normal)
    )
    val scale = remember { Animatable(0.8f) }
    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1.2f,
            animationSpec = tween(
                durationMillis = 2000,
                easing = FastOutSlowInEasing
            )
        )
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 2000,
                easing = FastOutSlowInEasing
            )
        )
        delay(1000)
        val user = FirebaseAuth.getInstance().currentUser

//        if (user != null) {
//            navController.navigate("home") {
//                popUpTo("splash") { inclusive = true }
//            }
//        } else {
//            navController.navigate("auth") {
//                popUpTo("splash") { inclusive = true }
//            }
//        }
        navController.navigate("auth") {
            popUpTo("splash") { inclusive = true }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(splashGradient), // soft light background
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Spendzyy",
            style = TextStyle(
                fontFamily = calligraphyFont,
                fontSize = 42.sp,
                color = Color(0xFFFBBF24) // soft gold
            ),
            modifier = Modifier.scale(scale.value)
        )
    }
}
