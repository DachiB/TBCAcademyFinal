package com.example.tbcacademyfinal.presentation.ui.splash

sealed interface SplashSideEffect {
    data object NavigateToMain : SplashSideEffect // User logged in
    data object NavigateToAuth : SplashSideEffect // User logged out, has seen landing
    data object NavigateToLanding : SplashSideEffect // User logged out, first time
}