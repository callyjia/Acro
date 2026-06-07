package com.v1.acro.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.v1.acro.database.AppDatabase
import com.v1.acro.database.Product.ProductData
import com.v1.acro.database.Product.ProductRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ProductViewModel — bridges UI and database
 * Survives screen rotation, runs DB operations on background thread
 *
 * Usage:
 *   val viewModel: ProductViewModel = viewModel()
 *   val products by viewModel.allProducts.collectAsState()
 *
 * UPDATE NOTE: unchanged this update. allProducts now also feeds the Analytics
 * inventory stats and the stock-aware product picker in OrderScreen.
 */
class ProductViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ProductRepository
    val allProducts: StateFlow<List<ProductData>>

    init {
        val dao = AppDatabase.getDatabase(application).productDao()
        repository = ProductRepository(dao)
        allProducts = repository.getAllProducts()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }

    fun addProduct(
        name: String,
        price: Double,
        quantity: Int,
        imageUri: String? = null,
        barcode: String? = null
    ) {
        viewModelScope.launch {
            repository.insertProduct(
                ProductData(
                    name = name,
                    price = price,
                    quantity = quantity,
                    imageUri = imageUri,
                    barcode = barcode
                )
            )
        }
    }

    fun updateProduct(product: ProductData) {
        viewModelScope.launch {
            repository.updateProduct(product)
        }
    }

    fun deleteProduct(product: ProductData) {
        viewModelScope.launch {
            repository.deleteProduct(product)
        }
    }

    fun reduceStock(pid: Int, amount: Int) {
        viewModelScope.launch {
            repository.reduceStock(pid, amount)
        }
    }
}