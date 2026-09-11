package com.kkrakowska.unblockme.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// ── Paleta ────────────────────────────────────────────────────────────────────

object AppColors {
    // Akcent
    val Accent       = Color(0xFF6C63FF)
    val AccentDark   = Color(0xFF5B52E8)

    // Klocki
    val BlockNormal  = Color(0xFFC4813A)
    val BlockTarget  = Color(0xFFE05C45)

    // Dark mode
    val DarkBg       = Color(0xFF0F0F13)
    val DarkCard     = Color(0xFF1A1A22)
    val DarkCell     = Color(0xFF252530)
    val DarkBoard    = Color(0xFF14141C)
    val DarkBtn      = Color(0xFF3A3A50)
    val DarkBtnBorder= Color(0xFF5A5A78)
    val DarkText     = Color(0xFFF0EEF8)
    val DarkTextSub  = Color(0xFF9B98B4)
    val DarkLocked   = Color(0xFF1A1A24)
    val DarkLockedTxt= Color(0xFF48476A)
    val DarkAccentSoft = Color(0xFF2A2550)

    // Light mode
    val LightBg      = Color(0xFFF5F4FA)
    val LightCard    = Color(0xFFFFFFFF)
    val LightCell    = Color(0xFFE2E0F0)
    val LightBoard   = Color(0xFFCCCAE0)
    val LightBtn     = Color(0xFFFFFFFF)
    val LightBtnBorder = Color(0xFFD0CEE8)
    val LightText    = Color(0xFF1A1828)
    val LightTextSub = Color(0xFF6E6B88)
    val LightLocked  = Color(0xFFEEEDFE)
    val LightLockedTxt = Color(0xFFB0AECB)
    val LightAccentSoft = Color(0xFFEEEDFE)
}

// ── Kolory zależne od motywu ───────────────────────────────────────────────────

data class AppColorScheme(
    val bg: Color,
    val card: Color,
    val cell: Color,
    val board: Color,
    val btn: Color,
    val btnBorder: Color,
    val text: Color,
    val textSub: Color,
    val accent: Color,
    val accentSoft: Color,
    val locked: Color,
    val lockedText: Color,
    val blockNormal: Color,
    val blockTarget: Color,
    val exit: Color,
)

val DarkScheme = AppColorScheme(
    bg          = AppColors.DarkBg,
    card        = AppColors.DarkCard,
    cell        = AppColors.DarkCell,
    board       = AppColors.DarkBoard,
    btn         = AppColors.DarkBtn,
    btnBorder   = AppColors.DarkBtnBorder,
    text        = AppColors.DarkText,
    textSub     = AppColors.DarkTextSub,
    accent      = AppColors.Accent,
    accentSoft  = AppColors.DarkAccentSoft,
    locked      = AppColors.DarkLocked,
    lockedText  = AppColors.DarkLockedTxt,
    blockNormal = AppColors.BlockNormal,
    blockTarget = AppColors.BlockTarget,
    exit        = AppColors.Accent,
)

val LightScheme = AppColorScheme(
    bg          = AppColors.LightBg,
    card        = AppColors.LightCard,
    cell        = AppColors.LightCell,
    board       = AppColors.LightBoard,
    btn         = AppColors.LightBtn,
    btnBorder   = AppColors.LightBtnBorder,
    text        = AppColors.LightText,
    textSub     = AppColors.LightTextSub,
    accent      = AppColors.AccentDark,
    accentSoft  = AppColors.LightAccentSoft,
    locked      = AppColors.LightLocked,
    lockedText  = AppColors.LightLockedTxt,
    blockNormal = AppColors.BlockNormal,
    blockTarget = AppColors.BlockTarget,
    exit        = AppColors.AccentDark,
)

val LocalAppColors = staticCompositionLocalOf { DarkScheme }

@Composable
fun UnblockMeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkScheme else LightScheme

    val material = if (darkTheme) {
        darkColorScheme(
            primary   = AppColors.Accent,
            background = AppColors.DarkBg,
            surface   = AppColors.DarkCard,
        )
    } else {
        lightColorScheme(
            primary   = AppColors.AccentDark,
            background = AppColors.LightBg,
            surface   = AppColors.LightCard,
        )
    }

    CompositionLocalProvider(LocalAppColors provides colors) {
        MaterialTheme(colorScheme = material, content = content)
    }
}
