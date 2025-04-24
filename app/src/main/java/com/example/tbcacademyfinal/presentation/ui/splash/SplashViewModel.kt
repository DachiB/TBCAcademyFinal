package com.example.tbcacademyfinal.presentation.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tbcacademyfinal.domain.repository.DataStoreRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth, // Using FirebaseAuth directly for now
    private val dataStoreRepository: DataStoreRepository // Using DataStore repository
) : ViewModel() {

    private val _uiEvent = MutableSharedFlow<SplashSideEffect>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        checkInitialDestination()
    }

    private fun checkInitialDestination() {
        viewModelScope.launch {
            // Ensure splash is visible for a minimum duration
            delay(1500) // Adjust duration as needed

            // Check Firebase Auth state
            val isLoggedIn = firebaseAuth.currentUser != null
            // Read the DataStore value for landing page status
            val hasSeenLanding = dataStoreRepository.hasSeenLanding().first()

            val sideEffect: SplashSideEffect = when {
                isLoggedIn -> SplashSideEffect.NavigateToMain
                hasSeenLanding -> SplashSideEffect.NavigateToAuth
                else -> SplashSideEffect.NavigateToLanding
            }
            _uiEvent.emit(sideEffect)
        }
    }
}