package com.v1.acro.uiscreens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import com.v1.acro.ui.theme.*

/**
 * ============================================================
 * AddProduct.kt
 * ============================================================
 *
 * Form screen to add new products to inventory.
 *
 * STRUCTURE:
 *   AddProduct (entry point)
 *     ├── ImagePicker     → camera capture for product photo
 *     ├── BarcodeScanner  → ML Kit barcode scan (auto-fills fields)
 *     ├── ProductForm     → name, price, quantity inputs
 *     └── SaveButton      → commits to DB
 *
 * TODO:
 *   - Wire camera intent to ImagePicker
 *   - Wire ML Kit barcode scanner to BarcodeScanner
 *   - Connect SaveButton to Room DB insert
 *   - Add input validation (empty fields, invalid price/quantity)
 *   - Add success/error toast on save
 *
 * NAVIGATION:
 *   Route: "AddProduct"
 *   Entry: HomeScreen TaskCard → navController.navigate("AddProduct")
 * ============================================================
 */

@Composable
fun AddProduct(navController: NavController) {
    // Auto-generate ID based on existing product count
    // TODO: Replace with Room DB auto-increment when DB is ready
    val nextId = remember { "P${String.format("%03d", fakeProducts.size + 1)}" }

    var productName by remember { mutableStateOf("") }
    var productPrice by remember { mutableStateOf("") }
    var productQuantity by remember { mutableStateOf("") }
    var productImageUri by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Add New Product",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        // Show auto-generated ID (read-only)
        Text(
            text = "Product ID: $nextId",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )

        ImagePicker(
            imageUri = productImageUri,
            onCaptureClick = { /* TODO */ }
        )

        BarcodeScannerButton(onScanClick = { /* TODO */ })

        // No more Product ID field — auto-generated
        ProductFormField(
            label = "Product Name",
            value = productName,
            onValueChange = { productName = it },
            placeholder = "e.g. Indomie Goreng"
        )
        ProductFormField(
            label = "Price",
            value = productPrice,
            onValueChange = { productPrice = it },
            placeholder = "e.g. 3500",
            keyboardType = KeyboardType.Number
        )
        ProductFormField(
            label = "Quantity",
            value = productQuantity,
            onValueChange = { productQuantity = it },
            placeholder = "e.g. 50",
            keyboardType = KeyboardType.Number
        )

        Spacer(modifier = Modifier.weight(1f))

        SaveButton(onClick = { /* TODO */ })
    }
}

/**
 * Image preview with camera capture button
 * Shows placeholder when no image; preview when captured
 */
@Composable
fun ImagePicker(
    imageUri: String?,
    onCaptureClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable { onCaptureClick() },
        contentAlignment = Alignment.Center
    ) {
        if (imageUri == null) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.PhotoCamera,
                    contentDescription = "Take photo",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    modifier = Modifier.size(48.dp)
                )
                Text(
                    text = "Tap to take photo",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    fontSize = 12.sp
                )
            }
        } else {
            // TODO: Replace with AsyncImage (Coil) once camera is wired
            Text(
                text = "Image captured",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 14.sp
            )
        }
    }
}

/**
 * Barcode scanner button — opens ML Kit scanner
 * On scan success, auto-fills product ID and name
 */
@Composable
fun BarcodeScannerButton(onScanClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onScanClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.QrCodeScanner,
                contentDescription = "Scan barcode",
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Scan Barcode",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/**
 * Reusable labeled text field for product form
 * @param keyboardType Number for price/quantity, Text for name/id
 */
@Composable
fun ProductFormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    placeholder,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                )
            },
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                cursorColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}

/**
 * Submit button — saves product to DB
 */
@Composable
fun SaveButton(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
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
                text = "Save Product",
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}