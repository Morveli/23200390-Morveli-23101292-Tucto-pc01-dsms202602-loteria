package com.example.lumilotto.ui.sorteo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lumilotto.data.LottoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel que conecta la pantalla de sorteo (`SorteoScreen`) con Cloud Firestore
 * en tiempo real utilizando `LottoRepository`.
 */
class SorteoLiveViewModel(
    private val repository: LottoRepository = LottoRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(SorteoUiState())
    val uiState: StateFlow<SorteoUiState> = _uiState.asStateFlow()

    init {
        sincronizarSorteoYJugada()
    }

    fun sincronizarSorteoYJugada() {
        viewModelScope.launch {
            // 1. Cargar la jugada guardada del usuario
            val jugada = repository.obtenerJugada() ?: emptyList()
            _uiState.update { it.copy(jugadaUsuario = jugada) }

            // 2. Escuchar cambios en tiempo real del documento del sorteo en Firestore
            repository.escucharSorteoActual().collect { sorteoModel ->
                if (sorteoModel != null) {
                    val estadoEnum = when (sorteoModel.estado.uppercase()) {
                        "EN_CURSO" -> EstadoSorteo.EN_CURSO
                        "FINALIZADO" -> EstadoSorteo.FINALIZADO
                        else -> EstadoSorteo.ABIERTO
                    }
                    _uiState.update {
                        it.copy(
                            nombreSorteo = sorteoModel.nombre,
                            fechaSorteo = sorteoModel.fecha,
                            estado = estadoEnum,
                            bolillasSorteadas = sorteoModel.bolillas,
                            jugadaUsuario = jugada
                        )
                    }
                }
            }
        }
    }
}
