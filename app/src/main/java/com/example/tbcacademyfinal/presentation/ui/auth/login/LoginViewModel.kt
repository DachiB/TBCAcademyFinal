package com.example.tbcacademyfinal.presentation.ui.auth.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tbcacademyfinal.common.Resource
import com.example.tbcacademyfinal.common.errorOrNull
import com.example.tbcacademyfinal.domain.repository.DataStoreRepository
import com.example.tbcacademyfinal.domain.usecase.auth.LoginUserUseCase
import com.example.tbcacademyfinal.domain.usecase.validation.ValidateEmailUseCase
import com.example.tbcacademyfinal.domain.usecase.validation.ValidatePasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUserUseCase: LoginUserUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    var state by mutableStateOf(LoginState())
        private set

    private val _event = MutableSharedFlow<LoginSideEffect>()
    val event = _event.asSharedFlow()

    // Function to process incoming intents from the UI
    fun processIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.EmailChanged -> updateEmail(intent.email)
            is LoginIntent.PasswordChanged -> updatePassword(intent.password)
            is LoginIntent.LoginClicked -> performLogin()
            is LoginIntent.RegisterLinkClicked -> navigateToRegister()
            is LoginIntent.PasswordVisibilityChanged -> updatePasswordVisibility()
            is LoginIntent.RememberMeChanged -> state = state.copy(rememberMe = intent.isChecked)
        }
    }


    private fun updateEmail(email: String) {
        state = state.copy(email = email, errorMessage = null)
    }

    private fun updatePassword(password: String) {
        state = state.copy(password = password, errorMessage = null)
    }

    private fun updatePasswordVisibility() {
        state = state.copy(isPasswordVisible = !state.isPasswordVisible)
    }

    private fun performLogin() {
        if (state.isLoading) return

        val email = state.email.trim()
        val password = state.password

        val firstError = validateEmailUseCase(email).errorOrNull()
            ?: validatePasswordUseCase(password).errorOrNull()

        firstError?.let {
            state = state.copy(errorMessage = it)
            return
        }

        state = state.copy(errorMessage = null)

        viewModelScope.launch {
            loginUserUseCase(email, password).collect { resource ->
                when (resource) {
                    is Resource.Loading -> state =
                        state.copy(isLoading = true) // Can keep updating loading just in case
                    is Resource.Success -> {
                        dataStoreRepository.setShouldRememberUser(state.rememberMe)
                        state = state.copy(
                            isLoading = false,
                            isLoginSuccess = true,
                            errorMessage = null
                        )
                        _event.emit(LoginSideEffect.NavigateToMain)
                    }

                    is Resource.Error -> {
                        state = state.copy(
                            isLoading = false,
                            errorMessage = resource.message
                        )
                    }
                }
            }
        }
    }

    private fun navigateToRegister() {
        viewModelScope.launch {
            _event.emit(LoginSideEffect.NavigateToRegister)
        }
    }
}