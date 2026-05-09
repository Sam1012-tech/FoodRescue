// bite_app/app/src/main/java/com/foodRescue/data/model/VolunteerMission.kt
package com.foodRescue.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint

/**
 * Represents an active or completed delivery mission assigned to a volunteer.
 * Mirrors the Firestore `missions/{missionId}` document written by the backend MatchingAgent.
 */
data class VolunteerMission(
    val id: String = "",
    val donationId: String = "",

    // Pickup point — the donor's location
    val donorName: String = "",
    val donorAddress: String = "",
    val donorLocation: GeoPoint? = null,

    // Drop-off point — the NGO's location
    val ngoId: String = "",
    val ngoName: String = "",
    val ngoAddress: String = "",
    val ngoLocation: GeoPoint? = null,

    // Mission meta
    val status: String = "matched",   // matched | picked_up | delivered | cancelled
    val assignedAt: Timestamp? = null,
    val pickedUpAt: Timestamp? = null,
    val deliveredAt: Timestamp? = null,

    // Payload summary (for display + impact stats)
    val mealsCount: Int = 0,
    val weightKg: Double = 0.0,
    val foodType: String = "veg",     // veg | non-veg | mixed
    val urgency: String = "medium",

    // AI explanation from matching agent
    val aiExplanation: Map<String, Any> = emptyMap()
)

/**
 * Aggregate impact stats for a volunteer's history.
 */
data class VolunteerImpact(
    val mealsThisMonth: Int = 0,
    val mealsThisYear: Int = 0,
    val deliveriesTotal: Int = 0,
    val co2SavedKg: Double = 0.0,     // 2.5 kg CO₂ per meal
    val reliabilityPct: Int = 100
)
