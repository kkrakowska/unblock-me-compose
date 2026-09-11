package com.kkrakowska.unblockme.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun MainMenu(onPlayClick: () -> Unit) {
    val c = LocalAppColors.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(c.bg),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        ) {
            Text(
                text = "UNBLOCK ME",
                fontSize = 26.sp,
                fontWeight = FontWeight.Medium,
                color = c.accent,
                letterSpacing = 4.sp
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Przesuń czerwony klocek do wyjścia",
                fontSize = 13.sp,
                color = c.textSub
            )

            Spacer(Modifier.height(48.dp))

            MiniBoard(c)

            Spacer(Modifier.height(48.dp))

            PrimaryButton(text = "Graj", onClick = onPlayClick)
        }
    }
}

@Composable
private fun MiniBoard(c: AppColorScheme) {
    val G = 4
    val blocks = listOf(
        Triple(1, 0, false),
        Triple(0, 1, true),
        Triple(1, 2, false),
    )

    Box(
        modifier = Modifier
            .size(130.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(c.board)
            .padding(7.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
            repeat(G) { row ->
                Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                    var col = 0
                    while (col < G) {
                        val block = blocks.firstOrNull { it.first == col && it.second == row }
                        if (block != null) {
                            Box(
                                modifier = Modifier
                                    .weight(2f)
                                    .aspectRatio(2f)
                                    .clip(RoundedCornerShape(5.dp))
                                    .background(if (block.third) c.blockTarget else c.blockNormal)
                            )
                            col += 2
                        } else {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(c.cell)
                            )
                            col += 1
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun LevelSelection(
    unlockedLevel: Int,
    totalLevels: Int = 15,
    levelStars: Map<Int, Int> = emptyMap(),
    onLevelSelected: (Int) -> Unit,
    onBack: () -> Unit
) {
    val c = LocalAppColors.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(c.bg)
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        ScreenHeader(title = "Wybierz poziom", onBack = onBack)

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(totalLevels) { index ->
                val level = index + 1
                val unlocked = level <= unlockedLevel
                LevelCell(
                    number = level,
                    unlocked = unlocked,
                    stars = levelStars[level] ?: 0,
                    onClick = { if (unlocked) onLevelSelected(level) }
                )
            }
        }
    }
}

@Composable
private fun LevelCell(number: Int, unlocked: Boolean, stars: Int, onClick: () -> Unit) {
    val c = LocalAppColors.current

    val bg        = if (unlocked) c.accentSoft else c.locked
    val border    = if (unlocked) c.accent     else c.btnBorder
    val textColor = if (unlocked) c.accent     else c.lockedText

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(14.dp))
            .background(bg)
            .border(1.5.dp, border, RoundedCornerShape(14.dp))
            .clickable(enabled = unlocked, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (unlocked) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = number.toString(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = textColor
                )
                if (stars > 0) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "★".repeat(stars) + "☆".repeat(3 - stars),
                        fontSize = 10.sp,
                        color = c.accent,
                        letterSpacing = 1.sp
                    )
                }
            }
        } else {
            Text(
                text = "🔒",
                fontSize = 18.sp
            )
        }
    }
}