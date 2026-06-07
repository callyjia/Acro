package com.v1.acro.uiscreens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.v1.acro.database.Product.ProductData
import com.v1.acro.viewmodel.ProductViewModel

/**
 * ============================================================
 * ProductListScreen.kt
 * ============================================================
 *
 * Displays all products from Room DB in a searchable, adjustable grid.
 *
 * STRUCTURE:
 *   ProductList (entry point)
 *     ├── SearchBar       → filters by name, ID, price, quantity
 *     ├── GridControls    → toggle between 1, 2, 4 columns
 *     └── ProductGrid     → LazyVerticalGrid
 *           └── ProductCard → individual product display
 *
 * DATA:
 *   - Loads from Room DB via ProductViewModel.allProducts (StateFlow)
 *   - Auto-updates when a product is added/deleted
 *   - Filtering done in-memory on the collected list
 *
 * UPDATE NOTE:
 *   - ProductDetail navigation on card click is wired.
 *   - Stock turns red (error color) when quantity is 0 (ProductCard).
 *
 * TODO:
 *   - Show the captured product photo (Coil AsyncImage) instead of the placeholder icon
 *   - Add price range filter
 *
 * NAVIGATION:
 *   Route: "Product"
 *   Entry: HomeScreen TaskCard → navController.navigate("Product")
 * ============================================================
 */

@Composable
fun ProductList(
    navController: NavController,
    viewModel: ProductViewModel = viewModel()
) {
    var searchQuery by remember { mutableStateOf("") }
    var columns by remember { mutableStateOf(2) }

    // Live data from Room — recomposes when DB changes
    val products by viewModel.allProducts.collectAsState()

    // Filter in-memory based on search query
    val filteredProducts = remember(searchQuery, products) {
        if (searchQuery.isBlank()) {
            products
        } else {
            products.filter { product ->
                product.name.contains(searchQuery, ignoreCase = true) ||
                        product.pid.toString().contains(searchQuery) ||
                        product.price.toString().contains(searchQuery) ||
                        product.quantity.toString().contains(searchQuery)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        SearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it }
        )
        Spacer(modifier = Modifier.height(12.dp))
        GridControls(
            selected = columns,
            onSelect = { columns = it }
        )
        Spacer(modifier = Modifier.height(12.dp))

        if (filteredProducts.isEmpty()) {
            // Empty state — no products or no search match
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (searchQuery.isBlank()) "No products yet"
                    else "No results found",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    fontSize = 14.sp
                )
            }
        } else {
            ProductGrid(
                products = filteredProducts,
                columns = columns,
                onProductClick = { product ->
                    navController.navigate("productDetail/${product.pid}")
                }
            )
        }
    }
}

/**
 * Search input — single field that matches across all product fields
 */
@Composable
fun SearchBar(query: String, onQueryChange: (String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = {
            Text(
                "Search by name, ID, price, or quantity",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        },
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "Search",
                tint = MaterialTheme.colorScheme.primary
            )
        },
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            cursorColor = MaterialTheme.colorScheme.primary
        )
    )
}

/**
 * Column count toggle — selected fills with primary, unselected shows outline
 */
@Composable
fun GridControls(selected: Int, onSelect: (Int) -> Unit) {
    val options = listOf(1, 2, 4)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "View",
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
        options.forEach { count ->
            val isSelected = selected == count
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surface
                    )
                    .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
                    .clickable { onSelect(count) }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "$count",
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onSurface,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

/**
 * Product grid — LazyVerticalGrid with dynamic column count
 */
@Composable
fun ProductGrid(
    products: List<ProductData>,
    columns: Int,
    onProductClick: (ProductData) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(products) { product ->
            ProductCard(
                product = product,
                onClick = { onProductClick(product) }
            )
        }
    }
}

/**
 * Single product card — surface auto-switches, stock turns error color when 0
 */
@Composable
fun ProductCard(product: ProductData, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Product image placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = product.name,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                    modifier = Modifier.size(40.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))

            // Product name
            Text(
                text = product.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )

            // Product ID
            Text(
                text = "ID: ${product.pid}",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )

            // Stock count — error color when out of stock
            Text(
                text = "Stock: ${product.quantity}",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = if (product.quantity > 0)
                    MaterialTheme.colorScheme.onSurface
                else
                    MaterialTheme.colorScheme.error
            )
        }
    }
}