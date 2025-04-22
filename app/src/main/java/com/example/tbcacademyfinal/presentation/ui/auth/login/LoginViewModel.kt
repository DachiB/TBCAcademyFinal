package com.example.tbcacademyfinal.presentation.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tbcacademyfinal.domain.repository.AuthRepository
import com.example.tbcacademyfinal.domain.usecase.validation.ValidateEmailUseCase
import com.example.tbcacademyfinal.domain.usecase.validation.ValidatePasswordUseCase
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
    private val authRepository: AuthRepository,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase
) : ViewModel() {

    private var _state = MutableStateFlow(LoginState())
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
            is LoginIntent.PasswordVisibilityChanged -> updatePasswordVisibility()
        }
    }

    private fun updateEmail(email: String) {
        _state.update { it.copy(email = email, errorMessage = null) }
    }

    private fun updatePassword(password: String) {
        _state.update { it.copy(password = password, errorMessage = null) }
    }

    private fun updatePasswordVisibility() {
        _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    private fun performLogin() {
        if (state.value.isLoading) return

        val email = state.value.email.trim() // Trim email here
        val password = state.value.password // No need to trim password

        // --- Run Validations ---
        val emailValidation = validateEmailUseCase(email)
        if (emailValidation is Resource.Error) {
            _state.update { it.copy(errorMessage = emailValidation.message) }
            return // Stop if validation fails
        }

        val passwordValidation = validatePasswordUseCase(password)
        if (passwordValidation is Resource.Error) {
            _state.update { it.copy(errorMessage = passwordValidation.message) }
            return // Stop if validation fails
        }
        // --- End Validations ---

        // If validations passed, clear any previous error and proceed
        _state.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            authRepository.login(email, password).collect { resource ->
                when (resource) {
                    is Resource.Loading -> _state.update { it.copy(isLoading = true) } // Can keep updating loading just in case
                    is Resource.Success -> {
                        _state.update { it.copy(isLoading = false, isLoginSuccess = true, errorMessage = null) }
                        _event.tryEmit(LoginSideEffect.NavigateToMain)
                    }
                    is Resource.Error -> {
                        _state.update { it.copy(isLoading = false, errorMessage = resource.message) }
                    }
                }
            }
        }
    }

    private fun navigateToRegister() {
        _event.tryEmit(LoginSideEffect.NavigateToRegister)
    }
}