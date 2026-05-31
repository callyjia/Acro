package com.v1.acro.navigation
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector


sealed class Item(
    val route: String,
    val icon: ImageVector,
    val label: String? =null
) {
    object Home: Item("home", Icons.Default.Home,"Home")
    object Menu: Item("menu", Icons.Default.Menu,"Menu")
    object Account: Item("Account", Icons.Default.Person2,"Account")
    object Receipt: Item("receipt",Icons.Default.Receipt,"Order History")
    object QR: Item("QrPayment",Icons.Default.QrCode2,"QrPayment")
    object Product: Item("Product", Icons.Default.Inventory,"All Products")
    object AddProduct: Item("AddProduct", Icons.Default.AddBox, "Add Products")
    object Analytics: Item("Analytics", Icons.Default.Analytics,"Analytics")

}


