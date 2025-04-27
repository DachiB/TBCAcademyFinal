package com.example.tbcacademyfinal.presentation.ui.main.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tbcacademyfinal.domain.usecase.user.GetUserUseCase
import com.example.tbcacademyfinal.domain.usecase.user.LogOutUseCase
import com.example.tbcacademyfinal.common.Resource
import com.example.tbcacademyfinal.domain.usecase.language.GetLanguageUseCase
import com.example.tbcacademyfinal.domain.usecase.language.SaveLanguageUseCase
import com.example.tbcacademyfinal.domain.usecase.theme.GetThemeUseCase
import com.example.tbcacademyfinal.domain.usecase.theme.SaveThemeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val logoutUseCase: LogOutUseCase,
    private val getThemeUseCase: GetThemeUseCase,
    private val saveThemeUseCase: SaveThemeUseCase,
    private val getLanguageUseCase: GetLanguageUseCase,
    private val saveLanguageUseCase: SaveLanguageUseCase
) : ViewModel() {

    var state by mutableStateOf(ProfileState())
        private set

    private val _event = MutableSharedFlow<ProfileSideEffect>()
    val event = _event.asSharedFlow()

    init {
        loadData()
        getCurrentTheme()
        getCurrentLanguage()
    }

    fun processIntent(intent: ProfileIntent) {
        when (intent) {
            is ProfileIntent.LoadProfileData -> loadData()
            is ProfileIntent.LogoutClicked -> performLogout()
            is ProfileIntent.LanguageChanged -> setLanguage(intent.language)
            is ProfileIntent.ThemeChanged -> toggleTheme(intent.theme)
        }
    }

    private fun loadData() {
        // Start loading state (already default, but explicit for clarity)
        // _state.update { it.copy(isLoading = true) } // Not strictly needed if initial state is loading=true

        viewModelScope.launch {
            // Call the use case which returns Flow<Resource<User>>
            getUserUseCase().collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        state = state.copy(isLoading = true, error = null)
                    }

                    is Resource.Success -> {
                        // Get User object from success resource
                        val user = resource.data
                        state = state.copy(
                            isLoading = false,
                            userEmail = user.email, // Extract email from domain User model
                            error = null
                        )

                    }

                    is Resource.Error -> {
                        state = state.copy(
                            isLoading = false,
                            error = resource.message // Set error message from resource
                        )

                    }
                }
            }
        }
    }

    private fun performLogout() {
        viewModelScope.launch {
            logoutUseCase()
            _event.emit(ProfileSideEffect.NavigateToLogin)
        }
    }


    private fun toggleTheme(current: Boolean) {
        viewModelScope.launch {
            saveThemeUseCase(!current)
            state = state.copy(isDarkTheme = !current)
        }
    }

    private fun setLanguage(language: String) {
        viewModelScope.launch {
            saveLanguageUseCase(language)
            state = state.copy(currentLanguage = language)
        }
    }

    private fun getCurrentTheme() {
        viewModelScope.launch {
            getThemeUseCase().collect { isDarkTheme ->
                state = state.copy(isDarkTheme = isDarkTheme)
            }
        }
    }

    private fun getCurrentLanguage() {
        viewModelScope.launch {
            getLanguageUseCase().collect { language ->
                state = state.copy(currentLanguage = language)
            }
        }
    }

}