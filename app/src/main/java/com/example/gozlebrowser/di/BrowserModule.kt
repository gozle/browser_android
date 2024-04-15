package com.example.gozlebrowser.di

import com.example.gozlebrowser.data.dataSource.BrowserApi
import com.example.gozlebrowser.data.repository.RecentRepositoryImpl
import com.example.gozlebrowser.domain.repository.RecentRepository
import com.example.gozlebrowser.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BrowserModule {

    @Provides
    @Singleton
    fun provideBrowserApi(): BrowserApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BrowserApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRecentRepository(api: BrowserApi): RecentRepository {
        return RecentRepositoryImpl(api)
    }
}