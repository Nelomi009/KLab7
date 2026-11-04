package com.example.activitylifecycle

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

class MainActivity : ComponentActivity() {

    private val TAG = "LifecycleLog"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate() вызван: Activity создается")

        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("Посмотрите в Logcat")
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() вызван: Activity становится видимой")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() вызван: Activity в фокусе, можно взаимодействовать")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() вызван: Activity теряет фокус (пауза)")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() вызван: Activity больше не видна")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() вызван: Activity уничтожается")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart() вызван: Activity возвращается на передний план")
    }
}