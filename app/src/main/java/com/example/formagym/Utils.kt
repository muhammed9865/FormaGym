package com.example.formagym

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import androidx.fragment.app.Fragment
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import java.text.SimpleDateFormat
import java.util.*

suspend fun Fragment.getBitmap(url: String): Bitmap {
    val loading = ImageLoader(requireContext())
    val request = ImageRequest.Builder(requireContext())
        .data(url)
        .build()

    val result = (loading.execute(request) as SuccessResult).drawable
    return (result as BitmapDrawable).bitmap
}

fun getData(milliseconds: Long): String {
    val sdf = SimpleDateFormat.getDateInstance()
    val date = Date(milliseconds).time
    return sdf.format(date)
}

fun getCurrentData(): String {
    val time = System.currentTimeMillis()
    val sdf = SimpleDateFormat.getDateTimeInstance()
    return sdf.format(time)
}