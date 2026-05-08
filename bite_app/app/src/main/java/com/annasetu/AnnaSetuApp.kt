// bite_app/app/src/main/java/com/annasetu/AnnaSetuApp.kt
package com.annasetu

import android.app.Application
import com.google.firebase.FirebaseApp

class AnnaSetuApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}
