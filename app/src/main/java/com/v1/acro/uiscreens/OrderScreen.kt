package com.v1.acro.uiscreens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.v1.acro.database.Product.ProductData
import com.v1.acro.ui.theme.*
import com.v1.acro.viewmodel.CartItem
import com.v1.acro.viewmodel.CartViewModel
import com.v1.acro.viewmodel.ProductViewModel
import com.v1.acro.uiscreens.BarcodeScannerButton
import com.v1.acro.viewmodel.TransactionViewModel


/**
 * ============================================================
 * OrderScreen.kt
 * ============================================================
 *
 * POS order screen — build a cart, then checkout.
 *
 * STRUCTURE:
 *   OrderScreen (entry point)
 *     ├── BarcodeScanButton  → scan to add product
 *     ├── ProductPickerRow   → tap available products to add
 *     ├── CartList           → current order with +/- controls
 *     └── CheckoutBar        → running total + checkout button
 *
 * DATA:
 *   - Cart held in memory via CartViewModel (not saved until checkout)
 *   - Available products loaded from ProductViewModel
 *
 * UPDATE NOTE:
 *   - Barcode scan is wired: a scanned value is matched against products and
 *     added to the cart (ProductPickerCard).
 *   - Product picker shows live stock and is disabled/greyed when out of stock.
 *   - The inline cart list was replaced by a CART POPUP (CartDialog) opened from
 *     the cart icon (with item-count badge) above the total. Quantities are
 *     adjusted and lines removed from inside the popup.
 *   - Added an optional order-name field (blank → "order{id}") passed to checkout.
 *   - Screen is vertically scrollable.
 *
 * NAVIGATION:
 *   Route: "order"
 *   Entry: HomeScreen OrderButton → navController.navigate("order")
 * ============================================================
 */

@Composable
fun OrderScreen(
    navController: NavController,
    cartViewModel: CartViewModel = viewModel(),
    productViewModel: ProductViewModel = viewModel(),
    transactionViewModel: TransactionViewModel = viewModel()
) {
    val products by productViewModel.allProducts.collectAsState()
    val cartItems = cartViewModel.cartItems
    var orderName by remember { mutableStateOf("") }
    var showCart by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "New Order",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(12.dp))

        // Barcode scan — finds product by barcode and adds it to cart
        BarcodeScannerButton(
            onBarcodeScanned = { scannedValue ->
                val match = products.find { it.barcode == scannedValue }
                if (match != null) cartViewModel.addToCart(match)
            }
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Available products to tap and add
        Text(
            text = "Add Products",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(products) { product ->
                ProductPickerCard(
                    product = product,
                    onClick = { cartViewModel.addToCart(product) }
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Spacer(modifier = Modifier.height(16.dp))

        // Optional custom order name (blank → "order{id}")
        OutlinedTextField(
            value = orderName,
            onValueChange = { orderName = it },
            label = { Text("Order name (optional)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Checkout bar (cart icon + total + button)
        CheckoutBar(
            totalPrice = cartViewModel.totalPrice,
            totalCount = cartViewModel.totalCount,
            enabled = cartItems.isNotEmpty(),
            onCartClick = { showCart = true },
            onCheckout = {
                cartViewModel.checkout(orderName, transactionViewModel, productViewModel)
                orderName = ""
                navController.popBackStack()
            }
        )
    }

    // Cart popup — opened by the cart logo above the total
    if (showCart) {
        CartDialog(
            cartItems = cartItems,
            totalPrice = cartViewModel.totalPrice,
            onIncrease = { cartViewModel.increaseQuantity(it) },
            onDecrease = { cartViewModel.decreaseQuantity(it) },
            onRemove = { cartViewModel.removeFromCart(it) },
            onDismiss = { showCart = false }
        )
    }
}

/**
 * Cart popup — shows every product in the cart with +/- quantity and remove.
 * Opened from the cart logo in the CheckoutBar.
 */
@Composable
fun CartDialog(
    cartItems: List<CartItem>,
    totalPrice: Double,
    onIncrease: (ProductData) -> Unit,
    onDecrease: (ProductData) -> Unit,
    onRemove: (ProductData) -> Unit,
    onDismiss: () -> Unit
) {
    androidx.compose.ui.window.Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 520.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Cart",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close cart",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))

                if (cartItems.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Cart is empty",
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            fontSize = 14.sp
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f, fill = false),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(cartItems) { item ->
                            CartItemRow(
                                item = item,
                                onIncrease = { onIncrease(item.product) },
                                onDecrease = { onDecrease(item.product) },
                                onRemove = { onRemove(item.product) }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Total",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "CNY $totalPrice",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}



/**
 * Small tappable card for available products
 * Tap to add to cart
 */
@Composable
fun ProductPickerCard(product: ProductData, onClick: () -> Unit) {
    val inStock = product.quantity > 0
    Card(
        modifier = Modifier
            .width(100.dp)
            .alpha(if (inStock) 1f else 0.4f)
            .clickable(enabled = inStock) { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = product.name,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = product.name,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
            Text(
                text = "CNY ${product.price}",
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            // Stock — red when out of stock
            Text(
                text = if (inStock) "Stock: ${product.quantity}" else "Out of stock",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = if (inStock)
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                else
                    MaterialTheme.colorScheme.error
            )
        }
    }
}

/**
 * A single line in the cart — shows product, quantity, +/- and remove
 */
@Composable
fun CartItemRow(
    item: CartItem,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Name + subtotal
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.product.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "CNY ${item.product.price} x ${item.quantity} = ${item.product.price * item.quantity}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            // Quantity controls
            Row(verticalAlignment = Alignment.CenterVertically) {
                QuantityButton(icon = Icons.Default.Remove, onClick = onDecrease)
                Text(
                    text = "${item.quantity}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                QuantityButton(icon = Icons.Default.Add, onClick = onIncrease)
                IconButton(onClick = onRemove) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Remove ${item.product.name}",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

/**
 * Small circular +/- button
 */
@Composable
fun QuantityButton(icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(28.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.primary)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.size(18.dp)
        )
    }
}

/**
 * Bottom bar — shows running total and checkout button
 */
@Composable
fun CheckoutBar(
    totalPrice: Double,
    totalCount: Int,
    enabled: Boolean,
    onCartClick: () -> Unit,
    onCheckout: () -> Unit
) {
    Column {
        Spacer(modifier = Modifier.height(8.dp))

        // Cart logo (with item-count badge) above the total — tap to view cart
        Box(
            modifier = Modifier.align(Alignment.End)
        ) {
            BadgedBox(
                badge = {
                    if (totalCount > 0) {
                        Badge { Text("$totalCount") }
                    }
                }
            ) {
                IconButton(onClick = onCartClick) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "View cart",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Total ($totalCount items)",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Text(
                text = "CNY $totalPrice",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = enabled) { onCheckout() },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (enabled)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Checkout",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}