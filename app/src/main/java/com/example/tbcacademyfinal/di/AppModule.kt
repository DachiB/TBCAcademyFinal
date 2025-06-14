package com.example.tbcacademyfinal.di


import android.content.Context
import androidx.work.WorkManager
import com.example.tbcacademyfinal.MyApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): MyApplication {
        return app as MyApplication
    }

    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context) =
        WorkManager.getInstance(context)

}