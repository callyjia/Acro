package com.v1.acro.database

import androidx.room.*
import com.v1.acro.database.Transaction.TransactionData
import com.v1.acro.database.Transaction.TransactionItemData
import kotlinx.coroutines.flow.Flow
import androidx.room.OnConflictStrategy.Companion.ABORT

/**
 * ============================================================
 * TransactionDao.kt
 * ============================================================
 *
 * Room DAO for orders (transactions) and their line items.
 *
 * UPDATE NOTE:
 *   - insertTransaction now returns the auto-generated id (Long) so the
 *     caller can attach line items / rename the order afterwards.
 *   - Added insertTransactionItem + getTransactionItems for order line items.
 *   - Added updateTransactionName (used to default a blank order name to "order{id}").
 *   - Removed the old duplicate Unit-returning insertTransaction (it caused an
 *     overload-resolution ambiguity).
 * ============================================================
 */
@Dao
interface TransactionDao {
    // look all order
    @Query("SELECT * FROM transactions ORDER BY tid DESC")
    fun getallTransaction(): Flow<List<TransactionData>>

    //get order by id
    @Query("SELECT*FROM transactions WHERE tid= :id")
    fun getTransactionById(id: Int): TransactionData?

    @Query("SELECT * FROM transactions WHERE name= :name")
    fun getTransactionByname(name: String): TransactionData?

    //get total money from transaction
    @Query("SELECT SUM(total) FROM transactions")
    fun geTotal(): Flow<Double?>

    @Insert
    suspend fun insertTransactionItem(item: TransactionItemData)

    // Get items for a specific transaction
    @Query("SELECT * FROM transaction_items WHERE transactionId = :tid")
    fun getTransactionItems(tid: Int): Flow<List<TransactionItemData>>

    // Insert transaction and return the auto-generated ID
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertTransaction(transaction: TransactionData): Long  // ← returns ID

    // Rename an order (used to default a blank name to "order{id}")
    @Query("UPDATE transactions SET name = :name WHERE tid = :tid")
    suspend fun updateTransactionName(tid: Int, name: String)
}
