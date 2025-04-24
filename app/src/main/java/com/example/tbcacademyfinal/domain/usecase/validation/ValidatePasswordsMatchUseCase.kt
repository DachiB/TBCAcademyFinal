package com.example.tbcacademyfinal.domain.usecase.validation

import com.example.tbcacademyfinal.util.Resource
import javax.inject.Inject

class ValidatePasswordsMatchUseCase @Inject constructor() {

    operator fun invoke(password: String, confirmPassword: String): Resource<Unit> { // Return Resource<Unit>
        if (password != confirmPassword) {
            return Resource.Error("Passwords do not match.") // TODO: Use string resources
        }
        return Resource.Success(Unit) // Success returns Unit
    }
}