package com.example.formagym.di

import com.example.formagym.ui.adapter.subsadapter.SubscribersAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AdaptersModule {

    @Provides
    @Singleton
    fun provideAdapter() = SubscribersAdapter()
}