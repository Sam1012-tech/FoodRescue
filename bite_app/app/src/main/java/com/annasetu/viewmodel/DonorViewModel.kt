// bite_app/app/src/main/java/com/annasetu/viewmodel/DonorViewModel.kt
package com.annasetu.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.annasetu.data.model.Donation
import com.annasetu.data.repository.DonationRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DonorViewModel : ViewModel() {
    private val repository = DonationRepository()
    private val auth = FirebaseAuth.getInstance()

    private val _donations = MutableStateFlow<List<Donation>>(emptyList())
    val donations = _donations.asStateFlow()

    private val _isPosting = MutableStateFlow(false)
    val isPosting = _isPosting.asStateFlow()

    fun fetchDonations() {
        viewModelScope.launch {
            val uid = auth.currentUser?.uid ?: return@launch
            _donations.value = repository.getDonorDonations(uid)
        }
    }

    fun postDonation(imageUri: Uri, onComplete: () -> Unit) {
        viewModelScope.launch {
            _isPosting.value = true
            try {
                val imageUrl = repository.uploadImage(imageUri)
                val donation = Donation(
                    donorId = auth.currentUser?.uid ?: "",
                    donorName = auth.currentUser?.displayName ?: "Donor",
                    photoUrl = imageUrl
                )
                repository.createDonation(donation)
                onComplete()
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isPosting.value = false
            }
        }
    }
}
