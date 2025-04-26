package com.example.tbcacademyfinal.domain.usecase.validation

import com.example.tbcacademyfinal.common.Resource
import javax.inject.Inject

class ValidatePasswordUseCase @Inject constructor() {

    companion object {
        const val MIN_PASSWORD_LENGTH = 6
    }

    operator fun invoke(password: String): Resource<Unit> { // Return Resource<Unit>
        if (password.isBlank()) {
            return Resource.Error("Password cannot be empty.") // TODO: Use string resources
        }
        if (password.length < MIN_PASSWORD_LENGTH) {
            return Resource.Error("Password must be at least $MIN_PASSWORD_LENGTH characters long.") // TODO: Use string resources
        }
        // Add more checks if needed...

        return Resource.Success(Unit) // Success returns Unit
    }
}

// additional CHECKS
//if (!password.any { it.isDigit() }) {
//    return ValidationResult(
//        isValid = false,
//        errorMessage = "Password must contain at least one digit." // TODO: Use string resources
//    )
//}
//if (!password.any { it.isUpperCase() }) {
//    return ValidationResult(
//        isValid = false,
//        errorMessage = "Password must contain at least one uppercase letter." // TODO: Use string resources
//    )
//}