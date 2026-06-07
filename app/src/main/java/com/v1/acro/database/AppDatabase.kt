package com.v1.acro.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.v1.acro.database.Product.ProductDao
import com.v1.acro.database.Product.ProductData
import androidx.sqlite.db.SupportSQLiteDatabase
import com.v1.acro.database.Transaction.TransactionData
import com.v1.acro.database.Transaction.TransactionItemData

/**
 * App database — Singleton pattern
 * Only one instance exists for the entire app
 * Access via: AppDatabase.getDatabase(context)
 *
 * UPDATE NOTE:
 *   - Registered the new TransactionItemData entity (table "transaction_items")
 *     so order line items are persisted. Schema bumped to version 5.
 *   - fallbackToDestructiveMigration is on, so the local DB is recreated on upgrade.
 */
@Database(entities = [ProductData::class, TransactionData::class, TransactionItemData::class], version = 5, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao
    abstract fun TransactionDao(): TransactionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "acro_database"
                )
                    .fallbackToDestructiveMigration(true)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
