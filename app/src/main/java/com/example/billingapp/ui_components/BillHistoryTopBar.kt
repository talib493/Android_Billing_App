package com.example.billingapp.ui_components

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.billingapp.R
import com.example.billingapp.ui.theme.CoffeeBrown

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BillHistoryTopBar(onBack: () -> Boolean, onBackup:()-> Unit) {
    TopAppBar(
    title = { Text(text = "BILL HISTORY",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold
        )
        },
        actions = {
            IconButton(onClick = {onBackup()}) {
                Icon(painter = painterResource(R.drawable.backup_24px),
                    contentDescription = "Backup Database")
            }
        },
        navigationIcon = {
            IconButton(onClick = {onBack()}) {
                Icon(
                    painter = painterResource(R.drawable.arrow_back_24px),
                    contentDescription = "Back", modifier = Modifier.padding(8.dp)
                )
            }
        }, colors = TopAppBarDefaults.topAppBarColors(
            containerColor = CoffeeBrown,
            titleContentColor = Color.Black,
            navigationIconContentColor = Color.Black,
            actionIconContentColor = Color.Black
        )
    )
}