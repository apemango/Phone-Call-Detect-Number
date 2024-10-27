package com.example.phonecalltracker.recevier

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.widget.Toast

class MyReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
            val incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

            when (state) {
                TelephonyManager.EXTRA_STATE_IDLE -> {
                    Toast.makeText(context, "Call ended or idle state.", Toast.LENGTH_LONG).show()
                }
                TelephonyManager.EXTRA_STATE_RINGING -> {
                    Toast.makeText(context, "Incoming call from: $incomingNumber", Toast.LENGTH_LONG).show()
                }
                TelephonyManager.EXTRA_STATE_OFFHOOK -> {
                    Toast.makeText(context, "Call is active.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}