package com.julm.mitecmi.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AuthUiState(
    val isAuthenticated: Boolean = false,
    val isLoading: Boolean = false,
    val userName: String = "",
    val userEmail: String = "",
    val errorMessage: String = "",
    val successMessage: String = "",
    val isEmailVerificationPending: Boolean = false,
    val lastVerificationEmailRequestAtMillis: Long? = null,
    val passwordResetEmail: String = "",
    val isPasswordResetEmailSent: Boolean = false,
    val isPasswordResetCompleted: Boolean = false
)

class AuthViewModel(
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
) : ViewModel() {

    private companion object {
        const val logTag = "AuthViewModel"
    }

    private val authStateListener =
        FirebaseAuth.AuthStateListener { auth ->
            updateUser(auth.currentUser)
        }

    private val _uiState = MutableStateFlow(
        AuthUiState()
    )

    val uiState: StateFlow<AuthUiState> =
        _uiState.asStateFlow()

    init {
        firebaseAuth.setLanguageCode("es")
        updateUser(firebaseAuth.currentUser)
        firebaseAuth.addAuthStateListener(authStateListener)
    }

    fun login(
        email: String,
        password: String
    ) {
        if (!validateLogin(email, password)) {
            return
        }

        _uiState.value = _uiState.value.copy(
            isLoading = true,
            errorMessage = "",
            successMessage = ""
        )

        firebaseAuth
            .signInWithEmailAndPassword(
                email.trim(),
                password
            )
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    refreshSignedInUser(
                        showUnverifiedMessage = true
                    )
                } else {
                    showError(
                        loginErrorMessage(task.exception)
                    )
                }
            }
    }

    fun register(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ) {
        if (!validateRegistration(
                name = name,
                email = email,
                password = password,
                confirmPassword = confirmPassword
            )
        ) {
            return
        }

        _uiState.value = _uiState.value.copy(
            isLoading = true,
            errorMessage = "",
            successMessage = ""
        )

        firebaseAuth
            .createUserWithEmailAndPassword(
                email.trim(),
                password
            )
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    updateUserProfile(
                        name.trim()
                    )
                } else {
                    showError(
                        registerErrorMessage(task.exception)
                    )
                }
            }
    }

    fun resendVerificationEmail() {
        val user = firebaseAuth.currentUser

        if (user == null) {
            showError("Inicia sesión para reenviar la verificación.")
            return
        }

        if (user.isEmailVerified) {
            updateUser(user)
            return
        }

        sendVerificationEmail(
            user = user,
            successMessage = "Te reenviamos el correo de verificación."
        )
    }

    fun checkEmailVerification() {
        val user = firebaseAuth.currentUser

        if (user == null) {
            showError("Inicia sesión para confirmar la verificación.")
            return
        }

        _uiState.value = _uiState.value.copy(
            isLoading = true,
            errorMessage = "",
            successMessage = ""
        )

        user.reload()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val refreshedUser = firebaseAuth.currentUser

                    when {
                        refreshedUser == null -> {
                            showError("No se pudo cargar tu sesión.")
                        }

                        refreshedUser.isEmailVerified -> {
                            updateUser(refreshedUser)
                        }

                        else -> {
                            showError(
                                "Tu correo todavía no ha sido verificado. Abre el enlace que " +
                                    "enviamos a tu correo e inténtalo nuevamente."
                            )
                        }
                    }
                } else {
                    showError(
                        "No se pudo revisar la verificación. Inténtalo de nuevo."
                    )
                }
            }
    }

    fun verifyEmailCode(
        code: String
    ) {
        val actionCode = extractActionCode(code)

        if (actionCode.isBlank()) {
            showError("Ingresa el código o pega el enlace de verificación que recibiste por correo.")
            return
        }

        _uiState.value = _uiState.value.copy(
            isLoading = true,
            errorMessage = "",
            successMessage = ""
        )

        firebaseAuth.applyActionCode(actionCode)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    refreshSignedInUser(
                        showUnverifiedMessage = false
                    )
                } else {
                    showError(
                        actionCodeErrorMessage(task.exception)
                    )
                }
            }
    }

    fun resetPassword(
        email: String
    ) {
        val normalizedEmail = email.trim()

        if (email.isBlank()) {
            showError("Ingresa tu correo para enviarte la recuperación.")
            return
        }

        if (!isInstitutionalEmail(email)) {
            showError("Usa un correo @tecmilenio.mx o @lobelisque.space.")
            return
        }

        _uiState.value = _uiState.value.copy(
            isLoading = true,
            errorMessage = "",
            successMessage = "",
            passwordResetEmail = normalizedEmail,
            isPasswordResetEmailSent = false,
            isPasswordResetCompleted = false
        )

        firebaseAuth
            .sendPasswordResetEmail(normalizedEmail)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.i(
                        logTag,
                        "Firebase aceptó el envío del correo de recuperación."
                    )
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "",
                        successMessage = "Correo enviado.",
                        passwordResetEmail = normalizedEmail,
                        isPasswordResetEmailSent = true,
                        isPasswordResetCompleted = false
                    )
                } else {
                    val exception = task.exception

                    logPasswordResetEmailError(exception)
                    showError(
                        resetPasswordErrorMessage(exception)
                    )
                }
            }
    }

    fun confirmPasswordReset(
        code: String,
        newPassword: String,
        confirmPassword: String
    ) {
        val actionCode = extractActionCode(code)

        if (actionCode.isBlank()) {
            showError("Ingresa el código o pega el enlace de recuperación que recibiste por correo.")
            return
        }

        if (newPassword.isBlank() || confirmPassword.isBlank()) {
            showError("Ingresa y confirma tu nueva contraseña.")
            return
        }

        if (newPassword.length < 6) {
            showError("La contraseña debe tener al menos 6 caracteres.")
            return
        }

        if (newPassword != confirmPassword) {
            showError("Las contraseñas no coinciden.")
            return
        }

        _uiState.value = _uiState.value.copy(
            isLoading = true,
            errorMessage = "",
            successMessage = "",
            isPasswordResetCompleted = false
        )

        firebaseAuth.confirmPasswordReset(
            actionCode,
            newPassword
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.i(
                    logTag,
                    "Firebase confirmó el cambio de contraseña."
                )
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "",
                    successMessage = "Tu contraseña se actualizó correctamente. Ya puedes iniciar sesión.",
                    isPasswordResetEmailSent = false,
                    isPasswordResetCompleted = true
                )
            } else {
                val exception = task.exception

                logPasswordResetConfirmationError(exception)
                showError(
                    actionCodeErrorMessage(exception)
                )
            }
        }
    }

    fun signOut() {
        firebaseAuth.signOut()
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(
            errorMessage = "",
            successMessage = ""
        )
    }

    fun clearPasswordResetState() {
        _uiState.value = _uiState.value.copy(
            errorMessage = "",
            successMessage = "",
            passwordResetEmail = "",
            isPasswordResetEmailSent = false,
            isPasswordResetCompleted = false
        )
    }

    private fun validateLogin(
        email: String,
        password: String
    ): Boolean {
        if (email.isBlank() || password.isBlank()) {
            showError("Ingresa correo y contraseña.")
            return false
        }

        if (!isInstitutionalEmail(email)) {
            showError("Usa un correo @tecmilenio.mx o @lobelisque.space.")
            return false
        }

        if (password.length < 6) {
            showError("La contraseña debe tener al menos 6 caracteres.")
            return false
        }

        return true
    }

    private fun validateRegistration(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        if (name.isBlank()) {
            showError("Ingresa tu nombre.")
            return false
        }

        if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            showError("Completa todos los campos.")
            return false
        }

        if (!isInstitutionalEmail(email)) {
            showError("Solo se permiten correos @tecmilenio.mx o @lobelisque.space.")
            return false
        }

        if (password.length < 6) {
            showError("La contraseña debe tener al menos 6 caracteres.")
            return false
        }

        if (password != confirmPassword) {
            showError("Las contraseñas no coinciden.")
            return false
        }

        return true
    }

    private fun isInstitutionalEmail(
        email: String
    ): Boolean {
        return email
            .trim()
            .lowercase()
            .let { normalizedEmail ->
                normalizedEmail.endsWith("@tecmilenio.mx") ||
                    normalizedEmail.endsWith("@lobelisque.space")
            }
    }

    private fun updateUserProfile(
        name: String
    ) {
        val user = firebaseAuth.currentUser

        if (user == null) {
            showError("La cuenta fue creada, pero no se pudo cargar el usuario.")
            return
        }

        val profileUpdates =
            UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()

        user.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    sendVerificationEmail(
                        user = user,
                        successMessage = "Te enviamos un correo de verificación para activar tu cuenta."
                    )
                } else {
                    showError(
                        task.exception?.localizedMessage
                            ?: "La cuenta fue creada, pero no se pudo guardar el nombre."
                    )
                }
            }
    }

    private fun extractActionCode(
        input: String
    ): String {
        val trimmedInput = input.trim()

        if (!trimmedInput.startsWith("http://") && !trimmedInput.startsWith("https://")) {
            return trimmedInput
        }

        return runCatching {
            Uri.parse(trimmedInput).getQueryParameter("oobCode").orEmpty()
        }.getOrDefault("")
    }

    private fun sendVerificationEmail(
        user: FirebaseUser,
        successMessage: String
    ) {
        val requestedAtMillis = System.currentTimeMillis()

        _uiState.value = _uiState.value.copy(
            isLoading = true,
            errorMessage = "",
            successMessage = "",
            lastVerificationEmailRequestAtMillis = requestedAtMillis
        )

        user.sendEmailVerification()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    updateUser(firebaseAuth.currentUser)
                    _uiState.value = _uiState.value.copy(
                        lastVerificationEmailRequestAtMillis = requestedAtMillis
                    )
                    showSuccess(successMessage)
                } else {
                    val exception = task.exception

                    logVerificationEmailError(exception)
                    showError(
                        verificationEmailErrorMessage(exception)
                    )
                }
            }
    }

    private fun refreshSignedInUser(
        showUnverifiedMessage: Boolean
    ) {
        val user = firebaseAuth.currentUser

        if (user == null) {
            showError("No se pudo cargar tu sesión.")
            return
        }

        user.reload()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val currentUser = firebaseAuth.currentUser

                    updateUser(currentUser)

                    if (showUnverifiedMessage && currentUser?.isEmailVerified == false) {
                        showError(
                            "Tu cuenta existe, pero falta verificar tu correo institucional."
                        )
                    }
                } else {
                    showError(
                        "No se pudo actualizar tu sesión. Inténtalo de nuevo."
                    )
                }
            }
    }

    private fun updateUser(
        user: FirebaseUser?
    ) {
        val previousState = _uiState.value
        val preserveVerificationRequest = user != null &&
            !user.isEmailVerified &&
            user.email == previousState.userEmail

        _uiState.value = AuthUiState(
            isAuthenticated = user != null && user.isEmailVerified,
            isLoading = false,
            userName = user?.displayName.orEmpty(),
            userEmail = user?.email.orEmpty(),
            errorMessage = "",
            isEmailVerificationPending = user != null && !user.isEmailVerified,
            lastVerificationEmailRequestAtMillis = if (preserveVerificationRequest) {
                previousState.lastVerificationEmailRequestAtMillis
            } else {
                null
            }
        )
    }

    private fun loginErrorMessage(
        exception: Exception?
    ): String {
        return when (exception) {
            is FirebaseAuthInvalidCredentialsException,
            is FirebaseAuthInvalidUserException -> {
                "Correo o contraseña incorrectos."
            }

            is FirebaseNetworkException -> {
                "Revisa tu conexión e inténtalo de nuevo."
            }

            is FirebaseTooManyRequestsException -> {
                "Demasiados intentos. Espera un momento e inténtalo de nuevo."
            }

            else -> {
                "No se pudo iniciar sesión. Inténtalo de nuevo."
            }
        }
    }

    private fun registerErrorMessage(
        exception: Exception?
    ): String {
        return when (exception) {
            is FirebaseAuthUserCollisionException -> {
                "Ya existe una cuenta con este correo."
            }

            is FirebaseAuthWeakPasswordException -> {
                "La contraseña debe tener al menos 6 caracteres."
            }

            is FirebaseNetworkException -> {
                "Revisa tu conexión e inténtalo de nuevo."
            }

            else -> {
                "No se pudo crear la cuenta. Inténtalo de nuevo."
            }
        }
    }

    private fun resetPasswordErrorMessage(
        exception: Exception?
    ): String {
        if (exception is FirebaseTooManyRequestsException) {
            return "Has realizado demasiadas solicitudes. Por seguridad, " +
                "espera unos minutos antes de volver a intentarlo."
        }

        val message = exception?.localizedMessage
            ?: exception?.message
            ?: "Firebase no proporcionó un mensaje para este fallo."
        val errorCode = (exception as? FirebaseAuthException)?.errorCode

        val userMessage = when (exception) {
            is FirebaseAuthInvalidCredentialsException -> {
                "Firebase rechazó el correo. Revisa que esté escrito correctamente."
            }

            is FirebaseAuthInvalidUserException -> {
                "Firebase no encontró una cuenta válida con ese correo."
            }

            is FirebaseNetworkException -> {
                "Firebase no pudo conectarse para enviar la recuperación. Revisa tu conexión."
            }

            else -> {
                "Firebase rechazó el envío del correo de recuperación."
            }
        }

        return buildString {
            append(userMessage)
            append(" Detalle de Firebase: ")
            append(message)

            if (errorCode != null) {
                append(" (código: ")
                append(errorCode)
                append(")")
            }
        }
    }

    private fun logPasswordResetEmailError(
        exception: Exception?
    ) {
        val exceptionType = exception?.javaClass?.name ?: "sin excepción"
        val message = exception?.message ?: "sin mensaje"
        val errorCode = (exception as? FirebaseAuthException)?.errorCode ?: "sin código"

        Log.e(
            logTag,
            "Error al enviar correo de recuperación. " +
                "tipo=$exceptionType, mensaje=$message, código=$errorCode",
            exception
        )
    }

    private fun logPasswordResetConfirmationError(
        exception: Exception?
    ) {
        val exceptionType = exception?.javaClass?.name ?: "sin excepción"
        val message = exception?.message ?: "sin mensaje"
        val errorCode = (exception as? FirebaseAuthException)?.errorCode ?: "sin código"

        Log.e(
            logTag,
            "Error al confirmar el cambio de contraseña. " +
                "tipo=$exceptionType, mensaje=$message, código=$errorCode",
            exception
        )
    }

    private fun verificationEmailErrorMessage(
        exception: Exception?
    ): String {
        if (exception is FirebaseTooManyRequestsException) {
            return "Has solicitado demasiados correos de verificación. " +
                "Espera unos minutos antes de volver a intentarlo."
        }

        val message = exception?.localizedMessage
            ?: exception?.message
            ?: "Firebase no proporcionó un mensaje para este fallo."
        val errorCode = (exception as? FirebaseAuthException)?.errorCode

        val userMessage = when (exception) {
            is FirebaseNetworkException -> {
                "Firebase no pudo conectarse para enviar el correo. Revisa tu conexión."
            }

            else -> {
                "Firebase rechazó el envío del correo de verificación."
            }
        }

        return buildString {
            append(userMessage)
            append(" Detalle de Firebase: ")
            append(message)

            if (errorCode != null) {
                append(" (código: ")
                append(errorCode)
                append(")")
            }
        }
    }

    private fun logVerificationEmailError(
        exception: Exception?
    ) {
        val exceptionType = exception?.javaClass?.name ?: "sin excepción"
        val message = exception?.message ?: "sin mensaje"
        val errorCode = (exception as? FirebaseAuthException)?.errorCode ?: "sin código"

        Log.e(
            logTag,
            "Error al enviar correo de verificación. " +
                "tipo=$exceptionType, mensaje=$message, código=$errorCode",
            exception
        )
    }

    private fun actionCodeErrorMessage(
        exception: Exception?
    ): String {
        return when (exception) {
            is FirebaseAuthInvalidCredentialsException -> {
                "El código no es válido o ya fue usado. Revisa el correo más reciente."
            }

            is FirebaseNetworkException -> {
                "Revisa tu conexión e inténtalo de nuevo."
            }

            is FirebaseTooManyRequestsException -> {
                "Demasiados intentos. Espera un momento e inténtalo de nuevo."
            }

            else -> {
                "No se pudo validar el código. Revisa el correo más reciente."
            }
        }
    }

    private fun showError(
        message: String
    ) {
        _uiState.value = _uiState.value.copy(
            isLoading = false,
            errorMessage = message,
            successMessage = ""
        )
    }

    private fun showSuccess(
        message: String
    ) {
        _uiState.value = _uiState.value.copy(
            isLoading = false,
            errorMessage = "",
            successMessage = message
        )
    }

    override fun onCleared() {
        firebaseAuth.removeAuthStateListener(authStateListener)
        super.onCleared()
    }
}
