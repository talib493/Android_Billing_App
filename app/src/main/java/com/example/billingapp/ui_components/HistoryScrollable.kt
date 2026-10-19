package com.example.billingapp.ui_components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.billingapp.database.BillEntity

@Composable
fun HistoryScrollable(
    groupedbills: Map<String, Map<String, List<BillEntity>>>,
    onDelete: (BillEntity) -> Unit,
    forDetails: (Long) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {

        groupedbills.forEach { (date, mopMap) ->

            item {

                val totalAmountOfDay = mopMap.values.flatten().sumOf { it.totalAmount }.toDouble()
                val noOfTransactions = mopMap.values.flatten().size

                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    border = BorderStroke(1.dp, Color.Gray),
                    shape = RoundedCornerShape(12.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        // Date Header
                        dateHeader(
                            date = date,
                            totalAmount = String.format(java.util.Locale.US, "%.2f", ((totalAmountOfDay)/100)),
                            noOfTransactshions = noOfTransactions.toString()
                        )

                        // Each Mode of Payment
                        mopMap.forEach { (mop, bills) ->

                            val totalMopAmount = bills.sumOf { it.totalAmount }.toDouble()

                            mopRow(
                                mop = mop,
                                totalofmop = String.format(java.util.Locale.US, "%.2f", ((totalMopAmount)/100)),
                                noOfbill = bills.size.toString()
                            )


                            bills.forEach { bill ->
                                HistoryCard(
                                    mop = bill.modeOfPayment,
                                    billNo = bill.billNo.toString(),
                                    customerName = bill.customerName,
                                    customerPhone = bill.customerPhone,
                                    date = bill.date,
                                    totalQty = bill.totalQty,
                                    totalAmount = bill.totalAmount,
                                    Delete = { onDelete(bill) },
                                    Details = { forDetails(bill.billNo) }
                                )

                            }
                        }
                    }
                }
            }
        }
    }
}