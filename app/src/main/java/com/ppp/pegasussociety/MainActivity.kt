package com.ppp.pegasussociety

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import com.ppp.pegasussociety.Screens.LogScreen
import com.ppp.pegasussociety.Screens.ScreenTimerScreen

import com.ppp.pegasussociety.ui.theme.PegasusSocietyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)


        setContent {
            PegasusSocietyTheme {
                MainAppScreen()
             //  NavigationComponent(loginStatus = false)
               //   ProfileScreen(navController = null)
              // ScreenTimerScreen(navController = null)
              //  ArticleScreen()
              //  HeartyHomeScreen(navController = null)
              //  ProfileScreen()
             //   LogScreenTimeScreen(navController = null)
             //  LogScreen(navController = null)
            }
        }
    }
}
