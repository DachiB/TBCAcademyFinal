package com.example.tbcacademyfinal.domain.usecase.user

import com.example.tbcacademyfinal.domain.repository.DataStoreRepository
import javax.inject.Inject

class RememberUserUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) {
    suspend operator fun invoke(
        shouldRememberUser: Boolean
    ) {
        dataStoreRepository.setShouldRememberUser(shouldRememberUser)
    }
}