package com.blackfish.a1pedal.API

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import com.blackfish.a1pedal.R

object Notification {

    var a = 0

    fun makeNotification(content: Int, context: Context) {
        if (content == 0) {
            return
        }
        createNotificationChannel(context)
        var builder = NotificationCompat.Builder(context, NotificationCompat.CATEGORY_EVENT)
                .setSmallIcon(R.drawable.icon_launcher)
                .setContentTitle("У вас новые события!\uD83D\uDCE1")
                .setContentText("$content новых уведомлений!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(a++, builder.build())
        }
    }

    private fun createNotificationChannel(context: Context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "1pedal"
            val descriptionText = "1pedals event"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(NotificationCompat.CATEGORY_EVENT, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}