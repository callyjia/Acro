package com.v1.acro.uiscreens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.v1.acro.analytics.PeriodStats
import com.v1.acro.analytics.computeSalesSummary
import com.v1.acro.ui.theme.money
import com.v1.acro.viewmodel.ProductViewModel
import com.v1.acro.viewmodel.TransactionViewModel

/**
 * ============================================================
 * Analytic.kt
 * ============================================================
 *
 * NEW FILE (this update). Real analytics dashboard derived from orders + products flow.
 *
 *   - Overview: total revenue, total orders, avg/min/max order value
 *   - By period: daily / weekly / monthly average + min + max revenue
 *   - Inventory: products, units in stock, out of stock, stock value
 *
 * NAVIGATION:
 *   Route: "Analytics"
 * ============================================================
 */
@Composable
fun Analytic(
    navController: NavController,
    transactionViewModel: TransactionViewModel = viewModel(),
    productViewModel: ProductViewModel = viewModel()
) {
    val transactions by transactionViewModel.allTransactions.collectAsState()
    val products by productViewModel.allProducts.collectAsState()

    val summary = remember(transactions) {
        computeSalesSummary(transactions, System.currentTimeMillis())
    }

    // Inventory figures from the product flow
    val inventoryValue = remember(products) { products.sumOf { it.price * it.quantity } }
    val unitsInStock = remember(products) { products.sumOf { it.quantity } }
    val outOfStock = remember(products) { products.count { it.quantity == 0 } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Analytics",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        if (transactions.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 48.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No sales data yet",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    fontSize = 14.sp
                )
            }
        } else {
            // Overview
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                MetricCard(
                    label = "Total Revenue",
                    value = money(summary.totalRevenue),
                    modifier = Modifier.weight(1f),
                    highlight = true
                )
                MetricCard(
                    label = "Total Orders",
                    value = "${summary.totalOrders}",
                    modifier = Modifier.weight(1f)
                )
            }

            // Order value avg / min / max
            SectionLabel("Order Value")
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                MetricCard("Average", money(summary.avgOrderValue), Modifier.weight(1f))
                MetricCard("Min", money(summary.minOrderValue), Modifier.weight(1f))
                MetricCard("Max", money(summary.maxOrderValue), Modifier.weight(1f))
            }

            // By period
            SectionLabel("Revenue by Period")
            PeriodStatsCard("Daily", summary.daily)
            PeriodStatsCard("Weekly", summary.weekly)
            PeriodStatsCard("Monthly", summary.monthly)

            // Inventory
            SectionLabel("Inventory")
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                MetricCard("Products", "${products.size}", Modifier.weight(1f))
                MetricCard("Units in Stock", "$unitsInStock", Modifier.weight(1f))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                MetricCard(
                    label = "Out of Stock",
                    value = "$outOfStock",
                    modifier = Modifier.weight(1f),
                    alert = outOfStock > 0
                )
                MetricCard(
                    label = "Stock Value",
                    value = money(inventoryValue),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

/** Bold section divider label. */
@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
    )
}

/** A single labelled metric card. */
@Composable
private fun MetricCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    highlight: Boolean = false,
    alert: Boolean = false
) {
    val container = when {
        highlight -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.surface
    }
    val labelColor = when {
        highlight -> MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
        else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
    }
    val valueColor = when {
        highlight -> MaterialTheme.colorScheme.onPrimary
        alert -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.onSurface
    }
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = container),
        border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(text = label, fontSize = 11.sp, color = labelColor)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = valueColor
            )
        }
    }
}

/** Card showing average / min / max revenue for one period type. */
@Composable
private fun PeriodStatsCard(title: String, stats: PeriodStats) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${stats.periods} ${if (stats.periods == 1) "period" else "periods"}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                StatColumn("Avg", stats.average, Modifier.weight(1f))
                StatColumn("Min", stats.min, Modifier.weight(1f))
                StatColumn("Max", stats.max, Modifier.weight(1f))
            }
        }
    }
}

/** One labelled money figure inside a PeriodStatsCard. */
@Composable
private fun StatColumn(label: String, value: Double, modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = money(value),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
