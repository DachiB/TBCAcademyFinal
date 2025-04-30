package com.example.tbcacademyfinal.domain.usecase.validation

import com.example.tbcacademyfinal.common.Resource
import javax.inject.Inject

class ValidatePasswordsMatchUseCase @Inject constructor() {

    operator fun invoke(password: String, confirmPassword: String): Resource<Unit> {
        if (password != confirmPassword) {
            return Resource.Error("Passwords do not match.")
        }
        return Resource.Success(Unit)
    }
}