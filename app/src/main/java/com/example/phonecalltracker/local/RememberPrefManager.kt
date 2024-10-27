package com.example.phonecalltracker.local

import android.content.Context
import android.content.SharedPreferences

class RememberPrefManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

    fun saveBoolean(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }


    // Save a phone number with a timestamp
    fun savePhoneNumber(phoneNumber: String) {
        val currentTime = System.currentTimeMillis()
        val phoneMap = getPhoneNumberMap().toMutableMap() // Retrieve existing numbers and timestamps
        phoneMap[phoneNumber] = currentTime // Add or update the number with the current timestamp
        savePhoneNumberMap(phoneMap) // Save the updated map
    }

    // Retrieve the map of phone numbers and their timestamps
    fun getPhoneNumberMap(): Map<String, Long> {
        val phoneNumbers = sharedPreferences.getStringSet("phone_numbers", emptySet()) ?: emptySet()
        val timestamps = sharedPreferences.getStringSet("phone_timestamps", emptySet()) ?: emptySet()

        return phoneNumbers.associateWith { number ->
            timestamps.find { it.startsWith(number) }?.substringAfter(":")?.toLong() ?: 0L
        }
    }

    // Save the map of phone numbers and their timestamps
    private fun savePhoneNumberMap(phoneMap: Map<String, Long>) {
        val phoneNumbersSet = phoneMap.keys.toSet()
        val timestampsSet = phoneMap.map { "${it.key}:${it.value}" }.toSet()

        sharedPreferences.edit()
            .putStringSet("phone_numbers", phoneNumbersSet)
            .putStringSet("phone_timestamps", timestampsSet)
            .apply()
    }

    // Remove a specific phone number
    fun removePhoneNumber(phoneNumber: String) {
        val phoneMap = getPhoneNumberMap().toMutableMap()
        phoneMap.remove(phoneNumber) // Remove the specified number
        savePhoneNumberMap(phoneMap) // Save the updated map
    }

    // Retrieve all saved phone numbers as a list of pairs (phone number, timestamp)
    fun getAllSavedPhoneNumbers(): List<Pair<String, Long>> {
        return getPhoneNumberMap().map { Pair(it.key, it.value) }
    }

}