package com.v1.acro.navigation


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.*


import com.v1.acro.viewmodel.CartViewModel
import com.v1.acro.ui.theme.*
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun AppNavGraph(cartViewModel: CartViewModel) {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            Header(navController)
        },
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
                    NavContainer(navController = navController,
                        cartViewModel = cartViewModel)
                }
            }
        }
    }
}

@Composable
fun BottomBarNavigation(navController: NavController) {

    val items = listOf(
        NavItem.Home,
        NavItem.Menu
    )

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route



    val navBarItemColors = NavigationBarItemDefaults.colors(
        indicatorColor = Color.White.copy(alpha = 0.18f)
    )

    NavigationBar(
        modifier = Modifier.background(Blue),
        containerColor = Color.Transparent,
        tonalElevation = 6.dp
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.route,
                        tint = Color.White
                    )
                },
                colors = navBarItemColors
            )
        }
    }
}


@Composable
fun NavContainer(navController: NavController,
                 cartViewModel: CartViewModel,
                 modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.fillMaxSize()){

    }

}

@Composable
fun Header(navController: NavController) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .background(Blue)
        .padding(12.dp)

    ) {
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Acro",
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(Modifier.height(12.dp))

    }
}