package com.v1.acro.navigation
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val icon: ImageVector
) {
    object Home: BottomNavItem("home", Icons.Default.Home)
    object Search: BottomNavItem("search", Icons.Default.Search)
    object Cart: BottomNavItem("cart", Icons.Default.ShoppingCart)
    object Profile: BottomNavItem("profile", Icons.Default.Person)
}


