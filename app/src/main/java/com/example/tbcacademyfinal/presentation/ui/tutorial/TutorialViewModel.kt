package com.example.tbcacademyfinal.presentation.ui.tutorial

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tbcacademyfinal.presentation.ui.main.store.StoreIntent
import com.example.tbcacademyfinal.presentation.ui.main.store.StoreSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TutorialViewModel @Inject constructor() : ViewModel() {

    private val _event = MutableSharedFlow<TutorialSideEffect>()
    var event = _event.asSharedFlow()

    fun processIntent(intent: TutorialIntent) {
        when (intent) {
            is TutorialIntent.ClickedSkipTutorial -> navigateToLogin()
            is TutorialIntent.ClickedStartTutorial -> startTutorial()
        }
    }

    private fun navigateToLogin() {
        viewModelScope.launch {
            _event.emit(TutorialSideEffect.NavigateToLogin)
        }
    }

    private fun startTutorial() {
        viewModelScope.launch {
            _event.emit(TutorialSideEffect.NavigateToNextTutorial)
        }
    }
}