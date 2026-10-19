package com.example.billingapp.ui_components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.billingapp.ui.theme.CoffeeBrown

@Composable
fun mopRow(
    mop: String,
    noOfbill: String,
    totalofmop: String
){
    Card(modifier = Modifier.fillMaxWidth().height(40.dp).clip(RoundedCornerShape(8.dp)),
        colors = CardDefaults.cardColors(
            containerColor = CoffeeBrown.copy(0.5f),
            contentColor = Color.Black
        )) {
        Row (modifier = Modifier.padding(8.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "${mop}/${noOfbill}",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black
            )
            Text(text = "Total: ${totalofmop}",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black
            )
        }
    }

}