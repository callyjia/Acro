package com.v1.acro.database.Transaction

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * TransactionData — one order/receipt row in the "transactions" table.
 * Holds only the summary (name, total, timestamp, item count); the individual
 * products bought are stored separately in TransactionItemData.
 *
 * UPDATE NOTE: `name` may be a user-typed order name, or "order{id}" when left blank.
 */
@Entity(tableName = "transactions")
data class TransactionData(
    @PrimaryKey(autoGenerate = true)
    val tid: Int = 0,
    val name: String,
    val total: Double,
    val timestamp: Long,
    val itemcount: Int
)