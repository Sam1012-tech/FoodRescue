// bite_app/app/src/main/java/com/foodRescue/ui/volunteer/VolunteerHomeScreen.kt
package com.foodRescue.ui.volunteer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
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
fun VolunteerHomeScreen() {
    Box(modifier = Modifier.fillMaxSize().background(AIDeepBlue)) {
        LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp)) {
            item {
                Spacer(modifier = Modifier.height(64.dp))
                Text("Rescue Personnel", color = Color.White.copy(alpha = 0.6f))
                Text("Arjun Kumar", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold, color = Color.White))
                Spacer(modifier = Modifier.height(32.dp))
            }

            item {
                Text("URGENT MISSION BROADCAST", style = MaterialTheme.typography.labelSmall, color = UrgencyRed)
                Spacer(modifier = Modifier.height(12.dp))
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(48.dp).background(UrgencyRed.copy(alpha = 0.1f), CircleShape), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.LocationOn, null, tint = UrgencyRed)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text("Manyata Tech Park", style = MaterialTheme.typography.titleMedium, color = Color.White)
                            Text("50 Meals • Pickup Urgency: HIGH", color = UrgencyRed, style = MaterialTheme.typography.labelSmall)
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(onClick = {}, modifier = Modifier.fillMaxWidth().height(56.dp), colors = ButtonDefaults.buttonColors(containerColor = UrgencyRed)) {
                        Text("ACCEPT MISSION", fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }

            item {
                Text("Performance Stats", style = MaterialTheme.typography.titleLarge, color = Color.White)
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    VolunteerStat("Reliability", "98%")
                    VolunteerStat("Impact", "4.2k")
                }
            }
        }
    }
}

@Composable
fun VolunteerStat(label: String, value: String) {
    GlassCard(modifier = Modifier.width(150.dp)) {
        Text(label, color = Color.White.copy(alpha = 0.5f), style = MaterialTheme.typography.labelSmall)
        Text(value, color = NeonGreen, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
    }
}
