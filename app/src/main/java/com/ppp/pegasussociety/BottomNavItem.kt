package com.ppp.pegasussociety

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

// Defines the routes and properties for each bottom navigation item
sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    object Activity : BottomNavItem("activity", "Activity", Icons.Default.Explore)
    object Library : BottomNavItem("library", "Library", Icons.Default.VideoLibrary)
    object Timer : BottomNavItem("logscreen", "Screen Timer", Icons.Default.AccessTimeFilled)
    object Profile : BottomNavItem("profile", "Child Profile", Icons.Default.Face)
}