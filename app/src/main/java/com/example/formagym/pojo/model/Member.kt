package com.example.formagym.pojo.model

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "members_table")
data class Member(
    val name: String,
    var subscribeStartDate: Long,
    val subscribeEndDate: Long,
    val memberPhoto: Bitmap
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
