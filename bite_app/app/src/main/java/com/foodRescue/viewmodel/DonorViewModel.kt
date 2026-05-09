// bite_app/app/src/main/java/com.foodRescue/viewmodel/DonorViewModel.kt
package com.foodRescue.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foodRescue.data.model.Donation
import com.foodRescue.data.model.DonorMetadata
import com.foodRescue.data.repository.DonationRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.GeoPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val TAG = "DonorViewModel"

class DonorViewModel : ViewModel() {
    private val repository = DonationRepository()
    private val auth = FirebaseAuth.getInstance()

    private val _donations = MutableStateFlow<List<Donation>>(emptyList())
    val donations = _donations.asStateFlow()

    private val _isPosting = MutableStateFlow(false)
    val isPosting = _isPosting.asStateFlow()

    /** Non-null means an error occurred — show a snackbar / toast with this message. */
    private val _uploadError = MutableStateFlow<String?>(null)
    val uploadError = _uploadError.asStateFlow()

    fun clearError() { _uploadError.value = null }

    fun fetchDonations() {
        viewModelScope.launch {
            val uid = auth.currentUser?.uid
            if (uid == null) {
                Log.w(TAG, "fetchDonations: no authenticated user — skipping")
                return@launch
            }
            Log.d(TAG, "fetchDonations: loading donations for uid=$uid")
            _donations.value = repository.getDonorDonations(uid)
            Log.d(TAG, "fetchDonations: loaded ${_donations.value.size} donations")
        }
    }

    /**
     * Uploads [imageUri] to Firebase Storage, then writes a donation doc to Firestore.
     *
     * Storage upload is AWAITED before the Firestore write — photoUrl will never be empty.
     *
     * [metadata]    — collected from the "Post Food" form
     * [location]    — device GPS location (should be passed from screen)
     * [onComplete]  — called only on success
     */
    fun postDonation(
        imageUri: Uri,
        metadata: DonorMetadata = DonorMetadata(),
        location: GeoPoint? = null,
        onComplete: () -> Unit
    ) {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            Log.e(TAG, "postDonation: user is not authenticated — aborting")
            _uploadError.value = "You must be logged in to post a donation."
            return
        }

        viewModelScope.launch {
            _isPosting.value = true
            _uploadError.value = null
            try {
                // Step 1: Upload photo to Firebase Storage
                Log.d(TAG, "postDonation: Step 1 — uploading image to Storage…")
                val imageUrl = repository.uploadImage(imageUri)
                Log.d(TAG, "postDonation: Step 1 ✅ — imageUrl=$imageUrl")

                // Step 2: Build donation document
                val donation = Donation(
                    donorId       = uid,
                    donorName     = auth.currentUser?.displayName ?: "Donor",
                    photoUrl      = imageUrl,          // always the Storage download URL
                    pickupLocation = location,         // GeoPoint from device GPS
                    status        = "posted",
                    donorMetadata = metadata
                )
                Log.d(TAG, "postDonation: Step 2 — writing Firestore doc, donorId=$uid, status=posted")

                // Step 3: Write to Firestore
                repository.createDonation(donation)
                Log.d(TAG, "postDonation: Step 3 ✅ — Firestore write successful!")

                onComplete()
            } catch (e: Exception) {
                // Bug C fix: errors are no longer silently discarded
                Log.e(TAG, "postDonation: ❌ FAILED — ${e.javaClass.simpleName}: ${e.message}", e)
                _uploadError.value = "Upload failed: ${e.message}"
            } finally {
                _isPosting.value = false
            }
        }
    }
}
