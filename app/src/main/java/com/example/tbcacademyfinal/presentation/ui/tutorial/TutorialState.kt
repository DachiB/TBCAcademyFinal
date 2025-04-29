package com.example.tbcacademyfinal.presentation.ui.tutorial

import androidx.compose.runtime.Immutable

@Immutable
data class TutorialState(
    val hasClickedNextTutorial: Boolean = false,
    val contentVisible: Boolean = false
)
