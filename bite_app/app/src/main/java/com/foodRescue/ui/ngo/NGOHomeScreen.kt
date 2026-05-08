// bite_app/app/src/main/java/com/foodRescue/ui/ngo/NGOHomeScreen.kt
package com.foodRescue.ui.ngo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NGOHomeScreen() {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Food Accepted") },
            text = { Text("A volunteer is being assigned for pickup from Google Cafeteria.") },
            confirmButton = { Button(onClick = { showDialog = false }) { Text("OK") } }
        )
    }

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("NGO Dashboard") }) }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp)) {
            item {
                Text("Available Surplus Nearby", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                MatchCard("Google Cafeteria", "50 Meals - Paneer Curry", "0.8 km away", onAccept = { showDialog = true })
            }
            item {
                MatchCard("Manyata Event Hall", "120 Meals - Biryani", "2.1 km away", onAccept = { showDialog = true })
            }
        }
    }
}

@Composable
fun MatchCard(source: String, food: String, distance: String, onAccept: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(source, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
            Text(food, style = MaterialTheme.typography.titleMedium)
            Text(distance, style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(12.dp))
            Row {
                Button(onClick = onAccept, modifier = Modifier.weight(1f)) { Text("Accept") }
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedButton(onClick = {}, modifier = Modifier.weight(1f)) { Text("Decline") }
            }
        }
    }
}
