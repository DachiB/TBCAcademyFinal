package com.example.tbcacademyfinal.domain.usecase.theme

import com.example.tbcacademyfinal.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetThemeUseCase @Inject constructor(
    private val repository: DataStoreRepository
) {
    operator fun invoke(): Flow<Boolean> = repository.getAppTheme()
}