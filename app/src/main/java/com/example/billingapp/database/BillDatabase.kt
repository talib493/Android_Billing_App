package com.example.billingapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities =  [
        BillEntity::class,
        BillInfoEntity::class,
        ShopEntity::class

    ],
    version = 1
)
abstract class BillingDatabase : RoomDatabase() {

    abstract fun billDao(): BillDao

    companion object{
        @Volatile
        private var INSTANCE: BillingDatabase?= null

        fun getDatabase(context: Context): BillingDatabase {
            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BillingDatabase::class.java,
                    "billing_db"
                ).build()

                INSTANCE = instance
                return instance
            }
        }

    }
}
