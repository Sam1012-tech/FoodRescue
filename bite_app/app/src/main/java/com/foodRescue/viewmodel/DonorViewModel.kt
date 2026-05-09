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
import kotlinx.coroutines.flow.Flow

private const val TAG = "DonorViewModel"

class DonorViewModel : ViewModel() {
    private val repository = DonationRepository()
    private val auth = FirebaseAuth.getInstance()

    private val _donations = MutableStateFlow<List<Donation>>(emptyList())
    val donations = _donations.asStateFlow()

    private val _isPosting = MutableStateFlow(false)
    val isPosting = _isPosting.asStateFlow()

    private val _uploadError = MutableStateFlow<String?>(null)
    val uploadError = _uploadError.asStateFlow()

    fun clearError() { _uploadError.value = null }

    fun fetchDonations() {
        viewModelScope.launch {
            val uid = auth.currentUser?.uid ?: return@launch
            _donations.value = repository.getDonorDonations(uid)
        }
    }

    /**
     * Step 1: Upload photo and create doc with status="analyzing"
     * Returns the doc ID so we can listen to it.
     */
    suspend fun initiateDonation(imageUri: Uri, location: GeoPoint? = null): String? {
        _isPosting.value = true
        _uploadError.value = null
        
        return try {
            // Ensure we are signed in (await if necessary)
            var user = auth.currentUser
            if (user == null) {
                Log.d(TAG, "initiateDonation: No user found, attempting anonymous sign-in…")
                user = auth.signInAnonymously().await().user
            }
            
            val uid = user?.uid ?: throw Exception("Authentication failed. Please check your internet connection and ensure Anonymous Auth is enabled in Firebase.")

            Log.d(TAG, "initiateDonation: Uploading image as $uid...")
            val imageUrl = repository.uploadImage(imageUri)
            
            val donation = Donation(
                donorId = uid,
                donorName = auth.currentUser?.displayName ?: "Donor",
                photoUrl = imageUrl,
                pickupLocation = location,
                status = "analyzing"
            )
            
            val docId = repository.createDonation(donation)
            Log.d(TAG, "initiateDonation: Created doc $docId with status=analyzing")
            docId
        } catch (e: Exception) {
            Log.e(TAG, "initiateDonation failed", e)
            _uploadError.value = "Upload failed: ${e.message}"
            null
        } finally {
            _isPosting.value = false
        }
    }


    /**
     * Step 2: Listen for AI analysis updates on a specific donation doc
     */
    fun listenToDonation(docId: String): Flow<Donation?> {
        return repository.listenToDonation(docId)
    }

    /**
     * Step 3: Finalize donation with user-entered metadata
     */
    fun finalizeDonation(docId: String, metadata: DonorMetadata, onComplete: () -> Unit) {
        viewModelScope.launch {
            _isPosting.value = true
            try {
                repository.updateDonationMetadata(docId, metadata)
                onComplete()
            } catch (e: Exception) {
                _uploadError.value = "Finalize failed: ${e.message}"
            } finally {
                _isPosting.value = false
            }
        }
    }
}

