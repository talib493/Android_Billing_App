package com.example.billingapp.database

import androidx.room.Embedded
import androidx.room.Relation
import kotlinx.serialization.Serializable

@Serializable
data class BillWithDetails(

    @Embedded
    val bill: BillEntity,

    @Relation(
        parentColumn = "billNo",
        entityColumn = "billNo"
    )
    val items : List<BillInfoEntity>
)