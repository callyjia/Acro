package com.v1.acro.database

import androidx.room.*
import com.v1.acro.database.Transaction.TransactionData
import kotlinx.coroutines.flow.Flow
import androidx.room.OnConflictStrategy.Companion.ABORT

@Dao
interface TransactionDao{
    // look all order
    @Query("SELECT * FROM transactions ORDER BY tid DESC")
    fun getallTransaction(): Flow<List<TransactionData>>

    //get order by id
    @Query("SELECT*FROM transactions WHERE tid= :id")
    fun getTransactionById(id: Int): TransactionData?

    //insert new order
    @Insert(onConflict = ABORT)
    suspend fun insertTransaction(transaction: TransactionData)

    //get total money from transaction
    @Query("SELECT SUM(total) FROM transactions")
    fun geTotal(): Flow<Double?>

}
