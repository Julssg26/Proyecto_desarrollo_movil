package com.julm.mitecmi.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.julm.mitecmi.ui.theme.TecmiBackground
import com.julm.mitecmi.ui.theme.TecmiDarkGreen
import com.julm.mitecmi.ui.theme.TecmiGreen
import com.julm.mitecmi.ui.theme.White
import com.julm.mitecmi.viewmodel.AuthUiState

@Composable
fun LoginScreen(
    authState: AuthUiState,
    onLogin: (String, String) -> Unit,
    onRegister: (String, String, String, String) -> Unit,
    onClearError: () -> Unit
) {

    var showRegister by remember { mutableStateOf(false) }

    if (showRegister) {
        RegisterContent(
            authState = authState,
            onRegister = onRegister,
            onBackToLogin = {
                onClearError()
                showRegister = false
            },
            onClearError = onClearError
        )
    } else {
        LoginContent(
            authState = authState,
            onLogin = onLogin,
            onGoToRegister = {
                onClearError()
                showRegister = true
            },
            onClearError = onClearError
        )
    }
}

@Composable
private fun LoginContent(
    authState: AuthUiState,
    onLogin: (String, String) -> Unit,
    onGoToRegister: () -> Unit,
    onClearError: () -> Unit
) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    AuthContainer(
        subtitle = "Accede con tu correo institucional"
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 3.dp
            )
        ) {
            Column(
                modifier = Modifier.padding(22.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "Iniciar sesion",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TecmiDarkGreen
                )

                AuthTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        if (authState.errorMessage.isNotBlank()) {
                            onClearError()
                        }
                    },
                    label = "Correo institucional",
                    keyboardType = KeyboardType.Email
                )

                AuthTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        if (authState.errorMessage.isNotBlank()) {
                            onClearError()
                        }
                    },
                    label = "Contrasena",
                    keyboardType = KeyboardType.Password,
                    isPassword = true
                )

                AuthError(
                    message = authState.errorMessage
                )

                PrimaryAuthButton(
                    text = "Entrar",
                    isLoading = authState.isLoading,
                    onClick = {
                        onLogin(email, password)
                    }
                )

                OutlinedButton(
                    onClick = onGoToRegister,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !authState.isLoading
                ) {
                    Text(
                        text = "Crear cuenta institucional",
                        color = TecmiGreen
                    )
                }
            }
        }
    }
}

@Composable
private fun RegisterContent(
    authState: AuthUiState,
    onRegister: (String, String, String, String) -> Unit,
    onBackToLogin: () -> Unit,
    onClearError: () -> Unit
) {

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    AuthContainer(
        subtitle = "Crea tu cuenta con correo @tecmilenio.mx"
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 3.dp
            )
        ) {
            Column(
                modifier = Modifier.padding(22.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "Crear cuenta",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TecmiDarkGreen
                )

                AuthTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        if (authState.errorMessage.isNotBlank()) {
                            onClearError()
                        }
                    },
                    label = "Nombre completo"
                )

                AuthTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        if (authState.errorMessage.isNotBlank()) {
                            onClearError()
                        }
                    },
                    label = "Correo institucional",
                    keyboardType = KeyboardType.Email
                )

                AuthTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        if (authState.errorMessage.isNotBlank()) {
                            onClearError()
                        }
                    },
                    label = "Contrasena",
                    keyboardType = KeyboardType.Password,
                    isPassword = true
                )

                AuthTextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        if (authState.errorMessage.isNotBlank()) {
                            onClearError()
                        }
                    },
                    label = "Confirmar contrasena",
                    keyboardType = KeyboardType.Password,
                    isPassword = true
                )

                Text(
                    text = "Solo se permiten cuentas con dominio @tecmilenio.mx.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                AuthError(
                    message = authState.errorMessage
                )

                PrimaryAuthButton(
                    text = "Crear cuenta",
                    isLoading = authState.isLoading,
                    onClick = {
                        onRegister(
                            name,
                            email,
                            password,
                            confirmPassword
                        )
                    }
                )

                TextButton(
                    onClick = onBackToLogin,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !authState.isLoading
                ) {
                    Text(
                        text = "Volver a iniciar sesion",
                        color = TecmiDarkGreen
                    )
                }
            }
        }
    }
}

@Composable
private fun AuthContainer(
    subtitle: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TecmiBackground)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(
            modifier = Modifier.height(36.dp)
        )

        Text(
            text = "Mi Tecmi",
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold,
            color = TecmiDarkGreen
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = subtitle,
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(
            modifier = Modifier.height(28.dp)
        )

        content()

        Spacer(
            modifier = Modifier.height(36.dp)
        )
    }
}

@Composable
private fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = {
            Text(label)
        },
        singleLine = true,
        visualTransformation = if (isPassword) {
            PasswordVisualTransformation()
        } else {
            androidx.compose.ui.text.input.VisualTransformation.None
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        )
    )
}

@Composable
private fun AuthError(
    message: String
) {
    if (message.isNotBlank()) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            fontSize = 13.sp
        )
    }
}

@Composable
private fun PrimaryAuthButton(
    text: String,
    isLoading: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        enabled = !isLoading,
        colors = ButtonDefaults.buttonColors(
            containerColor = TecmiDarkGreen
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = White,
                strokeWidth = 2.dp
            )
        } else {
            Text(text)
        }
    }
}
