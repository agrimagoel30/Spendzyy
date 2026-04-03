package com.agrima.spendzyy.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight


private val categoryColors = mapOf(
    "Food" to Color(0xFF66BB6A),
    "Travel" to Color(0xFF42A5F5),
    "Shopping" to Color(0xFFAB47BC),
    "Bills" to Color(0xFFFF7043),
    "Other" to Color(0xFFBDBDBD)
)



@Composable
fun RealPieChart(
    data: Map<String, Int>,
    modifier: Modifier = Modifier
) {
    val total = data.values.sum()
    if (total == 0) return

    Canvas(modifier = modifier.size(180.dp)) {

        var startAngle = -90f
        val radius = size.minDimension / 2
        val center = this.center

        // 🟢 CASE 1: Only ONE category → center text
        if (data.size == 1) {
            val (category, value) = data.entries.first()
            val sweepAngle = 360f

            // draw full pie
            drawArc(
                color = categoryColors[category] ?: Color.Gray,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = true
            )

            // draw 100% in exact center
            drawContext.canvas.nativeCanvas.drawText(
                "100%",
                center.x,
                center.y + 12f,
                android.graphics.Paint().apply {
                    color = android.graphics.Color.WHITE
                    textSize = 42f
                    textAlign = android.graphics.Paint.Align.CENTER
                    isFakeBoldText = true
                }
            )
            return@Canvas
        }

        // 🟣 CASE 2: Multiple categories → slice-wise %
        data.forEach { (category, value) ->

            val sweepAngle = (value.toFloat() / total) * 360f

            drawArc(
                color = categoryColors[category] ?: Color.Gray,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = true
            )

            val percentage = ((value.toFloat() / total) * 100).toInt()

            if (percentage > 5) { // avoid clutter for tiny slices
                val angleInRadians =
                    Math.toRadians((startAngle + sweepAngle / 2).toDouble())

                val textRadius = radius * 0.6f
                val x = center.x + textRadius * kotlin.math.cos(angleInRadians).toFloat()
                val y = center.y + textRadius * kotlin.math.sin(angleInRadians).toFloat()

                drawContext.canvas.nativeCanvas.drawText(
                    "$percentage%",
                    x,
                    y,
                    android.graphics.Paint().apply {
                        color = android.graphics.Color.WHITE
                        textSize = 28f
                        textAlign = android.graphics.Paint.Align.CENTER
                        isFakeBoldText = true
                    }
                )
            }

            startAngle += sweepAngle
        }
    }
}

@Composable
fun PieLegendItem(
    color: Color,
    label: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 6.dp)
    ) {

        // 🎨 Color box
        Box(
            modifier = Modifier
                .size(14.dp)
                .background(color, RoundedCornerShape(3.dp))
        )

        Spacer(modifier = Modifier.width(8.dp))

        // 🏷 Category name (no breaking)
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun PieChartLegend(
    data: Map<String, Int>
) {
    Column {
        data.forEach { (category, _) ->
            PieLegendItem(
                color = categoryColors[category] ?: Color.Gray,
                label = category
            )
        }
    }
}
