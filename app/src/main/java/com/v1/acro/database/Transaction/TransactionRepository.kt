package com.v1.acro.database.Transaction

import com.v1.acro.database.TransactionDao
import kotlinx.coroutines.flow.Flow

/**
 * TransactionRepository — middleman between TransactionViewModel and TransactionDao.
 *
 * NEW FILE (this update). Exposes order queries plus the new line-item operations
 * (insertTransactionItem / getTransactionItems) and updateTransactionName.
 * Note: insertTransaction returns the generated id (Long).
 */
class TransactionRepository(private val transactionDao: TransactionDao) {
    fun getAllTransaction(): Flow<List<TransactionData>> =
        transactionDao.getallTransaction()

    fun getTransactionById(tid: Int): TransactionData? =
        transactionDao.getTransactionById(tid)

    fun getTransactionByname(name: String): TransactionData? =
        transactionDao.getTransactionByname(name)

    suspend fun insertTransaction(transaction: TransactionData): Long =
        transactionDao.insertTransaction(transaction)

    suspend fun insertTransactionItem(item: TransactionItemData) =
        transactionDao.insertTransactionItem(item)

    suspend fun updateTransactionName(tid: Int, name: String) =
        transactionDao.updateTransactionName(tid, name)

    fun getTransactionItems(tid: Int): Flow<List<TransactionItemData>> =
        transactionDao.getTransactionItems(tid)

    fun getTotal(): Flow<Double?> =
        transactionDao.geTotal()
}