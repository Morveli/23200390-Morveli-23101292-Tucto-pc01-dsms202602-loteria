package com.example.lumilotto.ui.sorteo

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * Paleta de colores premium para LUMI Lotto.
 * Inspirada en aplicaciones modernas de lotería y fintech.
 */
object LumiColors {
    val MidnightBackground = Color(0xFF0B1026) // Fondo azul medianoche
    val SurfaceDark = Color(0xFF171D3A)        // Superficies de tarjetas
    val SurfaceVariantDark = Color(0xFF21284C) // Variante de superficie / bordes
    val CardBorder = Color(0xFF2E386B)         // Bordes sutiles

    val AccentViolet = Color(0xFF9B7BFF)       // Violeta principal
    val AccentVioletLight = Color(0xFFBCA6FF)  // Violeta claro
    val SoftGold = Color(0xFFF5C76B)           // Dorado suave
    val SoftGoldDark = Color(0xFFD4A33B)       // Dorado oscuro

    val TextWhite = Color(0xFFFFFFFF)          // Texto blanco
    val TextLavender = Color(0xFFA0A5C0)       // Texto gris lavanda
    val TextMuted = Color(0xFF6C739C)          // Texto atenuable

    // Estados
    val StatusAbierto = Color(0xFF3A86FF)      // Azul para Abierto
    val StatusEnCurso = Color(0xFF9B7BFF)      // Violeta para En Curso
    val StatusFinalizado = Color(0xFF2EC4B6)   // Verde agua para Finalizado
    val StatusWinner = Color(0xFFF5C76B)       // Dorado para Ganador

    // Gradientes
    val BackgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF0F1532),
            MidnightBackground,
            Color(0xFF070A18)
        )
    )

    val VioletGradient = Brush.linearGradient(
        colors = listOf(AccentViolet, Color(0xFF7A52F4))
    )

    val GoldGradient = Brush.linearGradient(
        colors = listOf(SoftGold, SoftGoldDark)
    )

    val BallEmptyGradient = Brush.radialGradient(
        colors = listOf(Color(0xFF20284A), Color(0xFF131830))
    )
}

private val LumiDarkColorScheme = darkColorScheme(
    primary = LumiColors.AccentViolet,
    onPrimary = LumiColors.TextWhite,
    primaryContainer = LumiColors.SurfaceVariantDark,
    onPrimaryContainer = LumiColors.TextWhite,
    secondary = LumiColors.SoftGold,
    onSecondary = LumiColors.MidnightBackground,
    background = LumiColors.MidnightBackground,
    onBackground = LumiColors.TextWhite,
    surface = LumiColors.SurfaceDark,
    onSurface = LumiColors.TextWhite,
    surfaceVariant = LumiColors.SurfaceVariantDark,
    onSurfaceVariant = LumiColors.TextLavender,
    outline = LumiColors.CardBorder
)

/**
 * Tema envolvente para garantizar la estética azul medianoche premium de LUMI Lotto.
 */
@Composable
fun LumiLottoSorteoTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LumiDarkColorScheme,
        content = content
    )
}
