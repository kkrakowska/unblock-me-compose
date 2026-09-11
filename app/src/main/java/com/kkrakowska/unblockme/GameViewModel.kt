package com.kkrakowska.unblockme

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.kkrakowska.unblockme.data.LevelLoader
import com.kkrakowska.unblockme.data.ProgressManager
import com.kkrakowska.unblockme.models.Level
import com.kkrakowska.unblockme.ui.Move
import com.kkrakowska.unblockme.utils.GameRules

data class GameUiState(
    val gameState: GameState = GameState.MENU,
    val currentLevelNumber: Int = 1,
    val currentLevel: Level? = null,
    val isLevelWon: Boolean = false,
    val moveHistory: List<Move> = emptyList(),
    val unlockedLevel: Int = 1,
    val levelStars: Map<Int, Int> = emptyMap(),
    val totalLevels: Int = 15
)

class GameViewModel(
    private val levelLoader: LevelLoader,
    private val progressManager: ProgressManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    init { loadProgress() }

    private fun loadProgress() {
        val total = _uiState.value.totalLevels
        _uiState.update { it.copy(
            unlockedLevel = progressManager.getUnlockedLevel(),
            levelStars = progressManager.getAllStars(total)
        ) }
    }

    fun onPlayClicked() = _uiState.update { it.copy(gameState = GameState.LEVEL_SELECT) }

    fun onBackToMenu() = _uiState.update { it.copy(gameState = GameState.MENU) }

    fun onBackToLevelSelect() = _uiState.update { it.copy(gameState = GameState.LEVEL_SELECT) }

    fun onLevelSelected(levelNumber: Int) {
        _uiState.update { it.copy(
            gameState = GameState.IN_GAME,
            currentLevelNumber = levelNumber,
            currentLevel = levelLoader.loadLevel(levelNumber),
            isLevelWon = false,
            moveHistory = emptyList()
        ) }
    }

    fun onBlockMoved(blockId: String, oldX: Int, oldY: Int, newX: Int, newY: Int) {
        val state = _uiState.value
        val level = state.currentLevel ?: return
        if (oldX == newX && oldY == newY) return

        val newHistory = state.moveHistory + Move(blockId, oldX, oldY)
        val newBlocks = level.blocks.map {
            if (it.id == blockId) it.copy(x = newX, y = newY) else it
        }

        val isWon = newBlocks.find { it.isTarget }?.let { it.x + it.length == level.gridSize } ?: false

        if (isWon) {
            val stars = GameRules.calculateStars(state.currentLevelNumber, newHistory.size)
            progressManager.unlockLevel(state.currentLevelNumber + 1)
            progressManager.saveStars(state.currentLevelNumber, stars)
            loadProgress()
        }

        _uiState.update { it.copy(
            currentLevel = level.copy(blocks = newBlocks),
            moveHistory = newHistory,
            isLevelWon = isWon
        ) }
    }

    fun onUndo() {
        val state = _uiState.value
        if (state.moveHistory.isEmpty() || state.currentLevel == null) return
        val lastMove = state.moveHistory.last()
        val newBlocks = state.currentLevel.blocks.map {
            if (it.id == lastMove.blockId) it.copy(x = lastMove.oldX, y = lastMove.oldY) else it
        }
        _uiState.update { it.copy(
            currentLevel = state.currentLevel.copy(blocks = newBlocks),
            moveHistory = state.moveHistory.dropLast(1)
        ) }
    }

    fun onRestart() = onLevelSelected(_uiState.value.currentLevelNumber)

    fun onNextLevel() = onLevelSelected(_uiState.value.currentLevelNumber + 1)
}