package com.example.formagym

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*


fun getData(milliseconds: Long): String {
    val sdf = SimpleDateFormat("d MMM, yyyy", Locale.getDefault())
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = milliseconds
    return sdf.format(calendar.time)
}


fun showError(view: View,message: String) {
    Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        .setBackgroundTint(Color.RED)
        .show()
}

@RequiresApi(Build.VERSION_CODES.M)
fun Fragment.checkForPermission(string: String): Boolean {
    if (requireContext().checkSelfPermission(string) == PackageManager.PERMISSION_GRANTED) {
        return true
    }
    return false
}

fun isOutdated(time: Long) = time < System.currentTimeMillis()



