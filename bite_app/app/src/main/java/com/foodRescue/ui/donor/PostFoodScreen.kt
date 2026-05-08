// bite_app/app/src/main/java/com/foodRescue/ui/donor/PostFoodScreen.kt
package com.foodRescue.ui.donor

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import java.io.File
import androidx.core.content.FileProvider

@Composable
fun PostFoodScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    var capturedImageUri by remember { mutableStateOf<Uri?>(null) }
    var photoRefreshTrigger by remember { mutableLongStateOf(0L) }
    var isPosting by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { _ -> }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            photoRefreshTrigger = System.currentTimeMillis()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Post Surplus Food", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(24.dp))

        Box(modifier = Modifier.size(300.dp).padding(8.dp), contentAlignment = Alignment.Center) {
            if (capturedImageUri != null) {
                // Use the trigger to force Coil to reload the image from disk
                AsyncImage(
                    model = capturedImageUri.toString() + "?t=" + photoRefreshTrigger,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Surface(color = MaterialTheme.colorScheme.surfaceVariant, modifier = Modifier.fillMaxSize(), shape = MaterialTheme.shapes.medium) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("No Photo Taken", style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            permissionLauncher.launch(Manifest.permission.CAMERA)
            try {
                val file = File(context.cacheDir, "temp_image.jpg")
                if (file.exists()) file.delete()
                file.createNewFile()
                
                val uri = FileProvider.getUriForFile(context, "com.foodRescue.fileprovider", file)
                capturedImageUri = uri
                cameraLauncher.launch(uri)
            } catch (e: Exception) {}
        }, modifier = Modifier.fillMaxWidth().height(56.dp)) {
            Text(if (capturedImageUri == null) "Take Photo" else "Retake Photo")
        }

        Spacer(modifier = Modifier.weight(1f))

        if (isPosting) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = { 
                    isPosting = true
                    onBack() 
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled = capturedImageUri != null
            ) {
                Text("Submit Donation")
            }
        }
    }
}
