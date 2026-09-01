package com.julm.mitecmi.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
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
    val errorMessage: String = ""
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
            errorMessage = ""
        )

        firebaseAuth
            .signInWithEmailAndPassword(
                email.trim(),
                password
            )
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    updateUser(firebaseAuth.currentUser)
                } else {
                    showError(
                        task.exception?.localizedMessage
                            ?: "No se pudo iniciar sesion."
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
            errorMessage = ""
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
                        task.exception?.localizedMessage
                            ?: "No se pudo crear la cuenta."
                    )
                }
            }
    }

    fun signOut() {
        firebaseAuth.signOut()
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(
            errorMessage = ""
        )
    }

    private fun validateLogin(
        email: String,
        password: String
    ): Boolean {
        if (email.isBlank() || password.isBlank()) {
            showError("Ingresa correo y contrasena.")
            return false
        }

        if (!isInstitutionalEmail(email)) {
            showError("Usa tu correo institucional @tecmilenio.mx.")
            return false
        }

        if (password.length < 6) {
            showError("La contrasena debe tener al menos 6 caracteres.")
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
            showError("Solo se permiten correos @tecmilenio.mx.")
            return false
        }

        if (password.length < 6) {
            showError("La contrasena debe tener al menos 6 caracteres.")
            return false
        }

        if (password != confirmPassword) {
            showError("Las contrasenas no coinciden.")
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
            .endsWith("@tecmilenio.mx")
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
                    updateUser(firebaseAuth.currentUser)
                } else {
                    showError(
                        task.exception?.localizedMessage
                            ?: "La cuenta fue creada, pero no se pudo guardar el nombre."
                    )
                }
            }
    }

    private fun updateUser(
        user: FirebaseUser?
    ) {
        _uiState.value = AuthUiState(
            isAuthenticated = user != null,
            isLoading = false,
            userName = user?.displayName.orEmpty(),
            userEmail = user?.email.orEmpty(),
            errorMessage = ""
        )
    }

    private fun showError(
        message: String
    ) {
        _uiState.value = _uiState.value.copy(
            isLoading = false,
            errorMessage = message
        )
    }

    override fun onCleared() {
        firebaseAuth.removeAuthStateListener(authStateListener)
        super.onCleared()
    }
}
