package com.example.lumilotto.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/**
 * Modelo de datos del sorteo almacenado en Cloud Firestore.
 */
data class SorteoFirestoreModel(
    val nombre: String = "LUMI Sorteo #1024",
    val fecha: String = "24 de Octubre, 2024",
    val estado: String = "ABIERTO", // ABIERTO, EN_CURSO, FINALIZADO
    val bolillas: List<Int> = emptyList()
)

/**
 * Repositorio centralizado para operaciones en Cloud Firestore (Jugadas y Sorteo en vivo).
 */
class LottoRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    val currentUserId: String?
        get() = auth.currentUser?.uid

    /**
     * Guarda la jugada de 6 números del usuario en Firestore (colección "jugadas", doc = uid).
     * Validaciones estrictas: exactamente 6 números distintos del 1 al 45.
     */
    suspend fun guardarJugada(numeros: List<Int>) {
        val uid = currentUserId ?: throw IllegalStateException("Usuario no autenticado")
        require(numeros.size == 6) { "Debe seleccionar exactamente 6 números." }
        require(numeros.toSet().size == 6) { "Los números de la jugada deben ser distintos." }
        require(numeros.all { it in 1..45 }) { "Los números deben estar en el rango de 1 a 45." }

        val data = mapOf(
            "uid" to uid,
            "numeros" to numeros.sorted(),
            "timestamp" to FieldValue.serverTimestamp()
        )

        firestore.collection("jugadas").document(uid).set(data).await()
    }

    /**
     * Recupera la jugada guardada del usuario autenticado desde Firestore.
     */
    suspend fun obtenerJugada(): List<Int>? {
        val uid = currentUserId ?: return null
        val doc = firestore.collection("jugadas").document(uid).get().await()
        if (doc.exists()) {
            val numeros = doc.get("numeros") as? List<*>
            return numeros?.mapNotNull { (it as? Number)?.toInt() }
        }
        return null
    }

    /**
     * Escucha en tiempo real el documento del sorteo actual ("sorteos/actual") mediante addSnapshotListener.
     */
    fun escucharSorteoActual(): Flow<SorteoFirestoreModel?> = callbackFlow {
        val listener: ListenerRegistration = firestore.collection("sorteos").document("actual")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(null)
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    val nombre = snapshot.getString("nombre") ?: "LUMI Sorteo #1024"
                    val fecha = snapshot.getString("fecha") ?: "24 de Octubre, 2024"
                    val estado = snapshot.getString("estado") ?: "ABIERTO"
                    val bolillasList = (snapshot.get("bolillas") as? List<*>)?.mapNotNull { (it as? Number)?.toInt() } ?: emptyList()

                    trySend(SorteoFirestoreModel(nombre, fecha, estado, bolillasList))
                } else {
                    // Si el documento aún no ha sido creado en Firebase Console, emitimos un estado por defecto ABIERTO
                    trySend(SorteoFirestoreModel())
                }
            }
        awaitClose { listener.remove() }
    }
}
