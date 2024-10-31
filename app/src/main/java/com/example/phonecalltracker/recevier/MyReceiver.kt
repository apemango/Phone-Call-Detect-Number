package com.example.phonecalltracker.recevier

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.widget.Toast
import com.example.phonecalltracker.accessbility.WhatsAppAccessibilityService
import com.example.phonecalltracker.local.RememberPrefManager

const val incoming_number = "incoming_number"

enum class CallType(val description: String) {
    INCOMING_CALL("Incoming Call"),
    OUTGOING_CALL("Outgoing Call"),
    MISSED_CALL("Missed Call");
    override fun toString(): String {
        return description
    }
}

data class CallRecord(val number: String, val type: CallType, val timestamp: Long)

class MyReceiver : BroadcastReceiver() {

    lateinit var rememberPrefManager: RememberPrefManager

    // List to store call records
    private val callRecords = mutableListOf<CallRecord>()

    override fun onReceive(context: Context, intent: Intent) {
        rememberPrefManager = RememberPrefManager(context)
        if (intent.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
            val incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

            when (state) {
                TelephonyManager.EXTRA_STATE_IDLE -> {
                    incomingNumber?.let {
                        savePhoneNumber(it, CallType.MISSED_CALL)
                    }
                    Toast.makeText(context, "Call ended or idle state.", Toast.LENGTH_LONG).show()
                }

                TelephonyManager.EXTRA_STATE_RINGING -> {
                    incomingNumber?.let {
                        savePhoneNumber(it, CallType.INCOMING_CALL)
                        Toast.makeText(context, "Incoming call from: $it", Toast.LENGTH_LONG).show()
                        sendToWhatsApp(context, it)
                    }
                }

                TelephonyManager.EXTRA_STATE_OFFHOOK -> {
                    // Outgoing call detected
                    incomingNumber?.let {
                        savePhoneNumber(it, CallType.OUTGOING_CALL)
                        Toast.makeText(context, "Outgoing call to: $it", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun savePhoneNumber(number: String, callType: CallType) {
        val currentTime = System.currentTimeMillis()
        val duplicateThreshold = 60 * 1000 // 1 minute in milliseconds

        // Check if the number with the same call type already exists within the threshold
        if (!callRecords.any {
                it.number == number &&
                        it.type == callType &&
                        (currentTime - it.timestamp) < duplicateThreshold
            }) {
            // Save new record
            callRecords.add(CallRecord(number, callType, currentTime))
            rememberPrefManager.savePhoneNumber(number, callType) // Save to preferences
        }
    }

    private fun sendToWhatsApp(context: Context, number: String) {
        val serviceIntent = Intent(context, WhatsAppAccessibilityService::class.java).apply {
            putExtra(incoming_number, number) // Pass the incoming number as an extra
        }
        context.startService(serviceIntent)
    }
}






/*
class MyReceiver : BroadcastReceiver() {

    lateinit var rememberPrefManager: RememberPrefManager

    // List to store call records
    private val callRecords = mutableListOf<CallRecord>()


    override fun onReceive(context: Context, intent: Intent) {
        rememberPrefManager = RememberPrefManager(context)
        if (intent.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
            val incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

            when (state) {
                TelephonyManager.EXTRA_STATE_IDLE -> {
                    incomingNumber?.let {
                        savePhoneNumber(it, CallType.MISSED_CALL)
                    }
                    Toast.makeText(context, "Call ended or idle state.", Toast.LENGTH_LONG).show()
                }

                TelephonyManager.EXTRA_STATE_RINGING -> {
                    incomingNumber?.let {
                        savePhoneNumber(it, CallType.INCOMING_CALL)
                        Toast.makeText(context, "Incoming call from: $it", Toast.LENGTH_LONG).show()
                        sendToWhatsApp(context, it)
                    }
                }

                TelephonyManager.EXTRA_STATE_OFFHOOK -> {
                    // Outgoing call detected
                    incomingNumber?.let {
                        savePhoneNumber(it, CallType.OUTGOING_CALL)
                        Toast.makeText(context, "Outgoing call to: $it", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun savePhoneNumber(number: String, callType: CallType) {
        // Save the phone number with its call type if it's not already in the list
        if (!callRecords.any { it.number == number && it.type == callType }) {
            callRecords.add(CallRecord(number, callType))
        }
        rememberPrefManager.savePhoneNumber(number,callType)

    }

    private fun sendToWhatsApp(context: Context, number: String) {
        val serviceIntent = Intent(context, WhatsAppAccessibilityService::class.java).apply {
            putExtra(incoming_number, number) // Pass the incoming number as an extra
        }
        context.startService(serviceIntent)

    }
}*/
