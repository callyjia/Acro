package com.v1.acro.navigation

import android.R.attr.background
import android.R.id.background
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.navigation.currentBackStackEntryAsState
import com.v1.acro.uiscreens.CartScreen
import com.v1.acro.uiscreens.HomeScreen
import com.v1.acro.uiscreens.OrderSuccessScreen
import com.v1.acro.uiscreens.PaymentScreen
import com.v1.acro.uiscreens.ProfileScreen
import com.v1.acro.uiscreens.SearchScreen
import com.v1.acro.viewmodel.CartViewModel

@Composable
fun AppNavGraph(cartViewModel: CartViewModel) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomBarNavigation(navController)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            NavHost(
                navController, startDestination = "home"
            ) {
                composable("home") {
                    HomeScreen()
                }
                composable("cart") {
                    CartScreen(navController, cartViewModel)
                }
                composable("payment") {
                    PaymentScreen(navController)
                }
                composable("success") {
                    OrderSuccessScreen(navController, cartViewModel)
                }
                composable("search") {
                    SearchScreen()
                }
                composable("profile") {
                    ProfileScreen()
                }
            }
        }

    }
}

@Composable
fun BottomBarNavigation(navController: NavController){

    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Search,
        BottomNavItem.Cart,
        BottomNavItem.Profile,
    )

    NavigationBar(
        modifier = Modifier.background(
            Brush.verticalGradient(
                colors = listOf(
                    Color(0xff6a1b9a),
                    Color(0xff8e24aa),
                )
            )
        ),
        containerColor = Color.Transparent,
        tonalElevation = 6.dp

    ) {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

        items.forEach { item ->
            val selected = currentRoute == item.route

            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },
                icon = {
                    Icon(imageVector = item.icon,
                        contentDesciption = "Icon",
                        tint = Color.White)
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.White.copy(alpha = 0.18f)
                )
            )
        }
    }
}

@Composable
fun Icon(imageVector: ImageVector, contentDesciption: String, tint: Color) {
    TODO("Not yet implemented")
}