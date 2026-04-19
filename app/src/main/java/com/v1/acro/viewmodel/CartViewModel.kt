package com.v1.acro.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.v1.acro.data.CartProduct
import com.v1.acro.data.Product

class CartViewModel: ViewModel() {

    var cartProducts = mutableStateListOf<CartProduct>()
        private set

    fun addToCart(product: Product) {
        val item = cartProducts.find { it.cartProduct.productID == product.productID }
        if (item != null) {
            item.quantity++
        } else {
            cartProducts.add(CartProduct(product, 1))
        }
    }

    fun increase(item: CartProduct) {
        item.quantity++
    }

    fun decrease(item: CartProduct) {
        if (item.quantity > 1) item.quantity--
        else cartProducts.remove(item)
    }

    fun clearCart(){
        cartProducts.clear()
    }
}