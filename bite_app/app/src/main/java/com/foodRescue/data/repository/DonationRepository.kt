// bite_app/app/src/main/java/com.foodRescue/data/repository/DonationRepository.kt
package com.foodRescue.data.repository

import android.net.Uri
import android.util.Log
import com.foodRescue.data.model.Donation
import com.foodRescue.data.model.DonorMetadata
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID

private const val TAG = "DonationRepository"

class DonationRepository {
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    suspend fun uploadImage(uri: Uri): String {
        val fileName = "donations/${UUID.randomUUID()}.jpg"
        val ref = storage.reference.child(fileName)
        ref.putFile(uri).await()
        return ref.downloadUrl.await().toString()
    }

    /**
     * Creates donation and returns the new document ID
     */
    suspend fun createDonation(donation: Donation): String {
        val ref = db.collection("donations").document()
        val docMap = mapOf(
            "id" to ref.id,
            "donorId" to donation.donorId,
            "donorName" to donation.donorName,
            "photoUrl" to donation.photoUrl,
            "pickupLocation" to donation.pickupLocation,
            "status" to donation.status,
            "createdAt" to FieldValue.serverTimestamp(),
            "donorMetadata" to donation.donorMetadata,
            "aiAnalysis" to donation.aiAnalysis,
            "safetyTier" to donation.safetyTier
        )
        ref.set(docMap).await()
        return ref.id
    }

    /**
     * Listens for real-time changes to a single donation document
     */
    fun listenToDonation(docId: String): Flow<Donation?> = callbackFlow {
        val subscription = db.collection("donations").document(docId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val donation = snapshot?.toObject(Donation::class.java)
                trySend(donation)
            }
        awaitClose { subscription.remove() }
    }

    /**
     * Finalizes the donation with metadata and sets status to "posted"
     */
    suspend fun updateDonationMetadata(docId: String, metadata: DonorMetadata) {
        db.collection("donations").document(docId).update(
            mapOf(
                "donorMetadata" to metadata,
                "status" to "posted"
            )
        ).await()
    }

    suspend fun getDonorDonations(donorId: String): List<Donation> {
        return db.collection("donations")
            .whereEqualTo("donorId", donorId)
            .get()
            .await()
            .toObjects(Donation::class.java)
    }
}

