package com.ppp.pegasussociety.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTimeFilled
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

// Define the routes for your entire application
object Graph {
    const val ROOT = "root_graph"
    const val AUTHENTICATION = "auth_graph"
    const val MAIN = "main_graph"
}

sealed class AuthScreen(val route: String) {
    object Login : AuthScreen("login")
    object SignUp : AuthScreen("signup")
    object Otp : AuthScreen("otpscreen/{userIdentifier}") {
        fun createRoute(userIdentifier: String) = "otpscreen/$userIdentifier"
    }
}

sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    object Home : BottomNavItem("home", "Home", Icons.Default.Home)
    object Timer : BottomNavItem("screen_timer", "Timer", Icons.Default.AccessTimeFilled)
    object Profile : BottomNavItem("profile", "Profile", Icons.Default.AccountCircle)
}

// Route for screens navigated to from within the main graph
const val LOG_SCREEN_TIME_ROUTE = "logscreen"