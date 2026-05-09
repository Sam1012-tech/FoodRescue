// bite_app/app/src/main/java/com/foodRescue/viewmodel/NGOViewModel.kt
package com.foodRescue.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foodRescue.data.model.Donation
import com.foodRescue.data.repository.NGORepository
import com.foodRescue.data.repository.etaMinutes
import com.foodRescue.data.repository.haversineKm
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.GeoPoint
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

private const val TAG = "NGOViewModel"

// ─── Filter / Sort enums ──────────────────────────────────────────────────────

enum class DonationFilter(val label: String) {
    ALL("All"),
    VEG_ONLY("Veg only"),
    WITHIN_3KM("Within 3 km"),
    EXPIRING_SOON("Expiring soon"),
    HIGH_PORTIONS("100+ portions")
}

enum class DonationSort(val label: String) {
    DISTANCE("Distance"),
    EXPIRY("Expiry"),
    PORTIONS("Portions")
}

// ─── UI state wrapper ─────────────────────────────────────────────────────────

data class DonationCardUiState(
    val donation: Donation,
    val distanceKm: Double,
    val etaMinutes: Int,
    val shelfLifeRemainingMs: Long   // countdown in ms
)

// ─── ViewModel ────────────────────────────────────────────────────────────────

class NGOViewModel : ViewModel() {

    private val repository = NGORepository()
    private val auth = FirebaseAuth.getInstance()

    // Configurable radius in km (default 5 km, from task spec)
    var radiusKm: Double = 5.0

    // NGO's own location (set when location permission granted)
    private val _ngoLocation = MutableStateFlow<GeoPoint?>(null)

    val activeFilter = MutableStateFlow(DonationFilter.ALL)
    val activeSort   = MutableStateFlow(DonationSort.DISTANCE)

    private val _rawDonations = MutableStateFlow<List<Donation>>(emptyList())

    private val _acceptError = MutableStateFlow<String?>(null)
    val acceptError = _acceptError.asStateFlow()

    fun clearError() { _acceptError.value = null }

    /**
     * Live, filtered, sorted list of donation cards for the NGO feed.
     * Combines: rawDonations + ngoLocation + activeFilter + activeSort.
     */
    val donationCards: StateFlow<List<DonationCardUiState>> = combine(
        _rawDonations, _ngoLocation, activeFilter, activeSort
    ) { donations, location, filter, sort ->
        val ngoLoc = location

        // Map to UI state (compute distance / ETA / countdown)
        val mapped = donations.mapNotNull { d ->
            val dist = if (ngoLoc != null && d.pickupLocation != null)
                haversineKm(ngoLoc, d.pickupLocation)
            else
                Double.MAX_VALUE  // unknown location → sort to end

            val eta = if (dist < Double.MAX_VALUE) etaMinutes(dist) else 99

            val nowMs = System.currentTimeMillis()
            val expiresMs = d.expiresAt?.toDate()?.time ?: (nowMs + 3 * 60 * 60 * 1000L)
            val remaining = expiresMs - nowMs

            DonationCardUiState(
                donation           = d,
                distanceKm         = if (dist == Double.MAX_VALUE) -1.0 else dist,
                etaMinutes         = eta,
                shelfLifeRemainingMs = maxOf(0L, remaining)
            )
        }

        // Apply radius filter first
        val withinRadius = if (ngoLoc != null)
            mapped.filter { it.distanceKm < 0 || it.distanceKm <= radiusKm }
        else
            mapped

        // Apply active filter
        val filtered = when (filter) {
            DonationFilter.ALL -> withinRadius
            DonationFilter.VEG_ONLY -> withinRadius.filter {
                it.donation.donorMetadata.vegStatus == "veg"
            }
            DonationFilter.WITHIN_3KM -> withinRadius.filter {
                it.distanceKm in 0.0..3.0
            }
            DonationFilter.EXPIRING_SOON -> withinRadius.filter {
                it.shelfLifeRemainingMs < 2 * 60 * 60 * 1000L // < 2 hrs
            }
            DonationFilter.HIGH_PORTIONS -> withinRadius.filter {
                it.donation.donorMetadata.portions >= 100
            }
        }

        // Apply sort
        when (sort) {
            DonationSort.DISTANCE -> filtered.sortedBy { it.distanceKm.let { d -> if (d < 0) Double.MAX_VALUE else d } }
            DonationSort.EXPIRY   -> filtered.sortedBy { it.shelfLifeRemainingMs }
            DonationSort.PORTIONS -> filtered.sortedByDescending { it.donation.donorMetadata.portions }
        }
    }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    init {
        observeDonations()
    }

    private fun observeDonations() {
        viewModelScope.launch {
            Log.d(TAG, "observeDonations: starting real-time listener")
            repository.listenActiveDonations().collect { list ->
                Log.d(TAG, "observeDonations: received ${list.size} donations")
                _rawDonations.value = list
            }
        }
    }

    /**
     * Called from the UI layer after location permission is granted.
     */
    fun setNgoLocation(lat: Double, lng: Double) {
        Log.d(TAG, "setNgoLocation: lat=$lat, lng=$lng")
        _ngoLocation.value = GeoPoint(lat, lng)
    }

    /**
     * Called when NGO taps "Accept" on a donation card.
     */
    fun acceptDonation(donationId: String) {
        val ngoId = auth.currentUser?.uid ?: run {
            _acceptError.value = "Not authenticated"
            return
        }
        viewModelScope.launch {
            try {
                Log.d(TAG, "acceptDonation: donationId=$donationId ngoId=$ngoId")
                repository.acceptDonation(donationId, ngoId)
                Log.d(TAG, "acceptDonation: ✅ accepted")
            } catch (e: Exception) {
                Log.e(TAG, "acceptDonation: ❌ ${e.message}", e)
                _acceptError.value = "Failed to accept: ${e.message}"
            }
        }
    }
}
