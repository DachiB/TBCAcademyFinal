package com.example.tbcacademyfinal.domain.usecase.theme

import com.example.tbcacademyfinal.domain.repository.DataStoreRepository
import javax.inject.Inject

class SaveThemeUseCase @Inject constructor(
    private val repository: DataStoreRepository
) {
    suspend operator fun invoke(darkTheme: Boolean) {
        repository.updateTheme(darkTheme)
    }
}