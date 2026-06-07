package com.v1.acro.uiscreens

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.v1.acro.ui.theme.LocalSettings
import com.v1.acro.ui.theme.money

/**
 * ============================================================
 * QrPayment.kt
 * ============================================================
 *
 * Payment screen — shows a scannable payment barcode (QR) for the store.
 * Opened from the center button in the bottom navigation bar (Item.QR).
 *
 * UPDATE NOTE (this update): was an empty stub; now a real screen.
 *   - Enter an amount → a QR payment code is generated on the fly (ZXing).
 *   - The customer scans it to pay.
 *
 * NAVIGATION:
 *   Route: "QrPayment"
 *   Entry: BottomBarNavigation center FAB → navController.navigate(Item.QR.route)
 * ============================================================
 */
@Composable
fun QrPayment(navController: NavController) {
    val currency = LocalSettings.current.currency
    var amountText by remember { mutableStateOf("") }
    val amount = amountText.toDoubleOrNull() ?: 0.0

    // Encodes a simple payment URI the customer's app would understand
    val payload = "acropay://pay?merchant=acro-store&currency=${currency.code}&amount=$amount"
    val qrBitmap = remember(payload) { generateQrBitmap(payload, 600) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Payment",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.align(Alignment.Start)
        )

        Text(
            text = "Show this code to the customer to pay",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )

        // QR code on a white card (so it scans in dark mode too)
        Box(
            modifier = Modifier
                .padding(top = 8.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            if (qrBitmap != null) {
                Image(
                    bitmap = qrBitmap.asImageBitmap(),
                    contentDescription = "Payment QR code",
                    modifier = Modifier.size(240.dp)
                )
            } else {
                Text("Could not generate code", color = Color.Black)
            }
        }

        Text(
            text = money(amount),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        // Amount input
        OutlinedTextField(
            value = amountText,
            onValueChange = { amountText = it.filter { ch -> ch.isDigit() || ch == '.' } },
            label = { Text("Amount (${currency.code})") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = "The QR code updates automatically as you type the amount.",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
    }
}

/**
 * Generates a QR code [Bitmap] from [content] using ZXing.
 * Returns null if encoding fails.
 */
private fun generateQrBitmap(content: String, size: Int): Bitmap? {
    return try {
        val matrix = MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, size, size)
        val bmp = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565)
        for (x in 0 until size) {
            for (y in 0 until size) {
                bmp.setPixel(x, y, if (matrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
            }
        }
        bmp
    } catch (e: Exception) {
        null
    }
}
