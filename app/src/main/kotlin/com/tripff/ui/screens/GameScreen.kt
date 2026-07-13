package com.tripff.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.tripff.data.*
import com.tripff.ui.components.CardView
import com.tripff.ui.components.EmptyCell
import com.tripff.ui.theme.*
import com.tripff.viewmodel.UiState

@Composable
fun GameScreen(
    uiState: UiState,
    onSelectCard: (Int) -> Unit,
    onPlaceCard: (Int, Int) -> Unit,
    onNewGame: () -> Unit
) {
    val gs = uiState.gameState

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(AppBackground, AppSurfaceVar)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // ── Red (AI) area ──────────────────────────────────────────────
            PlayerArea(
                hand = gs.redHand,
                player = Player.RED,
                score = gs.redScore,
                isCurrent = gs.currentPlayer == Player.RED,
                isThinking = uiState.isAiThinking,
                selectedIndex = null,
                onCardClick = {}
            )

            // ── Board ──────────────────────────────────────────────────────
            BoardGrid(
                board = gs.board,
                highlighted = uiState.highlightedCells,
                onCellClick = onPlaceCard
            )

            // ── Blue (Player) area ─────────────────────────────────────────
            PlayerArea(
                hand = gs.blueHand,
                player = Player.BLUE,
                score = gs.blueScore,
                isCurrent = gs.currentPlayer == Player.BLUE,
                isThinking = false,
                selectedIndex = uiState.selectedCardIndex,
                onCardClick = onSelectCard
            )
        }
    }

    // ── Game-over dialog ───────────────────────────────────────────────────
    if (gs.phase == GamePhase.GAME_OVER) {
        GameOverDialog(
            winner = gs.winner,
            blueScore = gs.blueScore,
            redScore = gs.redScore,
            onNewGame = onNewGame
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun PlayerArea(
    hand: List<Card>,
    player: Player,
    score: Int,
    isCurrent: Boolean,
    isThinking: Boolean,
    selectedIndex: Int?,
    onCardClick: (Int) -> Unit
) {
    val nameColor = if (player == Player.BLUE) TextBlue else TextRed
    val playerLabel = if (player == Player.BLUE) "YOU" else "CPU"

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // Status row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = playerLabel,
                color = nameColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isThinking) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(14.dp),
                        strokeWidth = 2.dp,
                        color = TextRed
                    )
                    Text("Thinking…", color = TextSecondary, fontSize = 12.sp)
                } else if (isCurrent) {
                    Text(
                        text = if (player == Player.BLUE) "Your turn" else "CPU turn",
                        color = nameColor,
                        fontSize = 12.sp
                    )
                }
            }
            ScoreBadge(score = score, player = player)
        }

        // Hand
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Fill available width evenly with the 5 cards (or fewer if placed)
            BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                val totalGaps = 4 * 6 // 4 gaps of 6dp
                val cardSize = ((maxWidth.value - totalGaps) / 5).dp.coerceAtLeast(48.dp)

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    hand.forEachIndexed { idx, card ->
                        CardView(
                            card = card,
                            owner = player,
                            size = cardSize,
                            isSelected = selectedIndex == idx,
                            onClick = if (player == Player.BLUE) ({ onCardClick(idx) }) else null
                        )
                    }
                    // Placeholders for played cards
                    repeat(5 - hand.size) {
                        Spacer(modifier = Modifier.size(cardSize))
                    }
                }
            }
        }
    }
}

@Composable
private fun ScoreBadge(score: Int, player: Player) {
    val bg = if (player == Player.BLUE) BlueCard else RedCard
    Box(
        modifier = Modifier
            .background(bg, RoundedCornerShape(4.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = score.toString(),
            color = TextPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun BoardGrid(
    board: Board,
    highlighted: Set<Pair<Int, Int>>,
    onCellClick: (Int, Int) -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        contentAlignment = Alignment.Center
    ) {
        val gap = 6.dp
        val totalGaps = 2 * gap.value
        val cellSize = ((maxWidth.value - totalGaps) / 3).dp

        Column(verticalArrangement = Arrangement.spacedBy(gap)) {
            for (r in 0..2) {
                Row(horizontalArrangement = Arrangement.spacedBy(gap)) {
                    for (c in 0..2) {
                        val cell = board[r][c]
                        if (cell != null) {
                            CardView(
                                card = cell.card,
                                owner = cell.owner,
                                size = cellSize
                            )
                        } else {
                            EmptyCell(
                                size = cellSize,
                                isHighlighted = (r to c) in highlighted,
                                onClick = { onCellClick(r, c) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GameOverDialog(
    winner: Player?,
    blueScore: Int,
    redScore: Int,
    onNewGame: () -> Unit
) {
    Dialog(onDismissRequest = {}) {
        Card(
            colors = CardDefaults.cardColors(containerColor = AppSurface),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val (headline, color) = when (winner) {
                    Player.BLUE -> "YOU WIN!" to TextBlue
                    Player.RED  -> "CPU WINS" to TextRed
                    null        -> "DRAW"     to TextSecondary
                }
                Text(
                    text = headline,
                    color = color,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 4.sp
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("YOU", color = TextBlue, fontSize = 11.sp, letterSpacing = 2.sp)
                        Text(blueScore.toString(), color = TextPrimary, fontSize = 28.sp,
                            fontWeight = FontWeight.Bold)
                    }
                    Text("vs", color = TextSecondary, fontSize = 16.sp)
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("CPU", color = TextRed, fontSize = 11.sp, letterSpacing = 2.sp)
                        Text(redScore.toString(), color = TextPrimary, fontSize = 28.sp,
                            fontWeight = FontWeight.Bold)
                    }
                }
                Button(
                    onClick = onNewGame,
                    colors = ButtonDefaults.buttonColors(containerColor = BlueCardLight),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("PLAY AGAIN", fontSize = 15.sp, fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp)
                }
            }
        }
    }
}
