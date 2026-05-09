// bite_app/app/src/main/java/com/foodRescue/data/repository/NGORepository.kt
package com.foodRescue.data.repository

import android.util.Log
import com.foodRescue.data.model.Donation
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.math.*

private const val TAG = "NGORepository"

class NGORepository {
    private val db = FirebaseFirestore.getInstance()

    /**
     * Returns a real-time Flow of active donations (status "posted" or "analyzed").
     * The app already has the matched donations via matchedTo field — we show ALL
     * active ones so the NGO can browse and optionally accept via a separate endpoint.
     *
     * Smart matching (priority scoring, distance ranking) is handled server-side by
     * the backend MatchingAgent. We don't re-implement scoring here — just display
     * the live Firestore feed that the backend has already enriched.
     */
    fun listenActiveDonations(): Flow<List<Donation>> = callbackFlow {
        Log.d(TAG, "listenActiveDonations: subscribing to real-time feed")

        val reg: ListenerRegistration = db.collection("donations")
            .whereIn("status", listOf("posted", "analyzed", "matched"))
            .addSnapshotListener { snap, err ->
                if (err != null) {
                    Log.e(TAG, "listenActiveDonations: Firestore error — ${err.message}", err)
                    return@addSnapshotListener
                }
                val list = snap?.documents?.mapNotNull { doc ->
                    try {
                        doc.toObject(Donation::class.java)?.copy(id = doc.id)
                    } catch (e: Exception) {
                        Log.w(TAG, "listenActiveDonations: could not deserialize doc ${doc.id}", e)
                        null
                    }
                } ?: emptyList()

                Log.d(TAG, "listenActiveDonations: received ${list.size} active donations")
                trySend(list)
            }

        awaitClose {
            Log.d(TAG, "listenActiveDonations: unsubscribing")
            reg.remove()
        }
    }

    /**
     * NGO accepts a donation — updates the matchedTo status in Firestore.
     * The backend matching agent will have already created the matchedTo entry;
     * we just flip its status to "accepted".
     */
    suspend fun acceptDonation(donationId: String, ngoId: String) {
        Log.d(TAG, "acceptDonation: donationId=$donationId, ngoId=$ngoId")
        // Update donation status to matched (backend will assign volunteer)
        db.collection("donations")
            .document(donationId)
            .update(mapOf("status" to "matched"))
        Log.d(TAG, "acceptDonation: ✅ status updated to matched")
    }
}

// ─── Geo utilities ────────────────────────────────────────────────────────────

/**
 * Haversine distance between two GeoPoints in kilometres.
 */
fun haversineKm(a: GeoPoint, b: GeoPoint): Double {
    val R = 6371.0
    val dLat = Math.toRadians(b.latitude - a.latitude)
    val dLon = Math.toRadians(b.longitude - a.longitude)
    val lat1 = Math.toRadians(a.latitude)
    val lat2 = Math.toRadians(b.latitude)
    val h = sin(dLat / 2).pow(2) + cos(lat1) * cos(lat2) * sin(dLon / 2).pow(2)
    return 2 * R * asin(sqrt(h))
}

/**
 * Rough ETA estimate: 3 km/min in Bengaluru traffic, min 2 min.
 */
fun etaMinutes(distKm: Double): Int = maxOf(2, (distKm / 0.5).toInt())
