package com.example.phonecalltracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import com.example.phonecalltracker.local.RememberPrefManager

class YourSettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SettingsScreen()
        }
    }
}

@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val prefManager = remember { RememberPrefManager(context) }
// Use remember to create the MutableState object
    var isChecked by remember {
        mutableStateOf(
            prefManager.getBoolean(
                "notifications_enabled",
                false
            )
        )
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = { newValue ->
                isChecked = newValue
                // Save the new state to SharedPreferences
                prefManager.saveBoolean("notifications_enabled", newValue)

            }
        )
        Text(text = if (isChecked) "Notifications Enabled" else "Notifications Disabled")
    }
}