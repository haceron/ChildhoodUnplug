package com.ppp.pegasussociety

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.ppp.pegasussociety.Login.LoginScreen
import com.ppp.pegasussociety.Login.OtpScreen
import com.ppp.pegasussociety.Signup.SignupScreen
import com.ppp.pegasussociety.navigation.AuthScreen
import com.ppp.pegasussociety.navigation.Graph

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RootNavigationGraph(loginStatus: Boolean) {
    val navController = rememberAnimatedNavController()

    AnimatedNavHost(
        navController = navController,
        startDestination = if (loginStatus) Graph.MAIN else Graph.AUTHENTICATION,
        route = Graph.ROOT
    ) {
        // --- Authentication Graph (No Bottom Bar) ---
        navigation(
            startDestination = AuthScreen.Login.route,
            route = Graph.AUTHENTICATION
        ) {
  /*          composable(AuthScreen.Login.route) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(Graph.MAIN) {
                            popUpTo(Graph.AUTHENTICATION) { inclusive = true }
                        }
                    },
                    onNavigateToSignUp = { navController.navigate(AuthScreen.SignUp.route) }
                )
            }*/
            composable(AuthScreen.SignUp.route) {
                SignupScreen(navController)
            }
            composable(
                route = AuthScreen.Otp.route,
                arguments = listOf(navArgument("userIdentifier") { type = NavType.StringType })
            ) { backStackEntry ->
                val userIdentifier = backStackEntry.arguments?.getString("userIdentifier") ?: ""
                OtpScreen(navController, userIdentifier)
            }
        }

        // --- Main App Graph (With Bottom Bar) ---
        navigation(
            startDestination = BottomNavItem.Activity.route,
            route = Graph.MAIN
        ) {
            // This single composable holds the Scaffold with the bottom bar and its own NavHost
            composable(BottomNavItem.Activity.route) { MainAppScreen() }
            composable(BottomNavItem.Timer.route) { MainAppScreen() }
            composable(BottomNavItem.Profile.route) { MainAppScreen() }
        }
    }
}