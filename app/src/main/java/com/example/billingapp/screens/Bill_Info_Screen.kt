package com.example.billingapp.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.WhitePoint
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.billingapp.R
import com.example.billingapp.database.DatabaseConnect
import com.example.billingapp.models.ProductsViewModel
import com.example.billingapp.printer.PrinterManager
import com.example.billingapp.ui.theme.CoffeeBrown
import com.example.billingapp.ui_components.FinalBillScrollable
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BillInfoScreen(database: DatabaseConnect, Billno: Long){
    val viewModel: ProductsViewModel= viewModel()
    val isLoading = viewModel.isLoading
    val context  = LocalContext.current
    val scope = rememberCoroutineScope()
    val itemsasList = database.getBillDetails(Billno)
    val itemms by itemsasList.collectAsState(initial = emptyList())
    val shop by database.shop.collectAsState(null)
    var dialog by remember { mutableStateOf(false) }
    val connection by viewModel.activeConnection.collectAsState()


    val model: Any = when {
        shop?.imageUri != null -> File(shop!!.imageUri!!)
        else -> R.drawable.placeholder
    }
    val shopName = if (shop?.shopName.isNullOrBlank()) {
        "ESTIMATE"
    } else {
        shop!!.shopName
    }
    val shopAddress = if (shop?.address.isNullOrBlank()) {
        "Address:"
    } else {
        shop!!.address
    }
    val shopGST = if (shop?.gstNo.isNullOrBlank()) {
        ""
    } else {
        shop!!.gstNo
    }
    val shopPhone = if (shop?.phoneNo.isNullOrBlank()) {
        ""
    } else {
        shop!!.phoneNo
    }
    val shopTerms = if (shop?.shopTerms.isNullOrBlank()) {
        ""
    } else {
        shop!!.shopTerms
    }


    Scaffold(
        containerColor = Color.White ,
        topBar = {
        TopAppBar(
            colors = TopAppBarColors(
            containerColor = CoffeeBrown, titleContentColor = Color.Black,
            scrolledContainerColor = CoffeeBrown,
            navigationIconContentColor = Color.Black,
            actionIconContentColor = Color.Black
        ),
            title = { Text(text = "Bill Details") },
            actions = {
                IconButton(
                    onClick = {
                        try {
                        if(connection!=null){
                            val receipt = PrinterManager.SavebuildReceiptDantsu(
                            shopAddress=shopAddress,
                            shopName = shopName,
                            shopMobile = shopPhone,
                            shopGST = shopGST,
                            shopTerms = shopTerms,
                            details = itemms
                        )
                            viewModel.printReceipt(context,receipt,connection!!,shop?.imageUri)
                    }else{
                        dialog=true
                    }}catch (e: Exception){
                        Log.e("PRINT_SAVE",e.message.toString())
                    }
                    }) {
                    Icon(painter = painterResource(R.drawable.print_24px),
                        contentDescription = "Print Saved Bill")
                }

            }
        )
    },

    ) {innerPadding->
        Column(modifier = Modifier.padding(innerPadding).padding(10.dp)) {
            FinalBillScrollable(itemms ,
                shopName =shopName,
                shopAdress = shopAddress,
                shopGST= shopGST,
                shopPhone = shopPhone,
                shopTerms = shopTerms,
                model = model)
        }
    }

if (dialog){
    AlertDialog(
        containerColor = CoffeeBrown, titleContentColor = Color.Black, textContentColor = Color.Black,
        onDismissRequest = {
        dialog = false
    },
    title = {
        Text(
            text = "Connect Printer",
            style = MaterialTheme.typography.titleMedium
        )
    },
    text = {
        Text(
            text = "Your Printer Is Disconnected, Do You Want To Connect Your Phone To Your Printer?",
            style = MaterialTheme.typography.bodyMedium
        )
    },
    confirmButton = {
        Button(
            colors = ButtonDefaults.buttonColors(
                contentColor = CoffeeBrown,
                containerColor = Color.White
            ),
            onClick = {
                scope.launch {
                    viewModel.connect(context = context )
                    if (viewModel.isLoading==false){
                        dialog = false
                    }
                }

            }
        ) {
            Text("Connect")
        }
    },
    dismissButton = {
        TextButton(
            colors = ButtonDefaults.buttonColors(
                contentColor = CoffeeBrown,
                containerColor = Color.White
            ),
            onClick = {
                dialog=false
            }
        ) {
            Text("Cancel")
        }
    })
}
    if (isLoading) {
        Dialog(
            onDismissRequest = {},
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            )
        ){
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
                    .pointerInput(Unit) {},
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

        }

    }
}




