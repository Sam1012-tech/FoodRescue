// bite_app/app/src/main/java/com.foodRescue/ui/auth/LoginScreen.kt
package com.foodRescue.ui.auth

import androidx.compose.foundation.layout.*
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
    var errorMsg by remember { mutableStateOf<String?>(null) }

    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("AnnaSetu", style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Food Redistribution Platform", style = MaterialTheme.typography.bodyMedium)
        
        Spacer(modifier = Modifier.height(48.dp))

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            LoginButton("Login as Donor", "donor@annasetu.com") {
                scope.launch {
                    try {
                        isLoading = true
                        auth.signInWithEmailAndPassword("donor@annasetu.com", "demo1234").await()
                        val userDoc = db.collection("users").document(auth.currentUser?.uid ?: "").get().await()
                        val role = userDoc.getString("role") ?: "donor"
                        onNavigateToHome(role)
                    } catch (e: Exception) {
                        errorMsg = e.message
                        isLoading = false
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LoginButton("Login as NGO", "ngo@annasetu.com") {
                scope.launch {
                    try {
                        isLoading = true
                        auth.signInWithEmailAndPassword("ngo@annasetu.com", "demo1234").await()
                        val userDoc = db.collection("users").document(auth.currentUser?.uid ?: "").get().await()
                        val role = userDoc.getString("role") ?: "ngo"
                        onNavigateToHome(role)
                    } catch (e: Exception) {
                        errorMsg = e.message
                        isLoading = false
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LoginButton("Login as Volunteer", "volunteer@annasetu.com") {
                scope.launch {
                    try {
                        isLoading = true
                        auth.signInWithEmailAndPassword("volunteer@annasetu.com", "demo1234").await()
                        val userDoc = db.collection("users").document(auth.currentUser?.uid ?: "").get().await()
                        val role = userDoc.getString("role") ?: "volunteer"
                        onNavigateToHome(role)
                    } catch (e: Exception) {
                        errorMsg = e.message
                        isLoading = false
                    }
                }
            }
        }

        errorMsg?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(it, color = Color.Red)
        }
    }
}

@Composable
fun LoginButton(text: String, email: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(56.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text)
            Text(email, style = MaterialTheme.typography.labelSmall)
        }
    }
}
