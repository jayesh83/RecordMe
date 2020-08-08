package com.japps.recordme

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.MediaRecorder
import android.os.Build
import android.util.Log
import java.io.IOException

open class RecorderService(name: String? = RecorderService::class.simpleName) :
    IntentService(name) {
    private lateinit var mediaRecorder: MediaRecorder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mediaRecorder = MediaRecorder()
        val outputFile = "${externalCacheDir?.absolutePath}/audiorecordtest.3gp"
        Log.e("Service", "output -> $outputFile")
        mediaRecorder.apply {
            setAudioSource(MediaRecorder.AudioSource.VOICE_CALL)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB)
            setOutputFile(outputFile)
        }

        try {
            mediaRecorder.prepare()
            Thread.sleep(1000)
            mediaRecorder.start()
        } catch (ise: IllegalStateException) {
            Log.e("Service", "Exception -> $ise")
        } catch (ioe: IOException) {
            Log.e("Service", "Exception -> $ioe")
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
            val notification = Notification.Builder(this, "default_channel")
                .setContentTitle("Using resource")
                .setContentText("RecordMe is using GPS in the background")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .build()
            startForeground(10, notification)
        }
        return Service.START_STICKY
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = getString(R.string.app_name)
            val importance = NotificationManager.IMPORTANCE_LOW
//            val channelId: String = CorUtility.Companion.getNotificationChannel()
            val channelId = "default_channel"
            val channel = NotificationChannel(channelId, name, importance)
            channel.enableLights(false)
            channel.setSound(null, null)
            channel.setShowBadge(false)
//            channel.description = SyncStateContract.Constants.NOTIFICATION_DESC
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaRecorder.stop();
        mediaRecorder.release();
        Log.e("Service", "Destroyed")
    }

    override fun onHandleIntent(intent: Intent?) {
        Log.e("Service", "OnHandleIntent")
    }

}