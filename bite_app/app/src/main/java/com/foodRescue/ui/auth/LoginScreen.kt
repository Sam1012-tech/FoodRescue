// bite_app/app/src/main/java/com/foodRescue/ui/auth/LoginScreen.kt
package com.foodRescue.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(onNavigateToHome: (String) -> Unit) {
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    var statusMsg by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("AnnaSetu", style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.primary)
        Text("Food Rescue Platform", style = MaterialTheme.typography.bodyMedium)
        
        Spacer(modifier = Modifier.height(48.dp))

        // DEMO MODE BUTTON - PRIMARY ACTION
        Button(
            onClick = { onNavigateToHome("donor") },
            modifier = Modifier.fillMaxWidth().height(64.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
        ) {
            Text("🚀 Launch Demo Mode (Skip Login)", style = MaterialTheme.typography.titleMedium)
        }

        Spacer(modifier = Modifier.height(32.dp))
        Text("OR LOGIN (Requires Firebase)", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        Spacer(modifier = Modifier.height(16.dp))

        LoginButton("Donor Login", "donor@annasetu.com") { onNavigateToHome("donor") }
        Spacer(modifier = Modifier.height(8.dp))
        LoginButton("NGO Login", "ngo@annasetu.com") { onNavigateToHome("ngo") }
        Spacer(modifier = Modifier.height(8.dp))
        LoginButton("Volunteer Login", "volunteer@annasetu.com") { onNavigateToHome("volunteer") }

        statusMsg?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(it, color = Color.Red, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun LoginButton(text: String, email: String, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(56.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text)
            Text(email, style = MaterialTheme.typography.labelSmall)
        }
    }
}
