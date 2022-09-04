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
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    private var url:String?=null
    private var fileName:String?=null
    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        custom_button.setOnClickListener {

            if (rb_udacity.isChecked){
                custom_button.buttonState=ButtonState.Loading
                url= UDACITY_URL
                fileName=rb_udacity.text.toString()
            }else if(rb_glide.isChecked){
                custom_button.buttonState=ButtonState.Loading
                url= GLIDE_URL
                fileName=rb_glide.text.toString()
            }else if(rb_retrofit.isChecked){
                custom_button.buttonState=ButtonState.Loading
                url= RETROFIT_URL
                fileName=rb_retrofit.text.toString()
            }
            else{
                Toast.makeText(this, "please select option first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            download()


        }

    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val query=DownloadManager.Query()
            query.setFilterById(downloadID)
            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            val cursor=downloadManager.query(query)
            cursor.moveToFirst()
            if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL){
                sendNotification("success")
                custom_button.buttonState=ButtonState.Completed
            }else if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_FAILED){
                sendNotification("failed")
                custom_button.buttonState=ButtonState.Completed
            }
            cursor.close()
        }
    }

    private fun download() {
        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.

        notificationManager=ContextCompat.getSystemService(
            applicationContext,
            NotificationManager::class.java
        ) as NotificationManager
        createChannel(CHANNEL_ID, CHANNEL_NAME)
    }

    private fun sendNotification(status:String) {
        val contentIntent= Intent(
            applicationContext,
            DetailActivity::class.java
        )

        contentIntent.putExtra("fileName",fileName)
        contentIntent.putExtra("status",status)

         pendingIntent= PendingIntent.getActivity(
            applicationContext,
            NOTIFICATION_ID,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        notificationManager.sendNotification(
            CHANNEL_ID,
            "download $status",
            applicationContext,
            NOTIFICATION_ID,
            pendingIntent
        )
    }

    companion object {
        private const val GLIDE_URL="https://github.com/bumptech/glide"
        private const val UDACITY_URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val RETROFIT_URL="https://github.com/square/retrofit"
        private const val CHANNEL_ID = "channelId"
        private const val CHANNEL_NAME="channel"
        private const val NOTIFICATION_ID = 0
    }

    private fun createChannel(channelId: String, channelName: String) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                setShowBadge(false)
            }
            notificationChannel.enableLights(true)
            notificationChannel.lightColor=Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description= "Loading App"
            val notificationManager = this.getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }

    }

}
