package com.example.billingapp.ui_components

import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.billingapp.ui.theme.CoffeeBrown
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun dateHeader(date: String,noOfTransactshions: String,totalAmount: String) {
    val indiaLocale = Locale("en", "IN")
    val inputFormatter = DateTimeFormatter.ofPattern("d MMM yyyy", indiaLocale)
    val Date = LocalDate.parse(date, inputFormatter)
    var day : String
    val today = LocalDate.now()

    if (Date.isEqual(today)) {
        day = "${Date.format(DateTimeFormatter.ofPattern("E", indiaLocale))}, (Today)"
    } else {
        day = Date.format(DateTimeFormatter.ofPattern("EEEE", indiaLocale))
    }
    Card(modifier = Modifier.fillMaxWidth().height(100.dp).clip(RoundedCornerShape(8.dp)),
        colors = CardDefaults.cardColors(
        containerColor = CoffeeBrown.copy(0.5f),
        contentColor = Color.Black
    )) {
        Column(modifier = Modifier.padding(4.dp)) {
            Text(text = "${date}, ${day}",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = totalAmount,
                fontSize = 30.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "No of Transaction: ${noOfTransactshions}",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = Color.Black
            )
        }
    }
}