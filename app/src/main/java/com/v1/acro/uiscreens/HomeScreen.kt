package com.v1.acro.uiscreens

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.v1.acro.analytics.computeSalesSummary
import com.v1.acro.analytics.formatMoney
import com.v1.acro.navigation.Item
import com.v1.acro.ui.theme.*
import androidx.compose.foundation.BorderStroke
import com.v1.acro.viewmodel.TransactionViewModel

/**
 * ============================================================
 * HomeScreen.kt
 * ============================================================
 *
 * Main dashboard screen — first screen users see after launch.
 *
 * STRUCTURE:
 *   HomeScreen (entry point)
 *     ├── TodaySale       → expandable card showing daily revenue
 *     ├── OrderButton     → navigates to order creation screen
 *     └── TaskSection     → grid of shortcut cards
 *           └── TaskCard  → single shortcut (dynamic from Item list)
 *
 * DATA:
 *   - Task items are pulled from Item sealed class
 *
 * UPDATE NOTE:
 *   - The sales card is now LIVE: today's revenue + today/week/month order counts
 *     come from TransactionViewModel via computeSalesSummary() (analytics/).
 *     The old hardcoded values were removed.
 *   - For full stats (avg/min/max, inventory) see the Analytics screen.
 *
 * NAVIGATION:
 *   Route: "home" (startDestination in NavHost)
 *   Outgoing: "order", Item.Product.route, Item.Receipt.route,
 *             Item.AddProduct.route, Item.Analytics.route
 * ============================================================
 */

/**
 * Entry point — called from NavHost composable("home")
 * Initializes sales data and task items, lays out dashboard
 */
@Composable
fun HomeScreen(
    navController: NavController,
    transactionViewModel: TransactionViewModel = viewModel()
) {
    // Live sales data from Room — recomputes whenever orders change
    val transactions by transactionViewModel.allTransactions.collectAsState()
    val summary = remember(transactions) {
        computeSalesSummary(transactions, System.currentTimeMillis())
    }
    val todaySales = formatMoney(summary.todayRevenue)
    val transactionDaily = summary.ordersToday
    val transactionMonth = summary.ordersThisMonth
    val transactionWeek = summary.ordersThisWeek

    // Task shortcuts — add/remove items here to update the grid automatically
    val taskItems = listOf(
        Item.Product,
        Item.Receipt,
        Item.AddProduct,
        Item.Analytics
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        TodaySale(
            todaySales = todaySales,
            transactionDaily = transactionDaily,
            transactionWeek = transactionWeek,
            transactionMonth = transactionMonth
        )
        Spacer(modifier = Modifier.height(16.dp))

        OrderButton(navController = navController)

        Spacer(modifier = Modifier.height(16.dp))

        TaskSection(
            taskItems = taskItems,
            navController = navController
        )
    }
}

/**
 * Expandable sales summary card
 * - Default: shows today's total revenue (55% width)
 * - Expanded: reveals daily, weekly, monthly transaction counts (60% width)
 * - Width transition is animated via animateFloatAsState
 * - Content transition is animated via animateContentSize
 *
 * @param todaySales formatted total sales string (e.g. "1,250.00")
 * @param transactionDaily number of transactions today
 * @param transactionWeek number of transactions this week
 * @param transactionMonth number of transactions this month
 */
@Composable
fun TodaySale(
    todaySales: String,
    transactionDaily: Int,
    transactionWeek: Int,
    transactionMonth: Int
) {
    var isExpanded by remember { mutableStateOf(false) }

    val cardWidth by animateFloatAsState(
        targetValue = if (isExpanded) 0.6f else 0.55f,
        label = "cardWidth"
    )

    Row(verticalAlignment = Alignment.Bottom) {
        Card(
            modifier = Modifier
                .fillMaxWidth(cardWidth)
                .animateContentSize()
                .clickable { isExpanded = !isExpanded },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(text = "Today's Sales", color = MaterialTheme.colorScheme.onPrimary)
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "CNY.", color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 16.sp, fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = todaySales, color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 32.sp, fontWeight = FontWeight.Bold
                    )
                }
                if (isExpanded) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Today: $transactionDaily", color = MaterialTheme.colorScheme.onPrimary)
                    Text(text = "This week: $transactionWeek", color = MaterialTheme.colorScheme.onPrimary)
                    Text(text = "This month: $transactionMonth", color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    }
}

/**
 * Full-width button that navigates to the order creation screen
 * Route: "order"
 *
 * @param navController navigation controller for routing
 */
@Composable
fun OrderButton(navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("order") },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Make New Order", color = MaterialTheme.colorScheme.onSurface)
        }
    }
}

/**
 * Task shortcut grid — displays items in a 2-column FlowRow
 * Dynamically generates TaskCards from the provided list
 * To add a new shortcut: add an Item object to taskItems in HomeScreen
 *
 * @param taskItems list of Item objects to display as shortcuts
 * @param navController navigation controller passed to each card's onClick
 */
@Composable
fun TaskSection(
    taskItems: List<Item>,
    navController: NavController
) {
    Text(
        text = "Tasks",
        color = MaterialTheme.colorScheme.onSurface,
        fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(8.dp))
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        maxItemsInEachRow = 2,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        taskItems.forEach { item ->
            TaskCard(
                modifier = Modifier.weight(1f),
                item = item,
                onClick = { navController.navigate(item.route) }
            )
        }
    }
}

/**
 * Single task shortcut card — reusable component
 * Displays icon and label from Item sealed class
 * Falls back to item.route if label is null
 *
 * @param modifier applied to outer Column (receives weight from FlowRow)
 * @param item Item object containing icon, label, and route
 * @param onClick callback triggered on card tap
 */
@Composable
fun TaskCard(
    modifier: Modifier = Modifier,
    item: Item,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .clickable { onClick() },
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.surface.copy(alpha = 0.3f)),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp,
                pressedElevation = 8.dp
            )
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.label,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.label ?: item.route,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 14.sp
                )
            }
        }
    }
}