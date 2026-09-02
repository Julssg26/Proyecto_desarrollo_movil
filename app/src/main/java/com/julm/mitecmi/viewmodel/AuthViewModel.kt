package com.julm.mitecmi.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
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
    val isEmailVerificationPending: Boolean = false
)

class AuthViewModel(
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
) : ViewModel() {

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
                    updateUser(firebaseAuth.currentUser)

                    if (firebaseAuth.currentUser?.isEmailVerified == false) {
                        showError(
                            "Todavía no aparece verificado. Revisa tu correo y vuelve a intentar."
                        )
                    }
                } else {
                    showError(
                        "No se pudo revisar la verificación. Inténtalo de nuevo."
                    )
                }
            }
    }

    fun resetPassword(
        email: String
    ) {
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
            successMessage = ""
        )

        firebaseAuth
            .sendPasswordResetEmail(email.trim())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showSuccess(
                        "Te enviamos un correo para recuperar tu contraseña."
                    )
                } else {
                    showError(
                        resetPasswordErrorMessage(task.exception)
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

    private fun sendVerificationEmail(
        user: FirebaseUser,
        successMessage: String
    ) {
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            errorMessage = "",
            successMessage = ""
        )

        user.sendEmailVerification()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    updateUser(firebaseAuth.currentUser)
                    showSuccess(successMessage)
                } else {
                    showError(
                        verificationEmailErrorMessage(task.exception)
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
        _uiState.value = AuthUiState(
            isAuthenticated = user != null && user.isEmailVerified,
            isLoading = false,
            userName = user?.displayName.orEmpty(),
            userEmail = user?.email.orEmpty(),
            errorMessage = "",
            isEmailVerificationPending = user != null && !user.isEmailVerified
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
        return when (exception) {
            is FirebaseAuthInvalidCredentialsException -> {
                "Revisa que el correo esté escrito correctamente."
            }

            is FirebaseAuthInvalidUserException -> {
                "No encontramos una cuenta con ese correo."
            }

            is FirebaseNetworkException -> {
                "Revisa tu conexión e inténtalo de nuevo."
            }

            else -> {
                "No se pudo enviar el correo de recuperación."
            }
        }
    }

    private fun verificationEmailErrorMessage(
        exception: Exception?
    ): String {
        return when (exception) {
            is FirebaseNetworkException -> {
                "Revisa tu conexión e inténtalo de nuevo."
            }

            is FirebaseTooManyRequestsException -> {
                "Firebase bloqueó temporalmente los envíos. Espera unos minutos e inténtalo de nuevo."
            }

            else -> {
                "No se pudo enviar el correo de verificación."
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
