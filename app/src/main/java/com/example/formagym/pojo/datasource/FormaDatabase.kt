package com.example.formagym.pojo.datasource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.formagym.pojo.datasource.converter.PhotoConverter
import com.example.formagym.pojo.model.Member

@Database(
    entities = [Member::class],
    version = 1,
)
@TypeConverters(PhotoConverter::class)
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