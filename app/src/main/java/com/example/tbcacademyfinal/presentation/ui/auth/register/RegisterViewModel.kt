package com.example.tbcacademyfinal.presentation.ui.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tbcacademyfinal.domain.usecase.auth.RegisterUserUseCase
import com.example.tbcacademyfinal.domain.usecase.validation.ValidateEmailUseCase
import com.example.tbcacademyfinal.domain.usecase.validation.ValidatePasswordUseCase
import com.example.tbcacademyfinal.domain.usecase.validation.ValidatePasswordsMatchUseCase
import com.example.tbcacademyfinal.common.Resource
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
class RegisterViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val validatePasswordsMatchUseCase: ValidatePasswordsMatchUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<RegisterSideEffect>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val event = _event.asSharedFlow()

    // Function to process incoming intents from the UI
    fun processIntent(intent: RegisterIntent) {
        when (intent) {
            is RegisterIntent.EmailChanged -> updateEmail(intent.email)
            is RegisterIntent.PasswordChanged -> updatePassword(intent.password)
            is RegisterIntent.ConfirmPasswordChanged -> updateConfirmPassword(intent.confirmPassword)
            is RegisterIntent.RegisterClicked -> performRegistration()
            is RegisterIntent.NavigateBackClicked -> navigateBack()
            is RegisterIntent.ConfirmPasswordVisibilityChanged -> updateConfirmPasswordVisibility()
            is RegisterIntent.PasswordVisibilityChanged -> updatePasswordVisibility()
        }
    }

    private fun updateEmail(email: String) {
        val emailValidation = validateEmailUseCase(email)
        val errorMessage = if (emailValidation is Resource.Error) emailValidation.message else null
        _state.update { it.copy(email = email, emailError = errorMessage) }
    }

    private fun updatePassword(password: String) {
        val passwordValidation = validatePasswordUseCase(password)
        val confirmPasswordValidation =
            validatePasswordsMatchUseCase(password, state.value.confirmPassword)
        val passwordError =
            if (passwordValidation is Resource.Error) passwordValidation.message else null
        val confirmPasswordError =
            if (confirmPasswordValidation is Resource.Error) confirmPasswordValidation.message else null
        _state.update {
            it.copy(
                password = password,
                passwordError = passwordError,
                confirmPasswordError = confirmPasswordError
            )
        }
    }

    private fun updateConfirmPassword(confirmPassword: String) {
        val passwordValidation = validatePasswordUseCase(state.value.password)
        val confirmPasswordValidation =
            validatePasswordsMatchUseCase(state.value.password, confirmPassword)
        val passwordError =
            if (passwordValidation is Resource.Error) passwordValidation.message else null
        val confirmPasswordError =
            if (confirmPasswordValidation is Resource.Error) confirmPasswordValidation.message else null
        _state.update {
            it.copy(
                confirmPassword = confirmPassword,
                passwordError = passwordError,
                confirmPasswordError = confirmPasswordError,
                )
        }
    }

    private fun updatePasswordVisibility() {
        _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    private fun updateConfirmPasswordVisibility() {
        _state.update { it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible) }
    }

    private fun performRegistration() {
        if (state.value.isLoading) return

        val email = state.value.email.trim()
        val password = state.value.password
        val confirmPassword = state.value.confirmPassword

        // --- Run Validations ---
        val emailValidation = validateEmailUseCase(email)
        if (emailValidation is Resource.Error) {
            _state.update { it.copy(serverErrorMessage = emailValidation.message) }
            return
        }

        val passwordValidation = validatePasswordUseCase(password)
        if (passwordValidation is Resource.Error) {
            _state.update { it.copy(serverErrorMessage = passwordValidation.message) }
            return
        }

        val matchValidation = validatePasswordsMatchUseCase(password, confirmPassword)
        if (matchValidation is Resource.Error) {
            _state.update { it.copy(serverErrorMessage = matchValidation.message) }
            return
        }
        // --- End Validations ---

        // If validations passed, clear error and proceed
        _state.update { it.copy(isLoading = true, serverErrorMessage = null) }

        viewModelScope.launch {
            registerUserUseCase(email, password).collect { resource ->
                when (resource) {
                    is Resource.Loading -> _state.update { it.copy(isLoading = true) }
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                isRegisterSuccess = true,
                                serverErrorMessage = null
                            )
                        }
                        _event.tryEmit(RegisterSideEffect.NavigateToMain)
                    }

                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                serverErrorMessage = resource.message
                            )
                        }
                        // Optionally emit event
//                        _event.tryEmit(RegisterSideEffect.ShowError(resource.message))
                    }
                }
            }
        }
    }

    private fun navigateBack() {
        _event.tryEmit(RegisterSideEffect.NavigateBackToLogin)
    }

}