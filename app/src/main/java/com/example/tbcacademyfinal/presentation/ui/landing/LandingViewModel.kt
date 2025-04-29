package com.example.tbcacademyfinal.presentation.ui.landing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tbcacademyfinal.domain.repository.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LandingViewModel @Inject constructor() : ViewModel() {

    private val _event = MutableSharedFlow<LandingSideEffect>()
    val event = _event.asSharedFlow()

    fun processIntent(
        intent: LandingIntent,
    ) {
        when (intent) {
            is LandingIntent.PressedProceed -> {
                navigateToTutorial()
            }
        }
    }

    private fun navigateToTutorial() {
        viewModelScope.launch {
            _event.emit(LandingSideEffect.NavigateToTutorial)
        }
    }
}