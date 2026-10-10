package com.example.lumilotto.ui.auth

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Modos disponibles para la autenticación.
 */
sealed interface AuthMode {
    data object Login : AuthMode
    data object Register : AuthMode
}

/**
 * Estado de la UI para el flujo de autenticación.
 */
data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val mode: AuthMode = AuthMode.Login,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val currentUser: FirebaseUser? = null
) {
    val isAuthenticated: Boolean
        get() = currentUser != null
}

/**
 * ViewModel encargado de manejar la lógica de negocio, validaciones y estados de autenticación.
 */
class AuthViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState(currentUser = repository.currentUser))
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun onEmailChanged(email: String) {
        _uiState.update { it.copy(email = email, errorMessage = null) }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password, errorMessage = null) }
    }

    fun onConfirmPasswordChanged(confirmPassword: String) {
        _uiState.update { it.copy(confirmPassword = confirmPassword, errorMessage = null) }
    }

    fun toggleMode(mode: AuthMode) {
        _uiState.update {
            it.copy(
                mode = mode,
                password = "",
                confirmPassword = "",
                errorMessage = null
            )
        }
    }

    fun submit() {
        val state = _uiState.value
        val email = state.email.trim()
        val password = state.password
        val confirmPassword = state.confirmPassword

        // Validaciones en español
        if (email.isEmpty()) {
            _uiState.update { it.copy(errorMessage = "Por favor ingrese su correo electrónico.") }
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _uiState.update { it.copy(errorMessage = "El formato del correo electrónico no es válido.") }
            return
        }
        if (password.isEmpty()) {
            _uiState.update { it.copy(errorMessage = "Por favor ingrese su contraseña.") }
            return
        }
        if (password.length < 6) {
            _uiState.update { it.copy(errorMessage = "La contraseña debe tener al menos 6 caracteres.") }
            return
        }

        if (state.mode is AuthMode.Register) {
            if (confirmPassword.isEmpty()) {
                _uiState.update { it.copy(errorMessage = "Por favor confirme su contraseña.") }
                return
            }
            if (password != confirmPassword) {
                _uiState.update { it.copy(errorMessage = "Las contraseñas no coinciden.") }
                return
            }
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val user = if (state.mode is AuthMode.Login) {
                    repository.login(email, password)
                } else {
                    repository.register(email, password)
                }
                _uiState.update { it.copy(isLoading = false, currentUser = user) }
            } catch (e: Exception) {
                val translatedError = translateFirebaseError(e.message)
                _uiState.update { it.copy(isLoading = false, errorMessage = translatedError) }
            }
        }
    }

    fun logout() {
        repository.logout()
        _uiState.update {
            AuthUiState(
                currentUser = null,
                email = "",
                password = "",
                confirmPassword = "",
                mode = AuthMode.Login
            )
        }
    }

    private fun translateFirebaseError(message: String?): String {
        val msg = message?.lowercase() ?: ""
        return when {
            msg.contains("email-already-in-use") || msg.contains("address is already in use") ->
                "Este correo ya está registrado. Inicie sesión o utilice otro correo."
            msg.contains("invalid-credential") || msg.contains("wrong-password") || msg.contains("user-not-found") || msg.contains("invalid-email") ->
                "Correo o contraseña incorrectos. Verifique sus datos."
            msg.contains("weak-password") ->
                "La contraseña es demasiado débil. Elija una más segura (mínimo 6 caracteres)."
            msg.contains("network-request-failed") ->
                "Error de conexión. Verifique su acceso a internet."
            else ->
                message ?: "Ocurrió un error inesperado. Intente nuevamente."
        }
    }
}
