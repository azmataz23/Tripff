package com.tripff.data

typealias Board = List<List<BoardCard?>>

enum class GamePhase { PLAYING, GAME_OVER }

data class GameState(
    val board: Board = List(3) { List(3) { null } },
    val blueHand: List<Card> = emptyList(),
    val redHand: List<Card> = emptyList(),
    val currentPlayer: Player = Player.BLUE,
    val blueScore: Int = 5,
    val redScore: Int = 5,
    val phase: GamePhase = GamePhase.PLAYING,
    val winner: Player? = null
)

fun Board.updated(row: Int, col: Int, value: BoardCard?): Board =
    mapIndexed { r, rowList ->
        rowList.mapIndexed { c, cell -> if (r == row && c == col) value else cell }
    }
