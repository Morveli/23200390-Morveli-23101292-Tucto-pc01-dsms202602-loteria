package com.example.lumilotto.ui.sorteo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Pantalla de seguimiento en tiempo real del sorteo y sus resultados.
 *
 * Recibe todos los datos a través de [SorteoUiState] para desacoplar
 * la interfaz de la lógica de red/Firestore o estado de ViewModel.
 *
 * @param uiState Estado inmutable con información del sorteo, bolillas y jugada.
 * @param onBackClick Callback invocado al presionar la acción de volver.
 * @param modifier Modificador de diseño.
 */
@Composable
fun SorteoScreen(
    uiState: SorteoUiState,
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    LumiLottoSorteoTheme {
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
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    // 1. Encabezado LUMI Lotto
                    SorteoHeader(
                        nombreSorteo = uiState.nombreSorteo,
                        fechaSorteo = uiState.fechaSorteo,
                        onBackClick = onBackClick
                    )

                    // 2. Fila con indicador de Estado del Sorteo
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Estado del Sorteo",
                            color = LumiColors.TextWhite,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        EstadoBadge(estado = uiState.estado)
                    }

                    // 3. Bolillas del Sorteo en vivo (6 posiciones)
                    BolillasSorteadasCard(
                        bolillasSorteadas = uiState.bolillasSorteadas
                    )

                    // 4. Jugada del Usuario con aciertos resaltados
                    JugadaUsuarioCard(
                        jugadaUsuario = uiState.jugadaUsuario,
                        bolillasSorteadas = uiState.bolillasSorteadas
                    )

                    // 5. Resumen de Bolillas publicadas y número de Aciertos
                    ResumenCard(
                        bolillasPublicadas = uiState.bolillasPublicadasCount,
                        cantidadAciertos = uiState.cantidadAciertos,
                        totalNumerosJugada = uiState.jugadaUsuario.size.coerceAtLeast(6)
                    )

                    // 6. Mensaje de estado / resultado
                    EstadoMensajeBanner(uiState = uiState)

                    Spacer(modifier = Modifier.height(12.dp))

                    // 7. Botón de acción Volver
                    VolverButton(onBackClick = onBackClick)

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

// ============================================================================
// PREVIEWS EXCLUSIVAS PARA PREVISUALIZACIÓN EN ANDROID STUDIO CON DATOS MOCK
// ============================================================================

@Preview(name = "1. Estado ABIERTO - Espera de inicio", showBackground = true)
@Composable
fun SorteoScreenAbiertoPreview() {
    SorteoScreen(
        uiState = SorteoUiState(
            nombreSorteo = "LUMI Sorteo #1024",
            fechaSorteo = "24 Oct 2024 - 21:00 hs",
            estado = EstadoSorteo.ABIERTO,
            bolillasSorteadas = emptyList(),
            jugadaUsuario = listOf(5, 12, 23, 34, 41, 45)
        )
    )
}

@Preview(name = "2. Estado EN CURSO - Bolillas parciales (2 aciertos)", showBackground = true)
@Composable
fun SorteoScreenEnCursoPreview() {
    SorteoScreen(
        uiState = SorteoUiState(
            nombreSorteo = "LUMI Sorteo #1024",
            fechaSorteo = "24 Oct 2024 - 21:00 hs",
            estado = EstadoSorteo.EN_CURSO,
            bolillasSorteadas = listOf(12, 34, 7),
            jugadaUsuario = listOf(5, 12, 23, 34, 41, 45)
        )
    )
}

@Preview(name = "3. Estado FINALIZADO - Faltan bolillas por publicar", showBackground = true)
@Composable
fun SorteoScreenFinalizadoIncompletoPreview() {
    SorteoScreen(
        uiState = SorteoUiState(
            nombreSorteo = "LUMI Sorteo #1024",
            fechaSorteo = "24 Oct 2024 - 21:00 hs",
            estado = EstadoSorteo.FINALIZADO,
            bolillasSorteadas = listOf(12, 34, 7, 21),
            jugadaUsuario = listOf(5, 12, 23, 34, 41, 45)
        )
    )
}

@Preview(name = "4. Estado FINALIZADO - Sigue Intentando (3 aciertos)", showBackground = true)
@Composable
fun SorteoScreenFinalizadoSigueIntentandoPreview() {
    SorteoScreen(
        uiState = SorteoUiState(
            nombreSorteo = "LUMI Sorteo #1024",
            fechaSorteo = "24 Oct 2024 - 21:00 hs",
            estado = EstadoSorteo.FINALIZADO,
            bolillasSorteadas = listOf(12, 34, 7, 21, 41, 18),
            jugadaUsuario = listOf(5, 12, 23, 34, 41, 45)
        )
    )
}

@Preview(name = "5. Estado FINALIZADO - ¡GANADOR! (6 aciertos)", showBackground = true)
@Composable
fun SorteoScreenFinalizadoGanadorPreview() {
    SorteoScreen(
        uiState = SorteoUiState(
            nombreSorteo = "LUMI Sorteo #1024",
            fechaSorteo = "24 Oct 2024 - 21:00 hs",
            estado = EstadoSorteo.FINALIZADO,
            bolillasSorteadas = listOf(5, 12, 23, 34, 41, 45),
            jugadaUsuario = listOf(5, 12, 23, 34, 41, 45)
        )
    )
}
