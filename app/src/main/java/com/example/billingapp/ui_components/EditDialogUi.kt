package com.example.billingapp.ui_components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.billingapp.models.ProductsViewModel

@Composable
fun EditProductDialogContent(
    viewModel: ProductsViewModel,
    index: Int// Or pass your explicit states/lambdas here
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- Row 1: The Three Text Labels ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)

        ) {
            Text(
                text = "Product Name",
                modifier = Modifier.weight(2f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Qty",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Price",
                modifier = Modifier.weight(1.5f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // --- Row 2: The Three Outlined Text Fields ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            OutlinedTextField(
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Gray,
                    unfocusedTextColor = Color.Black,
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.Black,
                    focusedContainerColor = Color.Black
                ),
                value = viewModel.newProduct,
                onValueChange = {viewModel.updateNewProduct(it)},
                modifier = Modifier.weight(2f),
                singleLine = true,
            )

            // Field 2: Price
            OutlinedTextField(
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Gray,
                    unfocusedTextColor = Color.Black,
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.Black,
                    focusedContainerColor = Color.Black

                ),
                value = viewModel.newQty,
                onValueChange = { viewModel.updateNewProductQty(it) },
                modifier = Modifier.weight(1f),
                singleLine = true
            )

            // Field 3: Stock
            OutlinedTextField(
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Gray,
                    unfocusedTextColor = Color.Black,
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.Black,
                    focusedContainerColor = Color.Black

                ),
                value = viewModel.newPrice,
                onValueChange = { viewModel.updateNewProductPrice(it) },
                modifier = Modifier.weight(1.5f),
                singleLine = true
            )
        }
    }
}