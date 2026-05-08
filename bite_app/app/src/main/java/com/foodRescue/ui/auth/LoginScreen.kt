// bite_app/app/src/main/java/com/foodRescue/ui/auth/LoginScreen.kt
package com.foodRescue.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color

@Composable
fun LoginScreen(onNavigateToHome: (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("AnnaSetu", style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.primary)
        Text("Food Rescue Platform", style = MaterialTheme.typography.bodyMedium)
        
        Spacer(modifier = Modifier.height(64.dp))

        // All buttons now bypass Firebase for the demo
        LoginButton("Login as Donor", "donor@annasetu.com") { onNavigateToHome("donor") }
        Spacer(modifier = Modifier.height(16.dp))
        LoginButton("Login as NGO", "ngo@annasetu.com") { onNavigateToHome("ngo") }
        Spacer(modifier = Modifier.height(16.dp))
        LoginButton("Login as Volunteer", "volunteer@annasetu.com") { onNavigateToHome("volunteer") }

        Spacer(modifier = Modifier.height(32.dp))
        Text("Demo Mode Active", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
    }
}

@Composable
fun LoginButton(text: String, email: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(64.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text, style = MaterialTheme.typography.titleMedium)
            Text(email, style = MaterialTheme.typography.labelSmall)
        }
    }
}
