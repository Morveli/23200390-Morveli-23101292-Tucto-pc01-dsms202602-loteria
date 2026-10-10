package com.example.lumilotto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.lumilotto.ui.auth.AuthScreen
import com.example.lumilotto.ui.auth.AuthViewModel
import com.example.lumilotto.ui.seleccion.SeleccionScreen
import com.example.lumilotto.ui.seleccion.SeleccionViewModel
import com.example.lumilotto.ui.sorteo.SorteoLiveViewModel
import com.example.lumilotto.ui.sorteo.SorteoScreen
import com.example.lumilotto.ui.sorteo.LumiLottoSorteoTheme

class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()
    private val seleccionViewModel: SeleccionViewModel by viewModels()
    private val sorteoLiveViewModel: SorteoLiveViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LumiLottoSorteoTheme {
                val authState by authViewModel.uiState.collectAsState()
                val seleccionState by seleccionViewModel.uiState.collectAsState()
                val sorteoState by sorteoLiveViewModel.uiState.collectAsState()

                // Pantalla activa: "AUTH", "SELECCION", "SORTEO"
                var currentScreen by remember { mutableStateOf("AUTH") }

                LaunchedEffect(authState.currentUser) {
                    if (authState.currentUser != null) {
                        seleccionViewModel.verificarEstadoYJugada()
                        sorteoLiveViewModel.sincronizarSorteoYJugada()
                    }
                }

                val jugadaExistente = seleccionState.jugadaExistente
                LaunchedEffect(jugadaExistente, seleccionState.jugadaGuardadaConExito, authState.currentUser) {
                    if (authState.currentUser == null) {
                        currentScreen = "AUTH"
                    } else if (seleccionState.jugadaGuardadaConExito || (jugadaExistente != null && jugadaExistente.size == 6)) {
                        currentScreen = "SORTEO"
                    } else {
                        currentScreen = "SELECCION"
                    }
                }

                when (currentScreen) {
                    "AUTH" -> {
                        AuthScreen(
                            state = authState,
                            onEmailChanged = { authViewModel.onEmailChanged(it) },
                            onPasswordChanged = { authViewModel.onPasswordChanged(it) },
                            onConfirmPasswordChanged = { authViewModel.onConfirmPasswordChanged(it) },
                            onToggleMode = { authViewModel.toggleMode(it) },
                            onSubmit = { authViewModel.submit() }
                        )
                    }
                    "SELECCION" -> {
                        SeleccionScreen(
                            state = seleccionState,
                            userEmail = authState.currentUser?.email ?: "Usuario",
                            onNumeroClick = { seleccionViewModel.toggleNumero(it) },
                            onGuardarJugada = { seleccionViewModel.guardarJugada() },
                            onVerSorteoClick = { currentScreen = "SORTEO" },
                            onLogoutClick = {
                                authViewModel.logout()
                                currentScreen = "AUTH"
                            }
                        )
                    }
                    "SORTEO" -> {
                        SorteoScreen(
                            uiState = sorteoState,
                            onBackClick = {
                                if (sorteoState.estado.name == "ABIERTO") {
                                    currentScreen = "SELECCION"
                                } else {
                                    authViewModel.logout()
                                    currentScreen = "AUTH"
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
