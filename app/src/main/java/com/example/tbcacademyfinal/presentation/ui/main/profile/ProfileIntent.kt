package com.example.tbcacademyfinal.presentation.ui.main.profile

sealed interface ProfileIntent {
    data object LoadProfileData : ProfileIntent
    data object LogoutClicked : ProfileIntent

    //TO-DO: Uncomment when needed
//    data class ThemeChanged(val theme: ThemeType) : ProfileIntent
//    data class LanguageChanged(val language: LanguageType) : ProfileIntent
}