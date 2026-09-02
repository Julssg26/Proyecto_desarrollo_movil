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
import androidx.compose.ui.text.input.VisualTransformation
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
    onResetPassword: (String) -> Unit,
    onResendVerification: () -> Unit,
    onCheckEmailVerification: () -> Unit,
    onClearError: () -> Unit
) {

    var showRegister by remember { mutableStateOf(false) }

    if (showRegister) {
        RegisterContent(
            authState = authState,
            onRegister = onRegister,
            onResendVerification = onResendVerification,
            onCheckEmailVerification = onCheckEmailVerification,
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
            onResetPassword = onResetPassword,
            onResendVerification = onResendVerification,
            onCheckEmailVerification = onCheckEmailVerification,
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
    onResetPassword: (String) -> Unit,
    onResendVerification: () -> Unit,
    onCheckEmailVerification: () -> Unit,
    onGoToRegister: () -> Unit,
    onClearError: () -> Unit
) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    AuthContainer(
        subtitle = "Accede con tu correo autorizado"
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
                    text = "Iniciar sesión",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TecmiDarkGreen
                )

                if (authState.isEmailVerificationPending) {
                    EmailVerificationNotice(
                        email = authState.userEmail,
                        isLoading = authState.isLoading,
                        onResendVerification = onResendVerification,
                        onCheckEmailVerification = onCheckEmailVerification
                    )
                }

                AuthTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        if (authState.hasFeedback()) {
                            onClearError()
                        }
                    },
                    label = "Correo autorizado",
                    keyboardType = KeyboardType.Email
                )

                AuthTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        if (authState.hasFeedback()) {
                            onClearError()
                        }
                    },
                    label = "Contraseña",
                    keyboardType = KeyboardType.Password,
                    isPassword = true
                )

                AuthFeedback(
                    errorMessage = authState.errorMessage,
                    successMessage = authState.successMessage
                )

                PrimaryAuthButton(
                    text = "Entrar",
                    isLoading = authState.isLoading,
                    enabled = email.isNotBlank() && password.isNotBlank(),
                    onClick = {
                        onLogin(email, password)
                    }
                )

                TextButton(
                    onClick = {
                        onResetPassword(email)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !authState.isLoading
                ) {
                    Text(
                        text = "Olvidé mi contraseña",
                        color = TecmiDarkGreen
                    )
                }

                OutlinedButton(
                    onClick = onGoToRegister,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !authState.isLoading
                ) {
                    Text(
                        text = "Crear cuenta",
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
    onResendVerification: () -> Unit,
    onCheckEmailVerification: () -> Unit,
    onBackToLogin: () -> Unit,
    onClearError: () -> Unit
) {

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    AuthContainer(
        subtitle = "Crea tu cuenta con correo autorizado"
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

                if (authState.isEmailVerificationPending) {
                    EmailVerificationNotice(
                        email = authState.userEmail,
                        isLoading = authState.isLoading,
                        onResendVerification = onResendVerification,
                        onCheckEmailVerification = onCheckEmailVerification
                    )
                }

                AuthTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        if (authState.hasFeedback()) {
                            onClearError()
                        }
                    },
                    label = "Nombre completo"
                )

                AuthTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        if (authState.hasFeedback()) {
                            onClearError()
                        }
                    },
                    label = "Correo autorizado",
                    keyboardType = KeyboardType.Email
                )

                AuthTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        if (authState.hasFeedback()) {
                            onClearError()
                        }
                    },
                    label = "Contraseña",
                    keyboardType = KeyboardType.Password,
                    isPassword = true
                )

                AuthTextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        if (authState.hasFeedback()) {
                            onClearError()
                        }
                    },
                    label = "Confirmar contraseña",
                    keyboardType = KeyboardType.Password,
                    isPassword = true
                )

                Text(
                    text = "Se permiten cuentas @tecmilenio.mx o @lobelisque.space.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                AuthFeedback(
                    errorMessage = authState.errorMessage,
                    successMessage = authState.successMessage
                )

                PrimaryAuthButton(
                    text = "Crear cuenta",
                    isLoading = authState.isLoading,
                    enabled = !authState.isEmailVerificationPending &&
                        name.isNotBlank() &&
                        email.isNotBlank() &&
                        password.isNotBlank() &&
                        confirmPassword.isNotBlank(),
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
                        text = "Volver a iniciar sesión",
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
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = {
            Text(label)
        },
        singleLine = true,
        visualTransformation = if (isPassword && !passwordVisible) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        trailingIcon = if (isPassword) {
            {
                TextButton(
                    onClick = {
                        passwordVisible = !passwordVisible
                    }
                ) {
                    Text(
                        text = if (passwordVisible) {
                            "Ocultar"
                        } else {
                            "Mostrar"
                        },
                        fontSize = 12.sp
                    )
                }
            }
        } else {
            null
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        )
    )
}

@Composable
private fun EmailVerificationNotice(
    email: String,
    isLoading: Boolean,
    onResendVerification: () -> Unit,
    onCheckEmailVerification: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Verifica tu correo para continuar.",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = TecmiDarkGreen
        )

        Text(
            text = "Enviamos el enlace a ${email.ifBlank { "tu correo autorizado" }}. Revisa spam o correo no deseado.",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        OutlinedButton(
            onClick = onResendVerification,
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            Text("Reenviar verificación")
        }

        TextButton(
            onClick = onCheckEmailVerification,
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            Text(
                text = "Ya verifiqué mi correo",
                color = TecmiDarkGreen
            )
        }
    }
}

@Composable
private fun AuthFeedback(
    errorMessage: String,
    successMessage: String
) {
    if (errorMessage.isNotBlank()) {
        Text(
            text = errorMessage,
            color = MaterialTheme.colorScheme.error,
            fontSize = 13.sp
        )
    } else if (successMessage.isNotBlank()) {
        Text(
            text = successMessage,
            color = TecmiDarkGreen,
            fontSize = 13.sp
        )
    }
}

private fun AuthUiState.hasFeedback(): Boolean {
    return errorMessage.isNotBlank() || successMessage.isNotBlank()
}

@Composable
private fun PrimaryAuthButton(
    text: String,
    isLoading: Boolean,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        enabled = enabled && !isLoading,
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
