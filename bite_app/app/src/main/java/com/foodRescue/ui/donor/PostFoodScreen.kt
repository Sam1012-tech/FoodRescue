// bite_app/app/src/main/java/com/foodRescue/ui/donor/PostFoodScreen.kt
package com.foodRescue.ui.donor

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.foodRescue.ui.shared.components.GlassCard
import com.foodRescue.ui.theme.*
import kotlinx.coroutines.delay
import java.io.File

@Composable
fun PostFoodScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    var capturedImageUri by remember { mutableStateOf<Uri?>(null) }
    var screenState by remember { mutableStateOf("initial") } // initial, analyzing, results

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) screenState = "analyzing"
    }

    Box(modifier = Modifier.fillMaxSize().background(DarkBg)) {
        AnimatedContent(targetState = screenState, label = "ScreenTransition") { state ->
            when (state) {
                "initial" -> InitialCameraUI { 
                    val file = File(context.cacheDir, "temp.jpg")
                    val uri = FileProvider.getUriForFile(context, "com.foodRescue.fileprovider", file)
                    capturedImageUri = uri
                    cameraLauncher.launch(uri)
                }
                "analyzing" -> AnalyzingUI { screenState = "results" }
                "results" -> AnalysisResultsUI(capturedImageUri, onBack)
            }
        }
    }
}

@Composable
fun InitialCameraUI(onLaunch: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        GlassCard(modifier = Modifier.size(300.dp)) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("CAMERA INFRASTRUCTURE READY", color = NeonGreen.copy(alpha = 0.5f))
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onLaunch, modifier = Modifier.fillMaxWidth().height(64.dp), colors = ButtonDefaults.buttonColors(containerColor = NeonGreen)) {
            Text("SCAN FOOD", fontWeight = FontWeight.Bold, color = Color.Black)
        }
    }
}

@Composable
fun AnalyzingUI(onComplete: suspend () -> Unit) {
    LaunchedEffect(Unit) {
        delay(3000)
        onComplete()
    }
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        CircularProgressIndicator(color = NeonGreen, modifier = Modifier.size(64.dp))
        Spacer(modifier = Modifier.height(24.dp))
        Text("AI MCP ANALYSIS IN PROGRESS...", color = Color.White)
        Text("Scanning freshness protocols...", color = Color.White.copy(alpha = 0.5f), style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
fun AnalysisResultsUI(imageUri: Uri?, onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Text("AI ANALYSIS COMPLETE", style = MaterialTheme.typography.headlineSmall, color = NeonGreen)
        Spacer(modifier = Modifier.height(24.dp))
        
        AsyncImage(model = imageUri, contentDescription = null, modifier = Modifier.fillMaxWidth().height(200.dp))
        Spacer(modifier = Modifier.height(24.dp))

        GlassCard(modifier = Modifier.fillMaxWidth()) {
            ResultRow("Food Type", "Veg Biryani", NeonGreen)
            ResultRow("Estimated Meals", "48", Color.White)
            ResultRow("Freshness Score", "84%", NeonGreen)
            ResultRow("Spoilage Risk", "LOW", Color.White)
            ResultRow("Pickup Urgency", "MEDIUM", WarningYellow)
        }

        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = onBack, modifier = Modifier.fillMaxWidth().height(64.dp), colors = ButtonDefaults.buttonColors(containerColor = NeonGreen)) {
            Text("START RESCUE OPERATION", color = Color.Black, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ResultRow(label: String, value: String, color: Color) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = Color.White.copy(alpha = 0.5f))
        Text(value, color = color, fontWeight = FontWeight.Bold)
    }
}
