package com.japps.recordme

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

private const val LOG_TAG = "AudioRecordTest"
private const val REQUEST_RECORD_AUDIO_PERMISSION = 200

class MainActivity : AppCompatActivity() {

    // Requesting permission to RECORD_AUDIO
    private var permissionToRecordAccepted = false
    private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.e(LOG_TAG, "Coming")
        permissionToRecordAccepted = if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        } else {
            false
        }
        if (!permissionToRecordAccepted) {
            // show reason
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val showReason =
                    shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO)
                if (showReason) {
                    // use checked " do not ask again "
                    Toast.makeText(
                        applicationContext,
                        "In order to record voice, we need your permission",
                        Toast.LENGTH_SHORT
                    ).show()
                    askRecordPermission()
                } else {
                    askRecordPermission()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        val permission = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.RECORD_AUDIO
        )

        if (permission != PackageManager.PERMISSION_GRANTED)
            askRecordPermission()

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    private fun askRecordPermission() {
        ActivityCompat.requestPermissions(
            this@MainActivity,
            permissions,
            REQUEST_RECORD_AUDIO_PERMISSION
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}