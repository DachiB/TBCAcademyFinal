package com.example.tbcacademyfinal.presentation.ui.auth.register

data class RegisterState(
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val isPasswordVisible: Boolean = false,
    val confirmPassword: String = "",
    val confirmPasswordError: String? = null,
    val isConfirmPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val serverErrorMessage: String? = null,
    val isRegisterSuccess: Boolean = false
)