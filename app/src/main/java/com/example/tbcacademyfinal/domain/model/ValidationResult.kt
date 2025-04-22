package com.example.tbcacademyfinal.domain.model

data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String? = null // Or resource ID Int? for i18n
)