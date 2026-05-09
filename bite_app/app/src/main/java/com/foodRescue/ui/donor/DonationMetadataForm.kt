// bite_app/app/src/main/java/com/foodRescue/ui/donor/DonationMetadataForm.kt
package com.foodRescue.ui.donor

import android.net.Uri
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.foodRescue.data.model.DonorMetadata
import com.foodRescue.data.model.FoodAnalysis
import com.foodRescue.ui.theme.*

// ─────────────────────────────────────────────────────────────────────────────
//  DonationMetadataForm
//  Screen shown after photo analysis is complete.
//  Warm editorial style — cream background, serif headline, sage / coral accents.
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun DonationMetadataForm(
    photoUri: Uri?,
    isPosting: Boolean,
    aiResults: FoodAnalysis? = null,
    onConfirm: (DonorMetadata) -> Unit,
    onCancel: () -> Unit
) {
    // ── Form state ─────────────────────────────────────────────────────────
    var vegStatus     by remember { mutableStateOf("veg") }
    var urgency       by remember { mutableStateOf("medium") }
    var weightKg      by remember { mutableStateOf(5) }
    var portions      by remember { mutableStateOf(20) }
    var shelfLifeHrs  by remember { mutableStateOf(3f) }
    var allergens     by remember { mutableStateOf(setOf<String>()) }
    var contactName   by remember { mutableStateOf("") }
    var contactPhone  by remember { mutableStateOf("") }
    var notes         by remember { mutableStateOf("") }
    var showDetails   by remember { mutableStateOf(true) } // Auto-expand if we have AI results

    // Pre-populate with AI results when they arrive
    LaunchedEffect(aiResults) {
        aiResults?.let {
            portions = it.estimatedMeals
            vegStatus = it.foodType
            // Assuming weight calculation if needed, else stay default
        }
    }


    // Cream page background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            // ── Top bar ────────────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back / cancel icon (line style)
                Text(
                    "✕",
                    color = InkLight,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .clickable(onClick = onCancel)
                        .padding(4.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    "Share your food",
                    style = WarmTypography.headlineSmall,
                    color = InkBrown
                )
                Spacer(modifier = Modifier.weight(1f))
                // Invisible spacer for balance
                Text("✕", color = Color.Transparent, fontSize = 20.sp)
            }

            HorizontalDivider(color = DividerBeige, thickness = 1.dp)

            // ── Photo thumbnail ────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 20.dp)
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(ChipFill)
            ) {
                AsyncImage(
                    model = photoUri,
                    contentDescription = "Captured food photo",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                // Tasteful overlay label
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp)
                        .background(InkBrown.copy(alpha = 0.55f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text("Food photo", style = WarmTypography.labelMedium, color = PaperWhite)
                }
            }

            // ── Section: Required details ──────────────────────────────────
            SectionLabel("Food type")

            // Veg / Non-veg / Mixed segmented control
            VegSegmentedControl(
                selected = vegStatus,
                onSelect = { vegStatus = it }
            )

            Spacer(modifier = Modifier.height(24.dp))
            SectionLabel("Pickup urgency")

            UrgencyChips(
                selected = urgency,
                onSelect = { urgency = it }
            )

            Spacer(modifier = Modifier.height(32.dp))
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 24.dp),
                color = DividerBeige
            )

            // ── Collapsible section ────────────────────────────────────────
            Spacer(modifier = Modifier.height(12.dp))
            ExpandCollapseRow(
                expanded = showDetails,
                label = if (showDetails) "Hide details" else "Add more details",
                onClick = { showDetails = !showDetails }
            )

            AnimatedVisibility(
                visible = showDetails,
                enter = fadeIn() + expandVertically(),
                exit  = fadeOut() + shrinkVertically()
            ) {
                Column {
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 24.dp),
                        color = DividerBeige
                    )

                    // Weight stepper
                    Spacer(modifier = Modifier.height(24.dp))
                    SectionLabel("Estimated weight (kg)")
                    NumberStepper(
                        value      = weightKg,
                        onDecrement = { if (weightKg > 1) weightKg-- },
                        onIncrement = { if (weightKg < 200) weightKg++ }
                    )

                    // Portions stepper
                    Spacer(modifier = Modifier.height(24.dp))
                    SectionLabel("Estimated portions")
                    NumberStepper(
                        value       = portions,
                        onDecrement = { if (portions > 1) portions-- },
                        onIncrement = { if (portions < 500) portions++ }
                    )

                    // Shelf life slider
                    Spacer(modifier = Modifier.height(24.dp))
                    SectionLabel("Shelf life — ${shelfLifeHrs.toInt()} hrs")
                    Slider(
                        value        = shelfLifeHrs,
                        onValueChange = { shelfLifeHrs = it },
                        valueRange   = 1f..12f,
                        steps        = 10,
                        modifier     = Modifier.padding(horizontal = 24.dp),
                        colors       = SliderDefaults.colors(
                            thumbColor         = SageGreen,
                            activeTrackColor   = SageGreen,
                            inactiveTrackColor = DividerBeige
                        )
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 28.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("1 hr",  style = WarmTypography.labelMedium, color = InkLight)
                        Text("12 hrs", style = WarmTypography.labelMedium, color = InkLight)
                    }

                    // Allergens
                    Spacer(modifier = Modifier.height(24.dp))
                    SectionLabel("Allergens present")
                    AllergenChips(
                        selected  = allergens,
                        onToggle  = { tag ->
                            allergens = if (allergens.contains(tag))
                                allergens - tag else allergens + tag
                        }
                    )

                    // Contact name
                    Spacer(modifier = Modifier.height(24.dp))
                    SectionLabel("Pickup contact name")
                    WarmTextField(
                        value         = contactName,
                        onValueChange = { contactName = it },
                        placeholder   = "Full name",
                        keyboardType  = KeyboardType.Text
                    )

                    // Contact phone
                    Spacer(modifier = Modifier.height(16.dp))
                    SectionLabel("Pickup contact phone")
                    WarmTextField(
                        value         = contactPhone,
                        onValueChange = { contactPhone = it },
                        placeholder   = "+91 98765 43210",
                        keyboardType  = KeyboardType.Phone
                    )

                    // Notes
                    Spacer(modifier = Modifier.height(16.dp))
                    SectionLabel("Notes for receiver (optional)")
                    WarmTextField(
                        value         = notes,
                        onValueChange = { notes = it },
                        placeholder   = "Any handling instructions, allergies, or context…",
                        keyboardType  = KeyboardType.Text,
                        singleLine    = false,
                        minLines      = 3
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            // ── CTA ────────────────────────────────────────────────────────
            Spacer(modifier = Modifier.height(32.dp))

            if (isPosting) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        color    = SageGreen,
                        modifier = Modifier.size(22.dp),
                        strokeWidth = 2.5.dp
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "Saving your donation…",
                        style = WarmTypography.bodyMedium,
                        color = InkMid
                    )
                }
            } else {
                Button(
                    onClick = {
                        onConfirm(
                            DonorMetadata(
                                vegStatus     = vegStatus,
                                urgency       = urgency,
                                weightKg      = weightKg.toDouble(),
                                portions      = portions,
                                shelfLifeHours = shelfLifeHrs.toInt(),
                                allergens     = allergens.toList(),
                                contactName   = contactName,
                                contactPhone  = contactPhone,
                                notes         = notes
                            )
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .height(58.dp),
                    shape  = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SageGreen)
                ) {
                    Text(
                        "Confirm donation",
                        style = WarmTypography.titleMedium,
                        color = PaperWhite,
                        letterSpacing = 0.3.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            TextButton(
                onClick = onCancel,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    "Cancel",
                    style = WarmTypography.bodyMedium,
                    color = InkLight
                )
            }

            // Bottom safe-area breathing room
            Spacer(modifier = Modifier.navigationBarsPadding().height(24.dp))
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  Sub-components
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun SectionLabel(text: String) {
    Text(
        text   = text,
        style  = WarmTypography.labelLarge,
        color  = InkMid,
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .padding(bottom = 10.dp)
    )
}

/** Veg / Non-veg / Mixed — 3-segment pill selector */
@Composable
private fun VegSegmentedControl(selected: String, onSelect: (String) -> Unit) {
    val options = listOf(
        Triple("veg",     "Veg",     SageGreen),
        Triple("non-veg", "Non-veg", CoralRed),
        Triple("mixed",   "Mixed",   AmberYellow)
    )

    Row(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(ChipFill),
        horizontalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        options.forEach { (key, label, accent) ->
            val isSelected = selected == key
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        animateColorAsState(
                            if (isSelected) accent else Color.Transparent,
                            label = "seg_$key"
                        ).value
                    )
                    .clickable { onSelect(key) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    label,
                    style = WarmTypography.labelLarge,
                    color = if (isSelected) PaperWhite else InkMid
                )
            }
        }
    }
}

/** Low / Medium / High urgency chips — color coded */
@Composable
private fun UrgencyChips(selected: String, onSelect: (String) -> Unit) {
    val chips = listOf(
        Triple("low",    "Low",    Pair(UrgencyLow,    SoftSage)),
        Triple("medium", "Medium", Pair(UrgencyMedium, SoftAmber)),
        Triple("high",   "High",   Pair(UrgencyHigh,   SoftCoral))
    )

    Row(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        chips.forEach { (key, label, colors) ->
            val (accent, _) = colors
            val isSelected = selected == key
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        animateColorAsState(
                            if (isSelected) accent else ChipFill,
                            label = "urgency_$key"
                        ).value
                    )
                    .border(
                        width = 1.5.dp,
                        color = if (isSelected) accent else DividerBeige,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable { onSelect(key) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    label,
                    style = WarmTypography.labelLarge,
                    color = if (isSelected) PaperWhite else InkMid,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

/** Numeric stepper (+/-) with a centred value display */
@Composable
private fun NumberStepper(
    value: Int,
    onDecrement: () -> Unit,
    onIncrement: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth()
            .height(52.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(PaperWhite)
            .border(1.dp, DividerBeige, RoundedCornerShape(14.dp)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Decrement
        Box(
            modifier = Modifier
                .size(52.dp)
                .clickable(onClick = onDecrement),
            contentAlignment = Alignment.Center
        ) {
            Text("−", fontSize = 22.sp, color = InkMid)
        }

        // Value
        Text(
            "$value",
            style = WarmTypography.headlineSmall,
            color = InkBrown,
            textAlign = TextAlign.Center
        )

        // Increment
        Box(
            modifier = Modifier
                .size(52.dp)
                .clickable(onClick = onIncrement),
            contentAlignment = Alignment.Center
        ) {
            Text("+", fontSize = 22.sp, color = SageGreen)
        }
    }
}

/** Multi-select allergen chips: Nuts, Dairy, Gluten, Eggs */
@Composable
private fun AllergenChips(
    selected: Set<String>,
    onToggle: (String) -> Unit
) {
    val tags = listOf("Nuts", "Dairy", "Gluten", "Eggs")

    // Wrap chips in a flow-like row (2 columns)
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        tags.chunked(2).forEach { rowTags ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowTags.forEach { tag ->
                    val isSelected = selected.contains(tag)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(42.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                animateColorAsState(
                                    if (isSelected) ChipSelected else ChipFill,
                                    label = "chip_$tag"
                                ).value
                            )
                            .border(
                                1.dp,
                                if (isSelected) ChipSelected else DividerBeige,
                                RoundedCornerShape(10.dp)
                            )
                            .clickable { onToggle(tag) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            tag,
                            style = WarmTypography.labelLarge,
                            color = if (isSelected) ChipTextSel else InkMid
                        )
                    }
                }
                // If odd number of tags in last row, fill remaining space
                if (rowTags.size < 2) Spacer(modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

/** Expand / Collapse row for the "Add more details" section */
@Composable
private fun ExpandCollapseRow(
    expanded: Boolean,
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            label,
            style = WarmTypography.labelLarge,
            color = SageGreen
        )
        Spacer(modifier = Modifier.weight(1f))
        // Animated chevron
        val rotation by animateFloatAsState(
            targetValue = if (expanded) 180f else 0f,
            label = "chevron"
        )
        Text(
            "∨",
            color = SageGreen,
            fontSize = 16.sp,
            modifier = Modifier
                .graphicsLayer { rotationZ = rotation }
        )
    }
}

/** Warm-styled text field on cream background */
@Composable
private fun WarmTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true,
    minLines: Int = 1
) {
    OutlinedTextField(
        value          = value,
        onValueChange  = onValueChange,
        placeholder    = {
            Text(placeholder, style = WarmTypography.bodyMedium, color = InkLight)
        },
        textStyle      = WarmTypography.bodyLarge.copy(color = InkBrown),
        singleLine     = singleLine,
        minLines       = minLines,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        shape          = RoundedCornerShape(14.dp),
        colors         = OutlinedTextFieldDefaults.colors(
            focusedBorderColor   = SageGreen,
            unfocusedBorderColor = DividerBeige,
            focusedContainerColor   = PaperWhite,
            unfocusedContainerColor = PaperWhite,
            cursorColor             = SageGreen
        ),
        modifier       = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    )
}
