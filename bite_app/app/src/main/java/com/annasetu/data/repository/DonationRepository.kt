// bite_app/app/src/main/java/com/annasetu/data/repository/DonationRepository.kt
package com.annasetu.data.repository

import android.net.Uri
import com.annasetu.data.model.Donation
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.UUID

class DonationRepository {
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    suspend fun uploadImage(uri: Uri): String {
        val fileName = "donations/${UUID.randomUUID()}.jpg"
        val ref = storage.reference.child(fileName)
        ref.putFile(uri).await()
        return ref.downloadUrl.await().toString()
    }

    suspend fun createDonation(donation: Donation) {
        val ref = db.collection("donations").document()
        val finalDonation = donation.copy(id = ref.id)
        ref.set(finalDonation).await()
    }

    suspend fun getDonorDonations(donorId: String): List<Donation> {
        return db.collection("donations")
            .whereEqualTo("donorId", donorId)
            .get()
            .await()
            .toObjects(Donation::class.java)
    }
}
