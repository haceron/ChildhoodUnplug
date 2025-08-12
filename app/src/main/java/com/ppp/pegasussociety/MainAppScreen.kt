package com.ppp.pegasussociety

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ppp.pegasussociety.Screens.CategoryDetailScreen
import com.ppp.pegasussociety.Screens.ContentScreen
import com.ppp.pegasussociety.Screens.HeartyBottomNavigationBar
import com.ppp.pegasussociety.Screens.HeartyHomeScreen
import com.ppp.pegasussociety.Screens.LogScreen
import com.ppp.pegasussociety.Screens.ProfileScreen
import com.ppp.pegasussociety.Screens.ScreenTimerScreen
import com.ppp.pegasussociety.navigation.LOG_SCREEN_TIME_ROUTE

@Composable
fun MainAppScreen() {
    val navController = rememberNavController()
    // 1. Hoist the background color state to the top-level Scaffold.
    var backgroundColor by remember { mutableStateOf(Color(0xFFB4DB6F)) } // Default color

    Scaffold(
        // 2. Apply the dynamic background color to the entire Scaffold.
        containerColor = backgroundColor,
        bottomBar = { HeartyBottomNavigationBar(navController = navController) }
    ) { innerPadding ->
        // 3. The NavHost now switches screens within this single, stateful Scaffold.
        MainAppNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
            // Pass a lambda to allow child screens to change the background.
            onBackgroundColorChange = { newColor ->
                backgroundColor = newColor
            }
        )
    }
}
@Composable
fun MainAppNavHost(
    navController: NavHostController,
    modifier: Modifier,
    onBackgroundColorChange: (Color) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Activity.route,
        modifier = modifier
    ) {
        composable(BottomNavItem.Activity.route) {
            HeartyHomeScreen(
                onBackgroundColorChange = onBackgroundColorChange,
                navController = navController
            )
        }
        composable(BottomNavItem.Timer.route) {
            LaunchedEffect(Unit) {
                onBackgroundColorChange(Color(0xFFF0F4F8))
            }
            ScreenTimerScreen(navController)
        }
        composable(BottomNavItem.Profile.route) {
            LaunchedEffect(Unit) {
                onBackgroundColorChange(Color(0xFFE6E6FA))
            }
            LogScreen()
        }
        composable(
            route = "article/{articleId}",
            arguments = listOf(navArgument("articleId") { type = NavType.IntType })
        ) { backStackEntry ->
            val articleId = backStackEntry.arguments?.getInt("articleId") ?: -1
            ContentScreen(articleId)
        }

        composable(
            route = "category/{apiKey}/{displayName}",
            arguments = listOf(
                navArgument("apiKey") { type = NavType.StringType },
                navArgument("displayName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val apiKey = backStackEntry.arguments?.getString("apiKey") ?: ""
            val displayName = backStackEntry.arguments?.getString("displayName") ?: ""
            CategoryDetailScreen(apiKey, displayName, navController)
        }

    }
}


