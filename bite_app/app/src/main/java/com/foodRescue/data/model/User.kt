// bite_app/app/src/main/java/com.foodRescue/data/model/User.kt
package com.foodRescue.data.model

import com.google.firebase.firestore.GeoPoint

data class User(
    val uid: String = "",
    val role: String = "",        // "donor" | "ngo" | "volunteer" | "admin"
    val name: String = "",
    val phone: String = "",
    val fcmToken: String = "",
    val location: GeoPoint? = null,
    val rating: Double = 5.0,     // donor / volunteer reliability rating (1–5)
    val status: String = "offline" // volunteer: "idle" | "offline" | "on_mission"
)
