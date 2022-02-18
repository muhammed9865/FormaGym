package com.example.formagym.pojo.datasource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database()
abstract class FormaDatabase(): RoomDatabase() {
    abstract fun getDao(): FormaDao

    companion object {
        private const val dbName = "forma.db"
        @Volatile
        private var instance: FormaDatabase? = null

        fun getInstance(context: Context) = instance ?: synchronized(this) {
            instance ?: buildDatabase(context)

        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            FormaDatabase::class.java,
            dbName
        ).build()
    }
}