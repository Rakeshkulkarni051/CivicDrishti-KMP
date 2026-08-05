package com.rvitmca64.civicdrishti.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.rvitmca64.civicdrishti.data.datastore.SessionManager
import com.rvitmca64.civicdrishti.data.model.UserData
import com.rvitmca64.civicdrishti.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AuthRepository()
    private val sessionManager = SessionManager(application)

    private val _userState = MutableStateFlow<UserData?>(null)
    val userState: StateFlow<UserData?> = _userState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadUserProfile()
    }

    /**
     * Load user profile from session and Firestore
     */
    fun loadUserProfile() {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val sessionUser = sessionManager.getSession()

                if (sessionUser != null) {
                    // Fetch full user data
                    val fullUserResult = repository.getUserById(sessionUser.userId)

                    if (fullUserResult.isSuccess) {
                        _userState.value = fullUserResult.getOrNull()
                    }
                }
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Logout user
     */
    fun logout() {
        viewModelScope.launch {
            sessionManager.clearSession()
            _userState.value = null
        }
    }
}