package com.v1.acro.database.Product

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import androidx.room.OnConflictStrategy.Companion.ABORT

/**
 * Data Access Object — defines all database operations
 * Room generates the SQL implementation at compile time
 *
 * Flow return types auto-update UI when data changes
 * suspend functions run on background thread
 */
@Dao
interface ProductDao {

    // Read all products, newest first
    @Query("SELECT * FROM products ORDER BY pid DESC")
    fun getAllProducts(): Flow<List<ProductData>>

    // Get single product by ID
    @Query("SELECT * FROM products WHERE pid = :pid")
    suspend fun getProductById(pid: Int): ProductData?

    // Search by name or barcode
    @Query("SELECT * FROM products WHERE name LIKE '%' " +
            "|| :query || '%' OR barcode LIKE '%' || :query || '%'")
    fun searchProducts(query: String): Flow<List<ProductData>>

    // Insert — replaces if same ID exists
    @Insert(onConflict = ABORT)
    suspend fun insertProduct(product: ProductData)

    // Update existing product
    @Update
    suspend fun updateProduct(product: ProductData)

    // Delete product
    @Delete
    suspend fun deleteProduct(product: ProductData)

    // Subtract stock after purchase
    @Query("UPDATE products SET quantity = quantity - :amount WHERE pid = :pid")
    suspend fun reduceStock(pid: Int, amount: Int)
}