package com.example.lumilotto.ui.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lumilotto.ui.sorteo.LumiColors
import com.example.lumilotto.ui.sorteo.LumiLottoSorteoTheme

/**
 * Pantalla de autenticación (Inicio de sesión y Registro) con diseño premium LUMI Lotto.
 */
@Composable
fun AuthScreen(
    state: AuthUiState,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onConfirmPasswordChanged: (String) -> Unit,
    onToggleMode: (AuthMode) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isLogin = state.mode is AuthMode.Login

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(LumiColors.BackgroundGradient)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // LOGO / ENCABEZADO
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(LumiColors.SurfaceDark)
                    .border(2.dp, LumiColors.SoftGold, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = LumiColors.SoftGold,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "LUMI",
                    color = LumiColors.SoftGold,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.5.sp
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "LOTTO",
                    color = LumiColors.TextWhite,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = if (isLogin) "Inicia sesión para continuar" else "Crea tu cuenta y participa",
                color = LumiColors.TextLavender,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // TARJETA DE FORMULARIO
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = LumiColors.SurfaceDark),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = SolidColor(LumiColors.CardBorder)
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // SELECTOR DE MODO (Píldoras)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(LumiColors.SurfaceVariantDark)
                            .padding(4.dp)
                    ) {
                        TabButton(
                            text = "Iniciar Sesión",
                            selected = isLogin,
                            onClick = { onToggleMode(AuthMode.Login) },
                            modifier = Modifier.weight(1f)
                        )
                        TabButton(
                            text = "Registrarse",
                            selected = !isLogin,
                            onClick = { onToggleMode(AuthMode.Register) },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // CAMPO CORREO
                    OutlinedTextField(
                        value = state.email,
                        onValueChange = onEmailChanged,
                        label = { Text("Correo electrónico") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Email, contentDescription = null, tint = LumiColors.AccentViolet)
                        },
                        singleLine = true,
                        enabled = !state.isLoading,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        colors = lottOutlinedTextFieldColors(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )

                    // CAMPO CONTRASEÑA
                    OutlinedTextField(
                        value = state.password,
                        onValueChange = onPasswordChanged,
                        label = { Text("Contraseña (mín. 6 caracteres)") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = LumiColors.AccentViolet)
                        },
                        singleLine = true,
                        enabled = !state.isLoading,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = if (isLogin) ImeAction.Done else ImeAction.Next
                        ),
                        colors = lottOutlinedTextFieldColors(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )

                    // CAMPO CONFIRMAR CONTRASEÑA (Solo en Registro)
                    if (!isLogin) {
                        OutlinedTextField(
                            value = state.confirmPassword,
                            onValueChange = onConfirmPasswordChanged,
                            label = { Text("Confirmar contraseña") },
                            leadingIcon = {
                                Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = LumiColors.AccentViolet)
                            },
                            singleLine = true,
                            enabled = !state.isLoading,
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Done
                            ),
                            colors = lottOutlinedTextFieldColors(),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp)
                        )
                    }

                    // MENSAJE DE ERROR
                    if (state.errorMessage != null) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFE53935).copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, androidx.compose.ui.graphics.Color(0xFFE53935).copy(alpha = 0.5f))
                        ) {
                            Text(
                                text = state.errorMessage,
                                color = androidx.compose.ui.graphics.Color(0xFFFF6B6B),
                                fontSize = 13.sp,
                                modifier = Modifier.padding(12.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // BOTÓN DE ACCIÓN PRINCIPAL
                    Button(
                        onClick = onSubmit,
                        enabled = !state.isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LumiColors.AccentViolet,
                            disabledContainerColor = LumiColors.AccentViolet.copy(alpha = 0.5f),
                            contentColor = LumiColors.TextWhite
                        )
                    ) {
                        if (state.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = LumiColors.TextWhite,
                                strokeWidth = 2.5.dp
                            )
                        } else {
                            Text(
                                text = if (isLogin) "Iniciar Sesión" else "Registrar Cuenta",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // BOTÓN CAMBIO DE MODO ALTERNATIVO
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = if (isLogin) "¿No tienes una cuenta?" else "¿Ya tienes una cuenta?",
                    color = LumiColors.TextLavender,
                    fontSize = 13.sp
                )
                TextButton(
                    onClick = {
                        onToggleMode(if (isLogin) AuthMode.Register else AuthMode.Login)
                    }
                ) {
                    Text(
                        text = if (isLogin) "Regístrate aquí" else "Inicia sesión",
                        color = LumiColors.SoftGold,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun TabButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp)),
        color = if (selected) LumiColors.AccentViolet else Color.Transparent,
        onClick = onClick
    ) {
        Box(
            modifier = Modifier.padding(vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = if (selected) LumiColors.TextWhite else LumiColors.TextLavender,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun lottOutlinedTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = LumiColors.AccentViolet,
    unfocusedBorderColor = LumiColors.CardBorder,
    focusedLabelColor = LumiColors.AccentViolet,
    unfocusedLabelColor = LumiColors.TextLavender,
    cursorColor = LumiColors.AccentViolet,
    focusedTextColor = LumiColors.TextWhite,
    unfocusedTextColor = LumiColors.TextWhite,
    focusedContainerColor = LumiColors.SurfaceVariantDark,
    unfocusedContainerColor = LumiColors.SurfaceVariantDark
)

@Preview(name = "AuthScreen - Login", showBackground = true)
@Composable
fun AuthScreenLoginPreview() {
    LumiLottoSorteoTheme {
        AuthScreen(
            state = AuthUiState(
                email = "usuario@lumilotto.com",
                mode = AuthMode.Login
            ),
            onEmailChanged = {},
            onPasswordChanged = {},
            onConfirmPasswordChanged = {},
            onToggleMode = {},
            onSubmit = {}
        )
    }
}

@Preview(name = "AuthScreen - Register", showBackground = true)
@Composable
fun AuthScreenRegisterPreview() {
    LumiLottoSorteoTheme {
        AuthScreen(
            state = AuthUiState(
                email = "nuevo@lumilotto.com",
                mode = AuthMode.Register,
                errorMessage = "Las contraseñas no coinciden."
            ),
            onEmailChanged = {},
            onPasswordChanged = {},
            onConfirmPasswordChanged = {},
            onToggleMode = {},
            onSubmit = {}
        )
    }
}
