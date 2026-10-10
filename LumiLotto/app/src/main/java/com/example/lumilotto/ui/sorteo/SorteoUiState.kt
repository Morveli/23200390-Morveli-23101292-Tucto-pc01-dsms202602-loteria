package com.example.lumilotto.ui.sorteo

/**
 * Estado general del sorteo en vivo.
 */
enum class EstadoSorteo {
    ABIERTO,
    EN_CURSO,
    FINALIZADO
}

/**
 * Modelo inmutable de estado UI para la pantalla de seguimiento de sorteo.
 *
 * @property nombreSorteo Nombre descriptivo del sorteo (ej. "LUMI Sorteo #1024").
 * @property fechaSorteo Fecha u horario del sorteo.
 * @property estado Estado del proceso ([EstadoSorteo.ABIERTO], [EstadoSorteo.EN_CURSO], [EstadoSorteo.FINALIZADO]).
 * @property bolillasSorteadas Bolillas extraídas hasta el momento, en estricto orden de salida (0 a 6 números, de 1 a 45).
 * @property jugadaUsuario Seis números seleccionados por el usuario para este sorteo (de 1 a 45).
 */
data class SorteoUiState(
    val nombreSorteo: String = "LUMI Sorteo #1024",
    val fechaSorteo: String = "24 de Octubre, 2024",
    val estado: EstadoSorteo = EstadoSorteo.ABIERTO,
    val bolillasSorteadas: List<Int> = emptyList(),
    val jugadaUsuario: List<Int> = emptyList()
) {
    /**
     * Conjunto de números acertados entre la jugada del usuario y las bolillas sorteadas.
     */
    val aciertos: Set<Int>
        get() = SorteoCalculoUtils.obtenerAciertos(jugadaUsuario, bolillasSorteadas)

    /**
     * Cantidad total de aciertos obtenidos.
     */
    val cantidadAciertos: Int
        get() = aciertos.size

    /**
     * Cantidad actual de bolillas publicadas (máximo 6).
     */
    val bolillasPublicadasCount: Int
        get() = bolillasSorteadas.size.coerceAtMost(6)

    /**
     * Indica si el resultado es un pleno ganador (6 aciertos y 6 bolillas publicadas con sorteo finalizado).
     */
    val esGanador: Boolean
        get() = estado == EstadoSorteo.FINALIZADO && bolillasPublicadasCount == 6 && cantidadAciertos == 6
}

/**
 * Clase de utilidad pura para realizar cálculos del sorteo.
 * Aislada del UI para facilitar la integración futura con Firestore o ViewModel.
 */
object SorteoCalculoUtils {

    /**
     * Calcula la intersección entre los números del usuario y los sorteados.
     * La comparación es por coincidencia de valor (1 a 45), sin importar el orden.
     */
    fun obtenerAciertos(jugadaUsuario: List<Int>, bolillasSorteadas: List<Int>): Set<Int> {
        val jugadaSet = jugadaUsuario.toSet()
        val sorteadasSet = bolillasSorteadas.toSet()
        return jugadaSet.intersect(sorteadasSet)
    }

    /**
     * Evalúa si un número específico de la jugada del usuario es un acierto.
     */
    fun esNumeroAcertado(numero: Int, bolillasSorteadas: List<Int>): Boolean {
        return bolillasSorteadas.contains(numero)
    }
}
