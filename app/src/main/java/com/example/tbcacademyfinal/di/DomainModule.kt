package com.example.tbcacademyfinal.di

import com.example.tbcacademyfinal.domain.usecase.validation.ValidateEmailUseCase
import com.example.tbcacademyfinal.domain.usecase.validation.ValidatePasswordUseCase
import com.example.tbcacademyfinal.domain.usecase.validation.ValidatePasswordsMatchUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent // Scope to ViewModel lifecycle
import dagger.hilt.android.scopes.ViewModelScoped // Use ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class) // Use cases often scoped to ViewModel
object DomainModule {

    @Provides
    @ViewModelScoped // Scope the use case to the ViewModel
    fun provideValidateEmailUseCase(): ValidateEmailUseCase {
        return ValidateEmailUseCase()
    }

    @Provides
    @ViewModelScoped
    fun provideValidatePasswordUseCase(): ValidatePasswordUseCase {
        return ValidatePasswordUseCase()
    }

    @Provides
    @ViewModelScoped
    fun provideValidatePasswordsMatchUseCase(): ValidatePasswordsMatchUseCase {
        return ValidatePasswordsMatchUseCase()
    }
}