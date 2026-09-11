package com.kkrakowska.unblockme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kkrakowska.unblockme.data.LevelLoader
import com.kkrakowska.unblockme.data.ProgressManager
import com.kkrakowska.unblockme.ui.*

enum class GameState { MENU, LEVEL_SELECT, IN_GAME }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return GameViewModel(LevelLoader(this@MainActivity), ProgressManager(this@MainActivity)) as T
            }
        }

        setContent {
            var darkTheme by remember { mutableStateOf(true) }
            val viewModel: GameViewModel = viewModel(factory = factory)
            val uiState by viewModel.uiState.collectAsState()

            UnblockMeTheme(darkTheme = darkTheme) {
                val c = LocalAppColors.current

                Box(modifier = Modifier.fillMaxSize().background(c.bg)) {
                    when (uiState.gameState) {
                        GameState.MENU -> MainMenu(onPlayClick = viewModel::onPlayClicked)

                        GameState.LEVEL_SELECT -> LevelSelection(
                            unlockedLevel = uiState.unlockedLevel,
                            totalLevels = uiState.totalLevels,
                            levelStars = uiState.levelStars,
                            onLevelSelected = viewModel::onLevelSelected,
                            onBack = viewModel::onBackToMenu
                        )

                        GameState.IN_GAME -> GamePlayScreen(
                            uiState = uiState,
                            onRestart = viewModel::onRestart,
                            onUndo = viewModel::onUndo,
                            onBackToMenu = viewModel::onBackToLevelSelect,
                            onNextLevel = viewModel::onNextLevel,
                            onMoveMade = viewModel::onBlockMoved
                        )
                    }

                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .statusBarsPadding()
                            .padding(top = 12.dp, end = 16.dp)
                            .size(36.dp)
                            .zIndex(999f)
                            .clip(RoundedCornerShape(9.dp))
                            .background(c.btn)
                            .border(1.5.dp, c.btnBorder, RoundedCornerShape(9.dp))
                            .clickable { darkTheme = !darkTheme },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (darkTheme) "☀" else "🌙",
                            style = TextStyle(fontSize = TextUnit(16f, TextUnitType.Sp))
                        )
                    }
                }
            }
        }
    }
}