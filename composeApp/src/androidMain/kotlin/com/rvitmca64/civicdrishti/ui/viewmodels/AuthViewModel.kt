package com.rvitmca64.civicdrishti.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.rvitmca64.civicdrishti.data.datastore.SessionManager
import com.rvitmca64.civicdrishti.data.model.UserData
import com.rvitmca64.civicdrishti.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    data class Success(val user: UserData) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}

data class AuthFormState(
    val name: String = "",
    val aadhaar: String = "",
    val nameError: String? = null,
    val aadhaarError: String? = null
)

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AuthRepository()
    private val sessionManager = SessionManager(application)

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val _formState = MutableStateFlow(AuthFormState())
    val formState: StateFlow<AuthFormState> = _formState.asStateFlow()

    private val _loggedInUser = MutableStateFlow<UserData?>(null)
    val loggedInUser: StateFlow<UserData?> = _loggedInUser.asStateFlow()

    companion object {
        private const val TAG = "AuthViewModel"
    }

    init {
        checkExistingSession()
    }

    /**
     * Update name input
     */
    fun onNameChanged(name: String) {
        _formState.value = _formState.value.copy(
            name = name,
            nameError = null
        )
    }

    /**
     * Update Aadhaar input
     */
    fun onAadhaarChanged(aadhaar: String) {
        if (aadhaar.length <= 12 && aadhaar.all { it.isDigit() }) {
            _formState.value = _formState.value.copy(
                aadhaar = aadhaar,
                aadhaarError = null
            )
        }
    }

    /**
     * Validate inputs
     */
    private fun validateInputs(): Boolean {
        val name = _formState.value.name
        val aadhaar = _formState.value.aadhaar

        var isValid = true
        var nameError: String? = null
        var aadhaarError: String? = null

        if (name.isBlank()) {
            nameError = "Name cannot be empty"
            isValid = false
        } else if (!name.matches(Regex("^[A-Za-z ]+$"))) {
            nameError = "Enter a valid name (letters and spaces only)"
            isValid = false
        } else if (name.length < 3) {
            nameError = "Name must be at least 3 characters"
            isValid = false
        }

        if (aadhaar.length != 12) {
            aadhaarError = "Aadhaar number must be exactly 12 digits"
            isValid = false
        }

        _formState.value = _formState.value.copy(
            nameError = nameError,
            aadhaarError = aadhaarError
        )

        return isValid
    }

    /**
     * Login or register user
     */
    fun loginOrRegisterUser() {
        if (!validateInputs()) {
            return
        }

        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading

            try {
                val name = _formState.value.name.trim()
                val aadhaar = _formState.value.aadhaar

                val existingUserResult = repository.getUserByAadhaarHash(aadhaar)

                if (existingUserResult.isFailure) {
                    _uiState.value = AuthUiState.Error(
                        existingUserResult.exceptionOrNull()?.message ?: "Failed to connect to server"
                    )
                    return@launch
                }

                val existingUser = existingUserResult.getOrNull()

                val user = if (existingUser != null) {
                    Log.d(TAG, "Existing user found. Logging in...")
                    repository.updateLastLogin(existingUser.userId)
                    existingUser.copy(lastLoginAt = System.currentTimeMillis())
                } else {
                    Log.d(TAG, "New user. Creating account...")
                    val createResult = repository.createUser(name, aadhaar)

                    if (createResult.isFailure) {
                        _uiState.value = AuthUiState.Error(
                            createResult.exceptionOrNull()?.message ?: "Failed to create account"
                        )
                        return@launch
                    }

                    createResult.getOrNull()!!
                }

                sessionManager.saveSession(user)
                _loggedInUser.value = user

                Log.d(TAG, "Login successful: ${user.name}")
                _uiState.value = AuthUiState.Success(user)

            } catch (e: Exception) {
                Log.e(TAG, "Login error", e)
                _uiState.value = AuthUiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }

    /**
     * Check if user has existing session
     */
    fun checkExistingSession() {
        viewModelScope.launch {
            try {
                val sessionUser = sessionManager.getSession()

                if (sessionUser != null) {
                    Log.d(TAG, "Valid session found for: ${sessionUser.name}")

                    val fullUserResult = repository.getUserById(sessionUser.userId)

                    if (fullUserResult.isSuccess) {
                        val fullUser = fullUserResult.getOrNull()
                        if (fullUser != null) {
                            _loggedInUser.value = fullUser
                            _uiState.value = AuthUiState.Success(fullUser)
                            repository.updateLastLogin(fullUser.userId)
                        }
                    }
                } else {
                    Log.d(TAG, "No valid session found")
                    _uiState.value = AuthUiState.Idle
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error checking session", e)
                _uiState.value = AuthUiState.Idle
            }
        }
    }

    /**
     * Logout user - PROPERLY CLEAR EVERYTHING
     */
    fun logout() {
        viewModelScope.launch {
            try {
                Log.d(TAG, "Logging out user...")

                // Clear session from DataStore
                sessionManager.clearSession()

                // Clear ViewModel state
                _loggedInUser.value = null
                _uiState.value = AuthUiState.Idle
                _formState.value = AuthFormState()

                Log.d(TAG, "Logout complete")
            } catch (e: Exception) {
                Log.e(TAG, "Error during logout", e)
            }
        }
    }

    /**
     * Reset UI state
     */
    fun resetUiState() {
        _uiState.value = AuthUiState.Idle
    }
}