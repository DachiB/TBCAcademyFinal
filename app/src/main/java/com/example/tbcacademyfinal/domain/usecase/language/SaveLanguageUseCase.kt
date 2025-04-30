package com.example.tbcacademyfinal.domain.usecase.language

import com.example.tbcacademyfinal.domain.repository.DataStoreRepository
import javax.inject.Inject

class SaveLanguageUseCase @Inject constructor(
    private val repository: DataStoreRepository
) {
    suspend operator fun invoke(language: String) {
        repository.updateLanguage(language)
    }
}