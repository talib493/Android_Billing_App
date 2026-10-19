package com.example.billingapp.ui_components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.billingapp.R
import com.example.billingapp.ui.theme.Cash
import com.example.billingapp.ui.theme.UPI

@Composable
fun DetailsHeader(
    mop: String,
    name: String,
    phoneNo: String,
    billNo: String,
    date: String,
    shopName: String,
    model: Any,
    shopAdress: String,
    shopGST: String,
    shopPhone: String)
//    Text(
//text = if (mop) "#UPI" else "#Cash",
//modifier = Modifier.align(Alignment.TopStart),
//fontStyle = FontStyle.Italic,
//color = if (mop) UPI else Cash
//)
//
//AsyncImage(
//model = model,
//contentDescription = "Store Logo",
//contentScale = ContentScale.Crop,
//modifier = Modifier
//.padding(top = 10.dp)
//.size(150.dp)
//.align(Alignment.TopCenter)
//.clip(CircleShape)
//)
 {
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(top = 20.dp, start = 20.dp, end = 20.dp, bottom = 0.dp)
    )
    {
        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = if (mop=="UPI") "#UPI" else "#Cash",
                modifier = Modifier.align(Alignment.TopStart),
                fontStyle = FontStyle.Italic,
                color = if (mop=="UPI") UPI else Cash
            )

            AsyncImage(
                model = model,
                contentDescription = "Store Logo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(150.dp)
                    .align(Alignment.TopCenter)
                    .clip(CircleShape)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = shopName ,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(
            modifier = Modifier.height(8.dp)
        )
        Text(
            text = shopAdress,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        Spacer(
            modifier = Modifier.height(8.dp)
        )
        Text(
            text = "GST: ${shopGST}",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "MOBILE: ${shopPhone}",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
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
            text = "NAME: ${name}",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = "PHONE NO: ${phoneNo}",
            style = MaterialTheme.typography.bodyMedium
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
            text = "BILL NO: INV-${billNo}",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(
            modifier = Modifier.height(2.dp)
        )
        Text(
            text = "DATE: ${date}",
            style = MaterialTheme.typography.bodyMedium
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
        Row() {
            Text(
                text = "SR.NO: ",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "ITEM NAME", style = MaterialTheme.typography.bodyMedium
            )
        }
        Spacer(
            modifier = Modifier.height(2.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "QTY",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "PRICE",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "AMOUNT",
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium,
            )

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