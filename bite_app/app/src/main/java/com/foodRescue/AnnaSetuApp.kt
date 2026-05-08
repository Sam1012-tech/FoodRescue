// bite_app/app/src/main/java/com.foodRescue/AnnaSetuApp.kt
package com.foodRescue

import android.app.Application
import com.google.firebase.FirebaseApp

class AnnaSetuApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}
