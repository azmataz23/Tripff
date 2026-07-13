package com.tripff.ai

import com.tripff.data.*
import com.tripff.engine.GameEngine

data class Move(val handIndex: Int, val row: Int, val col: Int)

object GameAI {

    fun bestMove(state: GameState, difficulty: Difficulty): Move {
        val moves = allMoves(state)
        check(moves.isNotEmpty()) { "No valid moves available" }
        return when (difficulty) {
            Difficulty.EASY -> moves.random()
            Difficulty.MEDIUM -> greedyMove(state, moves)
            Difficulty.HARD -> minimaxMove(state, moves, depth = 3)
        }
    }

    private fun allMoves(state: GameState): List<Move> {
        val hand = if (state.currentPlayer == Player.RED) state.redHand else state.blueHand
        return GameEngine.emptyCells(state.board).flatMap { (r, c) ->
            hand.indices.map { i -> Move(i, r, c) }
        }
    }

    private fun greedyMove(state: GameState, moves: List<Move>): Move =
        moves.maxByOrNull { move ->
            GameEngine.placeCard(state, move.handIndex, move.row, move.col)
                .let { ns -> scoreFor(ns, state.currentPlayer) }
        } ?: moves.first()

    private fun minimaxMove(state: GameState, moves: List<Move>, depth: Int): Move =
        moves.maxByOrNull { move ->
            val next = GameEngine.placeCard(state, move.handIndex, move.row, move.col)
            minimax(next, depth - 1, isMaximizing = false, aiPlayer = state.currentPlayer)
        } ?: moves.first()

    private fun minimax(state: GameState, depth: Int, isMaximizing: Boolean, aiPlayer: Player): Int {
        if (state.phase == GamePhase.GAME_OVER || depth == 0) return scoreFor(state, aiPlayer)
        val moves = allMoves(state)
        if (moves.isEmpty()) return scoreFor(state, aiPlayer)

        return if (isMaximizing) {
            moves.maxOf { move ->
                minimax(GameEngine.placeCard(state, move.handIndex, move.row, move.col),
                    depth - 1, false, aiPlayer)
            }
        } else {
            moves.minOf { move ->
                minimax(GameEngine.placeCard(state, move.handIndex, move.row, move.col),
                    depth - 1, true, aiPlayer)
            }
        }
    }

    private fun scoreFor(state: GameState, player: Player): Int =
        if (player == Player.BLUE) state.blueScore - state.redScore
        else state.redScore - state.blueScore
}
