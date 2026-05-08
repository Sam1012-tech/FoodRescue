// bite_app/app/src/main/java/com/foodRescue/ui/volunteer/VolunteerHomeScreen.kt
package com.foodRescue.ui.volunteer

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VolunteerHomeScreen() {
    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("Volunteer Mission") }) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Current Task: #4502", style = MaterialTheme.typography.titleLarge)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Pickup: Microsoft Signature Building", style = MaterialTheme.typography.bodyLarge)
                    Text("Drop-off: Mother Teresa Orphanage", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(onClick = {}, modifier = Modifier.fillMaxWidth()) {
                        Text("Start Navigation")
                    }
                }
            }
        }
    }
}
