package com.example.lumilotto.ui.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

/**
 * Repositorio encargado de gestionar la comunicación con Firebase Authentication.
 */
class AuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    /**
     * Retorna el usuario actualmente autenticado, o null si no hay sesión activa.
     */
    val currentUser: FirebaseUser?
        get() = auth.currentUser

    /**
     * Inicia sesión con correo y contraseña.
     */
    suspend fun login(email: String, pass: String): FirebaseUser? {
        val result = auth.signInWithEmailAndPassword(email, pass).await()
        return result.user
    }

    /**
     * Registra un nuevo usuario con correo y contraseña.
     */
    suspend fun register(email: String, pass: String): FirebaseUser? {
        val result = auth.createUserWithEmailAndPassword(email, pass).await()
        return result.user
    }

    /**
     * Cierra la sesión actual en Firebase Auth.
     */
    fun logout() {
        auth.signOut()
    }
}
