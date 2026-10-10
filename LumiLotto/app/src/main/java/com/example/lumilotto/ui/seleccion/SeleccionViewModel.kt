package com.example.lumilotto.ui.seleccion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lumilotto.data.LottoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SeleccionUiState(
    val numerosSeleccionados: Set<Int> = emptySet(),
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
    val jugadaExistente: List<Int>? = null,
    val sorteoEstado: String = "ABIERTO",
    val jugadaGuardadaConExito: Boolean = false
) {
    val cantidadSeleccionada: Int
        get() = numerosSeleccionados.size

    val puedeGuardar: Boolean
        get() = cantidadSeleccionada == 6 && sorteoEstado == "ABIERTO" && !isSaving
}

class SeleccionViewModel(
    private val repository: LottoRepository = LottoRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(SeleccionUiState())
    val uiState: StateFlow<SeleccionUiState> = _uiState.asStateFlow()

    init {
        verificarEstadoYJugada()
    }

    fun verificarEstadoYJugada() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                // 1. Obtener jugada existente del usuario
                val jugada = repository.obtenerJugada()

                // 2. Escuchar o obtener estado del sorteo actual
                // Para simplificar la pantalla de selección, leemos una vez el sorteo o escuchamos
                repository.escucharSorteoActual().collect { sorteo ->
                    val estado = sorteo?.estado ?: "ABIERTO"
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            jugadaExistente = jugada,
                            numerosSeleccionados = jugada?.toSet() ?: emptySet(),
                            sorteoEstado = estado
                        )
                    }
                    // Solo necesitamos la primera emisión para cargar el estado inicial
                    return@collect
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Error al cargar datos: ${e.localizedMessage}"
                    )
                }
            }
        }
    }

    fun toggleNumero(numero: Int) {
        val currentState = _uiState.value
        // Si el sorteo ya no está abierto, no permitir cambios
        if (currentState.sorteoEstado != "ABIERTO") {
            _uiState.update { it.copy(errorMessage = "El sorteo ya ha comenzado o finalizado. No se pueden modificar jugadas.") }
            return
        }

        val seleccionActual = currentState.numerosSeleccionados.toMutableSet()
        if (seleccionActual.contains(numero)) {
            seleccionActual.remove(numero)
            _uiState.update { it.copy(numerosSeleccionados = seleccionActual, errorMessage = null) }
        } else {
            if (seleccionActual.size >= 6) {
                _uiState.update { it.copy(errorMessage = "Solo puedes seleccionar exactamente 6 números.") }
            } else {
                seleccionActual.add(numero)
                _uiState.update { it.copy(numerosSeleccionados = seleccionActual, errorMessage = null) }
            }
        }
    }

    fun guardarJugada() {
        val state = _uiState.value
        if (state.numerosSeleccionados.size != 6) {
            _uiState.update { it.copy(errorMessage = "Debe seleccionar exactamente 6 números.") }
            return
        }
        if (state.sorteoEstado != "ABIERTO") {
            _uiState.update { it.copy(errorMessage = "El sorteo ya no está abierto para recibir jugadas.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessage = null) }
            try {
                val numerosList = state.numerosSeleccionados.toList().sorted()
                repository.guardarJugada(numerosList)
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        jugadaExistente = numerosList,
                        jugadaGuardadaConExito = true
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        errorMessage = "Error al guardar la jugada: ${e.localizedMessage}"
                    )
                }
            }
        }
    }
}
