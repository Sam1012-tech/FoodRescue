// bite_app/app/src/main/java/com/foodRescue/ui/volunteer/VolunteerHomeScreen.kt
package com.foodRescue.ui.volunteer

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.foodRescue.data.model.VolunteerMission
import com.foodRescue.ui.shared.components.GlassCard
import com.foodRescue.ui.theme.*
import com.foodRescue.viewmodel.VolunteerViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

// ─────────────────────────────────────────────────────────────────────────────
//  Volunteer Home Screen
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun VolunteerHomeScreen(vm: VolunteerViewModel = viewModel()) {
    val context        = LocalContext.current
    val isOnline       by vm.isOnline.collectAsState()
    val activeMission  by vm.activeMission.collectAsState()
    val impact         by vm.impact.collectAsState()
    val history        by vm.history.collectAsState()
    val inProgress     by vm.actionInProgress.collectAsState()
    val error          by vm.error.collectAsState()

    // Show errors as Toast
    LaunchedEffect(error) {
        error?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
            vm.clearError()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            // ── Header ─────────────────────────────────────────────────────
            item {
                VolunteerHeader(
                    isOnline = isOnline,
                    onToggle = { vm.setOnline(!isOnline) }
                )
            }

            // ── Impact banner ──────────────────────────────────────────────
            item {
                ImpactBanner(
                    mealsThisMonth  = impact.mealsThisMonth,
                    mealsThisYear   = impact.mealsThisYear,
                    co2SavedKg      = impact.co2SavedKg,
                    deliveriesTotal = impact.deliveriesTotal
                )
                Spacer(modifier = Modifier.height(20.dp))
            }

            // ── Active mission ─────────────────────────────────────────────
            item {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Active mission",
                    color   = Color.White.copy(alpha = 0.5f),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            item {
                AnimatedContent(
                    targetState = activeMission,
                    label = "mission_content"
                ) { mission ->
                    if (mission == null) {
                        IdleCard(isOnline = isOnline)
                    } else {
                        ActiveMissionCard(
                            mission    = mission,
                            inProgress = inProgress,
                            onPickedUp  = { vm.markPickedUp() },
                            onDelivered = { vm.markDelivered() }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(28.dp))
            }

            // ── History section label ──────────────────────────────────────
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Past deliveries",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                    Text(
                        "${history.size} total",
                        color   = NeonGreen,
                        fontSize = 12.sp
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // ── History rows ───────────────────────────────────────────────
            if (history.isEmpty()) {
                item { HistoryEmptyPlaceholder() }
            } else {
                items(history, key = { it.id }) { mission ->
                    HistoryRow(mission)
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        color    = Color.White.copy(alpha = 0.06f)
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  Header — name + online/offline toggle
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun VolunteerHeader(isOnline: Boolean, onToggle: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text("Rescue volunteer", color = Color.White.copy(alpha = 0.45f), fontSize = 12.sp)
            Text(
                "Arjun Kumar",
                style     = MaterialTheme.typography.headlineSmall,
                color     = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        // Online / Offline toggle pill
        OnlineTogglePill(isOnline = isOnline, onToggle = onToggle)
    }
}

@Composable
private fun OnlineTogglePill(isOnline: Boolean, onToggle: () -> Unit) {
    val bgColor by animateColorAsState(
        targetValue = if (isOnline) NeonGreen.copy(alpha = 0.18f) else Color.White.copy(alpha = 0.06f),
        label = "toggleBg"
    )
    val borderColor by animateColorAsState(
        targetValue = if (isOnline) NeonGreen else Color.White.copy(alpha = 0.15f),
        label = "toggleBorder"
    )
    val dotColor by animateColorAsState(
        targetValue = if (isOnline) NeonGreen else Color.White.copy(alpha = 0.3f),
        label = "toggleDot"
    )
    // Pulsing animation for the green dot when online
    val pulseScale by animateFloatAsState(
        targetValue = if (isOnline) 1.25f else 1f,
        animationSpec = if (isOnline)
            infiniteRepeatable(tween(800), RepeatMode.Reverse)
        else
            tween(300),
        label = "pulse"
    )

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(20.dp))
            .clickable(onClick = onToggle)
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .graphicsLayer { scaleX = pulseScale; scaleY = pulseScale }
                .background(dotColor, CircleShape)
        )
        Text(
            if (isOnline) "Online" else "Offline",
            color     = if (isOnline) NeonGreen else Color.White.copy(alpha = 0.5f),
            fontWeight = FontWeight.SemiBold,
            fontSize  = 13.sp
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  Impact banner
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun ImpactBanner(
    mealsThisMonth: Int,
    mealsThisYear: Int,
    co2SavedKg: Double,
    deliveriesTotal: Int
) {
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        // Main headline stat
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                "$mealsThisMonth",
                fontSize  = 42.sp,
                fontWeight = FontWeight.Bold,
                color     = NeonGreen
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "meals rescued\nthis month",
                color   = Color.White.copy(alpha = 0.65f),
                fontSize = 13.sp,
                modifier = Modifier.padding(bottom = 6.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(color = Color.White.copy(alpha = 0.08f))
        Spacer(modifier = Modifier.height(14.dp))

        // Secondary stats row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ImpactStat(
                value = "$mealsThisYear",
                label = "meals\nthis year"
            )
            VerticalDivider(
                modifier = Modifier.height(40.dp),
                color    = Color.White.copy(alpha = 0.1f)
            )
            ImpactStat(
                value = "${co2SavedKg.roundToInt()} kg",
                label = "CO₂\nsaved"
            )
            VerticalDivider(
                modifier = Modifier.height(40.dp),
                color    = Color.White.copy(alpha = 0.1f)
            )
            ImpactStat(
                value = "$deliveriesTotal",
                label = "total\ndeliveries"
            )
        }
    }
}

@Composable
private fun ImpactStat(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text(
            label,
            color     = Color.White.copy(alpha = 0.4f),
            fontSize  = 10.sp,
            textAlign = TextAlign.Center,
            lineHeight = 14.sp
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  Idle card — shown when no mission is assigned
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun IdleCard(isOnline: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White.copy(alpha = 0.04f))
            .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(20.dp))
            .padding(36.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("🛵", fontSize = 40.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                if (isOnline) "Waiting for a mission…" else "You are offline",
                color     = Color.White,
                fontSize  = 16.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                if (isOnline)
                    "The system will assign you automatically when a match is found"
                else
                    "Toggle online above to start receiving missions",
                color     = Color.White.copy(alpha = 0.4f),
                fontSize  = 13.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  Active mission card — map + route + action buttons
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun ActiveMissionCard(
    mission: VolunteerMission,
    inProgress: Boolean,
    onPickedUp: () -> Unit,
    onDelivered: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White.copy(alpha = 0.06f))
            .border(
                1.dp,
                when (mission.urgency) {
                    "high"   -> UrgencyRed.copy(alpha = 0.6f)
                    "medium" -> WarningYellow.copy(alpha = 0.4f)
                    else     -> NeonGreen.copy(alpha = 0.3f)
                },
                RoundedCornerShape(20.dp)
            )
    ) {
        // ── Route map ──────────────────────────────────────────────────────
        RouteMap(mission = mission)

        // ── Mission details ────────────────────────────────────────────────
        Column(modifier = Modifier.padding(16.dp)) {
            // Status pill
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatusPill(mission.status)
                if (mission.urgency == "high") {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(UrgencyRed.copy(alpha = 0.18f))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text("URGENT", color = UrgencyRed, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Pickup → Drop-off route summary
            RouteStopsRow(
                donorName    = mission.donorName,
                donorAddress = mission.donorAddress,
                ngoName      = mission.ngoName,
                ngoAddress   = mission.ngoAddress
            )

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color.White.copy(alpha = 0.07f))
            Spacer(modifier = Modifier.height(12.dp))

            // Payload row
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                MissionChip("${mission.mealsCount} meals")
                MissionChip("${mission.weightKg.roundToInt()} kg")
                MissionChip(mission.foodType.replaceFirstChar { it.uppercase() })
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action buttons
            if (inProgress) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(
                        color       = NeonGreen,
                        modifier    = Modifier.size(22.dp),
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Updating…", color = Color.White.copy(alpha = 0.6f))
                }
            } else {
                when (mission.status) {
                    "matched" -> {
                        Button(
                            onClick  = onPickedUp,
                            modifier = Modifier.fillMaxWidth().height(52.dp),
                            shape    = RoundedCornerShape(14.dp),
                            colors   = ButtonDefaults.buttonColors(containerColor = WarningYellow)
                        ) {
                            Text(
                                "✓  Picked up from donor",
                                color      = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize   = 15.sp
                            )
                        }
                    }
                    "picked_up" -> {
                        Button(
                            onClick  = onDelivered,
                            modifier = Modifier.fillMaxWidth().height(52.dp),
                            shape    = RoundedCornerShape(14.dp),
                            colors   = ButtonDefaults.buttonColors(containerColor = NeonGreen)
                        ) {
                            Text(
                                "✓  Delivered to ${mission.ngoName.take(20)}",
                                color      = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize   = 15.sp
                            )
                        }
                    }
                    else -> {
                        Text(
                            "Mission complete",
                            color   = NeonGreen,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  Route map — donor pin + NGO pin + polyline
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun RouteMap(mission: VolunteerMission) {
    val donorLatLng = mission.donorLocation?.let {
        LatLng(it.latitude, it.longitude)
    }
    val ngoLatLng = mission.ngoLocation?.let {
        LatLng(it.latitude, it.longitude)
    }

    // Centre camera between the two points, or Bengaluru default
    val defaultCentre = LatLng(12.9716, 77.5946)
    val centre = when {
        donorLatLng != null && ngoLatLng != null ->
            LatLng(
                (donorLatLng.latitude + ngoLatLng.latitude) / 2,
                (donorLatLng.longitude + ngoLatLng.longitude) / 2
            )
        donorLatLng != null -> donorLatLng
        else -> defaultCentre
    }

    val cameraState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(centre, 13f)
    }

    LaunchedEffect(centre) {
        cameraState.animate(CameraUpdateFactory.newLatLngZoom(centre, 13f))
    }

    GoogleMap(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
        cameraPositionState = cameraState,
        uiSettings = MapUiSettings(
            zoomControlsEnabled    = false,
            myLocationButtonEnabled = false,
            compassEnabled         = false
        )
    ) {
        // Donor pin — green (pickup)
        donorLatLng?.let {
            Marker(
                state = MarkerState(position = it),
                title = "Pickup: ${mission.donorName}",
                icon  = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
            )
        }

        // NGO pin — blue (drop-off)
        ngoLatLng?.let {
            Marker(
                state = MarkerState(position = it),
                title = "Drop-off: ${mission.ngoName}",
                icon  = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
            )
        }

        // Dashed polyline (straight line between points as route proxy)
        if (donorLatLng != null && ngoLatLng != null) {
            Polyline(
                points    = listOf(donorLatLng, ngoLatLng),
                color     = NeonGreen.copy(alpha = 0.7f),
                width     = 6f,
                geodesic  = true
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  Route stops summary
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun RouteStopsRow(
    donorName: String,
    donorAddress: String,
    ngoName: String,
    ngoAddress: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        // Donor (pickup)
        Column(modifier = Modifier.weight(1f)) {
            Text("PICKUP", color = NeonGreen, fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            Text(
                donorName.ifBlank { "Donor" },
                color     = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize  = 14.sp,
                maxLines  = 1,
                overflow  = TextOverflow.Ellipsis
            )
            Text(
                donorAddress.ifBlank { "Location from map" },
                color   = Color.White.copy(alpha = 0.4f),
                fontSize = 11.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }

        // Arrow
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
        ) {
            Text("→", color = WarningYellow, fontSize = 20.sp)
        }

        // NGO (drop-off)
        Column(modifier = Modifier.weight(1f)) {
            Text("DROP-OFF", color = Color(0xFF5BAFFF), fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            Text(
                ngoName.ifBlank { "NGO" },
                color     = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize  = 14.sp,
                maxLines  = 1,
                overflow  = TextOverflow.Ellipsis
            )
            Text(
                ngoAddress.ifBlank { "Location from map" },
                color   = Color.White.copy(alpha = 0.4f),
                fontSize = 11.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  History row
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun HistoryRow(mission: VolunteerMission) {
    val dateStr = mission.deliveredAt?.toDate()?.let {
        SimpleDateFormat("d MMM, h:mm a", Locale.getDefault()).format(it)
    } ?: "—"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Delivery icon
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(NeonGreen.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Text("🛵", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                mission.ngoName.ifBlank { "NGO" },
                color     = Color.White,
                fontWeight = FontWeight.Medium,
                fontSize  = 14.sp,
                maxLines  = 1,
                overflow  = TextOverflow.Ellipsis
            )
            Text(
                "${mission.mealsCount} meals · ${mission.donorName.ifBlank { "Donor" }}",
                color   = Color.White.copy(alpha = 0.4f),
                fontSize = 12.sp
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            // CO₂ saved for this delivery
            Text(
                "+ ${(mission.mealsCount * 2.5).roundToInt()} kg CO₂",
                color   = NeonGreen,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                dateStr,
                color   = Color.White.copy(alpha = 0.3f),
                fontSize = 10.sp
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  Small sub-components
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun StatusPill(status: String) {
    val (label, color) = when (status) {
        "matched"   -> Pair("Heading to pickup", WarningYellow)
        "picked_up" -> Pair("En route to NGO",   NeonGreen)
        "delivered" -> Pair("Delivered ✓",        Color.White.copy(alpha = 0.5f))
        else        -> Pair(status,               Color.White.copy(alpha = 0.4f))
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(color.copy(alpha = 0.15f))
            .border(1.dp, color.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(label, color = color, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun MissionChip(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White.copy(alpha = 0.07f))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(text, color = Color.White.copy(alpha = 0.75f), fontSize = 12.sp)
    }
}

@Composable
private fun HistoryEmptyPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, bottom = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            "No deliveries yet — start a mission!",
            color   = Color.White.copy(alpha = 0.3f),
            fontSize = 13.sp
        )
    }
}
