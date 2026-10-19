package com.example.billingapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "sales")
data class BillEntity(
    @PrimaryKey(autoGenerate = true)
    val billNo: Long=0,

    val customerName: String,
    val customerPhone: String,

    val date: String,

    val totalQty: Int,
    val totalAmount: Int,
    val modeOfPayment: String,
    val taxableAmount: Int,
    val cgstAmount: Int,
    val sgstAmount: Int
)