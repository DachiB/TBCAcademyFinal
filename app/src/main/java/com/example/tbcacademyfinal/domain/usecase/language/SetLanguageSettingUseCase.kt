package com.example.tbcacademyfinal.domain.usecase.language

import com.example.tbcacademyfinal.domain.repository.DataStoreRepository
import com.example.tbcacademyfinal.presentation.misc.LanguageType
import javax.inject.Inject

class SetLanguageSettingUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) {
    suspend operator fun invoke(language: LanguageType) {
        dataStoreRepository.setLanguage(language)
        // Note: Applying the locale change is NOT done here.
        // This use case only saves the preference.
    }
}