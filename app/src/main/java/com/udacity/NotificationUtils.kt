package com.udacity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import androidx.core.app.NotificationCompat




fun NotificationManager.sendNotification(
    channelID: String,
    message: String,
    applicationContext: Context,
    notificationId: Int,
    pendingIntent: PendingIntent
) {



    val builder= NotificationCompat.Builder(
        applicationContext,
        channelID
    )
        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setContentTitle(
            applicationContext.getString(R.string.notification_title)
        )
        .setContentText(message)
        .setAutoCancel(true)
        .addAction(
            R.drawable.ic_assistant_black_24dp,
            "check status",
            pendingIntent
        )
        .setPriority(NotificationCompat.PRIORITY_HIGH)
    notify(notificationId,builder.build())
}