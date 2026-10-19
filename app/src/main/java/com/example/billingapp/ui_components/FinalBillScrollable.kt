package com.example.billingapp.ui_components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.billingapp.database.BillWithDetails
import com.example.billingapp.ui.theme.recieptColor

@Composable

fun FinalBillScrollable(
    itemss: List<BillWithDetails>,
    shopName: String,
    shopAdress: String,
    shopGST: String,
    shopPhone: String,
    shopTerms: String,
    model: Any
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = recieptColor,
            contentColor = Color.Black
        ), elevation = CardDefaults.cardElevation(8.dp), border = CardDefaults.outlinedCardBorder(
            true
        )
    ) {
        LazyColumn(modifier = Modifier.padding(8.dp)) {
            items(itemss) { i ->
                DetailsHeader(
                    mop = i.bill.modeOfPayment,
                    name = i.bill.customerName,
                    phoneNo = i.bill.customerPhone,
                    date = i.bill.date,
                    billNo = i.bill.billNo.toString(),
                    model = model,
                    shopName = shopName,
                    shopAdress = shopAdress,
                    shopGST = shopGST,
                    shopPhone = shopPhone
                )
                i.items.forEachIndexed { index, product ->
                    DetailsBody(
                        srNo = index + 1,
                        itemName = product.productName,
                        qty = product.productQuantity,
                        price = product.productAmount.toString(),
                        amount = product.productTotal

                    )
                }
                DetailsFooter(
                    shopTerms = shopTerms,
                    qty = i.bill.totalQty.toString(),
                    total = String.format(java.util.Locale.US,"%.2f",i.bill.totalAmount / 100.0),
                    taxableAmount = String.format(java.util.Locale.US, "%.2f",i.bill.taxableAmount / 100.0),
                    cgst = String.format(java.util.Locale.US,"%.2f",i.bill.cgstAmount / 100.0),
                    sgst = String.format(java.util.Locale.US,"%.2f",i.bill.sgstAmount / 100.0)
                )
            }
        }
    }
}