package com.v1.acro.navigation
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector


sealed class NavItem(
    val route: String,
    val icon: ImageVector
) {
    object Home: NavItem("home", Icons.Default.Home)
    object Menu: NavItem("menu", Icons.Default.Menu)
}


