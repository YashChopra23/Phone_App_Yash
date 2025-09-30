package com.fossify.phone.services

import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.IBinder
import android.util.Log
import org.json.JSONObject

class AgenticService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val toolName = intent?.getStringExtra("toolName")
        val params = intent?.getStringExtra("parameters") // JSON string

        Log.d("AgenticService", "Received tool: $toolName with params: $params")

        when (toolName) {
            "phone.makeCall" -> makeCall(params)
            "phone.endCall" -> endCall()
            else -> Log.w("AgenticService", "Unknown tool: $toolName")
        }

        return START_STICKY
    }

    private fun makeCall(params: String?) {
        try {
            val json = JSONObject(params ?: "{}")
            val phoneNumber = json.optString("phoneNumber")

            if (phoneNumber.isNotEmpty()) {
                val callIntent = Intent(Intent.ACTION_CALL).apply {
                    data = Uri.parse("tel:$phoneNumber")
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                startActivity(callIntent)
                Log.d("AgenticService", "Calling $phoneNumber")
            } else {
                Log.e("AgenticService", "No phone number provided")
            }
        } catch (e: Exception) {
            Log.e("AgenticService", "Failed to parse call params", e)
        }
    }

    private fun endCall() {
        // Fossify Phone may already have utility classes to end calls.
        // For now, just log. Later hook into TelecomManager if available.
        Log.d("AgenticService", "Ending call (stub)")
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
