package com.v1.acro.navigation
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector


sealed class Item(
    val route: String,
    val icon: ImageVector,
    val label: String? =null
) {
    object Home: Item("home", Icons.Default.Home)
    object Menu: Item("menu", Icons.Default.Menu)
    object Receipt: Item("receipt",Icons.Default.Receipt,"History")
    object QR: Item("QrPayment",Icons.Default.QrCode2,"QrPayment")

}


