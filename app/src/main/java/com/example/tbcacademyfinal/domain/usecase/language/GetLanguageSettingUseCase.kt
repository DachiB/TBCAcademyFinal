package com.example.tbcacademyfinal.domain.usecase.language

import com.example.tbcacademyfinal.domain.repository.DataStoreRepository
import com.example.tbcacademyfinal.presentation.misc.LanguageType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLanguageSettingUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) {
    operator fun invoke(): Flow<LanguageType> {
        return dataStoreRepository.getLanguage()
    }
}