package com.example.billingapp.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.billingapp.database.DatabaseConnect
import com.example.billingapp.models.ProductsViewModel
import com.example.billingapp.ui.theme.CoffeeBrown
import com.example.billingapp.ui_components.BillHistoryTopBar
import com.example.billingapp.ui_components.HistoryScrollable
import kotlinx.coroutines.launch

@Composable
fun BillHistory(Database: DatabaseConnect, onBack: () -> Boolean, navigateToDetails: (Long) -> Unit){
    val viewModel: ProductsViewModel = viewModel()
    val scope = rememberCoroutineScope()
    var dialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val bills by Database.history.collectAsState(initial = emptyList())
    var confirm by remember { mutableStateOf(false) }
    LaunchedEffect(bills) {
        viewModel.groupBills(bills)
    }
    val groupedBills by viewModel.uiOutput.collectAsStateWithLifecycle()
    val createBackupLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        if (uri != null) {
            scope.launch {
                Database.backupDatabase(context, uri)
            }
        }
    }
    val createRestoreLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            scope.launch {
                Database.restoreBackup(context, uri)
            }
        }
    }

    Scaffold( containerColor = Color.White,
        floatingActionButton = {
            FloatingActionButton(onClick = {dialog=true}
                ,modifier = Modifier.padding(bottom = 8.dp)
                ,containerColor = CoffeeBrown, contentColor = Color.Black)
            {
        Text(text = "Restore", modifier = Modifier.padding(20.dp))
            }
                               },
        topBar = {BillHistoryTopBar(onBack,{createBackupLauncher.launch("Bill_Backup.json")})}){innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)
            .padding(start = 8.dp, end = 8.dp, bottom = 8.dp, top = 10.dp)) {
            HistoryScrollable(
                groupedBills,
                onDelete ={
                    bill->viewModel.selectedBill=bill
                          confirm=true},
                forDetails = { bill->navigateToDetails(bill)}

            )
//            bill->Database.deleteBill(bill)

        }

    }
    if (dialog){
        AlertDialog(
            containerColor = CoffeeBrown, titleContentColor = Color.Black, textContentColor = Color.Black,
            onDismissRequest = {
                dialog = false
                // Closes the dialog if the user taps outside of it
            },
            title = {
                Text(
                    text = "Confirm Action",
                    style = MaterialTheme.typography.titleMedium
                )
            },
            text = {
                Text(
                    text = "Confirming Restore Will Erase All Your Existing Data And Load The Data File You Will Select",
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
                        dialog=false
                        createRestoreLauncher.launch(arrayOf("application/json"))
                    }
                ) {
                    Text("Restore Data")
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
            }
        )
    }
    if (confirm){
        AlertDialog(
            containerColor = CoffeeBrown, titleContentColor = Color.Black, textContentColor = Color.Black,
            onDismissRequest = {
            confirm = false
            // Closes the dialog if the user taps outside of it
        },
        title = {
            Text(
                text = "Confirm Delete",
                style = MaterialTheme.typography.titleMedium
            )
        },
        text = {
            Text(
                text = "Are You Sure You Want To Delete This Bill",
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
                        Database.deleteBill(viewModel.selectedBill!!)
                    }
                    confirm=false
                }
            ) {
                Text("Delete Bill")
            }
        },
        dismissButton = {
            TextButton(
                colors = ButtonDefaults.buttonColors(
                    contentColor = CoffeeBrown,
                    containerColor = Color.White
                ),
                onClick = {
                    confirm=false
                }
            ) {
                Text("Cancel")
            }
        }
        )

    }
    }



