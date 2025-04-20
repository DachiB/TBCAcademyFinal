package com.example.tbcacademyfinal.presentation.ui.landing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tbcacademyfinal.domain.repository.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LandingViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    // This function will be called when the user proceeds from the landing page.
    fun onProceed() {
        viewModelScope.launch {
            dataStoreRepository.setHasSeenLanding(true)
            // No navigation event needed here, navigation is handled by the Composable's lambda
        }
    }
}