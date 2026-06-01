package com.v1.acro.uiscreens

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
import androidx.navigation.NavController
import androidx.compose.foundation.BorderStroke
import com.v1.acro.ui.theme.*

/**
 * ============================================================
 * ProductListScreen.kt
 * ============================================================
 *
 * Displays all products in a searchable, adjustable grid.
 *
 * STRUCTURE:
 *   ProductList (entry point)
 *     ├── SearchBar       → filters by name, ID, price, quantity
 *     ├── GridControls    → toggle between 1, 2, 4 columns
 *     └── ProductGrid     → LazyVerticalGrid
 *           └── ProductCard → individual product display
 *
 * COLORS:
 *   - Brand elements (selected pill, text) → MaterialTheme.colorScheme.primary
 *   - Surfaces (cards, image placeholder) → MaterialTheme.colorScheme.surface
 *   - Secondary text (ID, stock) → MaterialTheme.colorScheme.onSurface
 *   - Out of stock → MaterialTheme.colorScheme.error
 *
 * TODO:
 *   - Replace Product data class with Room entity (model/Product.kt)
 *   - Replace fakeProducts with ViewModel + Room DB
 *   - Add product image loading (Coil/Glide)
 *   - Add ProductDetail navigation on card click
 *   - Add price range filter
 *
 * NAVIGATION:
 *   Route: "Product" (defined in Item.kt)
 *   Entry: HomeScreen TaskCard → navController.navigate("Product")
 * ============================================================
 */

// TODO: Move to model/Product.kt when setting up Room DB
data class Product(
    val id: String,
    val name: String,
    val price: Double,
    val quantity: Int,
    val imageUrl: String? = null
)

// TODO: Remove when Room DB is ready — replace with ViewModel data
val fakeProducts = listOf(
    Product("P001", "Indomie Goreng", 3500.0, 50),
    Product("P002", "Aqua 600ml", 4000.0, 30),
    Product("P003", "Teh Botol", 5000.0, 25),
    Product("P004", "Chitato", 8000.0, 15),
    Product("P005", "Pocari Sweat", 7500.0, 20),
    Product("P006", "Kopi Kapal Api", 2500.0, 100),
    Product("P007", "Milo", 6000.0, 40),
    Product("P008", "Roma Kelapa", 4500.0, 60)
)

/**
 * Entry point — called from NavHost composable("Product")
 * Manages search state and grid column count
 */
@Composable
fun ProductList(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    var columns by remember { mutableStateOf(2) }

    val filteredProducts = remember(searchQuery) {
        if (searchQuery.isBlank()) {
            fakeProducts
        } else {
            fakeProducts.filter { product ->
                product.name.contains(searchQuery, ignoreCase = true) ||
                        product.id.contains(searchQuery, ignoreCase = true) ||
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
        ProductGrid(
            products = filteredProducts,
            columns = columns,
            onProductClick = { product ->
                // TODO: navController.navigate("productDetail/${product.id}")
            }
        )
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
    products: List<Product>,
    columns: Int,
    onProductClick: (Product) -> Unit
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
fun ProductCard(product: Product, onClick: () -> Unit) {
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
                    .background(MaterialTheme.colorScheme.surface),
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
                text = "ID: ${product.id}",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                modifier = Modifier.offset(y = (-2).dp)
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