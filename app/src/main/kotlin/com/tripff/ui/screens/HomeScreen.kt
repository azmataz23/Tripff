package com.tripff.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tripff.data.Difficulty
import com.tripff.ui.theme.*

@Composable
fun HomeScreen(onStart: (Difficulty) -> Unit) {
    var selected by remember { mutableStateOf(Difficulty.MEDIUM) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(AppBackground, AppSurfaceVar))
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            // Title block
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "TRIPLE",
                    color = TextBlue,
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 8.sp
                )
                Text(
                    text = "TRIAD",
                    color = TextRed,
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 8.sp
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Classic Card Battle",
                    color = TextSecondary,
                    fontSize = 14.sp
                )
            }

            // Difficulty selector
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "SELECT DIFFICULTY",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    letterSpacing = 2.sp
                )
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Difficulty.entries.forEach { diff ->
                        DifficultyChip(
                            label = diff.label,
                            isSelected = selected == diff,
                            onClick = { selected = diff }
                        )
                    }
                }
            }

            // Start button
            Button(
                onClick = { onStart(selected) },
                colors = ButtonDefaults.buttonColors(containerColor = BlueCardLight),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .width(200.dp)
                    .height(52.dp)
            ) {
                Text(
                    text = "PLAY",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 4.sp
                )
            }

            // Rules reminder
            Card(
                colors = CardDefaults.cardColors(containerColor = AppSurface),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.padding(horizontal = 32.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("How to play", color = TextSecondary, fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold)
                    Text(
                        "Each card has 4 values (Top · Right · Bottom · Left).\n" +
                        "Place a card on the 3×3 board and capture adjacent\n" +
                        "opponent cards whose facing value is lower.\n" +
                        "Most cards at game end wins!",
                        color = TextSecondary.copy(alpha = 0.7f),
                        fontSize = 11.sp,
                        lineHeight = 18.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
private fun DifficultyChip(label: String, isSelected: Boolean, onClick: () -> Unit) {
    val bg = if (isSelected) BlueCardLight else AppSurface
    val border = if (isSelected) BlueCardLight else Color(0x55FFFFFF)

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bg)
            .border(1.dp, border, RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = TextPrimary,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}
