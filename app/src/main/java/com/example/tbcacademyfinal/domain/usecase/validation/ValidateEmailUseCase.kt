package com.example.tbcacademyfinal.domain.usecase.validation

import android.util.Patterns
import com.example.tbcacademyfinal.common.Resource
import javax.inject.Inject

class ValidateEmailUseCase @Inject constructor() {

    operator fun invoke(email: String): Resource<Unit> { // Return Resource<Unit>
        val trimmedEmail = email.trim()
        if (trimmedEmail.isBlank()) {
            return Resource.Error("Email cannot be empty.") // TODO: Use string resources
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches()) {
            return Resource.Error("Please enter a valid email address.") // TODO: Use string resources
        }
        return Resource.Success(Unit) // Success returns Unit
    }
}