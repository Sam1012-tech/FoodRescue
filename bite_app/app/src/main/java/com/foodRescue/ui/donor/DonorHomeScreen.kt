// bite_app/app/src/main/java/com/foodRescue/ui/donor/DonorHomeScreen.kt
package com.foodRescue.ui.donor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.foodRescue.ui.shared.components.GlassCard
import com.foodRescue.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DonorHomeScreen(onNavigateToPost: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize().background(DarkBg)) {
        LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp)) {
            item {
                Spacer(modifier = Modifier.height(64.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text("Good Evening,", color = Color.White.copy(alpha = 0.6f))
                        Text("Organizer Sam", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold, color = Color.White))
                    }
                    IconButton(onClick = {}, modifier = Modifier.background(GlassWhite, CircleShape)) {
                        Icon(Icons.Default.Notifications, null, tint = Color.White)
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }

            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    StatCard("Meals Saved", "1,240", Modifier.weight(1f))
                    StatCard("CO2 Offset", "45kg", Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(32.dp))
            }

            item {
                Text("Active Rescue Missions", style = MaterialTheme.typography.titleLarge, color = Color.White)
                Spacer(modifier = Modifier.height(16.dp))
                MissionCard("Google Cafeteria", "In Transit", "ETA 8 mins", NeonGreen)
                Spacer(modifier = Modifier.height(16.dp))
                MissionCard("Manyata Tech Park", "Analyzing...", "Awaiting AI", WarningYellow)
                Spacer(modifier = Modifier.height(100.dp))
            }
        }

        // Floating Action Button - Futuristic
        ExtendedFloatingActionButton(
            onClick = onNavigateToPost,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 32.dp).fillMaxWidth(0.8f).height(64.dp),
            containerColor = NeonGreen,
            contentColor = Color.Black,
            shape = androidx.compose.foundation.shape.RoundedCornerShape(32.dp)
        ) {
            Icon(Icons.Default.Add, null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("START NEW RESCUE", fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
        }
    }
}

@Composable
fun StatCard(label: String, value: String, modifier: Modifier) {
    GlassCard(modifier = modifier) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.5f))
        Text(value, style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, color = NeonGreen))
    }
}

@Composable
fun MissionCard(name: String, status: String, eta: String, color: Color) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text(name, style = MaterialTheme.typography.titleMedium, color = Color.White)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(8.dp).background(color, CircleShape))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(status, color = color, style = MaterialTheme.typography.labelMedium)
                }
            }
            Text(eta, style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, color = Color.White.copy(alpha = 0.3f)))
        }
    }
}
