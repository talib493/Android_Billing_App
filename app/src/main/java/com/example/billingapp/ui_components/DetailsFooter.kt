package com.example.billingapp.ui_components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun DetailsFooter(
    qty: String,
    taxableAmount: String,
    cgst: String,
    sgst: String,
    total: String,
    shopTerms: String
) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(top = 0.dp, start = 20.dp, end = 20.dp, bottom = 20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "ITEMS/QTY :",
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = qty,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "TAXABLE AMOUNT :",
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = "Rs. ${taxableAmount}",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "CGST 2.5% :",
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = "Rs. ${cgst}",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "SGST 2.5% :",
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = "Rs. ${sgst}",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "TOTAL AMOUNT :",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Rs. ${total}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "TERMS AND CONDITIONS:",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = shopTerms,
            style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center
        )
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
        Text(
            text = "THANK YOU VISIT AGAIN!",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(
            modifier = Modifier.height(6.dp)
        )
        HorizontalDivider(
            modifier = Modifier.height(1.dp).padding(start = 40.dp, end = 40.dp),
            color = Color.Black
        )


    }

}