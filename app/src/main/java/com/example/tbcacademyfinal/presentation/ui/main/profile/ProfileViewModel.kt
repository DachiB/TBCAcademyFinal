package com.example.tbcacademyfinal.presentation.ui.main.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tbcacademyfinal.common.Resource
import com.example.tbcacademyfinal.domain.usecase.language.GetLanguageUseCase
import com.example.tbcacademyfinal.domain.usecase.language.SaveLanguageUseCase
import com.example.tbcacademyfinal.domain.usecase.theme.GetThemeUseCase
import com.example.tbcacademyfinal.domain.usecase.theme.SaveThemeUseCase
import com.example.tbcacademyfinal.domain.usecase.user.DeletePhotoUseCase
import com.example.tbcacademyfinal.domain.usecase.user.GetUserPhotosUseCase
import com.example.tbcacademyfinal.domain.usecase.user.GetUserUseCase
import com.example.tbcacademyfinal.domain.usecase.user.LogOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val logoutUseCase: LogOutUseCase,
    private val getThemeUseCase: GetThemeUseCase,
    private val saveThemeUseCase: SaveThemeUseCase,
    private val getLanguageUseCase: GetLanguageUseCase,
    private val saveLanguageUseCase: SaveLanguageUseCase,
    private val getUserPhotosUseCase: GetUserPhotosUseCase,
    private val deletePhotoUseCase: DeletePhotoUseCase
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
            is ProfileIntent.DeletePhotoClicked -> deletePhoto(intent.photoUrl)
        }
    }

    private fun loadData() {

        viewModelScope.launch {
            getUserUseCase().collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        state = state.copy(isLoading = true, error = null)
                    }

                    is Resource.Success -> {
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
            getUserPhotosUseCase().collect { photos ->
                when (photos) {
                    is Resource.Loading -> {
                        state = state.copy(isLoadingPhotos = true, photosError = null)

                    }

                    is Resource.Success -> {
                        state = state.copy(
                            photos = photos.data,
                            isLoadingPhotos = false,
                        )
                    }

                    is Resource.Error -> {
                        state = state.copy(
                            isLoadingPhotos = false,
                            photosError = photos.message
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

    private fun deletePhoto(photoUrl: String) {
        viewModelScope.launch {
            when (val result = deletePhotoUseCase(photoUrl)) {
                is Resource.Success -> {
                    state =
                        state.copy(photos = state.photos.filterNot { it == photoUrl })
                    _event.emit(ProfileSideEffect.ShowMessage("Photo deleted successfully")) // TODO: String resource
                }

                is Resource.Error -> {
                    _event.emit(ProfileSideEffect.ShowMessage("Error deleting photo: ${result.message}")) // TODO: String resource
                }

                is Resource.Loading -> {}
            }
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
            state = state.copy(currentLanguage = language)
            saveLanguageUseCase(language)
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