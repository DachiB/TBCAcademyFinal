package com.example.tbcacademyfinal.di

import com.example.tbcacademyfinal.data.util.ConnectivityObserverImpl
import com.example.tbcacademyfinal.domain.util.ConnectivityObserver
import dagger.Binds // Use @Binds for interface binding
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ObserverModule { // Use abstract class or object for @Provides

    // Use @Binds for simpler interface-implementation binding when constructor is @Inject
    // Ensure ConnectivityObserverImpl has @Inject constructor()
    @Binds
    @Singleton
    abstract fun bindConnectivityObserver(
        connectivityObserverImpl: ConnectivityObserverImpl
    ): ConnectivityObserver

    // If ConnectivityObserverImpl constructor isn't @Inject or you prefer @Provides:
    // (Your original approach was also fine, just slightly less idiomatic than @Binds here)
    // @Provides
    // @Singleton
    // fun provideConnectivityObserver(@ApplicationContext context: Context): ConnectivityObserver {
    //     return ConnectivityObserverImpl(context) // Hilt provides the context
    // }
}