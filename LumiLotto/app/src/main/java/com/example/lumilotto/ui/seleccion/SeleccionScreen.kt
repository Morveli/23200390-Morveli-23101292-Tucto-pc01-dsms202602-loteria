package com.example.lumilotto.ui.seleccion

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lumilotto.ui.sorteo.LumiColors
import java.util.Locale

/**
 * Pantalla de selección de 6 números (1 al 45) para la jugada de LUMI Lotto.
 */
@Composable
fun SeleccionScreen(
    state: SeleccionUiState,
    userEmail: String,
    onNumeroClick: (Int) -> Unit,
    onGuardarJugada: () -> Unit,
    onVerSorteoClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = LumiColors.MidnightBackground
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(LumiColors.BackgroundGradient)
                .padding(paddingValues)
        ) {
            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = LumiColors.SoftGold)
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // ENCABEZADO
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(LumiColors.SurfaceDark)
                                    .border(1.dp, LumiColors.SoftGold, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = LumiColors.SoftGold,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "LUMI LOTTO",
                                    color = LumiColors.SoftGold,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Black
                                )
                                Text(
                                    text = userEmail,
                                    color = LumiColors.TextLavender,
                                    fontSize = 11.sp,
                                    maxLines = 1
                                )
                            }
                        }

                        IconButton(
                            onClick = onLogoutClick,
                            modifier = Modifier
                                .size(36.dp)
                                .background(LumiColors.SurfaceDark, CircleShape)
                                .border(1.dp, LumiColors.CardBorder, CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                contentDescription = "Cerrar Sesión",
                                tint = LumiColors.TextWhite,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // TÍTULO Y CONTADOR
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = LumiColors.SurfaceDark),
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = SolidColor(LumiColors.CardBorder)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Selecciona tu Jugada",
                                color = LumiColors.TextWhite,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Elige exactamente 6 números distintos (1 al 45)",
                                color = LumiColors.TextLavender,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            // PÍLDORA CONTADOR
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = if (state.cantidadSeleccionada == 6) LumiColors.SoftGold.copy(alpha = 0.2f) else LumiColors.SurfaceVariantDark,
                                border = BorderStroke(
                                    1.dp,
                                    if (state.cantidadSeleccionada == 6) LumiColors.SoftGold else LumiColors.CardBorder
                                )
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Seleccionados: ",
                                        color = LumiColors.TextLavender,
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = "${state.cantidadSeleccionada} / 6",
                                        color = if (state.cantidadSeleccionada == 6) LumiColors.SoftGold else LumiColors.TextWhite,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    // MENSAJE DE ERROR O ADVERTENCIA
                    if (state.errorMessage != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFFE53935).copy(alpha = 0.15f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, androidx.compose.ui.graphics.Color(0xFFE53935).copy(alpha = 0.5f))
                        ) {
                            Text(
                                text = state.errorMessage,
                                color = androidx.compose.ui.graphics.Color(0xFFFF6B6B),
                                fontSize = 12.sp,
                                modifier = Modifier.padding(10.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // GRILLA DE 45 NÚMEROS (5 columnas)
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(5),
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentPadding = PaddingValues(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(45) { index ->
                            val numero = index + 1
                            val seleccionado = state.numerosSeleccionados.contains(numero)

                            NumeroBallItem(
                                numero = numero,
                                seleccionado = seleccionado,
                                onClick = { onNumeroClick(numero) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // BOTÓN CONFIRMAR / VER SORTEO
                    if (state.jugadaExistente != null && state.jugadaExistente.size == 6) {
                        Button(
                            onClick = onVerSorteoClick,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = LumiColors.AccentViolet)
                        ) {
                            Text(
                                text = "Ver Sorteo en Vivo (Jugada Guardada)",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                        Button(
                            onClick = onGuardarJugada,
                            enabled = state.puedeGuardar,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = LumiColors.SoftGold,
                                disabledContainerColor = LumiColors.SoftGold.copy(alpha = 0.3f),
                                contentColor = LumiColors.MidnightBackground,
                                disabledContentColor = LumiColors.TextLavender
                            )
                        ) {
                            if (state.isSaving) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = LumiColors.MidnightBackground,
                                    strokeWidth = 2.5.dp
                                )
                            } else {
                                Text(
                                    text = if (state.cantidadSeleccionada == 6) "Confirmar Jugada" else "Selecciona 6 números",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NumeroBallItem(
    numero: Int,
    seleccionado: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(52.dp)
            .shadow(
                elevation = if (seleccionado) 6.dp else 0.dp,
                shape = CircleShape,
                ambientColor = LumiColors.SoftGold,
                spotColor = LumiColors.SoftGold
            )
            .clip(CircleShape)
            .background(
                if (seleccionado) LumiColors.GoldGradient else SolidColor(
                    LumiColors.SurfaceDark
                )
            )
            .border(
                width = if (seleccionado) 1.5.dp else 1.dp,
                color = if (seleccionado) LumiColors.SoftGold else LumiColors.CardBorder,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Transparent,
            onClick = onClick
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = String.format(Locale.getDefault(), "%02d", numero),
                    color = if (seleccionado) LumiColors.MidnightBackground else LumiColors.TextWhite,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
