package com.example.billingapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "sale_info")
data class BillInfoEntity(
    @PrimaryKey(autoGenerate = true)
    val itemId: Int =0,

    val billNo: Long,
    val productName: String,
    val productQuantity: Int,
    val productAmount: Double,
    val productTotal: Double

)
