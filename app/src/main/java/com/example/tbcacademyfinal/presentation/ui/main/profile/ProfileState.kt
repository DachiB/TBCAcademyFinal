package com.example.tbcacademyfinal.presentation.ui.main.profile

import androidx.compose.runtime.Stable

@Stable
data class ProfileState(
    val userEmail: String? = null,
//    TO-DO: Uncomment when needed
//    val currentTheme: ThemeType = ThemeType.SYSTEM, // TODO: Load from DataStore
//    val currentLanguage: LanguageType = LanguageType.ENGLISH, // TODO: Load from DataStore/Config
    val isLoading: Boolean = true,
    val error: String? = null
)