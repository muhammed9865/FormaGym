package com.example.formagym.di

import android.content.Context
import androidx.room.Room
import com.example.formagym.data.datasource.FormaDao_FormaRepositoryImpl
import com.example.formagym.data.datasource.FormaDatabase
import com.example.formagym.domain.repository.FormaRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {


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
    fun provideFormaDao(formaDatabase: FormaDatabase): FormaDao_FormaRepositoryImpl = formaDatabase.getDao()

    @Provides
    @Singleton
    fun provideFormaRepository(formaDao: FormaDao_FormaRepositoryImpl) : FormaRepository = formaDao
}