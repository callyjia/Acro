package com.v1.acro.navigation


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn

import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import androidx.navigation.compose.*
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.*
import androidx.compose.material.ripple.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.*

import com.v1.acro.viewmodel.CartViewModel
import com.v1.acro.ui.theme.*
import com.v1.acro.uiscreens.*


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
                    HomeScreen(navController = navController)
                }
            }
        }
    }
}


// BOTTOM NAVIGATION BAR
@Composable
fun BottomBarNavigation(navController: NavController) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MidBlue)
            .navigationBarsPadding()
    ) {
        // left and right items
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // left items
            NavItemButton(Item.Home, currentRoute, navController)


            // middle spacer for FAB
            Spacer(Modifier.width(80.dp))

            // right items
            NavItemButton(Item.Receipt, currentRoute, navController)
        }

            // Payment Button
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-20).dp)
                .size(72.dp)
                .border(5.dp, MidBlue, RoundedCornerShape(20.dp))
                .clip(RoundedCornerShape(20.dp))
                .background(White)
                .clickable (indication = ripple(color = RippleBlue),interactionSource = remember { MutableInteractionSource()})
                {
                    navController.navigate(Item.QR.route)
                },
                contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Item.QR.icon,
                contentDescription = Item.QR.label,
                tint = MidBlue,
                modifier = Modifier.size(36.dp)
            )
        }
    }
}

@Composable
fun NavItemButton(item: Item, currentRoute: String?, navController: NavController) {
    val isSelected = currentRoute == item.route
    IconButton(onClick = {
        navController.navigate(item.route) {
            popUpTo(navController.graph.startDestinationId)
            launchSingleTop = true
        }
    }) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.route,
            tint = Color.White
        )
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


//DEFAULT HEADER
@Composable
fun Header(navController: NavController) {

    Column(modifier = Modifier
        .fillMaxWidth()
        .background(MidBlue)
        .statusBarsPadding()
        .padding(8.dp)

    ) {
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Acro",
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = { /* handle click */ }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = Color.White,
                    modifier = Modifier
                        .size(36.dp)
                )
            }
        }
        Spacer(Modifier.height(12.dp))
    }

}