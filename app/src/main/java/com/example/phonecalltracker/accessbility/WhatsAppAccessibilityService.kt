package com.example.phonecalltracker.accessbility

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.example.phonecalltracker.recevier.incoming_number

class WhatsAppAccessibilityService : AccessibilityService() {
    private var incomingNumber: String? = null

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Handle accessibility events if needed
        if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            // Attempt to get the root node here
            val rootNode = getRootInActiveWindow()
            // Proceed with your logic

            Log.e("TAG", "onAccessibilityEvent: "+rootNode )
        }
    }

    override fun onInterrupt() {
        // Handle interruptions
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        incomingNumber = intent?.getStringExtra(incoming_number) // Retrieve the number from the intent
        incomingNumber?.let { sendMessageToWhatsApp(it) } // Send the message if number is available
        return super.onStartCommand(intent, flags, startId)
    }
    fun sendMessageToWhatsApp(number: String) {
        // Construct the message
        val message = "Incoming call from: $number"
        Log.e("WHATS ACCESSIBILITY ---- > ", "sendMessageToWhatsApp: $message")

        Handler(Looper.getMainLooper()).postDelayed({
            val rootNode = getRootInActiveWindow()
            // Proceed with your logic
        }, 100) // Delay for 100 milliseconds

        // Get the root node of the active window
        val rootNode = rootInActiveWindow ?: run {
            Log.e("WHATS ACCESSIBILITY", "Root node is null")
            return
        }

        // Find the message input field
        val messageField = rootNode.findAccessibilityNodeInfosByViewId("com.whatsapp:id/entry").firstOrNull()
        if (messageField == null) {
            Log.e("WHATS ACCESSIBILITY", "Message field not found")
            return
        } else {
            Log.e("WHATS ACCESSIBILITY", "Message field found")
        }

        // Focus on the message input field
        if (!messageField.performAction(AccessibilityNodeInfo.ACTION_FOCUS)) {
            Log.e("WHATS ACCESSIBILITY", "Failed to focus on message field")
            return
        }

        // Set the text in the message field
        val arguments = Bundle().apply {
            putCharSequence(AccessibilityNodeInfo.ACTION_SET_TEXT.toString(), message)
        }

        if (!messageField.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments)) {
            Log.e("WHATS ACCESSIBILITY", "Failed to set text in message field")
            return
        }

        // Find and click the send button
        val sendButton = rootNode.findAccessibilityNodeInfosByViewId("com.whatsapp:id/send").firstOrNull()
        if (sendButton == null) {
            Log.e("WHATS ACCESSIBILITY", "Send button not found")
            return
        } else {
            Log.e("WHATS ACCESSIBILITY", "Send button found")
        }

        // Perform click action on the send button
        if (!sendButton.performAction(AccessibilityNodeInfo.ACTION_CLICK)) {
            Log.e("WHATS ACCESSIBILITY", "Failed to click send button")
        } else {
            Log.e("WHATS ACCESSIBILITY", "Message sent successfully")
        }
    }
}