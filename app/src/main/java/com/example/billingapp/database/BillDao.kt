package com.example.billingapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface BillDao {
    @Query("DELETE FROM sqlite_sequence WHERE name = 'sales'")
    suspend fun resetSalesAutoIncrement()
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShop(shop: ShopEntity)
    @Insert
    suspend fun insertItems(
        items: List<BillInfoEntity>
    )
    @Insert
    suspend fun insertBill (bill: BillEntity): Long

    @Query("SELECT * FROM sales ORDER BY billNo DESC")
    fun getAllBills(): Flow<List<BillEntity>>

    @Delete
    suspend fun delete(bill: BillEntity)

    @Transaction
    @Query("SELECT * FROM sales  WHERE billNo = :id")
    fun getBillWithItems(id: Long): Flow<List<BillWithDetails>>

    @Transaction
    @Query("SELECT * FROM sales ")
    suspend fun getFullBillS(): List<BillWithDetails>

    @Query("SELECT * FROM sale_info")
    fun getAllItems(): Flow<List<BillInfoEntity>>

    @Query("SELECT seq FROM sqlite_sequence WHERE name = 'sales' LIMIT 1")
    suspend fun getLastGeneratedId(): Long?

    @Query("DELETE FROM sale_info")
    suspend fun deleteAllItems()

    @Query("DELETE FROM sales")
    suspend fun deleteAllBills()
    @Query("SELECT * FROM shop WHERE id = 1")
    fun getShop(): Flow<ShopEntity?>
    @Delete
    suspend fun delete(shop: ShopEntity)

}