package com.v1.acro.database.Product

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * ProductData entity — defines the "products" table in SQLite
 * Room auto-creates the table from this class
 *
 * @param tid auto-incremented primary key
 * @param name product display name
 * @param price product price in CNY
 * @param quantity current stock count
 * @param imageUri local file path of product photo (nullable)
 * @param barcode scanned barcode value (nullable)
 *
 * UPDATE NOTE: no schema change this update, but `barcode` is now written by the
 * barcode scanner (AddProduct) and matched in OrderScreen, and `imageUri` now holds
 * a real captured-photo Uri.
 */
@Entity(tableName = "products")
data class ProductData(
    @PrimaryKey(autoGenerate = true)
    val pid: Int = 0,
    val name: String,
    val price: Double,
    val quantity: Int,
    val imageUri: String? = null,
    val barcode: String? = null
)