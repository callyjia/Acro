package com.v1.acro.uiscreens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.v1.acro.database.Product.ProductData
import com.v1.acro.viewmodel.ProductViewModel
import androidx.compose.foundation.clickable

/**
 * ============================================================
 * ProductDetail.kt
 * ============================================================
 *
 * Edit screen for a single product.
 *
 * STRUCTURE:
 *   ProductDetail (entry point)
 *     ├── ProductFormField  → edit name
 *     ├── ProductNumberField → edit price, quantity
 *     ├── UpdateButton      → saves changes to DB
 *     └── DeleteButton      → removes product from DB
 *
 * UPDATE NOTE: unchanged this update (edit/delete already worked). Listed here so
 * other devs know it was reviewed and is current. Future: allow editing the photo.
 *
 * NAVIGATION:
 *   Route: "productDetail/{pid}"
 *   Entry: ProductList ProductCard → navController.navigate("productDetail/${product.pid}")
 * ============================================================
 */

@Composable
fun ProductDetail(
    navController: NavController,
    pid: Int,
    viewModel: ProductViewModel = viewModel()
) {
    val products by viewModel.allProducts.collectAsState()
    val product = products.find { it.pid == pid }

    // If product not found (deleted or invalid ID)
    if (product == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Product not found",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
        return
    }

    // Editable fields — initialized from current product data
    var editName by remember(product) { mutableStateOf(product.name) }
    var editPrice by remember(product) { mutableStateOf(formatWithDots(product.price.toInt().toString())) }
    var editQuantity by remember(product) { mutableStateOf(product.quantity.toString()) }

    var priceError by remember { mutableStateOf(false) }
    var quantityError by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header with product ID
        Text(
            text = "Edit Product",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "Product ID: ${product.pid}",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )

        // Name
        ProductFormField(
            label = "Product Name",
            value = editName,
            onValueChange = { editName = it },
            placeholder = "e.g. Indomie Goreng"
        )

        // Price
        ProductNumberField(
            label = "Price",
            value = editPrice,
            onValueChange = {
                editPrice = it
                priceError = false
            },
            placeholder = "e.g. 3.500",
            isError = priceError,
            errorMessage = "Price can only contain numbers"
        )

        // Quantity
        ProductNumberField(
            label = "Quantity",
            value = editQuantity,
            onValueChange = {
                editQuantity = it
                quantityError = false
            },
            placeholder = "e.g. 50",
            isError = quantityError,
            errorMessage = "Quantity can only contain numbers"
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Update button
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    val rawPrice = editPrice.replace(".", "")
                    val rawQuantity = editQuantity.replace(".", "")

                    val price = rawPrice.toDoubleOrNull()
                    val quantity = rawQuantity.toIntOrNull()

                    priceError = price == null && editPrice.isNotBlank()
                    quantityError = quantity == null && editQuantity.isNotBlank()

                    if (editName.isNotBlank() && price != null && quantity != null) {
                        viewModel.updateProduct(
                            product.copy(
                                name = editName,
                                price = price,
                                quantity = quantity
                            )
                        )
                        navController.popBackStack()
                    }
                },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Update Product",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Delete button
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDeleteDialog = true },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.onError
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Delete Product",
                    color = MaterialTheme.colorScheme.onError,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    // Delete confirmation dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text(
                    text = "Delete Product",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text("Are you sure you want to delete ${product.name}?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteProduct(product)
                        showDeleteDialog = false
                        navController.popBackStack()
                    }
                ) {
                    Text(
                        "Delete",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}