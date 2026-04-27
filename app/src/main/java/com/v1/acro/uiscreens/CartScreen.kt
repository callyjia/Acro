package com.v1.acro.uiscreens



import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

import androidx.compose.runtime.Composable

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


import androidx.wear.compose.material.Text
import com.v1.acro.ui.theme.Violet
import com.v1.acro.viewmodel.CartViewModel

@Composable
fun CartScreen(navController: NavController,
               cartViewModel: CartViewModel
) {
    val totalPrice by remember {
        derivedStateOf {
            cartViewModel.cartProducts.sumOf {
                it.cartProduct.productPrice * it.quantity
            }
        }
    }
    Column(modifier = Modifier.fillMaxSize()
        .padding(16.dp)
    ) {
        if (cartViewModel.cartProducts.isEmpty()) {
            Column(modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(modifier = Modifier.size(120.dp)
                    .background(Color(0xFFF2E7F5),
                        shape = RoundedCornerShape(60.dp)
                    ), contentAlignment = Alignment.Center
                ) {
                    Text("😔", fontSize = 48.sp)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "Your Cart is Empty",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray)
            }
        } else {
            LazyColumn(modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(cartViewModel.cartProducts) { cartItem ->
                    Card(modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {
                        Row(modifier = Modifier.fillMaxWidth()
                            .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Image(painter = painterResource(cartItem.cartProduct.productImage),
                                contentDescription = null,
                                modifier = Modifier.size(80.dp)
                                    .clip(RoundedCornerShape(12.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)
                            ) {
                                Text(text = cartItem.cartProduct.productName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    maxLines = 1
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "¥${cartItem.cartProduct.productPrice}",
                                    color = Violet,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.background(
                                    Color(0xFFF2E7F5),
                                    shape = RoundedCornerShape(24.dp)
                                ).padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                IconButton(onClick = {cartViewModel.decrease(cartItem)},
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(Icons.Default.Remove,
                                        contentDescription = "Decrease",
                                        tint = Violet)
                                }
                                Text(text = cartItem.quantity.toString(),
                                    modifier = Modifier.padding(horizontal = 8.dp),
                                    fontWeight = FontWeight.Bold
                                )
                                IconButton(
                                    onClick = { cartViewModel.increase(cartItem)},
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(Icons.Default.Add,
                                        contentDescription = "Increase",
                                        tint = Violet
                                    )
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth()
                .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Total",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Violet
                )
                Text(text = "¥$totalPrice",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Violet
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {navController.navigate("payment")},
                modifier = Modifier.fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Violet
                ), shape = RoundedCornerShape(14.dp)
            ) {
                Text(text = "Pay Now",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}