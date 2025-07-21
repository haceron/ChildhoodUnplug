package com.ppp.pegasussociety

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import androidx.navigation.navArgument

import com.ppp.pegasussociety.Login.LoginScreen
import com.ppp.pegasussociety.Login.OtpScreen
//import com.ppp.pegasussociety.Signup.PrivacyPolicyScreen
import com.ppp.pegasussociety.Signup.SignupScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavigationComponent(loginStatus: Boolean) {
    val navController = rememberAnimatedNavController()

    AnimatedNavHost(
        navController = navController,
        startDestination = "login",
       // startDestination = "home",
        enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn() },
        exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut() },
        popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn() },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut() }
    ) {
     /*   composable("splash") {
            SplashScreen(navController, loginStatus)
        }
*/
        composable("login") {
            LoginScreen(navController)
        }

        composable("signup") {
            SignupScreen(navController)
        }

        composable(
            "otpscreen/{userIdentifier}",
            arguments = listOf(navArgument("userIdentifier") { type = NavType.StringType })
        ) { backStackEntry ->
            val userIdentifier = backStackEntry.arguments?.getString("userIdentifier") ?: ""
            OtpScreen(navController, userIdentifier)
        }

      /*  composable("dashboard") {
            DashboardScreen(navController)
        }*/

     /*   composable("scanner") {
            BarcodeScannerScreen(navController)
        }*/

/*        composable(
            route = "book_info/{title}",
            arguments = listOf(navArgument("title") { type = NavType.StringType })
        ) { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title") ?: ""
            BookInfoScreen(navController = navController, title = title)
        }*/


/*
        composable(
            route = "book_info/{isbn}",
            arguments = listOf(navArgument("isbn") { type = NavType.StringType })
        ) { backStackEntry ->
            val isbn = backStackEntry.arguments?.getString("isbn") ?: ""
            BookInfo(navController = navController, isbn = isbn)
        }
*/

/*        composable(
            route = "book_info/{isbn}",
            arguments = listOf(navArgument("isbn") { type = NavType.StringType })
        ) { backStackEntry ->
            val isbn = backStackEntry.arguments?.getString("isbn") ?: ""
            BookInfo(navController = navController, isbn = isbn)
        }

        composable(
            route = "book_info/image?path={path}",
            arguments = listOf(navArgument("path") {
                type = NavType.StringType
                nullable = true
                defaultValue = ""
            })
        ) { backStackEntry ->
            val path = backStackEntry.arguments?.getString("path") ?: ""
            BookInfo(navController = navController, isbn = "", imagePath = path)
        }*/


        /*     composable(
                 route = "book_info/image?path={path}",
                 arguments = listOf(
                     navArgument("path") {
                         type = NavType.StringType
                         nullable = false
                         defaultValue = ""
                     }
                 )
             ) { backStackEntry ->
                 val path = backStackEntry.arguments?.getString("path") ?: ""
                 BookInfo(navController = navController, isbn = "", imagePath = path)
             }
     *//*
        composable("library") {
            LibraryScreen(navController)
        }

        composable(
            "otpscreen/{userIdentifier}",
            arguments = listOf(navArgument("userIdentifier") { type = NavType.StringType })
        ) { backStackEntry ->
            val userIdentifier = backStackEntry.arguments?.getString("userIdentifier") ?: ""
            OtpScreen(navController, userIdentifier)
        }

        composable("privacypolicy") {
            PrivacyPolicyScreen(navController)
        }

        composable("home"){
            HomeScreen(navController)
        }*/
    }
}
