package com.example.billingapp.ui_components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.billingapp.models.Products
import com.example.billingapp.models.ProductsViewModel
import com.example.billingapp.ui.theme.recieptColor

@Composable
fun FullPreview(
    shopAddress: String,
    shopName: String,
    model: Any,
    mop: Boolean,
    date: String, billNo: String, cPhone: String, cName: String, gQty: Int, gTotal: Double
    , products: List<Products>, shopGST: String, shopPhone: String, shopTerms: String
){
    val viewModel : ProductsViewModel = viewModel()

    Card(shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = recieptColor,
            contentColor = Color.Black
    ), elevation = CardDefaults.cardElevation(8.dp)) {
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            item {
                PreviewHeader(
                    name = cName,
                    phoneNo = cPhone,
                    billNo = billNo,
                    date = date,
                    mop = mop,
                    model = model,
                    shopName = shopName,
                    shopAdress = shopAddress,
                    shopGST = shopGST,
                    shopPhone = shopPhone,
                    onDateChange = {
                        viewModel.datePickerIsVisible = true
                    }
                )
            }
            itemsIndexed(products) { index, product ->
                PreviewBody(
                    srNo = index+ 1,
                    itemName = product.productName,
                    qty = product.quantity,
                    price = product.price.toString(),
                    amount = product.quantity*product.price,
                    DialogOpen = {id->viewModel.statechange(id)}
                )
                }
            item {
                PreviewFooter(
                    qty = gQty.toString(),
                    taxableAmount = String.format(java.util.Locale.US, "%.2f", ((gTotal/105)*100)),
                    cgst =String.format(java.util.Locale.US, "%.2f", ((((gTotal/105)*100)/100)*2.5)),
                    sgst =String.format(java.util.Locale.US, "%.2f", ((((gTotal/105)*100)/100)*2.5)),
                    total = String.format(java.util.Locale.US, "%.2f", gTotal),
                    shopTerms = shopTerms
                )
            }
        }
    }
}