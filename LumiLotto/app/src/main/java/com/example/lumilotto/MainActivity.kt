package com.example.lumilotto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.lumilotto.ui.auth.AuthScreen
import com.example.lumilotto.ui.auth.AuthViewModel
import com.example.lumilotto.ui.auth.HomeProvisionalScreen
import com.example.lumilotto.ui.sorteo.LumiLottoSorteoTheme

class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LumiLottoSorteoTheme {
                val state by authViewModel.uiState.collectAsState()

                if (state.currentUser != null) {
                    HomeProvisionalScreen(
                        currentUser = state.currentUser,
                        onLogoutClick = { authViewModel.logout() }
                    )
                } else {
                    AuthScreen(
                        state = state,
                        onEmailChanged = { authViewModel.onEmailChanged(it) },
                        onPasswordChanged = { authViewModel.onPasswordChanged(it) },
                        onConfirmPasswordChanged = { authViewModel.onConfirmPasswordChanged(it) },
                        onToggleMode = { authViewModel.toggleMode(it) },
                        onSubmit = { authViewModel.submit() }
                    )
                }
            }
        }
    }
}
