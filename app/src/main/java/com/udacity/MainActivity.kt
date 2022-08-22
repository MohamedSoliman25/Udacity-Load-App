package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.udacity.Constants.GLIDE_URL
import com.udacity.Constants.LOAD_APP_URL
import com.udacity.Constants.RETROFIT_URL
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var glideId:Long = 0
    private var loadId:Long = 0
    private var retrofitId:Long = 0
    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action
    private lateinit var downloadManager: DownloadManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager

        createChannel(
                getString(R.string.notification_channel_id),
                getString(R.string.notification_title)
        )

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        custom_button.setOnClickListener {

            if(radio_group.checkedRadioButtonId != -1)
                custom_button.setLoadingState(ButtonState.Clicked)
            when(radio_group.checkedRadioButtonId){

                R.id.glide-> download(GLIDE_URL)
                R.id.load_app-> download(LOAD_APP_URL)
                R.id.retofit-> download(RETROFIT_URL)
                else ->Toast.makeText(this,"Please Select File !",Toast.LENGTH_LONG).show()
            }
//            download()
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            val cursor = downloadManager.query(DownloadManager.Query().setFilterById(id!!))
            if (cursor.moveToNext()) {
                val downloadStatus = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                val notificationManager =
                        ContextCompat.getSystemService(context!!, NotificationManager::class.java)
                cursor.close()
                when(downloadStatus){
                    DownloadManager.STATUS_SUCCESSFUL -> {
                        notificationManager?.sendNotification(
                                context, context.getString(R.string.notification_description),context.getString(
                                        when (id) {
                                            glideId -> R.string.glide
                                            loadId -> R.string.loadapp
                                            else -> R.string.retrofit
                                        }
                                ),
                                "Success"
                        )
                    }
                    DownloadManager.STATUS_FAILED ->{
                        notificationManager?.sendNotification(context, context.getString(R.string.notification_description),
                                context.getString(
                                        when (id) {
                                            glideId -> R.string.glide
                                            loadId -> R.string.loadapp
                                            else -> R.string.retrofit
                                        }
                                ),
                                "Failed"
                        )

                    }
                }


              }
        }
    }

    private fun download(urlFile:String) {
        val request =
            DownloadManager.Request(Uri.parse(urlFile))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

       // enqueue puts the download request in the queue.
        when(urlFile){

            GLIDE_URL-> glideId = downloadManager.enqueue(request)
            LOAD_APP_URL-> loadId =  downloadManager.enqueue(request)
            RETROFIT_URL-> retrofitId = downloadManager.enqueue(request)
        }
    }

    private fun createChannel(channelId: String, channelName: String) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_HIGH
            ).apply {
                setShowBadge(false)
            }

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.MAGENTA
            notificationChannel.enableVibration(true)
            notificationChannel.description = applicationContext.getString(R.string.notification_description)

            notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

//    companion object {
//        private const val URL =
//            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
//        private const val CHANNEL_ID = "channelId"
//    }

}
