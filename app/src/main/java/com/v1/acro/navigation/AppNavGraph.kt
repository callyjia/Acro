package com.v1.acro.navigation


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import kotlinx.coroutines.launch
import androidx.compose.material3.*
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.*
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection

import com.v1.acro.ui.theme.*
import com.v1.acro.uiscreens.*
import androidx.compose.runtime.CompositionLocalProvider
import com.v1.acro.viewmodel.ProductViewModel


@Composable
fun AppNavGraph(cartViewModel: ProductViewModel) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    DrawerMenu(navController)
                }
            }
        ) {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                Scaffold(
                    containerColor = MaterialTheme.colorScheme.background,
                    topBar = { Header(navController, drawerState) },
                    bottomBar = { BottomBarNavigation(navController) }
                ) { paddingValues ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        NavHost(navController, startDestination = "home") {
                            composable("home") { HomeScreen(navController) }
                            composable("order") { OrderScreen(navController) }
                            composable("receipt") { OrderHistory(navController) }
                            composable("Product") { ProductList(navController) }
                            composable("Analytics") { Analytic(navController) }
                            composable("AddProduct") { AddProduct(navController) }
                            composable("Account") { ProfileAcc(navController) }
                            composable("QrPayment") { QrPayment(navController) }
                        }
                    }
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
            NavItemButton(Item.Account, currentRoute, navController)
        }

            // Payment Button
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-20).dp)
                .size(72.dp)
                .clip(RoundedCornerShape(20.dp))
                .border(5.dp, MidBlue, RoundedCornerShape(20.dp))
                .background(White, RoundedCornerShape(24.dp))
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


/*
* Item in Navigation Bar
*/
@Composable
fun NavItemButton(item: Item, currentRoute: String?, navController: NavController) {
    val isSelected = currentRoute == item.route
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = 8.dp).scale(1.2f)
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .size(40.dp)
            .clip(CircleShape)
            .clickable {
                navController.navigate(item.route) {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            }
            .wrapContentSize()

    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.route,
            tint = Color.White,
        )
        Text(
            text = item.label ?: "",
            fontSize = 8.sp,
            color = White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.offset(y=(-6).dp)
        )
    }
}

@Composable
fun NavContainer(navController: NavController,
                 cartViewModel: ProductViewModel,
                 modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.fillMaxSize()){

    }

}


//DEFAULT HEADER
@Composable
fun Header(navController: NavController, drawerState: DrawerState) {
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MidBlue)
            .statusBarsPadding()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Acro",
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }
        }
        Spacer(Modifier.height(12.dp))
    }
}