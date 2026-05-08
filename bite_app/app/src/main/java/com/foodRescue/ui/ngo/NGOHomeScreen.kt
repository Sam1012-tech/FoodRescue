// bite_app/app/src/main/java/com/foodRescue/ui/ngo/NGOHomeScreen.kt
package com.foodRescue.ui.ngo

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.foodRescue.ui.shared.components.GlassCard
import com.foodRescue.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NGOHomeScreen() {
    Box(modifier = Modifier.fillMaxSize().background(DarkBg)) {
        LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp)) {
            item {
                Spacer(modifier = Modifier.height(64.dp))
                Text("NGO Infrastructure", color = Color.White.copy(alpha = 0.6f))
                Text("Hope Shelter Hub", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold, color = Color.White))
                Spacer(modifier = Modifier.height(32.dp))
            }

            // PREDICTIVE ALERT - FUTURISTIC
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth().border(1.dp, NeonGreen.copy(alpha = 0.3f), RoundedCornerShape(24.dp))
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Info, null, tint = NeonGreen)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("PREDICTIVE LOGISTICS ALERT", color = NeonGreen, style = MaterialTheme.typography.labelSmall)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "60-80 meals likely from nearby office event (ETA 1h). Suggested: Reduce dinner prep by 40 meals.",
                        color = Color.White.copy(alpha = 0.8f),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
            }

            item {
                Text("Incoming Rescue Operations", style = MaterialTheme.typography.titleLarge, color = Color.White)
                Spacer(modifier = Modifier.height(16.dp))
                IncomingCard("Google Cafeteria", "48 Meals", "8 mins", NeonGreen)
                Spacer(modifier = Modifier.height(16.dp))
                IncomingCard("Manyata Tech Park", "120 Meals", "24 mins", WarningYellow)
            }
        }
    }
}

@Composable
fun IncomingCard(donor: String, quantity: String, eta: String, color: Color) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text(donor, style = MaterialTheme.typography.titleMedium, color = Color.White)
                Text(quantity, color = color, fontWeight = FontWeight.Bold)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(eta, style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, color = Color.White))
                Text("ETA", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.3f))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Button(onClick = {}, modifier = Modifier.weight(1f).height(40.dp), colors = ButtonDefaults.buttonColors(containerColor = NeonGreen)) {
                Text("ACCEPT", color = Color.Black, style = MaterialTheme.typography.labelLarge)
            }
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedButton(onClick = {}, modifier = Modifier.weight(1f).height(40.dp)) {
                Text("REJECT", color = Color.White)
            }
        }
    }
}
