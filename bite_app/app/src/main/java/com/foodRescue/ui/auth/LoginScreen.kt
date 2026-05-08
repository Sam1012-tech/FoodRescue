// bite_app/app/src/main/java/com/foodRescue/ui/auth/LoginScreen.kt
package com.foodRescue.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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

@Composable
fun LoginScreen(onNavigateToHome: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(AIDeepBlue, DarkBg)
                )
            )
    ) {
        // Background Glow Decor
        Box(
            modifier = Modifier
                .size(300.dp)
                .offset(x = (-100).dp, y = (-50).dp)
                .background(NeonGreen.copy(alpha = 0.1f), CircleShape)
        )

        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "RescueBite AI",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    color = Color.White
                )
            )
            Text(
                "Real-time Urban Food Infrastructure",
                style = MaterialTheme.typography.bodySmall,
                color = NeonGreen.copy(alpha = 0.8f)
            )
            
            Spacer(modifier = Modifier.height(64.dp))

            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Text(
                    "Select Your Infrastructure Role",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(24.dp))

                RoleButton("ORGANIZER", "Manage surplus & AI Analysis") { onNavigateToHome("donor") }
                Spacer(modifier = Modifier.height(16.dp))
                RoleButton("NGO ENTITY", "Live matching & logistics") { onNavigateToHome("ngo") }
                Spacer(modifier = Modifier.height(16.dp))
                RoleButton("RESCUE VOLUNTEER", "Mission execution & navigation") { onNavigateToHome("volunteer") }
            }
        }
    }
}

@Composable
fun RoleButton(title: String, subtitle: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(80.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.05f)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()) {
            Text(title, style = MaterialTheme.typography.titleMedium, color = NeonGreen)
            Text(subtitle, style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.5f))
        }
    }
}

// Re-defining RoundedCornerShape locally to avoid import confusion if not present
private fun RoundedCornerShape(size: androidx.compose.ui.unit.Dp) = androidx.compose.foundation.shape.RoundedCornerShape(size)
