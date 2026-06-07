package com.v1.acro.database.Product

import kotlinx.coroutines.flow.Flow

/**
 * Middleman between ViewModel and Dao
 * ViewModel never talks to Dao directly
 *
 * UPDATE NOTE: unchanged this update. See TransactionRepository for the new
 * order + line-item persistence added alongside the checkout flow.
 */
class ProductRepository(private val productDao: ProductDao) {

    fun getAllProducts(): Flow<List<ProductData>> =
        productDao.getAllProducts()

    fun searchProducts(query: String): Flow<List<ProductData>> =
        productDao.searchProducts(query)

    suspend fun getProductById(pid: Int): ProductData? =
        productDao.getProductById(pid)

    suspend fun insertProduct(product: ProductData) =
        productDao.insertProduct(product)

    suspend fun updateProduct(product: ProductData) =
        productDao.updateProduct(product)

    suspend fun deleteProduct(product: ProductData) =
        productDao.deleteProduct(product)

    suspend fun reduceStock(pid: Int, amount: Int) =
        productDao.reduceStock(pid, amount)
}