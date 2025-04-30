package com.example.tbcacademyfinal.presentation.ui.main.profile

import android.net.Uri


data class ProfileState(
    val userEmail: String? = null,
    val isLoading: Boolean = true,
    val error: String? = null,
    val isDarkTheme: Boolean = false,
    val currentLanguage: String = "en",

    val photos           : List<String>  = emptyList(),
    val isLoadingPhotos  : Boolean    = false,
    val photosError      : String?    = null
)