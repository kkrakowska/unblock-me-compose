package com.kkrakowska.unblockme.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kkrakowska.unblockme.GameUiState
import com.kkrakowska.unblockme.models.Block
import com.kkrakowska.unblockme.models.Level
import com.kkrakowska.unblockme.models.Orientation
import com.kkrakowska.unblockme.utils.GameRules
import kotlin.math.roundToInt

data class Move(val blockId: String, val oldX: Int, val oldY: Int)

@Composable
fun GamePlayScreen(
    uiState: GameUiState,
    onRestart: () -> Unit,
    onUndo: () -> Unit,
    onBackToMenu: () -> Unit,
    onNextLevel: () -> Unit,
    onMoveMade: (String, Int, Int, Int, Int) -> Unit
) {
    val c = LocalAppColors.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(c.bg)
    ) {
        when {
            uiState.isLevelWon -> WinScreen(
                levelNumber = uiState.currentLevelNumber,
                moveCount = uiState.moveHistory.size,
                onNextLevel = onNextLevel,
                onBackToMenu = onBackToMenu
            )

            uiState.currentLevel != null -> GameContent(
                level = uiState.currentLevel,
                levelNumber = uiState.currentLevelNumber,
                moveCount = uiState.moveHistory.size,
                canUndo = uiState.moveHistory.isNotEmpty(),
                onRestart = onRestart,
                onUndo = onUndo,
                onBack = onBackToMenu,
                onMoveMade = onMoveMade
            )

            else -> NoMoreLevels(onBack = onBackToMenu)
        }
    }
}

@Composable
private fun GameContent(
    level: Level,
    levelNumber: Int,
    moveCount: Int,
    canUndo: Boolean,
    onRestart: () -> Unit,
    onUndo: () -> Unit,
    onBack: () -> Unit,
    onMoveMade: (String, Int, Int, Int, Int) -> Unit
) {
    val c = LocalAppColors.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        ScreenHeader(
            title = "",
            onBack = onBack,
            trailingContent = {
                Box(modifier = Modifier.width(36.dp))
            }
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(bottom = 4.dp)
        ) {
            Text(
                text = "Poziom $levelNumber",
                fontSize = 28.sp,
                fontWeight = FontWeight.Medium,
                color = c.text,
                letterSpacing = 1.sp
            )
            Spacer(Modifier.height(4.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text("Ruchy:", fontSize = 14.sp, color = c.textSub)
                Text(
                    text = "$moveCount",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium,
                    color = c.accent
                )
            }
        }

        val targetBlock = level.blocks.firstOrNull { it.isTarget }

        BoxWithConstraints(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            val boardPaddingDp = 6.dp
            val boardPaddingPx = with(LocalDensity.current) { boardPaddingDp.toPx() }
            val boardWidthPx = with(LocalDensity.current) { maxWidth.toPx() }
            val innerWidthPx = boardWidthPx - boardPaddingPx * 2
            val gapPx = innerWidthPx * 0.018f
            val cellPx = (innerWidthPx - gapPx * (level.gridSize + 1)) / level.gridSize

            val exitRow = targetBlock?.y ?: (level.gridSize / 2)
            val exitTopPx = boardPaddingPx + gapPx + exitRow * (cellPx + gapPx)
            val exitHeightPx = cellPx
            val exitTopDp = with(LocalDensity.current) { exitTopPx.toDp() }
            val exitHeightDp = with(LocalDensity.current) { exitHeightPx.toDp() }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(c.board)
                    .padding(boardPaddingDp)
            ) {
                GameBoard(
                    level = level,
                    onMoveMade = onMoveMade
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 6.dp, y = exitTopDp)
                    .width(8.dp)
                    .height(exitHeightDp)
                    .clip(RoundedCornerShape(topEnd = 5.dp, bottomEnd = 5.dp))
                    .background(c.exit)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = onUndo,
                enabled = canUndo,
                modifier = Modifier.weight(1f).height(58.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = c.btn,
                    contentColor = c.text,
                    disabledContainerColor = c.locked,
                    disabledContentColor = c.lockedText
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.5.dp,
                    if (canUndo) c.btnBorder else c.locked
                )
            ) {
                Text("↩ Cofnij ($moveCount)", fontSize = 14.sp)
            }

            OutlinedButton(
                onClick = onRestart,
                modifier = Modifier.weight(1f).height(58.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = c.btn,
                    contentColor = c.text
                ),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, c.btnBorder)
            ) {
                Text("↺ Restart", fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun GameBoard(level: Level, onMoveMade: (String, Int, Int, Int, Int) -> Unit) {
    val c = LocalAppColors.current
    val density = LocalDensity.current

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    ) {

        val widthPx = with(density) { maxWidth.toPx() }
        val gapPx = widthPx * 0.018f
        val cellPx = (widthPx - gapPx * (level.gridSize + 1)) / level.gridSize
        val cellSize = with(density) { cellPx.toDp() }
        val gap = with(density) { gapPx.toDp() }

        Canvas(modifier = Modifier.fillMaxSize()) {
            for (row in 0 until level.gridSize) {
                for (col in 0 until level.gridSize) {
                    val x = gapPx + col * (cellPx + gapPx)
                    val y = gapPx + row * (cellPx + gapPx)
                    drawRoundRect(
                        color = c.cell,
                        topLeft = Offset(x, y),
                        size = Size(cellPx, cellPx),
                        cornerRadius = CornerRadius(5f, 5f)
                    )
                }
            }
        }

        level.blocks.forEach { block ->
            BlockView(
                block = block,
                allBlocks = level.blocks,
                cellSize = cellSize,
                gap = gap,
                gridSize = level.gridSize,
                onMoveMade = onMoveMade
            )
        }
    }
}

@Composable
fun BlockView(
    block: Block,
    allBlocks: List<Block>,
    cellSize: Dp,
    gap: Dp,
    gridSize: Int,
    onMoveMade: (String, Int, Int, Int, Int) -> Unit
) {
    val c = LocalAppColors.current
    val density = LocalDensity.current
    val cellSizePx = with(density) { cellSize.toPx() }
    val gapPx = with(density) { gap.toPx() }

    fun cellToOffset(index: Int) = gapPx + index * (cellSizePx + gapPx)

    var offsetX by remember { mutableFloatStateOf(cellToOffset(block.x)) }
    var offsetY by remember { mutableFloatStateOf(cellToOffset(block.y)) }
    var startX by remember { mutableIntStateOf(block.x) }
    var startY by remember { mutableIntStateOf(block.y) }
    var minBoundPx by remember { mutableFloatStateOf(0f) }
    var maxBoundPx by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(block.x, block.y) {
        offsetX = cellToOffset(block.x)
        offsetY = cellToOffset(block.y)
    }

    val blockColor = if (block.isTarget) c.blockTarget else c.blockNormal

    val cols = if (block.orientation == Orientation.HORIZONTAL) block.length else 1
    val rows = if (block.orientation == Orientation.VERTICAL) block.length else 1
    val blockW = with(density) { (cols * cellSizePx + (cols - 1) * gapPx).toDp() }
    val blockH = with(density) { (rows * cellSizePx + (rows - 1) * gapPx).toDp() }

    Box(
        modifier = Modifier
            .offset(
                x = with(density) { offsetX.toDp() },
                y = with(density) { offsetY.toDp() }
            )
            .size(width = blockW, height = blockH)
            .clip(RoundedCornerShape(7.dp))
            .background(blockColor)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        startX = block.x
                        startY = block.y
                        val bounds = GameRules.calculateBounds(block, allBlocks, gridSize)
                        minBoundPx = cellToOffset(bounds.first)
                        maxBoundPx = cellToOffset(bounds.second)
                    },
                    onDragEnd = {
                        val bounds = GameRules.calculateBounds(block, allBlocks, gridSize)
                        val newX = if (block.orientation == Orientation.HORIZONTAL) snapToGrid(offsetX, cellSizePx, gapPx, bounds.first, bounds.second) else block.x
                        val newY = if (block.orientation == Orientation.VERTICAL) snapToGrid(offsetY, cellSizePx, gapPx, bounds.first, bounds.second) else block.y

                        if (newX != startX || newY != startY) {
                            onMoveMade(block.id, startX, startY, newX, newY)
                        } else {
                            offsetX = cellToOffset(startX)
                            offsetY = cellToOffset(startY)
                        }
                    }
                ) { change, drag ->
                    change.consume()
                    if (block.orientation == Orientation.HORIZONTAL) {
                        offsetX = (offsetX + drag.x).coerceIn(minBoundPx, maxBoundPx)
                    } else {
                        offsetY = (offsetY + drag.y).coerceIn(minBoundPx, maxBoundPx)
                    }
                }
            }
    )
}

private fun snapToGrid(offset: Float, cellSize: Float, gap: Float, min: Int, max: Int): Int {
    val idx = ((offset - gap) / (cellSize + gap)).roundToInt()
    return idx.coerceIn(min, max)
}

@Composable
fun WinScreen(
    levelNumber: Int,
    moveCount: Int,
    onNextLevel: () -> Unit,
    onBackToMenu: () -> Unit
) {
    val c = LocalAppColors.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("🏆", fontSize = 52.sp)

        Spacer(Modifier.height(12.dp))

        Text(
            text = "Poziom ukończony!",
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium,
            color = c.text
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = "Poziom $levelNumber",
            fontSize = 13.sp,
            color = c.textSub
        )

        Spacer(Modifier.height(20.dp))

        val stars = GameRules.calculateStars(levelNumber, moveCount)
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            repeat(3) { i ->
                Text(
                    text = if (i < stars) "★" else "☆",
                    fontSize = 36.sp,
                    color = if (i < stars) c.accent else c.lockedText
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatCard(label = "Ruchów", value = moveCount.toString(), modifier = Modifier.weight(1f))
        }

        Spacer(Modifier.height(24.dp))

        PrimaryButton(text = "Następny poziom", onClick = onNextLevel)
        Spacer(Modifier.height(10.dp))
        SecondaryButton(text = "Wybór poziomów", onClick = onBackToMenu)
    }
}

@Composable
private fun StatCard(label: String, value: String, modifier: Modifier = Modifier) {
    val c = LocalAppColors.current
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(c.btn)
            .border(1.5.dp, c.btnBorder, RoundedCornerShape(12.dp))
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(value, fontSize = 22.sp, fontWeight = FontWeight.Medium, color = c.text)
        Spacer(Modifier.height(2.dp))
        Text(label, fontSize = 11.sp, color = c.textSub)
    }
}

@Composable
private fun NoMoreLevels(onBack: () -> Unit) {
    val c = LocalAppColors.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("🎉", fontSize = 48.sp)
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Ukończono wszystkie poziomy!",
            fontSize = 18.sp,
            color = c.text,
            fontWeight = FontWeight.Medium
        )
        Spacer(Modifier.height(32.dp))
        SecondaryButton(text = "Wróć do menu", onClick = onBack)
    }
}