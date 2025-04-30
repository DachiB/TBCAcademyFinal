package com.example.tbcacademyfinal.presentation.ui.main.profile

sealed interface ProfileIntent {
    data object LoadProfileData : ProfileIntent
    data object LogoutClicked : ProfileIntent
    data class ThemeChanged(val theme: Boolean) : ProfileIntent
    data class LanguageChanged(val language: String) : ProfileIntent
    data class DeletePhotoClicked(val photoUrl: String) : ProfileIntent
}