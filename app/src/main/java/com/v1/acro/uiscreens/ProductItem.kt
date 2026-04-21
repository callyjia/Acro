package com.v1.acro.uiscreens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.v1.acro.data.Product
import com.v1.acro.ui.theme.Violet

@Composable
fun ProductItem(product: Product, onAdd: () -> Unit) {

    val context = LocalContext.current

    Card(modifier = Modifier.padding(8.dp)
        .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Violet
        )
    ) {
        Column {
            Box{
                Image(painter = painterResource(id = product.productImage),
                    contentDescription = product.productName,
                    modifier = Modifier.fillMaxSize()
                        .height(140.dp),
                    contentScale = ContentScale.Crop
                )
                IconButton(onClick = {
                    onAdd()
                    Toast.makeText(context, "Added to the cart", Toast.LENGTH_SHORT).show()
                },
                    modifier = Modifier.align(Alignment.TopEnd)
                        .padding(8.dp)
                        .background(Violet, CircleShape)
                ) {
                    Icon(imageVector = Icons.Default.Add,
                        contentDescription = "Add to cart",
                        tint = Color.White
                    )
                }
            }
            Column(modifier = Modifier.padding(8.dp)
            ) {
                Text(text = product.productName,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))

                Text(text = "¥${product.productPrice}",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}