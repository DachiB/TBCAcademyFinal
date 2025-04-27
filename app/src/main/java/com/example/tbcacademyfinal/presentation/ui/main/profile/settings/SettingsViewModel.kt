package com.example.tbcacademyfinal.presentation.ui.main.profile.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tbcacademyfinal.domain.repository.DataStoreRepository
import com.example.tbcacademyfinal.domain.usecase.language.GetLanguageUseCase
import com.example.tbcacademyfinal.domain.usecase.language.SaveLanguageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repo: DataStoreRepository,
    private val getLanguageUseCase: GetLanguageUseCase,
    private val saveLanguageUseCase: SaveLanguageUseCase
) : ViewModel() {

    /** Emits the current theme (persisted or system default). */
    val darkThemeFlow: StateFlow<Boolean> =
        repo.darkThemeFlow()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = false // will immediately be replaced by flow’s first emission
            )

    val languageFlow: StateFlow<String> = getLanguageUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = Locale.getDefault().language
        )

    /** Toggle between dark/light and persist choice. */
    fun toggleTheme(current: Boolean) {
        viewModelScope.launch {
            repo.setDarkTheme(!current)
        }
    }

    fun setLanguage(language: String) {
        viewModelScope.launch {
            saveLanguageUseCase(language)
        }
    }

}