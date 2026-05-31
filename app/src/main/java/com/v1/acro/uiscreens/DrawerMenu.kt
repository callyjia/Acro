package com.v1.acro.uiscreens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.navigation.NavController

@Composable
fun DrawerMenu(navController: NavController) {
    ModalDrawerSheet(
        modifier = Modifier.fillMaxWidth(0.4f),
        drawerShape = RectangleShape
    ) {
        // menu content here
    }
}