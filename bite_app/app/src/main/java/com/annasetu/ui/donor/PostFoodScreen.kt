// bite_app/app/src/main/java/com/annasetu/ui/donor/PostFoodScreen.kt
package com.annasetu.ui.donor

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
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.annasetu.viewmodel.DonorViewModel
import java.io.File

@Composable
fun PostFoodScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val viewModel: DonorViewModel = viewModel()
    val isPosting by viewModel.isPosting.collectAsState()
    
    var capturedImageUri by remember { mutableStateOf<Uri?>(null) }
    
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (!success) capturedImageUri = null
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Post Surplus Food", style = MaterialTheme.typography.headlineSmall)
        
        Spacer(modifier = Modifier.height(24.dp))

        if (capturedImageUri != null) {
            AsyncImage(
                model = capturedImageUri,
                contentDescription = null,
                modifier = Modifier.size(300.dp).padding(8.dp)
            )
        }

        Button(onClick = {
            val file = File(context.cacheDir, "temp_image.jpg")
            val uri = androidx.core.content.FileProvider.getUriForFile(
                context, "${context.packageName}.fileprovider", file
            )
            capturedImageUri = uri
            cameraLauncher.launch(uri)
        }) {
            Text(if (capturedImageUri == null) "Take Photo" else "Retake Photo")
        }

        Spacer(modifier = Modifier.weight(1f))

        if (isPosting) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = { capturedImageUri?.let { viewModel.postDonation(it, onBack) } },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled = capturedImageUri != null
            ) {
                Text("Submit Donation")
            }
        }
    }
}
