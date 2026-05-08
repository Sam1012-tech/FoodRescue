// bite_app/app/src/main/java/com/foodRescue/ui/donor/DonorHomeScreen.kt
package com.foodRescue.ui.donor

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DonorHomeScreen(onNavigateToPost: () -> Unit) {
    Scaffold(
        topBar = { 
            CenterAlignedTopAppBar(
                title = { Text("AnnaSetu Donor") },
                actions = { IconButton(onClick = {}) { Icon(Icons.Default.Notifications, null) } }
            ) 
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNavigateToPost,
                icon = { Icon(Icons.Default.Add, null) },
                text = { Text("Post Surplus Food") },
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp)) {
            item {
                Text("Your Active Donations", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            // MOCK DONATIONS
            item {
                MockDonationCard(
                    title = "45 Meals - Veg Fried Rice",
                    status = "Matched with Akshaya Patra",
                    time = "Posted 20 mins ago",
                    color = Color(0xFF4CAF50),
                    image = "https://images.unsplash.com/photo-1512058560366-1a51042f5170?q=80&w=300&auto=format&fit=crop"
                )
            }
            item {
                MockDonationCard(
                    title = "15kg Fresh Vegetables",
                    status = "Volunteer En Route",
                    time = "Posted 1 hour ago",
                    color = Color(0xFFFF9800),
                    image = "https://images.unsplash.com/photo-1540420773420-3366772f4999?q=80&w=300&auto=format&fit=crop"
                )
            }
        }
    }
}

@Composable
fun MockDonationCard(title: String, status: String, time: String, color: Color, image: String) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), elevation = CardDefaults.cardElevation(2.dp)) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(model = image, contentDescription = null, modifier = Modifier.size(80.dp).padding(4.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(title, style = MaterialTheme.typography.titleMedium)
                Surface(color = color.copy(alpha = 0.1f), shape = MaterialTheme.shapes.small) {
                    Text(status, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), color = color, style = MaterialTheme.typography.labelMedium)
                }
                Text(time, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
        }
    }
}
