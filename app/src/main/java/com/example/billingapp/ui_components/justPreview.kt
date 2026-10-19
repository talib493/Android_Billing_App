package com.example.billingapp.ui_components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.billingapp.database.DatabaseConnect
import com.example.billingapp.screens.BillingScreen
//@Preview(showSystemUi = true, showBackground = true)
@Composable
fun justPreview() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val database : DatabaseConnect = DatabaseConnect(context)
    fun navigate(){
    }
    BillingScreen(database, { navigate() }, { navigate() })

}
