package com.example.phonecalltracker.local

import android.content.Context
import android.content.SharedPreferences
import com.example.phonecalltracker.recevier.CallType
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RememberPrefManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

    fun saveBoolean(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    // Data class to represent a phone call entry
    data class PhoneCallEntry(val phoneNumber: String, val callType: CallType, val timestamp: String)

    // Save a phone number with a timestamp
    fun savePhoneNumber(phoneNumber: String, callType: CallType) {
        val gson = Gson()

        // Retrieve existing entries
        val existingEntriesJson = sharedPreferences.getString("call_list", null)
        val type = object : TypeToken<MutableList<PhoneCallEntry>>() {}.type
        val callList: MutableList<PhoneCallEntry> = if (existingEntriesJson != null) {
            gson.fromJson(existingEntriesJson, type)
        } else {
            mutableListOf()
        }

        // Add new entry
        val currentTime = System.currentTimeMillis()
        val newEntry = PhoneCallEntry(phoneNumber, callType, formatTimestamp(currentTime))
        callList.add(newEntry)

        // Save updated list back to SharedPreferences
        val updatedEntriesJson = gson.toJson(callList)
        sharedPreferences.edit().putString("call_list", updatedEntriesJson).apply()

    }

    // Retrieve all saved phone numbers as a list of pairs (phone number, timestamp)
    fun getAllSavedPhoneNumbers(): List<PhoneCallEntry> {
        val gson = Gson()

        // Retrieve existing entries
        val existingEntriesJson = sharedPreferences.getString("call_list", null)
        val type = object : TypeToken<List<PhoneCallEntry>>() {}.type

        return if (existingEntriesJson != null) {
            gson.fromJson(existingEntriesJson, type) ?: emptyList()
        } else {
            emptyList()
        }
    }

    // Format timestamp to a human-readable date string
    fun formatTimestamp(timestamp: Long): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return dateFormat.format(Date(timestamp))
    }

}