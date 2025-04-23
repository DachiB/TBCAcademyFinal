package com.example.tbcacademyfinal.domain.usecase.theme

import com.example.tbcacademyfinal.domain.repository.DataStoreRepository
import com.example.tbcacademyfinal.presentation.misc.ThemeType
import javax.inject.Inject

class SetThemeSettingUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) {
    suspend operator fun invoke(theme: ThemeType) {
        dataStoreRepository.setTheme(theme)
    }
}