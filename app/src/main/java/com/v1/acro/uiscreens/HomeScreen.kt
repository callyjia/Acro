package com.v1.acro.uiscreens

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.v1.acro.ui.theme.*
import androidx.compose.animation.core.animateFloatAsState

@Composable
fun HomeScreen(navController: NavController) {

    val todaySales = "1,250.00"
    val transactionDaily = 24
    val transactionMonth = 200
    val transactionWeek = 50

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        SalesCard(todaySales,
            transactionDaily = transactionDaily,
            transactionWeek = transactionWeek,
            transactionMonth = transactionMonth)
    }
}

@Composable
fun SalesCard(todaySales: String,
              transactionDaily : Int,
              transactionWeek : Int,
              transactionMonth : Int) {
    var isExpanded by remember { mutableStateOf(false) }

    val cardWith by animateFloatAsState(
        targetValue = if (isExpanded) 0.6f else 0.55f,
        label = "cardWidth"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth(cardWith)
            .animateContentSize()
            .clickable { isExpanded = !isExpanded },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MidBlue)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(text = "Today's Sales", color = Color.White)
            Row(verticalAlignment = Alignment.Bottom){
                Text(text = "CNY.", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(text = todaySales, color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
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