package com.example.formagym.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.formagym.Constants
import com.example.formagym.R

class NotificationHelper(private val context: Context, private val channelId: String) {
    private var notificationCompat: NotificationCompat.Builder? = null

    init {
        notificationCompat = builder(channelId)
    }

    private fun builder(channelId: String): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, channelId)
    }

    fun setIcon(icon: Int) {
        notificationCompat?.setSmallIcon(icon) ?: builder(channelId).setSmallIcon(icon)
    }

    fun notifySubscriptionFinished(userName: String) {
        notificationCompat?.setContentText(userName + " ${context.getString(R.string.sub_finish)}")

    }

    fun notifyUser() {
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= 26) {
            val channel = NotificationChannel(Constants.NCI, Constants.NCI, NotificationManager.IMPORTANCE_HIGH)
            nm.createNotificationChannel(channel)
        }

        nm.notify(123, this.build())
    }

    fun build() = notificationCompat?.build()
}