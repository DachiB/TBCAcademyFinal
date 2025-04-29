package com.example.tbcacademyfinal.presentation.ui.tutorial

sealed interface TutorialIntent {
    data object ClickedStartTutorial : TutorialIntent
    data object ClickedSkipTutorial : TutorialIntent
    data object ClickedFinishTutorial : TutorialIntent

}