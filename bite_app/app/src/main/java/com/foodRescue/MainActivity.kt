// bite_app/app/src/main/java/com.foodRescue/MainActivity.kt
package com.foodRescue

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.foodRescue.navigation.AppNavigation
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // --- PROTOTYPE AUTO-LOGIN ---
        // Automatically sign in anonymously if no user is present.
        // This satisfies Firestore security rules for the demo.
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser == null) {
            auth.signInAnonymously()
                .addOnSuccessListener {
                    Log.d("MainActivity", "✅ Anonymous sign-in success: ${it.user?.uid}")
                }
                .addOnFailureListener {
                    Log.e("MainActivity", "❌ Anonymous sign-in failed", it)
                }
        } else {
            Log.d("MainActivity", "👤 User already signed in: ${auth.currentUser?.uid}")
        }

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

