package com.example.billingapp.ui_components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.billingapp.R

@Composable

fun PreviewBody(
    srNo: Int, itemName: String, qty: Int, price: String, amount: Double,
    DialogOpen:(Int)-> Unit
){
    Column(modifier = Modifier.fillMaxWidth()
        .padding(top = 0.dp, start = 20.dp, end = 20.dp, bottom=0.dp)){
        Row() {
            Text(text = "${srNo} :",
                style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
            text = itemName, style = MaterialTheme.typography.bodyMedium
        )
        }
        Spacer(
            modifier = Modifier.height(2.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = qty.toString(),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f))
            Text(text = price,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f))
            Text(text = amount.toString(),
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium,
            )
            IconButton(modifier = Modifier.weight(0.4f),
                onClick = {DialogOpen(srNo)}) {
                Icon(painter = painterResource(R.drawable.edit_24px),
                    contentDescription = "edit products")
            }

        }
        Spacer(
            modifier = Modifier.height(6.dp)
        )
        HorizontalDivider(
            modifier = Modifier.height(1.dp),
            color = Color.Black
        )
        Spacer(
            modifier = Modifier.height(6.dp)
        )
    }
}