package com.kvs.healthconnectreporter.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kvs.healthconnectreporter.HealthConnectReporter

/** Shows whether Health Connect is available; the demo rows follow with the services. */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val availability = HealthConnectReporter.availability(this)
        setContent {
            MaterialTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
                    Text(
                        text = "Health Connect: $availability",
                        modifier = Modifier.padding(padding).padding(16.dp),
                    )
                }
            }
        }
    }
}
