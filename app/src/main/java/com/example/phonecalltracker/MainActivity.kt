package com.example.phonecalltracker

import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.phonecalltracker.recevier.MyReceiver
import com.example.phonecalltracker.ui.theme.PhoneCallTrackerTheme


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PhoneCallTrackerTheme {
                checkPermissions(this)
                // Registering the receiver with correct intent filter
                val receiver = MyReceiver()
                val intentFilter = IntentFilter("android.intent.action.PHONE_STATE")
                registerReceiver(receiver, intentFilter)

                Column(modifier = Modifier.fillMaxSize()) {
                    Text("Listening for phone calls...")
                }
            }
        }
    }
}

private fun checkPermissions(context: ComponentActivity) {
    /*if (ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.READ_PHONE_STATE
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        ActivityCompat.requestPermissions(
            context,
            arrayOf(android.Manifest.permission.READ_PHONE_STATE),
            200
        )
    }*/

    /*READ_CALL_LOG
    READ_PHONE_STATE
    */
    if (ContextCompat.checkSelfPermission(context,android.Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(context, arrayOf(android.Manifest.permission.READ_CALL_LOG),200)
    }
    if (ContextCompat.checkSelfPermission(context,android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(context, arrayOf(android.Manifest.permission.READ_PHONE_STATE), 201)
    }
}