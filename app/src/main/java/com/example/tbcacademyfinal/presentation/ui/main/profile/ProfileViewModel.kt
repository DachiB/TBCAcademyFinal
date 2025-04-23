package com.example.tbcacademyfinal.presentation.ui.main.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tbcacademyfinal.domain.usecase.user.GetUserUseCase
import com.example.tbcacademyfinal.domain.usecase.user.LogOutUseCase
import com.example.tbcacademyfinal.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase, // UseCase now returns Flow<Resource<User>>
    private val logoutUseCase: LogOutUseCase
    // Removed theme/language UseCase injections
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<ProfileSideEffect>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val event = _event.asSharedFlow()

    init {
        processIntent(ProfileIntent.LoadProfileData)
    }

    fun processIntent(intent: ProfileIntent) {
        when (intent) {
            is ProfileIntent.LoadProfileData -> loadData()
            is ProfileIntent.LogoutClicked -> performLogout()
            // Removed ThemeChanged and LanguageChanged cases
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
                        _state.update { it.copy(isLoading = true, error = null) }
                    }

                    is Resource.Success -> {
                        // Get User object from success resource
                        val user = resource.data
                        _state.update {
                            it.copy(
                                isLoading = false,
                                userEmail = user.email, // Extract email from domain User model
                                error = null
                                // Update other fields here if added to state (e.g., displayName = user.displayName)
                            )
                        }
                    }

                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = resource.message // Set error message from resource
                            )
                        }
                    }
                }
            }
        }
    }

    private fun performLogout() {
        viewModelScope.launch {
            logoutUseCase()
            _event.tryEmit(ProfileSideEffect.NavigateToLogin)
        }
    }

    // Removed changeTheme and changeLanguage private functions
}