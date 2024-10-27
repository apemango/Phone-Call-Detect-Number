package com.example.phonecalltracker.recevier

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.telephony.TelephonyManager
import android.widget.Toast
import com.example.phonecalltracker.accessbility.WhatsAppAccessibilityService
import com.example.phonecalltracker.local.RememberPrefManager

const val incoming_number = "incoming_number"

class MyReceiver : BroadcastReceiver() {

    lateinit var rememberPrefManager: RememberPrefManager

    override fun onReceive(context: Context, intent: Intent) {
        rememberPrefManager = RememberPrefManager(context)
        if (intent.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
            val incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

            when (state) {
                TelephonyManager.EXTRA_STATE_IDLE -> {
                    Toast.makeText(context, "Call ended or idle state.", Toast.LENGTH_LONG).show()
                }

                TelephonyManager.EXTRA_STATE_RINGING -> {
                    // Notify about the incoming call
                    Toast.makeText(
                        context,
                        "Incoming call from: $incomingNumber",
                        Toast.LENGTH_LONG
                    ).show()

                    // Send the number to a specific WhatsApp contact
                    if (!incomingNumber.isNullOrEmpty()) {
                        rememberPrefManager.savePhoneNumber(incomingNumber)
                        sendToWhatsApp(context, incomingNumber)
                    }
                }

                TelephonyManager.EXTRA_STATE_OFFHOOK -> {
                    Toast.makeText(context, "Call is active.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun sendToWhatsApp(context: Context, number: String) {
        /* // Start the accessibility service to send the message
         val serviceIntent = Intent(context, WhatsAppAccessibilityService::class.java)
         context.startService(serviceIntent)*/
        val serviceIntent = Intent(context, WhatsAppAccessibilityService::class.java).apply {
            putExtra(incoming_number, number) // Pass the incoming number as an extra
        }
        context.startService(serviceIntent)

    }
}