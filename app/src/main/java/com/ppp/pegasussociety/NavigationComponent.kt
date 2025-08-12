package com.ppp.pegasussociety

import android.net.Uri
import androidx.compose.animation.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.animation.composable
import androidx.navigation.navArgument

import com.ppp.pegasussociety.Screens.ContentScreen
import com.ppp.pegasussociety.Screens.HeartyBottomNavigationBar
import com.ppp.pegasussociety.Screens.HeartyHomeScreen
//import com.ppp.pegasussociety.Signup.PrivacyPolicyScreen

/*

@OptIn(ExperimentalAnimationApi::class, ExperimentalFoundationApi::class)
@Composable
fun NavigationComponent(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onBackgroundColorChange: (Color) -> Unit
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = "logscreen",
        modifier = modifier,
        enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn() },
        exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut() },
        popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn() },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut() }
    ) {
        composable("login") {
            LaunchedEffect(Unit) { onBackgroundColorChange(Color.White) }
            LoginScreen(navController)
        }
        composable("signup") {
            LaunchedEffect(Unit) { onBackgroundColorChange(Color.White) }
            SignupScreen(navController)
        }
        composable(
            "otpscreen/{userIdentifier}",
            arguments = listOf(navArgument("userIdentifier") { type = NavType.StringType })
        ) { backStackEntry ->
            LaunchedEffect(Unit) { onBackgroundColorChange(Color.White) }
            val userIdentifier = backStackEntry.arguments?.getString("userIdentifier") ?: ""
            OtpScreen(navController, userIdentifier)
        }
        composable("profile") {
            LaunchedEffect(Unit) { onBackgroundColorChange(Color(0xFFE6E6FA)) }
            ProfileScreen(navController)
        }
        composable("screen_timer") {
            LaunchedEffect(Unit) { onBackgroundColorChange(Color(0xFFF0F4F8)) }
            ScreenTimerScreen(navController)
        }
        composable("home") {
            HeartyHomeScreen(onBackgroundColorChange)
        }
        composable("logscreen") {
            LogScreen(navController)
        }
    }
}
*/sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Timer : Screen("screen_timer")
    object Profile : Screen("profile")
    object Content : Screen("article/{articleId}") {
        fun createRoute(articleId: Int) = "article/$articleId"
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    var backgroundColor by remember { mutableStateOf(Color.White) }

    Scaffold(
        bottomBar = { HeartyBottomNavigationBar(navController) },
        containerColor = backgroundColor
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route, // ✅ Start at home
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Home.route) {
                HeartyHomeScreen(
                    onBackgroundColorChange = { backgroundColor = it },
                    navController = navController
                )
            }

            composable(Screen.Timer.route) {
                Text(
                    "Timer Screen",
                    modifier = Modifier.fillMaxSize(),
                    textAlign = TextAlign.Center
                )
            }

            composable(Screen.Profile.route) {
                Text(
                    "Profile Screen",
                    modifier = Modifier.fillMaxSize(),
                    textAlign = TextAlign.Center
                )
            }

            composable(
                route = Screen.Content.route, // ✅ matches createRoute()
                arguments = listOf(navArgument("articleId") { type = NavType.IntType })
            ) { backStackEntry ->
                val articleId = backStackEntry.arguments?.getInt("articleId") ?: -1
                ContentScreen(articleId)
            }
        }
    }
}


/*@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavigationComponent(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onBackgroundColorChange: (androidx.compose.ui.graphics.Color) -> Unit
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = "logscreen",
        modifier = modifier,
        enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn() },
        exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut() },
        popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn() },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut() }
    ) {
        composable("login") {
            // Set a static background for this screen
            LaunchedEffect(Unit) { onBackgroundColorChange(androidx.compose.ui.graphics.Color.White) }
            LoginScreen(navController)
        }

        composable("signup") {
            LaunchedEffect(Unit) { onBackgroundColorChange(androidx.compose.ui.graphics.Color.White) }
            SignupScreen(navController)
        }

        composable(
            "otpscreen/{userIdentifier}",
            arguments = listOf(navArgument("userIdentifier") { type = NavType.StringType })
        ) { backStackEntry ->
            LaunchedEffect(Unit) { onBackgroundColorChange(androidx.compose.ui.graphics.Color.White) }
            val userIdentifier = backStackEntry.arguments?.getString("userIdentifier") ?: ""
            OtpScreen(navController, userIdentifier)
        }

        composable("profile") {
            LaunchedEffect(Unit) { onBackgroundColorChange(Color(0xFFE6E6FA)) } // Light purple
            ProfileScreen(navController)
        }

          composable("screen_timer") {
            LaunchedEffect(Unit) { onBackgroundColorChange(Color(0xFFF0F4F8)) } // Light blue-gray
            ScreenTimerScreen(navController)
        }

        composable("home") {
            HeartyHomeScreen(
                onBackgroundColorChange = onBackgroundColorChange,
                onCardClick = { title, content, imageUrl ->
                    navController.navigate(
                        "detail/${Uri.encode(title)}/${Uri.encode(content)}/${Uri.encode(imageUrl)}"
                    )
                }
            )
        }

        composable("logscreen") {
            LogScreen(navController)
        }

        composable(
            "detail/{title}/{content}/{imageUrl}",
            arguments = listOf(
                navArgument("title") { type = NavType.StringType },
                navArgument("content") { type = NavType.StringType },
                navArgument("imageUrl") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title") ?: ""
            val content = backStackEntry.arguments?.getString("content") ?: ""
            val imageUrl = backStackEntry.arguments?.getString("imageUrl") ?: ""
            DetailScreen(title, content, imageUrl)
        }
    }



    *//*  composable("home") {
          // HeartyHomeScreen dynamically controls the background itself
          HeartyHomeScreen(onBackgroundColorChange = onBackgroundColorChange)
      }


      composable("logscreen"){// Light purple
          LogScreen(navController)
      }*//*



   *//*     composable("log_screen_time") {
            LaunchedEffect(Unit) { onBackgroundColorChange(Color(0xFFE0F7FA)) } // Light cyan
            LogScreenTimeScreen(navController = navController)
        }*//*
    }
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

