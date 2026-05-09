// bite_app/app/src/main/java/com/foodRescue/data/model/NGO.kt
package com.foodRescue.data.model

import com.google.firebase.firestore.GeoPoint

/**
 * Maps to the ngos/{ngoId} Firestore document.
 */
data class NGO(
    val id: String = "",
    val name: String = "",
    val address: String = "",
    val location: GeoPoint? = null,
    val capacityCurrent: Int = 0,
    val capacityMax: Int = 100,
    val activeStatus: String = "available",  // "available" | "full" | "offline"
    val reliabilityScore: Double = 1.0,      // 0.0 – 1.0
    val priority: Int = 1                    // higher = higher priority in matching
)
