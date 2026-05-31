package com.v1.acro.uiscreens

import android.text.Layout
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.v1.acro.navigation.Item
import com.v1.acro.ui.theme.*
import com.v1.acro.viewmodel.CartViewModel

@Composable
fun HomeScreen(navController: NavController) {
    val todaySales = "1,250.00"
    val transactionDaily = 24
    val transactionMonth = 200
    val transactionWeek = 50
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

        OrderButton(
            navController = navController
        )

        Spacer(modifier = Modifier.height(16.dp))

        TaskSection(
            taskItems = taskItems,
            navController = navController
        )
    }
}


// Today's sale button
@Composable
fun TodaySale(todaySales: String,
              transactionDaily: Int,
              transactionWeek: Int,
              transactionMonth: Int){
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
            colors = CardDefaults.cardColors(containerColor = MidBlue)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(text = "Today's Sales", color = Color.White)
                Spacer (modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(text = "CNY.", color = Color.White, fontSize = 16.sp,
                        fontWeight = FontWeight.Bold)
                    Text(text = todaySales, color = Color.White, fontSize = 32.sp,
                        fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(8.dp))
                if (isExpanded) {
                    Text(text = "Today: $transactionDaily", color = Color.White)
                    Text(text = "This week: $transactionWeek", color = Color.White)
                    Text(text = "This Month: $transactionMonth", color = Color.White)
                }
            }
        }
    }
}

//Make order Button
@Composable
fun OrderButton(navController: NavController){
        Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp)
            .clickable { navController.navigate("order") },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(DarkerWhite)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Make New Order",
                color = MidBlue
            )
        }
    }
}

//Loop For Task
@Composable
fun TaskSection(
    taskItems: List<Item>,
    navController: NavController
) {
    Text(
        text = "Tasks",
        color = MidBlue,
        fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(8.dp))
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        maxItemsInEachRow = 2,
        horizontalArrangement = Arrangement.SpaceBetween,
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


// Task display function
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
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(60.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(color = White)
                .border(2.dp, MidBlue, RoundedCornerShape(20.dp))

                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.label,
                    tint = MidBlue,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.label ?: item.route,
                    color = MidBlue,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 14.sp
                )

            }
        }
    }
}
