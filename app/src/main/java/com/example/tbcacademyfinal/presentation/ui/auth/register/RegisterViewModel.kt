package com.example.tbcacademyfinal.presentation.ui.auth.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tbcacademyfinal.domain.usecase.auth.RegisterUserUseCase
import com.example.tbcacademyfinal.domain.usecase.validation.ValidateEmailUseCase
import com.example.tbcacademyfinal.domain.usecase.validation.ValidatePasswordUseCase
import com.example.tbcacademyfinal.domain.usecase.validation.ValidatePasswordsMatchUseCase
import com.example.tbcacademyfinal.common.Resource
import com.example.tbcacademyfinal.common.errorOrNull
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

    var state by mutableStateOf(RegisterState())
        private set

    private val _event = MutableSharedFlow<RegisterSideEffect>()
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



    private fun validatePasswords(password: String, confirm: String): Pair<String?, String?> {
        val passErr = validatePasswordUseCase(password).errorOrNull()
        val confirmErr = validatePasswordsMatchUseCase(password, confirm).errorOrNull()
        return passErr to confirmErr
    }

    private fun updateEmail(email: String) {
        val emailErr = validateEmailUseCase(email).errorOrNull()
        state = state.copy(
            email = email,
            emailError = emailErr
        )
    }

    private fun updatePassword(password: String) {
        val (passErr, confirmErr) = validatePasswords(password, state.confirmPassword)
        state = state.copy(
            password = password,
            passwordError = passErr,
            confirmPasswordError = confirmErr
        )
    }

    private fun updateConfirmPassword(confirmPassword: String) {
        val (passErr, confirmErr) = validatePasswords(state.password, confirmPassword)
        state = state.copy(
            confirmPassword = confirmPassword,
            passwordError = passErr,
            confirmPasswordError = confirmErr
        )
    }


    private fun updatePasswordVisibility() {
        state = state.copy(isPasswordVisible = !state.isPasswordVisible)
    }

    private fun updateConfirmPasswordVisibility() {
        state = state.copy(isConfirmPasswordVisible = !state.isConfirmPasswordVisible)
    }

    private fun performRegistration() {
        if (state.isLoading) return

        val email = state.email.trim()
        val password = state.password
        val confirmPassword = state.confirmPassword

        val firstError = validateEmailUseCase(email).errorOrNull()
            ?: validatePasswordUseCase(password).errorOrNull()
            ?: validatePasswordsMatchUseCase(password, confirmPassword).errorOrNull()

        firstError?.let {
            state = state.copy(serverErrorMessage = it)
            return
        }

        state = state.copy(serverErrorMessage = null)

        viewModelScope.launch {
            registerUserUseCase(email, password).collect { resource ->
                state = when (resource) {
                    is Resource.Loading -> state.copy(isLoading = true)
                    is Resource.Error -> state.copy(
                        isLoading = false,
                        serverErrorMessage = resource.message
                    )

                    is Resource.Success -> state.copy(
                        isLoading = false,
                        isRegisterSuccess = true,
                        serverErrorMessage = null
                    )
                }
            }
        }
    }


    private fun navigateBack() {
        viewModelScope.launch {
            _event.emit(RegisterSideEffect.NavigateBackToLogin)
        }
    }

}