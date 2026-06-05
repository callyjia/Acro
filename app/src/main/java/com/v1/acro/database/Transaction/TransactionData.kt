package com.v1.acro.database.Transaction

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionData(
    @PrimaryKey(autoGenerate = true)
    val tid: Int = 0,
    val total: Double,
    val timestamp: Long,
    val itemcount: Int
)