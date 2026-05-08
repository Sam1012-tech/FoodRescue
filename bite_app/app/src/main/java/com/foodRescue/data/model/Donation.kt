// bite_app/app/src/main/java/com.foodRescue/data/model/Donation.kt
package com.foodRescue.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint

data class FoodAnalysis(
    val items: List<Map<String, Any>> = emptyList(),
    val estimatedMeals: Int = 0,
    val freshness: String = "good",
    val foodType: String = "veg"
)

data class MatchedNGO(
    val ngoId: String = "",
    val ngoName: String = "",
    val mealsAllocated: Int = 0,
    val status: String = "pending" // pending|accepted|declined|delivered
)

data class Donation(
    val id: String = "",
    val donorId: String = "",
    val donorName: String = "",
    val pickupLocation: GeoPoint? = null,
    val photoUrl: String = "",
    val aiAnalysis: FoodAnalysis = FoodAnalysis(),
    val status: String = "posted", // posted|matched|in_transit|delivered|expired|escalated
    val createdAt: Timestamp = Timestamp.now(),
    val expiresAt: Timestamp = Timestamp.now(),
    val matchedTo: List<MatchedNGO> = emptyList(),
    val volunteerId: String = "",
    val escalationLevel: Int = 0
)
