// bite_app/app/src/main/java/com/foodRescue/ui/donor/PostFoodScreen.kt
package com.foodRescue.ui.donor

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.border
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.foodRescue.ui.shared.components.GlassCard
import com.foodRescue.ui.theme.*
import com.foodRescue.viewmodel.DonorViewModel
import com.foodRescue.data.model.Donation
import kotlinx.coroutines.delay
import java.io.File

@Composable
fun PostFoodScreen(
    onBack: () -> Unit,
    donorViewModel: DonorViewModel = viewModel()
) {
    val context = LocalContext.current
    var capturedImageUri by remember { mutableStateOf<Uri?>(null) }
    var currentDocId by remember { mutableStateOf<String?>(null) }
    var aiAnalysisResult by remember { mutableStateOf<Donation?>(null) }

    // States: initial | analyzing | metadata
    var screenState by remember { mutableStateOf("initial") }

    val uploadError by donorViewModel.uploadError.collectAsState()
    val isPosting   by donorViewModel.isPosting.collectAsState()

    // Listen for AI analysis updates
    LaunchedEffect(currentDocId) {
        currentDocId?.let { id ->
            donorViewModel.listenToDonation(id)
                .catch { e -> donorViewModel.initiateDonationFailed(e.message ?: "Unknown error") }
                .collect { updated ->
                    if (updated != null) {
                        aiAnalysisResult = updated
                        // If AI has finished (estimatedMeals > 0 or status changed from analyzing)
                        if (updated.aiAnalysis.estimatedMeals > 0 || updated.status != "analyzing") {
                            screenState = "metadata"
                        }
                    }
                }
        }
    }

    LaunchedEffect(uploadError) {
        uploadError?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
            donorViewModel.clearError()
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) screenState = "analyzing"
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            capturedImageUri = uri
            screenState = "analyzing"
        }
    }

    if (screenState == "metadata") {
        DonationMetadataForm(
            photoUri  = capturedImageUri,
            isPosting = isPosting,
            aiResults = aiAnalysisResult?.aiAnalysis, // Pass AI results to form
            onConfirm = { metadata ->
                currentDocId?.let { id ->
                    donorViewModel.finalizeDonation(id, metadata) {
                        Toast.makeText(context, "Donation posted! 🎉", Toast.LENGTH_SHORT).show()
                        onBack()
                    }
                }
            },
            onCancel = onBack
        )
        return
    }

    Box(modifier = Modifier.fillMaxSize().background(DarkBg)) {
        AnimatedContent(
            targetState = screenState,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "ScreenTransition"
        ) { state ->
            when (state) {
                "initial" -> InitialCameraUI(
                    onScan = {
                        val file = File(context.cacheDir, "temp_donation.jpg")
                        val uri  = FileProvider.getUriForFile(context, "com.foodRescue.fileprovider", file)
                        capturedImageUri = uri
                        cameraLauncher.launch(uri)
                    },
                    onUpload = { galleryLauncher.launch("image/*") }
                )

                "analyzing" -> {
                    // Trigger real upload when entering analyzing state
                    LaunchedEffect(Unit) {
                        capturedImageUri?.let { uri ->
                            currentDocId = donorViewModel.initiateDonation(uri)
                        }
                    }
                    
                    val authStatus by donorViewModel.authStatus.collectAsState()
                    AnalyzingUI(authStatus)
                }
            }
        }
    }
}

@Composable
fun InitialCameraUI(onScan: () -> Unit, onUpload: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Add your surplus food", style = MaterialTheme.typography.headlineSmall, color = Color.White, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Take a photo or upload from gallery", style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.45f))
        Spacer(modifier = Modifier.height(40.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            OptionCard(Modifier.weight(1f), "📷", "Scan food", "Use camera", NeonGreen, onScan)
            OptionCard(Modifier.weight(1f), "🖼️", "Upload photo", "From gallery", Color(0xFF5BAFFF), onUpload)
        }
    }
}

@Composable
private fun OptionCard(modifier: Modifier, emoji: String, label: String, subLabel: String, accent: Color, onClick: () -> Unit) {
    Box(
        modifier = modifier.height(130.dp).background(Color.White.copy(alpha = 0.05f), RoundedCornerShape(20.dp))
            .border(1.dp, accent.copy(alpha = 0.4f), RoundedCornerShape(20.dp)).clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(emoji, style = MaterialTheme.typography.headlineLarge)
            Text(label, color = Color.White, fontWeight = FontWeight.SemiBold)
            Text(subLabel, color = accent, style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
fun AnalyzingUI(status: String = "") {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(color = NeonGreen, modifier = Modifier.size(64.dp))
        Spacer(modifier = Modifier.height(24.dp))
        Text("AI is analysing your food…", color = Color.White, fontWeight = FontWeight.Bold)
        Text(status, color = NeonGreen, style = MaterialTheme.typography.labelSmall)
        Text("Identifying portions & safety tier", color = Color.White.copy(alpha = 0.5f), style = MaterialTheme.typography.labelSmall)
    }
}

