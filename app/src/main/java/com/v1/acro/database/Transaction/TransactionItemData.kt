package com.v1.acro.database.Transaction

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * TransactionItemData — a single line item inside an order ("transaction_items" table).
 *
 * NEW FILE (this update): added so an order can record exactly which products were
 * bought (name, price, quantity). Linked to TransactionData via [transactionId].
 * Read by the Order Detail screen through TransactionDao.getTransactionItems().
 */
@Entity(tableName = "transaction_items")
data class TransactionItemData(
    @PrimaryKey(autoGenerate = true)
    val tiid: Int = 0,
    val transactionId: Int,
    val productName: String,
    val price: Double,
    val quantity: Int
)