package com.example.lumilotto.ui.sorteo

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

/**
 * Encabezado de la pantalla con marca LUMI LOTTO, información del sorteo y botón de retorno.
 */
@Composable
fun SorteoHeader(
    nombreSorteo: String,
    fechaSorteo: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .size(40.dp)
                .background(LumiColors.SurfaceDark, CircleShape)
                .border(1.dp, LumiColors.CardBorder, CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Volver",
                tint = LumiColors.TextWhite
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "LUMI",
                    color = LumiColors.SoftGold,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "LOTTO",
                    color = LumiColors.TextWhite,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
            Text(
                text = "$nombreSorteo • $fechaSorteo",
                color = LumiColors.TextLavender,
                fontSize = 12.sp,
                maxLines = 1
            )
        }
    }
}

private data class BadgeConfig(
    val backgroundColor: Color,
    val textColor: Color,
    val label: String,
    val icon: ImageVector
)

/**
 * Badge o pastilla indicadora del estado del sorteo (ABIERTO, EN CURSO, FINALIZADO).
 */
@Composable
fun EstadoBadge(
    estado: EstadoSorteo,
    modifier: Modifier = Modifier
) {
    val config = when (estado) {
        EstadoSorteo.ABIERTO -> BadgeConfig(
            backgroundColor = LumiColors.StatusAbierto.copy(alpha = 0.18f),
            textColor = LumiColors.StatusAbierto,
            label = "ABIERTO",
            icon = Icons.Default.Info
        )
        EstadoSorteo.EN_CURSO -> BadgeConfig(
            backgroundColor = LumiColors.StatusEnCurso.copy(alpha = 0.22f),
            textColor = LumiColors.StatusEnCurso,
            label = "EN CURSO",
            icon = Icons.Default.PlayArrow
        )
        EstadoSorteo.FINALIZADO -> BadgeConfig(
            backgroundColor = LumiColors.StatusFinalizado.copy(alpha = 0.22f),
            textColor = LumiColors.StatusFinalizado,
            label = "FINALIZADO",
            icon = Icons.Default.Check
        )
    }

    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, config.textColor.copy(alpha = 0.4f), RoundedCornerShape(20.dp)),
        color = config.backgroundColor
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = config.icon,
                contentDescription = null,
                tint = config.textColor,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = config.label,
                color = config.textColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }
    }
}

/**
 * Representa una posición individual de bolilla del sorteo (extraída o vacía).
 */
@Composable
fun BolillaSlot(
    posicion: Int,
    numero: Int?,
    modifier: Modifier = Modifier
) {
    val size = 48.dp

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .shadow(
                    elevation = if (numero != null) 6.dp else 0.dp,
                    shape = CircleShape,
                    ambientColor = LumiColors.AccentViolet,
                    spotColor = LumiColors.SoftGold
                )
                .clip(CircleShape)
                .background(
                    if (numero != null) LumiColors.VioletGradient else LumiColors.BallEmptyGradient
                )
                .border(
                    width = if (numero != null) 1.5.dp else 1.dp,
                    color = if (numero != null) LumiColors.SoftGold else LumiColors.CardBorder,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (numero != null) {
                Text(
                    text = String.format(Locale.getDefault(), "%02d", numero),
                    color = LumiColors.TextWhite,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            } else {
                Text(
                    text = "-",
                    color = LumiColors.TextLavender,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "N° $posicion",
            color = LumiColors.TextLavender,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

/**
 * Contenedor con las 6 posiciones de bolillas mostradas en estricto orden de salida.
 */
@Composable
fun BolillasSorteadasCard(
    bolillasSorteadas: List<Int>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = LumiColors.SurfaceDark),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = SolidColor(LumiColors.CardBorder)
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Bolillas del Sorteo",
                    color = LumiColors.TextWhite,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Orden de salida",
                    color = LumiColors.TextLavender,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                for (i in 1..6) {
                    val numeroExtraido = bolillasSorteadas.getOrNull(i - 1)
                    BolillaSlot(
                        posicion = i,
                        numero = numeroExtraido
                    )
                }
            }
        }
    }
}

/**
 * Muestra la jugada del usuario resaltando los aciertos.
 */
@Composable
fun JugadaUsuarioCard(
    jugadaUsuario: List<Int>,
    bolillasSorteadas: List<Int>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = LumiColors.SurfaceDark),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = SolidColor(LumiColors.CardBorder)
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = LumiColors.SoftGold,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Tu Jugada",
                        color = LumiColors.TextWhite,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = "6 números (1-45)",
                    color = LumiColors.TextLavender,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (jugadaUsuario.isEmpty()) {
                Text(
                    text = "No se ha registrado una jugada para este sorteo.",
                    color = LumiColors.TextLavender,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                )
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    jugadaUsuario.take(6).forEach { num ->
                        val esAcierto = SorteoCalculoUtils.esNumeroAcertado(num, bolillasSorteadas)
                        JugadaNumeroChip(
                            numero = num,
                            esAcierto = esAcierto
                        )
                    }
                }
            }
        }
    }
}

/**
 * Chip individual para cada número jugado por el usuario.
 */
@Composable
fun JugadaNumeroChip(
    numero: Int,
    esAcierto: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(44.dp)
            .shadow(
                elevation = if (esAcierto) 8.dp else 0.dp,
                shape = CircleShape,
                ambientColor = LumiColors.SoftGold,
                spotColor = LumiColors.SoftGold
            )
            .clip(CircleShape)
            .background(
                if (esAcierto) LumiColors.GoldGradient else SolidColor(LumiColors.SurfaceVariantDark)
            )
            .border(
                width = if (esAcierto) 1.5.dp else 1.dp,
                color = if (esAcierto) LumiColors.SoftGold else LumiColors.CardBorder,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = String.format(Locale.getDefault(), "%02d", numero),
                color = if (esAcierto) LumiColors.MidnightBackground else LumiColors.TextWhite,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            if (esAcierto) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Acierto",
                    tint = LumiColors.MidnightBackground,
                    modifier = Modifier.size(10.dp)
                )
            }
        }
    }
}

/**
 * Tarjeta de resumen de bolillas publicadas y aciertos.
 */
@Composable
fun ResumenCard(
    bolillasPublicadas: Int,
    cantidadAciertos: Int,
    totalNumerosJugada: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Publicadas
        Card(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = LumiColors.SurfaceDark),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = SolidColor(LumiColors.CardBorder)
            )
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Publicadas",
                    color = LumiColors.TextLavender,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$bolillasPublicadas / 6",
                    color = LumiColors.AccentViolet,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }

        // Aciertos
        Card(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = LumiColors.SurfaceDark),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = SolidColor(
                    if (cantidadAciertos > 0) LumiColors.SoftGold else LumiColors.CardBorder
                )
            )
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Aciertos",
                    color = LumiColors.TextLavender,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "$cantidadAciertos",
                        color = if (cantidadAciertos > 0) LumiColors.SoftGold else LumiColors.TextWhite,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = " / $totalNumerosJugada",
                        color = LumiColors.TextLavender,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

private data class BannerConfig(
    val containerBg: Color,
    val borderBrush: Brush,
    val iconTint: Color,
    val icon: ImageVector,
    val title: String,
    val message: String
)

/**
 * Mensaje informativo dinámico que cambia según el estado del sorteo y las bolillas publicadas.
 */
@Composable
fun EstadoMensajeBanner(
    uiState: SorteoUiState,
    modifier: Modifier = Modifier
) {
    val estado = uiState.estado
    val bolillasCount = uiState.bolillasPublicadasCount
    val esGanador = uiState.esGanador

    val config = when {
        // 1. Estado ABIERTO
        estado == EstadoSorteo.ABIERTO -> BannerConfig(
            containerBg = LumiColors.SurfaceDark,
            borderBrush = SolidColor(LumiColors.StatusAbierto.copy(alpha = 0.5f)),
            iconTint = LumiColors.StatusAbierto,
            icon = Icons.Default.Info,
            title = "Sorteo Abierto",
            message = "El sorteo aún no ha comenzado. Las bolillas se mostrarán a medida que salgan."
        )

        // 2. Estado EN_CURSO
        estado == EstadoSorteo.EN_CURSO -> BannerConfig(
            containerBg = LumiColors.SurfaceDark,
            borderBrush = SolidColor(LumiColors.StatusEnCurso.copy(alpha = 0.6f)),
            iconTint = LumiColors.AccentViolet,
            icon = Icons.Default.PlayArrow,
            title = "Sorteo en Curso",
            message = "Sorteo en curso. Publicando bolillas en tiempo real ($bolillasCount de 6)..."
        )

        // 3. FINALIZADO pero faltan bolillas por publicar
        estado == EstadoSorteo.FINALIZADO && bolillasCount < 6 -> BannerConfig(
            containerBg = LumiColors.SurfaceDark,
            borderBrush = SolidColor(LumiColors.SoftGold.copy(alpha = 0.5f)),
            iconTint = LumiColors.SoftGold,
            icon = Icons.Default.Info,
            title = "Esperando Resultados",
            message = "Sorteo finalizado. Esperando la confirmación de las bolillas restantes..."
        )

        // 4. FINALIZADO + 6 bolillas + GANADOR (6 aciertos)
        estado == EstadoSorteo.FINALIZADO && bolillasCount == 6 && esGanador -> BannerConfig(
            containerBg = LumiColors.SurfaceDark,
            borderBrush = LumiColors.GoldGradient,
            iconTint = LumiColors.SoftGold,
            icon = Icons.Default.Star,
            title = "¡GANADOR!",
            message = "¡FELICITACIONES! Has acertado los 6 números del sorteo. ¡Eres un feliz ganador!"
        )

        // 5. FINALIZADO + 6 bolillas + NO GANADOR (< 6 aciertos)
        else -> BannerConfig(
            containerBg = LumiColors.SurfaceDark,
            borderBrush = SolidColor(LumiColors.AccentViolet.copy(alpha = 0.5f)),
            iconTint = LumiColors.AccentViolet,
            icon = Icons.Default.Info,
            title = "SIGUE INTENTANDO",
            message = "Sorteo finalizado. Obtuviste ${uiState.cantidadAciertos} aciertos. ¡Muchos éxitos en la próxima jugada!"
        )
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = config.containerBg),
        border = CardDefaults.outlinedCardBorder().copy(brush = config.borderBrush)
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(config.iconTint.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = config.icon,
                    contentDescription = null,
                    tint = config.iconTint,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column {
                Text(
                    text = config.title,
                    color = if (esGanador) LumiColors.SoftGold else LumiColors.TextWhite,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = config.message,
                    color = LumiColors.TextLavender,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

/**
 * Botón principal de retorno/volver.
 */
@Composable
fun VolverButton(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onBackClick,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = LumiColors.SurfaceDark,
            contentColor = LumiColors.TextWhite
        ),
        border = BorderStroke(1.dp, LumiColors.CardBorder)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                tint = LumiColors.TextWhite,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Volver",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
