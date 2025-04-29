package com.example.tbcacademyfinal.presentation.ui.tutorial

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tbcacademyfinal.domain.usecase.landing.SetHadSeenLandingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TutorialViewModel @Inject constructor(
    private val setHadSeenLandingUseCase: SetHadSeenLandingUseCase
) : ViewModel() {

    var state by mutableStateOf(TutorialState())
        private set

    private val _event = MutableSharedFlow<TutorialSideEffect>()
    var event = _event.asSharedFlow()

    init {
        launchAnimation()
    }

    fun processIntent(intent: TutorialIntent) {
        when (intent) {
            is TutorialIntent.ClickedFinishTutorial,
            is TutorialIntent.ClickedSkipTutorial -> finishTutorial()

            is TutorialIntent.ClickedStartTutorial -> startTutorial()

        }
    }


    private fun startTutorial() {
        viewModelScope.launch {
            state = state.copy(hasClickedNextTutorial = true, contentVisible = false)
            launchAnimation()
        }
    }

    private fun launchAnimation() {
        viewModelScope.launch {
            delay(500)
            state = state.copy(contentVisible = true)
        }
    }

    private fun finishTutorial() {
        viewModelScope.launch {
            setHadSeenLandingUseCase(true)
            _event.emit(TutorialSideEffect.NavigateToLogin)
        }
    }
}