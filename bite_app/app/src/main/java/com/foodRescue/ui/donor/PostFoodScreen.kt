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

    // ── Camera launcher ──────────────────────────────────────────────────────
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) screenState = "analyzing"
    }

    // ── Gallery launcher ─────────────────────────────────────────────────────
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            capturedImageUri = uri
            screenState = "analyzing"
        }
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
                    Toast.makeText(context, "No photo — please scan or upload first.", Toast.LENGTH_SHORT).show()
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
                "initial" -> InitialCameraUI(
                    onScan = {
                        val file = File(context.cacheDir, "temp_donation.jpg")
                        val uri  = FileProvider.getUriForFile(
                            context, "com.foodRescue.fileprovider", file
                        )
                        capturedImageUri = uri
                        cameraLauncher.launch(uri)
                    },
                    onUpload = {
                        galleryLauncher.launch("image/*")
                    }
                )

                "analyzing" -> AnalyzingUI {
                    screenState = "metadata"
                }

                else -> { /* metadata handled above */ }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  Initial prompt — camera scan OR gallery upload
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun InitialCameraUI(
    onScan:   () -> Unit,
    onUpload: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Hero label
        Text(
            "Add your surplus food",
            style     = MaterialTheme.typography.headlineSmall,
            color     = Color.White,
            fontWeight = FontWeight.Bold,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Take a fresh photo or upload one from your gallery",
            style     = MaterialTheme.typography.bodySmall,
            color     = Color.White.copy(alpha = 0.45f),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(modifier = Modifier.height(40.dp))

        // ── Two option cards ──────────────────────────────────────────────
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Camera card
            OptionCard(
                modifier  = Modifier.weight(1f),
                emoji     = "📷",
                label     = "Scan food",
                subLabel  = "Use camera",
                accent    = NeonGreen,
                onClick   = onScan
            )

            // Gallery card
            OptionCard(
                modifier  = Modifier.weight(1f),
                emoji     = "🖼️",
                label     = "Upload photo",
                subLabel  = "From gallery",
                accent    = Color(0xFF5BAFFF),
                onClick   = onUpload
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // ── Primary CTA: full-width camera button ─────────────────────────
        Button(
            onClick  = onScan,
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            shape  = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = NeonGreen)
        ) {
            Text(
                "📷  Scan food now",
                fontWeight = FontWeight.Bold,
                color      = Color.Black,
                fontSize   = androidx.compose.ui.unit.TextUnit.Unspecified
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ── Secondary: outlined gallery button ────────────────────────────
        OutlinedButton(
            onClick  = onUpload,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape  = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.25f))
        ) {
            Text(
                "🖼️  Upload from gallery",
                color    = Color.White,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun OptionCard(
    modifier:  Modifier,
    emoji:     String,
    label:     String,
    subLabel:  String,
    accent:    Color,
    onClick:   () -> Unit
) {
    Box(
        modifier = modifier
            .height(130.dp)
            .background(
                Color.White.copy(alpha = 0.05f),
                RoundedCornerShape(20.dp)
            )
            .border(
                width = 1.dp,
                color = accent.copy(alpha = 0.4f),
                shape = RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(emoji, fontSize = androidx.compose.ui.unit.sp(32f))
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                label,
                color      = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize   = androidx.compose.ui.unit.sp(13f)
            )
            Text(
                subLabel,
                color    = accent,
                fontSize = androidx.compose.ui.unit.sp(11f)
            )
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
