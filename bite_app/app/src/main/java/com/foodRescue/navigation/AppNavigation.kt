// bite_app/app/src/main/java/com.foodRescue/navigation/AppNavigation.kt
package com.foodRescue.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.foodRescue.ui.auth.LoginScreen
import com.foodRescue.ui.donor.DonorHomeScreen
import com.foodRescue.ui.donor.PostFoodScreen
import com.foodRescue.ui.ngo.NGOHomeScreen
import com.foodRescue.ui.volunteer.VolunteerHomeScreen

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
