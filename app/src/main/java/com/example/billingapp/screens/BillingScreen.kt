package com.example.billingapp.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.keepScreenOn
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.billingapp.R
import com.example.billingapp.database.DatabaseConnect
import com.example.billingapp.models.ProductsViewModel
import com.example.billingapp.printer.PrinterManager
import com.example.billingapp.ui.theme.Cash
import com.example.billingapp.ui.theme.CoffeeBrown
import com.example.billingapp.ui.theme.UPI
import com.example.billingapp.ui_components.CustomDatePickerDialog
import com.example.billingapp.ui_components.EditProductDialogContent
import com.example.billingapp.ui_components.FullPreview
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BillingScreen(
    Database: DatabaseConnect,
    navigateToHistory: () -> Unit,
    navigateToSprofile: () -> Unit
){
    val viewModel: ProductsViewModel = viewModel()
    val scope = rememberCoroutineScope()
    val connection by viewModel.activeConnection.collectAsState()
    val isPrinterConnected = connection != null
    val context = LocalContext.current
    val isLoading = viewModel.isLoading
    var showSheet by remember { mutableStateOf(false) }
    var dialog = viewModel.edit
    var sure by remember { mutableStateOf(false) }
    val customerName =viewModel.customer.customerName
    val customerNo =viewModel.customer.customerNo
    var date =viewModel.dateState.format(viewModel.date)
    val grandTotal= viewModel.getBillTotal()
    val totalQty= viewModel.getTotalQuantity()
    val index = viewModel.index
    val shop by Database.shop.collectAsState(initial = null)
    var datePickerState = rememberDatePickerState()


    LaunchedEffect(Unit) {
        val nextBillNo = withContext(Dispatchers.IO) {
            Database.billNo()
        }
        viewModel.updateBillNo(nextBillNo)
    }
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
    val mop = if (viewModel.isChecked) {
        "UPI"
    } else {
        "CASH"
    }



    Column(modifier = Modifier.fillMaxSize().padding(0.dp).background(color = Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
        ) {
        Column(modifier = Modifier.fillMaxWidth().background(color = Color.White).padding(top = 25.dp, start = 20.dp, end = 20.dp)) {

            Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically) {

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .clickable { navigateToSprofile() },
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = model ,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
            Button(
                modifier = Modifier.weight(1.5f),
                onClick = {
                    scope.launch {
                        viewModel.connect(context)
                    }


                },
                colors = ButtonDefaults
                    .buttonColors(
                        containerColor = CoffeeBrown,
                        contentColor = Color.Black),
                elevation = ButtonDefaults
                    .buttonElevation(
                        defaultElevation = 6.dp,
                        pressedElevation = 10.dp
                    )
            ) {
                Text(text = "Connect",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Spacer(modifier = Modifier.width(6.dp))

            Button(
                enabled = isPrinterConnected,
                modifier = Modifier.weight(1f),
                onClick = {
                    if (customerName == "" && customerNo == "" && totalQty == 0 && grandTotal == 0.0) {
                        Toast.makeText(context,"Fill All The Fields First", Toast.LENGTH_LONG)
                            .show()
                    } else {
                        val receipt=PrinterManager.buildReceiptDantsu(
                            date = date,
                            billNo = viewModel.billNo,
                            cName = customerName,
                            cPhone = customerNo,
                            gTotal = grandTotal,
                            gQty = totalQty,
                            products = viewModel.products,
                            shopName = shopName,
                            shopTerms = shopTerms,
                            shopGST = shopGST,
                            shopMobile = shopPhone,
                            shopAddress = shopAddress,
                            mop = mop
                        )
                        viewModel.printReceipt(context,receipt,connection,shop?.imageUri)


                    }

                },
                colors = ButtonDefaults.buttonColors(
                    disabledContainerColor = CoffeeBrown.copy(0.5f),
                    disabledContentColor = Color.Black.copy(0.5f),
                    containerColor = CoffeeBrown,
                    contentColor = Color.Black),
                elevation = ButtonDefaults.
                buttonElevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 10.dp
                )
            ) {
                Text(text = "Print",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium)
            }

        }
            Spacer(modifier = Modifier.height(5.dp))
            Row(modifier = Modifier.fillMaxWidth().padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween) {
                Button(
                    modifier = Modifier.weight(2f)
                    ,onClick = { showSheet = true},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CoffeeBrown,
                        contentColor = Color.Black
                    )
                ) {
                    Text(
                        "Create Invoice",
                        modifier = Modifier
                            .padding(8.dp))
                }
                Spacer(modifier = Modifier.width(4.dp))
                IconButton( onClick = {
                    if (customerName == "" && customerNo == "" && totalQty == 0 && grandTotal == 0.0) {
                        Toast.makeText(context,"Fill At least one field to save", Toast.LENGTH_LONG)
                            .show()
                    }else {
                        sure = true
                    }
                }
                    ,colors = IconButtonDefaults.iconButtonColors(
                        containerColor = CoffeeBrown), modifier = Modifier.weight(0.5f).size(50.dp)) {
                    Icon(painter = painterResource(R.drawable.save_24px),
                        contentDescription = "Data Rerd")
                }
                Spacer(modifier = Modifier.width(4.dp))
                IconButton( onClick = {navigateToHistory()}, colors = IconButtonDefaults.iconButtonColors(
                    containerColor = CoffeeBrown), modifier = Modifier.weight(0.5f).size(50.dp)) {
                    Icon(painter = painterResource(R.drawable.history_24px),
                        contentDescription = "Data History")
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))



       Column(modifier = Modifier.fillMaxWidth().padding(start = 26.dp, end = 26.dp, bottom = 10.dp)) {
           FullPreview(
           date = date,
           billNo = viewModel.billNo,
           cName = customerName,
           cPhone = customerNo,
           gTotal = grandTotal,
           gQty = totalQty,
           products = viewModel.products,
           mop = viewModel.isChecked,
               model = model,
               shopName = shopName,
               shopAddress = shopAddress,
               shopGST = shopGST,
               shopPhone = shopPhone,
               shopTerms = shopTerms

           ) }

        Spacer(modifier = Modifier.height(10.dp))

    }
    if (showSheet) {
        ModalBottomSheet(
            containerColor = Color.White,
            contentColor = Color.Black,
            onDismissRequest = { showSheet = false },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            {
                Text(
                    text = "Mode of Payment",
                    style = MaterialTheme.typography.titleMedium)

                Spacer(modifier = Modifier.height(10.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Cash", fontWeight = FontWeight.SemiBold, fontStyle = FontStyle.Italic, color = Cash)
                    Spacer(modifier = Modifier.width(6.dp))
                    Switch(colors = SwitchDefaults.colors(
                        uncheckedIconColor = Cash,
                        uncheckedBorderColor = Cash,
                        uncheckedTrackColor = Cash,
                        uncheckedThumbColor = Color.Black,
                        checkedIconColor = UPI,
                        checkedTrackColor = UPI,
                        checkedThumbColor = Color.Black,
                        checkedBorderColor = UPI
                    ),
                        checked = viewModel.isChecked, onCheckedChange = {it->viewModel.isChecked=it
                    })
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "UPI", fontWeight = FontWeight.SemiBold, fontStyle = FontStyle.Italic, color = UPI )
                }
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Customer Details",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row() {
                    OutlinedTextField(
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = CoffeeBrown,
                            unfocusedTextColor = Color.Black,
                            focusedBorderColor = CoffeeBrown,
                            unfocusedBorderColor = Color.Black,
                            focusedLabelColor = CoffeeBrown,
                            unfocusedLabelColor = Color.Black,
                            cursorColor = CoffeeBrown,
                        ),
                    modifier = Modifier.weight(2f),
                    value = customerName,
                    onValueChange = {viewModel.updateCustomerName(it) },
                    label = { Text("Customer Name") },
                    maxLines = 1
                )
                    Spacer(modifier = Modifier.width(4.dp))

                    OutlinedTextField(
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = CoffeeBrown,
                            unfocusedTextColor = Color.Black,
                            focusedBorderColor = CoffeeBrown,
                            unfocusedBorderColor = Color.Black,
                            focusedLabelColor = CoffeeBrown,
                            unfocusedLabelColor = Color.Black,
                            cursorColor = CoffeeBrown,
                        ),
                        modifier = Modifier.weight(2f),
                        value = customerNo,
                        onValueChange = {viewModel.updateCustomerNo(it)},
                        label = { Text("Phone No") },
                        maxLines = 1
                    ) }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Add Product",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = CoffeeBrown,
                        unfocusedTextColor = Color.Black,
                        focusedBorderColor = CoffeeBrown,
                        unfocusedBorderColor = Color.Black,
                        focusedLabelColor = CoffeeBrown,
                        unfocusedLabelColor = Color.Black,
                        cursorColor = CoffeeBrown,
                    ),
                    value = viewModel.name,
                    onValueChange = { viewModel.updateName(it) },
                    label = { Text("Product Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Row() {
                    OutlinedTextField(
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = CoffeeBrown,
                            unfocusedTextColor = Color.Black,
                            focusedBorderColor = CoffeeBrown,
                            unfocusedBorderColor = Color.Black,
                            focusedLabelColor = CoffeeBrown,
                            unfocusedLabelColor = Color.Black,
                            cursorColor = CoffeeBrown,
                        ),
                        value = viewModel.quantity,
                        onValueChange = { viewModel.updateQuantity(it) },
                        label = { Text("Quantity") },
                        modifier = Modifier.weight(2f)
                    )
                    Spacer(modifier = Modifier.width(4.dp))

                    OutlinedTextField(
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = CoffeeBrown,
                            unfocusedTextColor = Color.Black,
                            focusedBorderColor = CoffeeBrown,
                            unfocusedBorderColor = Color.Black,
                            focusedLabelColor = CoffeeBrown,
                            unfocusedLabelColor = Color.Black,
                            cursorColor = CoffeeBrown,
                        ),
                        value = viewModel.price,
                        onValueChange = { viewModel.updatePrice(it) },
                        label = { Text("Price") },
                        modifier = Modifier.weight(2f)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {
                        viewModel.addProducts()?.let { productName ->
                            Toast.makeText(
                                context,
                                "$productName Added To Invoice",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = CoffeeBrown, contentColor = Color.Black)
                ) {
                    Text("Add Product")
                }
            }
        }
    }
    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                // Dims background subtly
                .background(Color.Black.copy(alpha = 0.3f))
                // Blocks touch interactions from clicking background buttons
                .pointerInput(Unit) {},
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
    if(dialog){
        viewModel.loadProductForEditing(index)
            AlertDialog(
                containerColor = CoffeeBrown, titleContentColor = Color.Black, textContentColor = Color.Black,
                onDismissRequest = {
                    viewModel.DialogClose() },
                title = {
                    Text(text = "Edit Confirmation")
                },
                text = { EditProductDialogContent(viewModel = viewModel,index) },
                confirmButton = {
                    TextButton(
                        colors = ButtonDefaults.buttonColors(
                            contentColor = CoffeeBrown,
                            containerColor = Color.White
                        ),onClick = {
                        viewModel.updateList(index)
                        viewModel.DialogClose()
                    }) {
                        Text("Update")
                    }
                },
                dismissButton = {
                    TextButton( colors = ButtonDefaults.buttonColors(
                        contentColor = CoffeeBrown,
                        containerColor = Color.White),
                        onClick = { viewModel.DialogClose()
                    }) {
                        Text("Cancel")
                    }
                }
            )

        }
    if (sure){
        AlertDialog(
            containerColor = CoffeeBrown, titleContentColor = Color.Black, textContentColor = Color.Black,
            onDismissRequest = {
                sure = false
            },
            title = {
                Text(
                    text = "Confirm Action!!",
                    style = MaterialTheme.typography.titleMedium
                )
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center
                ) {
                Text(
                    text = "Are you sure that all fields are correct to save? Once saved, you cannot change this data.",
                    style = MaterialTheme.typography.bodyLarge
                )
                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = Color.Black)
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Mode of Payment (Re-check)",
                    style = MaterialTheme.typography.titleMedium)

                Spacer(modifier = Modifier.height(10.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Cash", fontWeight = FontWeight.SemiBold, fontStyle = FontStyle.Italic, color = Cash)
                    Spacer(modifier = Modifier.width(6.dp))
                    Switch(colors = SwitchDefaults.colors(
                        uncheckedIconColor = Cash,
                        uncheckedBorderColor = Cash,
                        uncheckedTrackColor = Cash,
                        uncheckedThumbColor = Color.Black,
                        checkedIconColor = UPI,
                        checkedTrackColor = UPI,
                        checkedThumbColor = Color.Black,
                        checkedBorderColor = UPI
                    ),
                        checked = viewModel.isChecked, onCheckedChange = {it->viewModel.isChecked=it
                        })
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "UPI", fontWeight = FontWeight.SemiBold, fontStyle = FontStyle.Italic, color = UPI )
                }
                    }
            },
            confirmButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        contentColor = CoffeeBrown,
                        containerColor = Color.White
                    ),
                    onClick = {

                        if (customerName == "" && customerNo == "" && totalQty == 0 && grandTotal == 0.0) {
                            Toast.makeText(context,"Fill All The Fields First", Toast.LENGTH_LONG)
                                .show()
                        } else {
                            Log.d("ROOM_TEST", "--- BUTTON CLICKED ---")
                            Log.d("ROOM_TEST", "ViewModel Instance ID: ${viewModel.hashCode()}")
                            Log.d("ROOM_TEST", "Is products list empty? ${viewModel.products.isEmpty()}")
                            Log.d("ROOM_TEST", "Items count in VM: ${viewModel.products.size}")
                            val snapshot = viewModel.products.toList()
                            scope.launch {

                                Database.adddata(
                                    CustomerName = customerName,
                                    CustomerPhone = customerNo,
                                    Date = date,
                                    TotalQty = totalQty,
                                    TotalAmount = grandTotal,
                                    modeOP = viewModel.isChecked,
                                    products = snapshot
                                )
                            }

                            Toast.makeText(context,"Bill NO: ${viewModel.billNo} Saved", Toast.LENGTH_LONG)
                                .show()
                            viewModel.refresh()
                            sure =false

                        }
                    }
                ) {
                    Text("Save Data")
                }
            },
            dismissButton = {
                TextButton(
                    colors = ButtonDefaults.buttonColors(
                        contentColor = CoffeeBrown,
                        containerColor = Color.White
                    ),
                    onClick = {
               sure=false
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
    if (viewModel.datePickerIsVisible){
        CustomDatePickerDialog(
            datePickerState = datePickerState,
            onConfirm = {
                datePickerState.selectedDateMillis?.let { d ->
                            viewModel.dateState = Instant.ofEpochMilli(d)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                }
                viewModel.datePickerIsVisible = false
                        },
            onDismiss = {
                viewModel.datePickerIsVisible=false
            }
        )
    }
}





