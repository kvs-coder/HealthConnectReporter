package com.kvs.healthconnectreporter.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * The privacy policy Health Connect links to from its permission screen. A real app explains here how it uses
 * and protects health data; Google Play reviews this screen.
 */
class PermissionsRationaleActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Column(Modifier.fillMaxSize().systemBarsPadding().padding(24.dp)) {
                    Text("Privacy policy", style = MaterialTheme.typography.headlineSmall)
                    Text(
                        text =
                            "The HealthConnectReporter demo reads and writes Health Connect data only on this " +
                                "device to show what the library does. It sends no data anywhere.",
                        modifier = Modifier.padding(top = 16.dp),
                    )
                }
            }
        }
    }
}
