package com.v1.acro.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.v1.acro.database.AppDatabase
import com.v1.acro.database.Transaction.TransactionData
import com.v1.acro.database.Transaction.TransactionItemData
import com.v1.acro.database.TransactionDao
import com.v1.acro.database.Transaction.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * TransactionViewModel — exposes orders to the UI and handles checkout.
 *
 * UPDATE NOTE:
 *   - checkout(name, items): inserts the order, defaults a blank name to "order{id}",
 *     and saves every cart line as a TransactionItemData in one coroutine.
 *   - getItems(tid): Flow of line items for the Order Detail screen.
 *   - allTransactions also powers the Analytics screen and Home sales card.
 */
class TransactionViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TransactionRepository
    val allTransactions: StateFlow<List<TransactionData>>

    init {
        val dao = AppDatabase.getDatabase(application).TransactionDao()
        repository = TransactionRepository(dao)
        allTransactions = repository.getAllTransaction()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }

    /**
     * Save an order and all of its line items in one go.
     * If [name] is blank, the order is named "order{id}" using its generated id.
     */
    fun checkout(name: String, items: List<CartItem>) {
        if (items.isEmpty()) return
        viewModelScope.launch {
            val total = items.sumOf { it.product.price * it.quantity }
            val count = items.sumOf { it.quantity }

            val tid = repository.insertTransaction(
                TransactionData(
                    name = name,
                    total = total,
                    timestamp = System.currentTimeMillis(),
                    itemcount = count
                )
            ).toInt()

            // Default a blank name to "order{id}"
            if (name.isBlank()) {
                repository.updateTransactionName(tid, "order$tid")
            }

            // Save each cart line as a transaction item
            items.forEach { item ->
                repository.insertTransactionItem(
                    TransactionItemData(
                        transactionId = tid,
                        productName = item.product.name,
                        price = item.product.price,
                        quantity = item.quantity
                    )
                )
            }
        }
    }

    /** Line items belonging to a given order — used by OrderDetail. */
    fun getItems(tid: Int): Flow<List<TransactionItemData>> =
        repository.getTransactionItems(tid)

}