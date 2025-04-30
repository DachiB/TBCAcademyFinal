package com.example.tbcacademyfinal.di

import com.example.tbcacademyfinal.data.repository.ConnectivityObserverImpl
import com.example.tbcacademyfinal.domain.repository.ConnectivityObserver
import dagger.Binds // Use @Binds for interface binding
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ObserverModule {


    @Binds
    @Singleton
    abstract fun bindConnectivityObserver(
        connectivityObserverImpl: ConnectivityObserverImpl
    ): ConnectivityObserver

}