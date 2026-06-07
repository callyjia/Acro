package com.v1.acro.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.v1.acro.database.Product.ProductData
import com.v1.acro.database.Transaction.TransactionData
import com.v1.acro.viewmodel.TransactionViewModel

/**
 * Holds one product + how many of it are in the cart
 */
data class CartItem(
    val product: ProductData,
    val quantity: Int
)

/**
 * CartViewModel — manages the shopping cart IN MEMORY
 * Nothing is saved to DB until checkout() is called
 *
 * Features:
 *   - add product to cart
 *   - increase/decrease quantity
 *   - remove product
 *   - auto-calculate running total
 *
 * UPDATE NOTE:
 *   - checkout() now takes an order [name] and delegates to
 *     TransactionViewModel.checkout(), which also persists the line items.
 *   - The cart snapshot is taken before clearing so items aren't lost.
 */
class CartViewModel : ViewModel() {

    // The cart — list of items, observable by UI
    val cartItems = mutableStateListOf<CartItem>()

    // Running total price (recalculated on every change)
    val totalPrice: Double
        get() = cartItems.sumOf { it.product.price * it.quantity }

    // Total number of items
    val totalCount: Int
        get() = cartItems.sumOf { it.quantity }

    /**
     * Add product to cart
     * If already in cart → increase quantity
     * If new → add with quantity 1
     */
    fun addToCart(product: ProductData) {
        val index = cartItems.indexOfFirst { it.product.pid == product.pid }
        if (index >= 0) {
            // Already in cart — increase quantity
            val existing = cartItems[index]
            cartItems[index] = existing.copy(quantity = existing.quantity + 1)
        } else {
            // New product — add it
            cartItems.add(CartItem(product, 1))
        }
    }

    /**
     * Increase quantity of a product in cart
     */
    fun increaseQuantity(product: ProductData) {
        val index = cartItems.indexOfFirst { it.product.pid == product.pid }
        if (index >= 0) {
            val existing = cartItems[index]
            cartItems[index] = existing.copy(quantity = existing.quantity + 1)
        }
    }

    /**
     * Decrease quantity of a product in cart
     * If quantity hits 0 → remove from cart
     */
    fun decreaseQuantity(product: ProductData) {
        val index = cartItems.indexOfFirst { it.product.pid == product.pid }
        if (index >= 0) {
            val existing = cartItems[index]
            if (existing.quantity > 1) {
                cartItems[index] = existing.copy(quantity = existing.quantity - 1)
            } else {
                cartItems.removeAt(index)  // last one — remove entirely
            }
        }
    }

    /**
     * Remove product from cart completely
     */
    fun removeFromCart(product: ProductData) {
        cartItems.removeAll { it.product.pid == product.pid }
    }

    /**
     * Clear cart (called after successful checkout)
     */
    fun clearCart() {
        cartItems.clear()
    }


    /**
     * Checkout — saves transaction to DB + reduces product stock
     * Called when user taps "Checkout" button
     */
    fun checkout(
        name: String,
        transactionViewModel: TransactionViewModel,
        productViewModel: ProductViewModel
    ) {
        if (cartItems.isEmpty()) return

        // Snapshot before clearing — save transaction + its line items
        val itemsSnapshot = cartItems.toList()
        transactionViewModel.checkout(name, itemsSnapshot)

        // Reduce stock for each product
        itemsSnapshot.forEach { item ->
            productViewModel.reduceStock(item.product.pid, item.quantity)
        }

        // Clear cart after checkout
        clearCart()
    }
}
