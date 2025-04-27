package com.example.tbcacademyfinal.domain.usecase.language

import com.example.tbcacademyfinal.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLanguageUseCase @Inject constructor(
    private val repository: DataStoreRepository
) {
    operator fun invoke(): Flow<String> = repository.getAppLanguage()
}