// bite_app/app/src/main/java/com/foodRescue/data/repository/VolunteerRepository.kt
package com.foodRescue.data.repository

import android.util.Log
import com.foodRescue.data.model.VolunteerImpact
import com.foodRescue.data.model.VolunteerMission
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.Calendar

private const val TAG = "VolunteerRepository"

class VolunteerRepository {
    private val db = FirebaseFirestore.getInstance()

    // ── Availability ──────────────────────────────────────────────────────────

    /**
     * Toggles the volunteer's online/offline status in `users/{uid}`.
     * Backend matching agent only assigns missions to volunteers whose
     * status == "idle" (online).
     */
    suspend fun setAvailability(uid: String, isOnline: Boolean) {
        val status = if (isOnline) "idle" else "offline"
        Log.d(TAG, "setAvailability: uid=$uid → $status")
        db.collection("users").document(uid)
            .update("status", status)
            .await()
        Log.d(TAG, "setAvailability: ✅ updated to $status")
    }

    // ── Active mission ────────────────────────────────────────────────────────

    /**
     * Real-time listener for the volunteer's current active mission.
     * Emits the first non-delivered mission assigned to this volunteer,
     * or null when idle.
     */
    fun listenActiveMission(uid: String): Flow<VolunteerMission?> = callbackFlow {
        Log.d(TAG, "listenActiveMission: subscribing for uid=$uid")

        val reg: ListenerRegistration = db.collection("missions")
            .whereEqualTo("volunteer_id", uid)
            .whereIn("status", listOf("matched", "picked_up"))
            .addSnapshotListener { snap, err ->
                if (err != null) {
                    Log.e(TAG, "listenActiveMission: error — ${err.message}", err)
                    return@addSnapshotListener
                }
                val mission = snap?.documents?.firstOrNull()?.let { doc ->
                    try {
                        val m = doc.toObject(VolunteerMission::class.java)
                        m?.copy(id = doc.id)
                    } catch (e: Exception) {
                        Log.w(TAG, "listenActiveMission: deserialize error for ${doc.id}", e)
                        null
                    }
                }
                Log.d(TAG, "listenActiveMission: mission=${mission?.id ?: "none"}")
                trySend(mission)
            }

        awaitClose {
            Log.d(TAG, "listenActiveMission: unsubscribing")
            reg.remove()
        }
    }

    // ── Mission actions ───────────────────────────────────────────────────────

    /**
     * Volunteer marks food as picked up from the donor.
     * Updates mission status to "picked_up" and sets pickedUpAt timestamp.
     */
    suspend fun markPickedUp(missionId: String, donationId: String) {
        Log.d(TAG, "markPickedUp: missionId=$missionId")
        db.collection("missions").document(missionId)
            .update(
                mapOf(
                    "status"       to "picked_up",
                    "pickedUpAt"   to FieldValue.serverTimestamp()
                )
            ).await()

        // Also update the donation status
        db.collection("donations").document(donationId)
            .update("status", "in_transit")
            .await()

        Log.d(TAG, "markPickedUp: ✅ mission=$missionId → picked_up, donation=$donationId → in_transit")
    }

    /**
     * Volunteer marks the delivery as complete.
     * Updates mission + donation status to "delivered".
     */
    suspend fun markDelivered(missionId: String, donationId: String) {
        Log.d(TAG, "markDelivered: missionId=$missionId")
        db.collection("missions").document(missionId)
            .update(
                mapOf(
                    "status"       to "delivered",
                    "deliveredAt"  to FieldValue.serverTimestamp()
                )
            ).await()

        db.collection("donations").document(donationId)
            .update("status", "delivered")
            .await()

        Log.d(TAG, "markDelivered: ✅ mission=$missionId → delivered, donation=$donationId → delivered")
    }

    // ── History + impact ─────────────────────────────────────────────────────

    /**
     * Fetches the volunteer's past delivered missions and computes impact stats.
     */
    suspend fun getImpactStats(uid: String): VolunteerImpact {
        Log.d(TAG, "getImpactStats: fetching history for uid=$uid")

        val allDelivered = db.collection("missions")
            .whereEqualTo("volunteer_id", uid)
            .whereEqualTo("status", "delivered")
            .get()
            .await()
            .documents

        Log.d(TAG, "getImpactStats: found ${allDelivered.size} delivered missions")

        val now = Calendar.getInstance()
        val startOfMonth = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
        }
        val startOfYear = Calendar.getInstance().apply {
            set(Calendar.MONTH, 0)
            set(Calendar.DAY_OF_MONTH, 1)
        }

        var mealsMonth = 0
        var mealsYear  = 0
        var total      = 0

        for (doc in allDelivered) {
            val meals       = (doc.getLong("mealsCount") ?: 0).toInt()
            val deliveredAt = doc.getTimestamp("deliveredAt")?.toDate()

            total += meals

            if (deliveredAt != null) {
                if (!deliveredAt.before(startOfYear.time)) mealsYear  += meals
                if (!deliveredAt.before(startOfMonth.time)) mealsMonth += meals
            }
        }

        val co2 = total * 2.5   // 2.5 kg CO₂ per meal saved

        return VolunteerImpact(
            mealsThisMonth  = mealsMonth,
            mealsThisYear   = mealsYear,
            deliveriesTotal = allDelivered.size,
            co2SavedKg      = co2
        )
    }

    /**
     * Fetches the last N delivered missions for the history list.
     */
    suspend fun getDeliveryHistory(uid: String, limit: Long = 20): List<VolunteerMission> {
        return db.collection("missions")
            .whereEqualTo("volunteer_id", uid)
            .whereEqualTo("status", "delivered")
            .orderBy("deliveredAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(limit)
            .get()
            .await()
            .documents
            .mapNotNull { doc ->
                try { doc.toObject(VolunteerMission::class.java)?.copy(id = doc.id) }
                catch (e: Exception) { null }
            }
    }
}
