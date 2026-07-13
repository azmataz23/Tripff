package com.tripff.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tripff.ai.GameAI
import com.tripff.data.*
import com.tripff.engine.GameEngine
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class UiState(
    val gameState: GameState = GameState(),
    val selectedCardIndex: Int? = null,
    val highlightedCells: Set<Pair<Int, Int>> = emptySet(),
    val difficulty: Difficulty = Difficulty.MEDIUM,
    val isAiThinking: Boolean = false
)

class GameViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun startGame(difficulty: Difficulty) {
        val blueHand = CardDatabase.playerStarterDeck()
        val redHand = CardDatabase.deckForDifficulty(difficulty)
        _uiState.value = UiState(
            gameState = GameState(
                blueHand = blueHand,
                redHand = redHand,
                blueScore = blueHand.size,
                redScore = redHand.size
            ),
            difficulty = difficulty
        )
    }

    fun selectCard(index: Int) {
        val state = _uiState.value
        if (state.gameState.currentPlayer != Player.BLUE || state.isAiThinking) return

        val newSelection = if (state.selectedCardIndex == index) null else index
        val highlighted = if (newSelection != null) {
            GameEngine.emptyCells(state.gameState.board).toSet()
        } else emptySet()

        _uiState.value = state.copy(selectedCardIndex = newSelection, highlightedCells = highlighted)
    }

    fun placeCard(row: Int, col: Int) {
        val state = _uiState.value
        val idx = state.selectedCardIndex ?: return
        if (state.gameState.currentPlayer != Player.BLUE || state.isAiThinking) return
        if (state.gameState.board[row][col] != null) return

        val newGameState = GameEngine.placeCard(state.gameState, idx, row, col)
        _uiState.value = state.copy(
            gameState = newGameState,
            selectedCardIndex = null,
            highlightedCells = emptySet()
        )

        if (newGameState.phase == GamePhase.PLAYING && newGameState.currentPlayer == Player.RED) {
            scheduleAiMove(newGameState, state.difficulty)
        }
    }

    private fun scheduleAiMove(gameState: GameState, difficulty: Difficulty) {
        _uiState.value = _uiState.value.copy(isAiThinking = true)
        viewModelScope.launch {
            delay(750L)
            val move = GameAI.bestMove(gameState, difficulty)
            val newState = GameEngine.placeCard(gameState, move.handIndex, move.row, move.col)
            _uiState.value = _uiState.value.copy(gameState = newState, isAiThinking = false)

            // AI could play again if somehow current player flips back (shouldn't happen, but guard)
            if (newState.phase == GamePhase.PLAYING && newState.currentPlayer == Player.RED) {
                scheduleAiMove(newState, difficulty)
            }
        }
    }
}
