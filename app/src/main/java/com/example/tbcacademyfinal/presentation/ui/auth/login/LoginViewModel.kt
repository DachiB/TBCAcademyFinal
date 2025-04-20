package com.example.tbcacademyfinal.presentation.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tbcacademyfinal.domain.repository.AuthRepository
import com.example.tbcacademyfinal.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<LoginSideEffect>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val event = _event.asSharedFlow()

    // Function to process incoming intents from the UI
    fun processIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.EmailChanged -> updateEmail(intent.email)
            is LoginIntent.PasswordChanged -> updatePassword(intent.password)
            is LoginIntent.LoginClicked -> performLogin()
            is LoginIntent.RegisterLinkClicked -> navigateToRegister()
        }
    }

    // Private functions handling the logic for each intent
    private fun updateEmail(email: String) {
        _state.update { it.copy(email = email, errorMessage = null) }
    }

    private fun updatePassword(password: String) {
        _state.update { it.copy(password = password, errorMessage = null) }
    }

    private fun performLogin() {
        // Prevent multiple login attempts while one is in progress
        if (state.value.isLoading) return

        viewModelScope.launch {
            authRepository.login(state.value.email.trim(), state.value.password).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _state.update { it.copy(isLoading = true, errorMessage = null) }
                    }
                    is Resource.Success<*> -> {
                        _state.update { it.copy(isLoading = false, isLoginSuccess = true) }
                        _event.tryEmit(LoginSideEffect.NavigateToMain)
                    }
                    is Resource.Error -> {
                        _state.update { it.copy(isLoading = false, errorMessage = resource.message) }
                        // Optionally emit event if needed elsewhere:
                        // _event.tryEmit(LoginSideEffect.ShowError(resource.message))
                    }

                }
            }
        }
    }

    private fun navigateToRegister() {
        _event.tryEmit(LoginSideEffect.NavigateToRegister)
    }
}