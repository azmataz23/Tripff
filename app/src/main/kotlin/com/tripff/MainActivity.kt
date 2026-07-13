package com.tripff

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tripff.data.Difficulty
import com.tripff.ui.screens.GameScreen
import com.tripff.ui.screens.HomeScreen
import com.tripff.ui.theme.AppBackground
import com.tripff.ui.theme.TripffTheme
import com.tripff.viewmodel.GameViewModel

private sealed interface NavScreen {
    data object Home : NavScreen
    data object Game : NavScreen
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TripffTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .safeDrawingPadding(),
                    color = AppBackground
                ) {
                    val viewModel: GameViewModel = viewModel()
                    var screen by remember { mutableStateOf<NavScreen>(NavScreen.Home) }

                    when (screen) {
                        NavScreen.Home -> HomeScreen(
                            onStart = { difficulty ->
                                viewModel.startGame(difficulty)
                                screen = NavScreen.Game
                            }
                        )
                        NavScreen.Game -> {
                            val uiState by viewModel.uiState.collectAsState()
                            GameScreen(
                                uiState = uiState,
                                onSelectCard = viewModel::selectCard,
                                onPlaceCard = viewModel::placeCard,
                                onNewGame = { screen = NavScreen.Home }
                            )
                        }
                    }
                }
            }
        }
    }
}
