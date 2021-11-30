package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.udacity.util.sendNotification
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {
    private var URL: String? = null
    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager

    private lateinit var downloadManager: DownloadManager
    private lateinit var status: String
    private lateinit var fileName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        createChannel(getString(R.string.download_channel_id), getString(R.string.download_channel_name))

        notificationManager = getSystemService(NotificationManager::class.java) as NotificationManager

        custom_button.setOnClickListener {
            if(URL != null) {
                custom_button.updateButtonState(ButtonState.Loading)
                download()
            } else {
                Toast.makeText(applicationContext, getString(R.string.select_suggestion), Toast.LENGTH_SHORT).show()
            }

        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (id == downloadID) {
                checkDownloadStatus()
                notificationManager.sendNotification(fileName, status, applicationContext)
            }
        }
    }

    private fun download() {
        val request =
            DownloadManager.Request(Uri.parse(URL))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    private fun checkDownloadStatus() {
        val cursor = downloadManager.query(DownloadManager.Query().setFilterById(downloadID))
        if (cursor.moveToFirst()) {
            val statusId=cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
            when(statusId) {
                DownloadManager.STATUS_SUCCESSFUL -> status = "SUCCESS"
                DownloadManager.STATUS_FAILED -> status = "FAILED"
                else -> status = "ERROR"
            }
        custom_button.updateButtonState(ButtonState.Completed)

        }
    }

    companion object {
        private const val URL_glide = "https://github.com/bumptech/glide"
        private const val URL_loadApp =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val URL_retrofit = "https://github.com/square/retrofit"

    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked

            when (view.getId()) {
                R.id.radio_glide ->
                    if (checked) {
                        URL = URL_glide
                        fileName = resources.getString(R.string.radio_glide)
                    }
                R.id.radio_loadApp ->
                    if (checked) {
                        URL = URL_loadApp
                        fileName = resources.getString(R.string.radio_loadApp)
                    }
                R.id.radio_retrofit ->
                    if (checked) {
                        URL = URL_retrofit
                        fileName = resources.getString(R.string.radio_retrofit)

                    }
            }
        }
    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId, channelName, NotificationManager.IMPORTANCE_HIGH
            ).apply {setShowBadge(false)}


            notificationChannel.description = resources.getString(R.string.notification_channel_description)

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

}
