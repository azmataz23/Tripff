package com.tripff.engine

import com.tripff.data.*

object GameEngine {

    fun placeCard(state: GameState, handIndex: Int, row: Int, col: Int): GameState {
        if (state.phase != GamePhase.PLAYING) return state
        if (state.board[row][col] != null) return state

        val player = state.currentPlayer
        val hand = if (player == Player.BLUE) state.blueHand else state.redHand
        if (handIndex !in hand.indices) return state

        val card = hand[handIndex]
        val newHand = hand.toMutableList().also { it.removeAt(handIndex) }

        val newBlueHand = if (player == Player.BLUE) newHand else state.blueHand
        val newRedHand = if (player == Player.RED) newHand else state.redHand

        var newBoard = state.board.updated(row, col, BoardCard(card, player))
        newBoard = processCaptures(newBoard, row, col, player)

        val (blueScore, redScore) = calculateScores(newBoard, newBlueHand, newRedHand)
        val cardsPlaced = newBoard.sumOf { r -> r.count { it != null } }
        val gameOver = cardsPlaced == 9

        return state.copy(
            board = newBoard,
            blueHand = newBlueHand,
            redHand = newRedHand,
            currentPlayer = if (player == Player.BLUE) Player.RED else Player.BLUE,
            blueScore = blueScore,
            redScore = redScore,
            phase = if (gameOver) GamePhase.GAME_OVER else GamePhase.PLAYING,
            winner = if (gameOver) when {
                blueScore > redScore -> Player.BLUE
                redScore > blueScore -> Player.RED
                else -> null
            } else null
        )
    }

    private fun processCaptures(board: Board, row: Int, col: Int, attacker: Player): Board {
        val placed = board[row][col] ?: return board
        var b = board

        if (row > 0) {
            val n = b[row - 1][col]
            if (n != null && n.owner != attacker && placed.card.top > n.card.bottom)
                b = b.updated(row - 1, col, n.copy(owner = attacker))
        }
        if (row < 2) {
            val n = b[row + 1][col]
            if (n != null && n.owner != attacker && placed.card.bottom > n.card.top)
                b = b.updated(row + 1, col, n.copy(owner = attacker))
        }
        if (col > 0) {
            val n = b[row][col - 1]
            if (n != null && n.owner != attacker && placed.card.left > n.card.right)
                b = b.updated(row, col - 1, n.copy(owner = attacker))
        }
        if (col < 2) {
            val n = b[row][col + 1]
            if (n != null && n.owner != attacker && placed.card.right > n.card.left)
                b = b.updated(row, col + 1, n.copy(owner = attacker))
        }

        return b
    }

    fun calculateScores(board: Board, blueHand: List<Card>, redHand: List<Card>): Pair<Int, Int> {
        var blue = blueHand.size
        var red = redHand.size
        board.forEach { row -> row.forEach { cell ->
            if (cell != null) { if (cell.owner == Player.BLUE) blue++ else red++ }
        }}
        return blue to red
    }

    fun emptyCells(board: Board): List<Pair<Int, Int>> =
        (0..2).flatMap { r -> (0..2).filter { c -> board[r][c] == null }.map { c -> r to c } }
}
