package com.example.billingapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shop")
data class ShopEntity(

    @PrimaryKey
    val id: Int = 1,

    val shopName: String,
    val address: String,
    val gstNo: String,
    val phoneNo: String,
    val shopTerms: String,
    val imageUri: String?
)