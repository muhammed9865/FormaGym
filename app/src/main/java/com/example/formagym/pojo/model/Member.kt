package com.example.formagym.pojo.model

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.Nullable

@Entity(tableName = "members_table")
data class Member(
    var name: String,
    var subscribeStartDate: Long,
    var subscribeEndDate: Long,
    @Nullable
    @ColumnInfo(defaultValue = "0")
    var memberPhoto: Bitmap? = null
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
