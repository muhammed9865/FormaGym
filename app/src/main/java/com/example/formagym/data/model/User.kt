package com.example.formagym.data.model

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.Nullable

@Entity(tableName = "members_table")
data class User(
    var name: String,
    var subscribeStartDate: Long,
    var subscribeEndDate: Long,
    @Nullable
    @ColumnInfo(defaultValue = "0")
    var memberPhoto: Bitmap? = null,

    @ColumnInfo(defaultValue = "0")
    var paymentPrice: Double = 0.0
){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

}
