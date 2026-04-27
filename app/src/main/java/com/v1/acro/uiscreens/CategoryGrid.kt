package com.v1.acro.uiscreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Text
import com.v1.acro.data.ProductCategory

@Composable
fun CategoryGrid(productCategory: ProductCategory, onClick: () -> Unit) {
    Column(modifier = Modifier.padding(8.dp)
        .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(painterResource(productCategory.categoryImage),
            contentDescription = null,
            modifier = Modifier.size(64.dp)
                .clip(RoundedCornerShape(12.dp))
        )
        Text(productCategory.categoryName, fontWeight = FontWeight.Bold)
    }
}