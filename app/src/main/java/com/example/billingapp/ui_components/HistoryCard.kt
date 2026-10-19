package com.example.billingapp.ui_components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.example.billingapp.R
import com.example.billingapp.ui.theme.Cash
import com.example.billingapp.ui.theme.CoffeeBrown
import com.example.billingapp.ui.theme.UPI

@Composable

fun HistoryCard(
    mop: String,
    billNo: String,
    customerName: String,
    customerPhone: String,
    date: String,
    totalQty: Int,
    totalAmount: Int,
    Delete:()-> Unit,
    Details:()-> Unit

){
    Card(modifier = Modifier.clickable(onClick = {Details()} )
        .fillMaxWidth()
        .height(110.dp), shape = RoundedCornerShape(8.dp), colors = CardDefaults.cardColors(
        contentColor = Color.Black,
        containerColor = Color.LightGray)){
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween){
            Column(modifier = Modifier.padding(8.dp)) {
                Text(text = "INV-${billNo}",
                    style = MaterialTheme.typography.bodyMedium)
                Spacer(
                    modifier = Modifier.height(4.dp)
                )
                Text(text = customerName,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(
                    modifier = Modifier.height(4.dp)
                )
                Text(
                    text = customerPhone,
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(
                    modifier = Modifier.height(4.dp)
                )
                Text(text = date,
                    style = MaterialTheme.typography.bodyMedium)
            }
            Column(modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .fillMaxHeight()
                ,horizontalAlignment = Alignment.End) {
                Text(text = mop, style = MaterialTheme.typography.bodySmall, modifier = Modifier.background(
                    color = if (mop.equals("UPI", ignoreCase = true)) UPI else Cash
                    , shape = RoundedCornerShape(4.dp)).padding(start = 4.dp, end = 4.dp), fontStyle = FontStyle.Italic)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "QTY/TOTAL",
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(modifier = Modifier.height(4.dp))

                Text(text = "${totalQty}/${String.format(java.util.Locale.US,"%.2f",totalAmount/100.0)}",
                    style = MaterialTheme.typography.bodyLarge,

                )
                Spacer(modifier = Modifier.height(4.dp))
                IconButton(onClick = {Delete()}) {
                    Icon(painter = painterResource(R.drawable.delete_24px),
                        contentDescription = "Delete Bill")
                }

            }
        }

    }
}