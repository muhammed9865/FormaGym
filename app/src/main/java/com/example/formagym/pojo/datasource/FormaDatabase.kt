package com.example.formagym.pojo.datasource

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.formagym.Constants
import com.example.formagym.pojo.datasource.converter.PhotoConverter
import com.example.formagym.pojo.model.Payment
import com.example.formagym.pojo.model.User

@Database(
    entities = [User::class, Payment::class],
    version = Constants.DATABASE_VERSION,
   /* autoMigrations = [

        AutoMigration(from = 6, to = 4),
        //AutoMigration(from = 3, to = 4),
    ],*/
    exportSchema = true
)
@TypeConverters(PhotoConverter::class)
abstract class FormaDatabase : RoomDatabase() {
    abstract fun getDao(): FormaDao

}