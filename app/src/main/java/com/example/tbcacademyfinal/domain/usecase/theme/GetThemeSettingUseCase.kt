package com.example.tbcacademyfinal.domain.usecase.theme

import com.example.tbcacademyfinal.domain.repository.DataStoreRepository
import com.example.tbcacademyfinal.presentation.misc.ThemeType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetThemeSettingUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) {
    operator fun invoke(): Flow<ThemeType> {
        return dataStoreRepository.getTheme()
    }
}