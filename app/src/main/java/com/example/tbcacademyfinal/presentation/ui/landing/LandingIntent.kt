package com.example.tbcacademyfinal.presentation.ui.landing

sealed interface LandingIntent {
    data object PressedProceed : LandingIntent
}