// bite_app/app/src/main/java/com.foodRescue/data/repository/DonationRepository.kt
package com.foodRescue.data.repository

import android.net.Uri
import android.util.Log
import com.foodRescue.data.model.Donation
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.UUID

private const val TAG = "DonationRepository"

class DonationRepository {
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    /**
     * Uploads a photo to Firebase Storage under donations/<uuid>.jpg
     * and returns the public download URL.
     *
     * This MUST complete successfully before createDonation() is called —
     * the caller (DonorViewModel) enforces this via sequential suspend calls.
     */
    suspend fun uploadImage(uri: Uri): String {
        val fileName = "donations/${UUID.randomUUID()}.jpg"
        Log.d(TAG, "uploadImage: starting upload → gs://foodrescue-34370.firebasestorage.app/$fileName")

        val ref = storage.reference.child(fileName)
        ref.putFile(uri).await()
        Log.d(TAG, "uploadImage: putFile() completed, fetching download URL…")

        val downloadUrl = ref.downloadUrl.await().toString()
        Log.d(TAG, "uploadImage: ✅ download URL obtained → $downloadUrl")
        return downloadUrl
    }

    /**
     * Writes a new donation document to Firestore collection "donations".
     * Uses FieldValue.serverTimestamp() so createdAt is authoritative.
     *
     * Required fields per schema:
     *   donorId, donorName, photoUrl, pickupLocation (GeoPoint),
     *   status = "posted", createdAt = serverTimestamp
     */
    suspend fun createDonation(donation: Donation) {
        val ref = db.collection("donations").document()
        Log.d(TAG, "createDonation: preparing Firestore doc → donations/${ref.id}")
        Log.d(TAG, "createDonation: donorId=${donation.donorId}, photoUrl=${donation.photoUrl}, status=${donation.status}")

        // Build a plain map so we can mix typed values with FieldValue sentinels.
        // Firestore data classes cannot use FieldValue.serverTimestamp() directly.
        val docMap: Map<String, Any?> = mapOf(
            "id"             to ref.id,
            "donorId"        to donation.donorId,
            "donorName"      to donation.donorName,
            "photoUrl"       to donation.photoUrl,
            "pickupLocation" to donation.pickupLocation, // GeoPoint or null
            "status"         to "posted",                // always "posted" on creation
            "createdAt"      to FieldValue.serverTimestamp(), // server-authoritative
            "expiresAt"      to donation.expiresAt,
            "aiAnalysis"     to donation.aiAnalysis,
            "matchedTo"      to donation.matchedTo,
            "volunteerId"    to donation.volunteerId,
            "escalationLevel" to donation.escalationLevel,
            "donorMetadata"  to donation.donorMetadata
        )

        ref.set(docMap).await()
        Log.d(TAG, "createDonation: ✅ Firestore write succeeded → donations/${ref.id}")
    }

    suspend fun getDonorDonations(donorId: String): List<Donation> {
        Log.d(TAG, "getDonorDonations: querying donations where donorId=$donorId")
        return db.collection("donations")
            .whereEqualTo("donorId", donorId)
            .get()
            .await()
            .toObjects(Donation::class.java)
    }
}
