package com.udacity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.udacity.Constants.DOWNLOAD_STATUS
import com.udacity.Constants.FILE_NAME
import com.udacity.Constants.NOTIFICATION_ID

fun NotificationManager.sendNotification(applicationContext: Context, messageBody: String,
                                         fileName: String, downloadStatus: String) {

    val intent = Intent(applicationContext, DetailActivity::class.java)
    intent.putExtra(FILE_NAME, fileName)
    intent.putExtra(DOWNLOAD_STATUS, downloadStatus)

    val pendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val notificationBuilder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.notification_channel_id)
    )
        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setContentTitle(applicationContext.getString(R.string.notification_title))
        .setContentText(messageBody)
        .setAutoCancel(true)
        .setContentIntent(pendingIntent)
        .addAction(R.drawable.ic_assistant_black_24dp, applicationContext.getString(R.string.notification_action), pendingIntent)
    notify(NOTIFICATION_ID, notificationBuilder.build())
}

fun NotificationManager.cancelAllNotifications() {
    cancelAll()
}