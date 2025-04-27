package com.example.tbcacademyfinal.presentation.ui.main.profile


data class ProfileState(
    val userEmail: String? = null,
//    TO-DO: Uncomment when needed
//    val currentTheme: ThemeType = ThemeType.SYSTEM, // TODO: Load from DataStore
//    val currentLanguage: LanguageType = LanguageType.ENGLISH, // TODO: Load from DataStore/Config
    val isLoading: Boolean = true,
    val error: String? = null,
    val isDarkTheme: Boolean = false,
    val currentLanguage: String = "en"
)