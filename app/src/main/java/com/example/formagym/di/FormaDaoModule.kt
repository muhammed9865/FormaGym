package com.example.formagym.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import com.example.formagym.pojo.datasource.FormaDao
import com.example.formagym.pojo.datasource.FormaDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FormaDaoModule {


    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): FormaDatabase {
        return Room
            .databaseBuilder(context, FormaDatabase::class.java, "forma.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideFormaDao(formaDatabase: FormaDatabase): FormaDao = formaDatabase.getDao()
}