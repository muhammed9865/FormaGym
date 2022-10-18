package com.example.formagym.data.datasource.converter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream

class PhotoConverter {

    @TypeConverter
    fun fromBitmap(bitmap: Bitmap?): ByteArray {
        if (bitmap != null) {
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            return outputStream.toByteArray()
        }
        return ByteArray(1)
    }

    @TypeConverter
    fun toBitmap(byteArray: ByteArray?): Bitmap? {
        byteArray?.let {
            return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        }
        return null
    }
}