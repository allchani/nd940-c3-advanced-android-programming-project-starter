package com.udacity.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.udacity.DetailActivity
import com.udacity.R


private val NOTIFICATION_ID = 0

fun NotificationManager.sendNotification(fileName: String, status: String, applicationContext: Context) {

    val intent = Intent(applicationContext, DetailActivity::class.java)
    intent.putExtra("fileName", fileName)
    intent.putExtra("status", status)

    val pendingIntent = PendingIntent.getActivity(applicationContext, NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT)


    val builder = NotificationCompat.Builder(applicationContext, applicationContext.getString(R.string.download_channel_id))
            .setSmallIcon(R.drawable.ic_assistant_black_24dp)
            .setContentTitle(applicationContext.getString(R.string.notification_title))
            .setContentText(applicationContext.getString(R.string.notification_description))
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .addAction(R.drawable.ic_assistant_black_24dp,applicationContext.getString(R.string.notification_button),pendingIntent)


    notify(NOTIFICATION_ID, builder.build())
}