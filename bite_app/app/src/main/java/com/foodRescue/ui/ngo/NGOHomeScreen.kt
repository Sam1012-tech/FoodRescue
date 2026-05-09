// bite_app/app/src/main/java/com/foodRescue/ui/ngo/NGOHomeScreen.kt
package com.foodRescue.ui.ngo

import android.Manifest
import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.foodRescue.data.model.Donation
import com.foodRescue.ui.theme.*
import com.foodRescue.viewmodel.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

// ─────────────────────────────────────────────────────────────────────────────
//  NGO Home Screen — Live donation feed with map, filters, and sort
// ─────────────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalPermissionsApi::class)
@SuppressLint("MissingPermission")
@Composable
fun NGOHomeScreen(viewModel: NGOViewModel = viewModel()) {
    val context = LocalContext.current

    // Observe ViewModel state
    val cards       by viewModel.donationCards.collectAsState()
    val activeFilter by viewModel.activeFilter.collectAsState()
    val activeSort  by viewModel.activeSort.collectAsState()
    val acceptError by viewModel.acceptError.collectAsState()

    // Show errors as Toast
    LaunchedEffect(acceptError) {
        acceptError?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
            viewModel.clearError()
        }
    }

    // Location permission
    val locationPermission = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    // Fetch device location once permission is granted
    LaunchedEffect(locationPermission.status) {
        if (locationPermission.status.let { it is com.google.accompanist.permissions.PermissionStatus.Granted }) {
            val fusedClient = LocationServices.getFusedLocationProviderClient(context)
            fusedClient.lastLocation.addOnSuccessListener { loc ->
                if (loc != null) viewModel.setNgoLocation(loc.latitude, loc.longitude)
            }
        } else {
            locationPermission.launchPermissionRequest()
        }
    }

    // Map camera — centres on Bengaluru by default
    val defaultLatLng = LatLng(12.9716, 77.5946)
    val cameraState   = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLatLng, 12f)
    }

    // When cards change, re-centre map on closest donation
    LaunchedEffect(cards) {
        val first = cards.firstOrNull()?.donation?.pickupLocation
        if (first != null) {
            cameraState.animate(
                CameraUpdateFactory.newLatLngZoom(LatLng(first.latitude, first.longitude), 13f)
            )
        }
    }

    // Map / list toggle state
    var showMapExpanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize().background(DarkBg)) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {

            // ── Header ────────────────────────────────────────────────────────
            item {
                NGOHeader(
                    donationCount = cards.size,
                    mapExpanded   = showMapExpanded,
                    onToggleMap   = { showMapExpanded = !showMapExpanded }
                )
            }

            // ── Map ───────────────────────────────────────────────────────────
            item {
                AnimatedContent(
                    targetState = showMapExpanded,
                    label = "map_height"
                ) { expanded ->
                    DonationMap(
                        cameraState = cameraState,
                        donations   = cards.mapNotNull { c ->
                            c.donation.pickupLocation?.let { gp ->
                                DonationPin(
                                    id       = c.donation.id,
                                    latLng   = LatLng(gp.latitude, gp.longitude),
                                    vegStatus = c.donation.donorMetadata.vegStatus,
                                    urgency  = c.donation.donorMetadata.urgency
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(if (expanded) 360.dp else 200.dp)
                    )
                }
            }

            // ── Filter chips ──────────────────────────────────────────────────
            item {
                FilterChipRow(
                    activeFilter = activeFilter,
                    onFilter     = { viewModel.activeFilter.value = it }
                )
            }

            // ── Sort tabs ─────────────────────────────────────────────────────
            item {
                SortTabRow(
                    activeSort = activeSort,
                    onSort     = { viewModel.activeSort.value = it }
                )
            }

            // ── Donation cards ────────────────────────────────────────────────
            if (cards.isEmpty()) {
                item { EmptyFeedMessage() }
            } else {
                items(cards, key = { it.donation.id }) { card ->
                    DonationCard(
                        card     = card,
                        onAccept = { viewModel.acceptDonation(card.donation.id) },
                        onView   = { /* navigate to detail screen in Task 3b */ }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  Header
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun NGOHeader(
    donationCount: Int,
    mapExpanded: Boolean,
    onToggleMap: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                "Active donations",
                color = Color.White.copy(alpha = 0.55f),
                fontSize = 13.sp
            )
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    "$donationCount",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = NeonGreen
                    )
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    "nearby",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.55f),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
        }

        // Map expand toggle
        IconToggleButton(
            checked = mapExpanded,
            onCheckedChange = { onToggleMap() },
            modifier = Modifier
                .size(44.dp)
                .background(GlassWhite, CircleShape)
        ) {
            Text(if (mapExpanded) "⊙" else "⊙", color = NeonGreen, fontSize = 20.sp)
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  Map
// ─────────────────────────────────────────────────────────────────────────────

data class DonationPin(
    val id: String,
    val latLng: LatLng,
    val vegStatus: String,
    val urgency: String
)

@Composable
private fun DonationMap(
    cameraState: CameraPositionState,
    donations: List<DonationPin>,
    modifier: Modifier = Modifier
) {
    GoogleMap(
        modifier      = modifier.clip(RoundedCornerShape(bottomStart = 0.dp, bottomEnd = 0.dp)),
        cameraPositionState = cameraState,
        properties    = MapProperties(isMyLocationEnabled = false),
        uiSettings    = MapUiSettings(
            zoomControlsEnabled   = false,
            myLocationButtonEnabled = false,
            compassEnabled        = false
        )
    ) {
        donations.forEach { pin ->
            val hue = when {
                pin.urgency == "high"  -> BitmapDescriptorFactory.HUE_RED
                pin.vegStatus == "veg" -> BitmapDescriptorFactory.HUE_GREEN
                else                   -> BitmapDescriptorFactory.HUE_ORANGE
            }
            Marker(
                state = MarkerState(position = pin.latLng),
                icon  = BitmapDescriptorFactory.defaultMarker(hue),
                title = "Donation ${pin.id.take(6)}"
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  Filter chip row
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun FilterChipRow(
    activeFilter: DonationFilter,
    onFilter: (DonationFilter) -> Unit
) {
    LazyRow(
        contentPadding        = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(DonationFilter.values()) { filter ->
            val selected = filter == activeFilter
            FilterChip(
                selected = selected,
                onClick  = { onFilter(filter) },
                label    = { Text(filter.label, fontSize = 12.sp) },
                colors   = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = NeonGreen,
                    selectedLabelColor     = Color.Black,
                    containerColor         = GlassWhite,
                    labelColor             = Color.White
                )
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  Sort tabs
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun SortTabRow(
    activeSort: DonationSort,
    onSort: (DonationSort) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(bottom = 8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            "Sort:",
            color = Color.White.copy(alpha = 0.4f),
            fontSize = 12.sp,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
        DonationSort.values().forEach { sort ->
            val selected = sort == activeSort
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (selected) NeonGreen.copy(alpha = 0.15f) else Color.Transparent)
                    .border(
                        1.dp,
                        if (selected) NeonGreen else Color.White.copy(alpha = 0.15f),
                        RoundedCornerShape(20.dp)
                    )
                    .clickable { onSort(sort) }
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text(
                    sort.label,
                    fontSize  = 12.sp,
                    color     = if (selected) NeonGreen else Color.White.copy(alpha = 0.55f),
                    fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  Donation card
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun DonationCard(
    card: DonationCardUiState,
    onAccept: () -> Unit,
    onView: () -> Unit
) {
    val d = card.donation

    // Countdown timer — ticks every second
    var remaining by remember { mutableLongStateOf(card.shelfLifeRemainingMs) }
    LaunchedEffect(card.donation.id) {
        while (remaining > 0) {
            delay(1_000)
            remaining -= 1_000
        }
    }

    val urgencyColor = when (d.donorMetadata.urgency) {
        "high"   -> UrgencyRed
        "medium" -> WarningYellow
        else     -> NeonGreen
    }
    val safetyColor = when (d.safetyTier) {
        "green"  -> NeonGreen
        "yellow" -> WarningYellow
        else     -> UrgencyRed
    }
    val vegColor = when (d.donorMetadata.vegStatus) {
        "veg"   -> NeonGreen
        "non-veg" -> UrgencyRed
        else    -> WarningYellow
    }
    val vegLabel = when (d.donorMetadata.vegStatus) {
        "veg"   -> "VEG"
        "non-veg" -> "NON-VEG"
        else    -> "MIXED"
    }

    // Urgency left-border gradient
    val cardBorderColor = urgencyColor.copy(alpha = 0.6f)

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White.copy(alpha = 0.06f))
            .border(1.dp, cardBorderColor, RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        // ── Row 1: photo + donor info ──────────────────────────────────────
        Row(verticalAlignment = Alignment.Top) {
            // Food thumbnail
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(GlassWhite)
            ) {
                if (d.photoUrl.isNotBlank()) {
                    AsyncImage(
                        model              = d.photoUrl,
                        contentDescription = "Food photo",
                        modifier           = Modifier.fillMaxSize(),
                        contentScale       = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🍱", fontSize = 32.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                // Donor name
                Text(
                    d.donorName.ifBlank { "Anonymous Donor" },
                    style     = MaterialTheme.typography.titleMedium,
                    color     = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    maxLines  = 1,
                    overflow  = TextOverflow.Ellipsis
                )

                // Veg badge + safety tier
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Badge(vegColor, vegLabel)
                    Badge(safetyColor, d.safetyTier.uppercase())
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Portions + weight
                Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    InfoChip("${d.donorMetadata.portions} portions")
                    InfoChip("${d.donorMetadata.weightKg.roundToInt()} kg")
                }
            }

            // Urgency dot
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(urgencyColor, CircleShape)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
        HorizontalDivider(color = Color.White.copy(alpha = 0.08f))
        Spacer(modifier = Modifier.height(12.dp))

        // ── Row 2: distance + ETA + countdown + rating ─────────────────────
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Distance & ETA
            Column {
                Text(
                    if (card.distanceKm >= 0) "%.1f km".format(card.distanceKm) else "—",
                    color     = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize  = 18.sp
                )
                Text(
                    if (card.etaMinutes < 99) "~${card.etaMinutes} min ETA" else "ETA unknown",
                    color   = Color.White.copy(alpha = 0.45f),
                    fontSize = 11.sp
                )
            }

            // Countdown timer
            CountdownDisplay(remaining)

            // Donor reliability stars
            RatingStars(score = d.donorMetadata.let { 4.0 }) // placeholder — users.rating
        }

        Spacer(modifier = Modifier.height(14.dp))

        // ── Row 3: action buttons ──────────────────────────────────────────
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedButton(
                onClick  = onView,
                modifier = Modifier
                    .weight(1f)
                    .height(42.dp),
                shape    = RoundedCornerShape(12.dp),
                border   = BorderStroke(1.dp, Color.White.copy(alpha = 0.25f))
            ) {
                Text("View", color = Color.White, fontSize = 13.sp)
            }
            Button(
                onClick  = onAccept,
                modifier = Modifier
                    .weight(2f)
                    .height(42.dp),
                shape    = RoundedCornerShape(12.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = NeonGreen)
            ) {
                Text("Accept rescue", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  Sub-components
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun Badge(color: Color, label: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(color.copy(alpha = 0.18f))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(label, color = color, fontSize = 9.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun InfoChip(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(GlassWhite)
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(text, color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp)
    }
}

/** Animated countdown — turns red when < 30 min remain */
@Composable
private fun CountdownDisplay(remainingMs: Long) {
    val hours   = TimeUnit.MILLISECONDS.toHours(remainingMs)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(remainingMs) % 60
    val seconds = TimeUnit.MILLISECONDS.toSeconds(remainingMs) % 60

    val urgent = remainingMs < 30 * 60 * 1000L  // < 30 min
    val color  = if (urgent) UrgencyRed else WarningYellow

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = when {
                hours > 0   -> "%dh %02dm".format(hours, minutes)
                minutes > 0 -> "%dm %02ds".format(minutes, seconds)
                else        -> "%ds".format(seconds)
            },
            color     = color,
            fontSize  = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            "shelf life",
            color   = Color.White.copy(alpha = 0.35f),
            fontSize = 9.sp
        )
    }
}

/** 1–5 star rating display */
@Composable
private fun RatingStars(score: Double) {
    val fullStars = score.toInt()
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row {
            repeat(5) { i ->
                Text(
                    if (i < fullStars) "★" else "☆",
                    color   = if (i < fullStars) WarningYellow else Color.White.copy(alpha = 0.2f),
                    fontSize = 12.sp
                )
            }
        }
        Text("reliability", color = Color.White.copy(alpha = 0.35f), fontSize = 9.sp)
    }
}

@Composable
private fun EmptyFeedMessage() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 64.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("🍽", fontSize = 48.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "No active donations nearby",
            color   = Color.White.copy(alpha = 0.55f),
            fontSize = 16.sp
        )
        Text(
            "Check back soon or widen your radius",
            color   = Color.White.copy(alpha = 0.3f),
            fontSize = 13.sp
        )
    }
}
