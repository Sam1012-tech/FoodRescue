// bite_app/app/src/main/java/com/foodRescue/ui/donor/PostFoodScreen.kt
package com.foodRescue.ui.donor

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.foodRescue.ui.shared.components.GlassCard
import com.foodRescue.ui.theme.*
import com.foodRescue.viewmodel.DonorViewModel
import kotlinx.coroutines.delay
import java.io.File

/**
 * Post Food Flow — screen states:
 *   initial   → Camera viewfinder prompt
 *   analyzing → Fake AI analysis spinner (3s)
 *   metadata  → Warm editorial metadata form  ← NEW
 *   posting   → Handled inside metadata form (isPosting spinner)
 *
 * After the user fills in the form and taps "Confirm donation":
 *   → ViewModel.postDonation() uploads photo + writes Firestore doc
 *   → On success → navigate back
 */
@Composable
fun PostFoodScreen(
    onBack: () -> Unit,
    donorViewModel: DonorViewModel = viewModel()
) {
    val context = LocalContext.current
    var capturedImageUri by remember { mutableStateOf<Uri?>(null) }

    // States: initial | analyzing | metadata
    var screenState by remember { mutableStateOf("initial") }

    val uploadError by donorViewModel.uploadError.collectAsState()
    val isPosting   by donorViewModel.isPosting.collectAsState()

    // Show any upload errors as Toast
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

    // The metadata form gets the full screen — no neon wrapper
    if (screenState == "metadata") {
        DonationMetadataForm(
            photoUri  = capturedImageUri,
            isPosting = isPosting,
            onConfirm = { metadata ->
                val uri = capturedImageUri
                if (uri != null) {
                    donorViewModel.postDonation(
                        imageUri = uri,
                        metadata = metadata,
                        onComplete = {
                            Toast.makeText(context, "Donation posted! 🎉", Toast.LENGTH_SHORT).show()
                            onBack()
                        }
                    )
                } else {
                    Toast.makeText(context, "No photo captured — please retake.", Toast.LENGTH_SHORT).show()
                }
            },
            onCancel = onBack
        )
        return
    }

    // Dark neon wrapper for initial / analyzing states
    Box(modifier = Modifier.fillMaxSize().background(DarkBg)) {
        AnimatedContent(
            targetState = screenState,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "ScreenTransition"
        ) { state ->
            when (state) {
                "initial" -> InitialCameraUI {
                    val file = File(context.cacheDir, "temp_donation.jpg")
                    val uri  = FileProvider.getUriForFile(
                        context, "com.foodRescue.fileprovider", file
                    )
                    capturedImageUri = uri
                    cameraLauncher.launch(uri)
                }

                "analyzing" -> AnalyzingUI {
                    // After fake AI analysis, move to the metadata form
                    screenState = "metadata"
                }

                else -> { /* metadata handled above */ }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  Initial camera prompt — dark / neon style kept from original
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun InitialCameraUI(onLaunch: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        GlassCard(modifier = Modifier.size(300.dp)) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    "Tap below to photograph\nyour surplus food",
                    color = NeonGreen.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick  = onLaunch,
            modifier = Modifier.fillMaxWidth().height(64.dp),
            colors   = ButtonDefaults.buttonColors(containerColor = NeonGreen)
        ) {
            Text("Scan food", fontWeight = FontWeight.Bold, color = Color.Black)
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  Analyzing spinner (3 second fake progress, then triggers onComplete)
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun AnalyzingUI(onComplete: suspend () -> Unit) {
    LaunchedEffect(Unit) {
        delay(3_000)
        onComplete()
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(color = NeonGreen, modifier = Modifier.size(64.dp))
        Spacer(modifier = Modifier.height(24.dp))
        Text("Analysing your food…", color = Color.White)
        Text(
            "Checking freshness and portions",
            color = Color.White.copy(alpha = 0.5f),
            style = MaterialTheme.typography.labelSmall
        )
    }
}
