// bite_app/app/src/main/java/com.foodRescue/data/model/Donation.kt
package com.foodRescue.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ServerTimestamp

// ─── Sub-models ──────────────────────────────────────────────────────────────

data class FoodAnalysis(
    val items: List<Map<String, Any>> = emptyList(),
    val estimatedMeals: Int = 0,
    val freshness: String = "good",   // "good" | "fair" | "poor"
    val foodType: String = "veg",
    val confidence: Double = 0.0
)

data class MatchedNGO(
    val ngoId: String = "",
    val ngoName: String = "",
    val mealsAllocated: Int = 0,
    val status: String = "pending"    // pending|accepted|declined|delivered
)

// donorMetadata sub-document (matches Firestore schema exactly)
data class DonorMetadata(
    val vegStatus: String = "veg",       // "veg" | "non-veg" | "mixed"
    val urgency: String = "medium",      // "low" | "medium" | "high"
    val weightKg: Double = 5.0,
    val portions: Int = 20,
    val shelfLifeHours: Int = 3,
    val allergens: List<String> = emptyList(), // ["Nuts", "Dairy", "Gluten", "Eggs"]
    val contactName: String = "",
    val contactPhone: String = "",
    val notes: String = ""
)

// ─── Root donation document ───────────────────────────────────────────────────

data class Donation(
    val id: String = "",
    val donorId: String = "",
    val donorName: String = "",
    val photoUrl: String = "",
    val pickupLocation: GeoPoint? = null,   // Firestore GeoPoint — NOT a Map<String,Double>
    val status: String = "posted",           // posted|analyzed|matched|in_transit|delivered
    @ServerTimestamp
    val createdAt: Timestamp? = null,        // null triggers @ServerTimestamp on write
    val expiresAt: Timestamp? = null,        // populated by Cloud Function after analysis
    val aiAnalysis: FoodAnalysis = FoodAnalysis(),
    val safetyTier: String = "green",        // "green" | "yellow" | "delist"
    val matchedTo: List<MatchedNGO> = emptyList(),
    val volunteerId: String = "",
    val escalationLevel: Int = 0,
    val donorMetadata: DonorMetadata = DonorMetadata()
)
