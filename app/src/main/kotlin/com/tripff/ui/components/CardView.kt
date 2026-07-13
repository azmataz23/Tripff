package com.tripff.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tripff.data.Card
import com.tripff.data.Player
import com.tripff.ui.theme.*

@Composable
fun CardView(
    card: Card,
    owner: Player,
    size: Dp,
    isSelected: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    val bgColor = if (owner == Player.BLUE) BlueCard else RedCard
    val borderColor = if (isSelected) GoldSelected else bgColor.copy(alpha = 0.4f)
    val valueSp = (size.value * 0.19f).sp
    val symbolSp = (size.value * 0.28f).sp
    val padding = (size.value * 0.05f).dp

    Box(
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(6.dp))
            .background(bgColor)
            .border(if (isSelected) 2.dp else 1.dp, borderColor, RoundedCornerShape(6.dp))
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
    ) {
        // Top value
        Text(
            text = card.topStr,
            color = TextPrimary,
            fontSize = valueSp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = padding)
        )
        // Bottom value
        Text(
            text = card.bottomStr,
            color = TextPrimary,
            fontSize = valueSp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = padding)
        )
        // Left value
        Text(
            text = card.leftStr,
            color = TextPrimary,
            fontSize = valueSp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = padding)
        )
        // Right value
        Text(
            text = card.rightStr,
            color = TextPrimary,
            fontSize = valueSp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = padding)
        )
        // Centre: symbol and abbreviated name
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(
                text = card.symbol,
                color = TextPrimary,
                fontSize = symbolSp,
                fontWeight = FontWeight.Light
            )
            if (size >= 80.dp) {
                Text(
                    text = card.name.take(8),
                    color = TextPrimary.copy(alpha = 0.75f),
                    fontSize = (size.value * 0.095f).sp,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun EmptyCell(
    size: Dp,
    isHighlighted: Boolean,
    onClick: () -> Unit
) {
    val bg = if (isHighlighted) GreenHighAlpha else Color(0x22FFFFFF)
    val border = if (isHighlighted) GreenHighlight else Color(0x44FFFFFF)

    Box(
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(6.dp))
            .background(bg)
            .border(if (isHighlighted) 2.dp else 1.dp, border, RoundedCornerShape(6.dp))
            .clickable { onClick() }
    )
}
