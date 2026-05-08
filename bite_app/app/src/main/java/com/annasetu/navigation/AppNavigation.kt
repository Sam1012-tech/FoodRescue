// bite_app/app/src/main/java/com/annasetu/navigation/AppNavigation.kt
package com.annasetu.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.annasetu.ui.auth.LoginScreen
import com.annasetu.ui.donor.DonorHomeScreen
import com.annasetu.ui.donor.PostFoodScreen
import com.annasetu.ui.ngo.NGOHomeScreen
import com.annasetu.ui.volunteer.VolunteerHomeScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                onNavigateToHome = { role ->
                    val route = when (role) {
                        "donor" -> "donor_home"
                        "ngo" -> "ngo_home"
                        "volunteer" -> "volunteer_home"
                        else -> "login"
                    }
                    navController.navigate(route) {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
        composable("donor_home") { 
            DonorHomeScreen(onNavigateToPost = { navController.navigate("post_food") }) 
        }
        composable("post_food") { 
            PostFoodScreen(onBack = { navController.popBackStack() }) 
        }
        composable("ngo_home") { NGOHomeScreen() }
        composable("volunteer_home") { VolunteerHomeScreen() }
    }
}
