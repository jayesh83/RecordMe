package com.japps.recordme

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.content.ContextCompat

object ServiceStarter {
    fun startCallReceiverService(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            ContextCompat.startForegroundService(context, Intent(context, CallReceiverService::class.java))
        }else{
            context.startService(Intent(context, CallReceiverService::class.java))
        }
    }
}