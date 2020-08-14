package com.japps.recordme.services

import android.app.IntentService
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.japps.recordme.utils.ServiceStarter
import com.japps.recordme.notification.ServiceNotificationManager
import com.japps.recordme.receivers.CallReceiver

private val tag = CallReceiverService::class.java.simpleName
const val restartBroadcastAction = "com.japps.restartCallReceiverService"

class CallReceiverService : IntentService(CallReceiverService::class.java.simpleName) {
    private lateinit var callReceiverBroadcast: CallReceiver

    override fun onCreate() {
        Log.e(tag, "onCreate")
//        Log.e(tag, "CallReceiverService created at ${Calendar.getInstance().time}}")
        Toast.makeText(baseContext, "CallReceiverService Started", Toast.LENGTH_SHORT).show()
        registerCallReceiver()
        initializeNotification()
    }

    private fun initializeNotification() {
        val notification = ServiceNotificationManager(applicationContext).createNotification()
        startForeground(NOTIFICATION_ID_CALLRECEIVER_SERVICE, notification)
    }

    private fun registerCallReceiver() {
        isReceiverRegistered = true
        callReceiverBroadcast = CallReceiver()
        val intentFilter = IntentFilter("android.intent.action.PHONE_STATE")
        intentFilter.addAction("android.intent.action.NEW_OUTGOING_CALL")
        registerReceiver(callReceiverBroadcast, intentFilter)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!isReceiverRegistered)
            registerCallReceiver()
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onHandleIntent(intent: Intent?) {
        TODO("Not yet implemented")
    }

    private fun unregisterCallReceiverAndRestartReceiver() {
        unregisterCallReceiver()
        Intent(baseContext, CallReceiverServiceRestarter::class.java).let {
            it.action = restartBroadcastAction
            sendBroadcast(it)
            Toast.makeText(baseContext, "Destroyed", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        unregisterCallReceiverAndRestartReceiver()
    }

    override fun onDestroy() {
        unregisterCallReceiverAndRestartReceiver()
        super.onDestroy()
    }

//    override fun onDestroy() {
//        ServiceStarter.startCallReceiverService(applicationContext)
//        unregisterCallReceiver()
////        Log.e(tag, "CallReceiverService destroyed at ${Calendar.getInstance().time}}")
////        Toast.makeText(this, "CallReceiverService Stopped", Toast.LENGTH_SHORT).show()
////        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
////        val interval = System.currentTimeMillis() + 10000L
////        val intent = Intent(this, CallReceiverServiceRestarter::class.java)
////        val pendingIntent = PendingIntent.getBroadcast(baseContext, 0, intent, 0)
////        alarmManager.set(AlarmManager.RTC_WAKEUP, interval, pendingIntent)
//        // TODO: 8/11/2020 if service gets destroyed then set the alarmManager and start this service again after 5 seconds
//        super.onDestroy()
//    }

    private fun unregisterCallReceiver() {
        if (isReceiverRegistered){
            isReceiverRegistered = false
            unregisterReceiver(callReceiverBroadcast)
        }
    }

    companion object {
        private var isReceiverRegistered = false
        private const val NOTIFICATION_ID_CALLRECEIVER_SERVICE = 109
    }
}

class CallReceiverServiceRestarter : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (!intent?.action.equals(restartBroadcastAction))
            return
        val cntx = context ?: context?.applicationContext
        cntx?.also {
            ServiceStarter.startCallReceiverService(it)
            Toast.makeText(cntx, "Service Restarted", Toast.LENGTH_SHORT).show()
        }
    }
}