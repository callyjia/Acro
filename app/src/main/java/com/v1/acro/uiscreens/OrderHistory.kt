package com.v1.acro.uiscreens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.v1.acro.database.Transaction.TransactionData
import com.v1.acro.viewmodel.TransactionViewModel
import java.text.SimpleDateFormat
import java.util.*

/**
 * ============================================================
 * OrderHistory.kt
 * ============================================================
 *
 * Shows all checked-out orders with sorting options.
 *
 * STRUCTURE:
 *   OrderHistory (entry point)
 *     ├── SortControls   → sort by date, name, price
 *     └── HistoryList    → list of past transactions
 *           └── HistoryCard → single order summary
 *
 * SORT OPTIONS:
 *   - Newest / Oldest (by timestamp)
 *   - Name A-Z / Z-A
 *   - Price High / Low
 *
 * UPDATE NOTE:
 *   - Each HistoryCard is now tappable and navigates to the Order Detail screen
 *     ("OrderDetail/{tid}") to show the order's line items.
 *
 * NAVIGATION:
 *   Route: "receipt"
 *   Entry: HomeScreen TaskCard → navController.navigate("receipt")
 *   Outgoing: "OrderDetail/{tid}"
 * ============================================================
 */

// Sort options
enum class SortOption(val label: String) {
    NEWEST("Newest"),
    OLDEST("Oldest"),
    NAME_AZ("Name A-Z"),
    PRICE_HIGH("Price High"),
    PRICE_LOW("Price Low")
}

@Composable
fun OrderHistory(
    navController: NavController,
    viewModel: TransactionViewModel = viewModel()
) {
    val transactions by viewModel.allTransactions.collectAsState()
    var sortOption by remember { mutableStateOf(SortOption.NEWEST) }

    // Sort the list based on selected option
    val sortedTransactions = remember(transactions, sortOption) {
        when (sortOption) {
            SortOption.NEWEST -> transactions.sortedByDescending { it.timestamp }
            SortOption.OLDEST -> transactions.sortedBy { it.timestamp }
            SortOption.NAME_AZ -> transactions.sortedBy { it.name }
            SortOption.PRICE_HIGH -> transactions.sortedByDescending { it.total }
            SortOption.PRICE_LOW -> transactions.sortedBy { it.total }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Order History",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(12.dp))

        // Sort controls
        SortControls(
            selected = sortOption,
            onSelect = { sortOption = it }
        )
        Spacer(modifier = Modifier.height(12.dp))

        if (sortedTransactions.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No orders yet",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    fontSize = 14.sp
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(sortedTransactions) { transaction ->
                    HistoryCard(
                        transaction = transaction,
                        onClick ={navController.navigate("OrderDetail/${transaction.tid}")}
                    )
                }
            }
        }
    }
}

/**
 * Horizontal scrollable sort option pills
 */
@Composable
fun SortControls(
    selected: SortOption,
    onSelect: (SortOption) -> Unit
) {
    androidx.compose.foundation.lazy.LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(SortOption.entries.toTypedArray()) { option ->
            val isSelected = selected == option
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surface
                    )
                    .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
                    .clickable { onSelect(option) }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = option.label,
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
 * Single order summary card
 * Shows name, date, item count, total
 */
@Composable
fun HistoryCard(transaction: TransactionData,onClick:()->Unit) {
    Card(
        modifier = Modifier.fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = formatTimestamp(transaction.timestamp),
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Text(
                    text = "${transaction.itemcount} items",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            Text(
                text = "CNY ${transaction.total}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

/**
 * Converts Long timestamp to readable date string
 * 1717564800000 → "05 Jun 2026, 14:30"
 */
fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}