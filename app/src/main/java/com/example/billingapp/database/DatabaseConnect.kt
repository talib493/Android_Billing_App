package com.example.billingapp.database

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.room.withTransaction
import com.example.billingapp.models.Products
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlin.math.roundToInt

class DatabaseConnect(context: Context) {
    val database = BillingDatabase.getDatabase(context = context)
    val billDao = database.billDao()

    val history = billDao.getAllBills()

    val shop = billDao.getShop()

    suspend fun backupDatabase(context: Context, uri: Uri) {
        withContext(Dispatchers.IO){
            val bills = billDao.getFullBillS()
            val json = Json.encodeToString(bills)
            context.contentResolver.openOutputStream(uri)?.use { stream ->
                stream.write(json.toByteArray())
        }
            }
        withContext(Dispatchers.Main){
            Toast.makeText(context,"Backup File Saved Successfully", Toast.LENGTH_SHORT).show()
        }
    }

    suspend fun restoreBackup(context: Context, uri: Uri) {
        val statusMessage = withContext(Dispatchers.IO) {
            try {
                val json = context.contentResolver
                    .openInputStream(uri)
                    ?.bufferedReader()
                    ?.readText()

                if (json == null) return@withContext "Restoration Failed! File is empty"

                val restoredBills = Json.decodeFromString<List<BillWithDetails>>(json)
                val newBills = restoredBills.mapIndexed { index, details ->
                    val newId = (index + 1).toLong()

                    details.copy(
                        bill = details.bill.copy(
                            billNo = newId
                        ) ,
                        items = details.items.map {
                            it.copy(
                                billNo = newId
                            )
                        }
                    )
                }

                database.withTransaction {
                    billDao.deleteAllItems()
                    billDao.deleteAllBills()

                    billDao.resetSalesAutoIncrement()
                    newBills.forEach {
                        billDao.insertBill(it.bill)
                        billDao.insertItems(it.items)
                    }
                }
                "Backup restored successfully!"
            } catch (e: SerializationException) {
                Log.e("RESTORE", "Corrupted or invalid JSON format", e)
                "Restoration Failed! The File Format Is Corrupted"
            } catch (e: Exception) {
                Log.e("RESTORE", "Database error or file read error", e)
                "Restoration Failed! An unexpected error occurred"
            }
        }

        Toast.makeText(context, statusMessage, Toast.LENGTH_SHORT).show()
    }



    fun getBillDetails(billNo: Long): Flow<List<BillWithDetails>> {
        return billDao.getBillWithItems(billNo)
    }


    suspend fun adddata(
        CustomerName: String, CustomerPhone: String,
        Date: String,
        TotalQty: Int,
        TotalAmount: Double,
        modeOP: Boolean,
        products: List<Products>
    ) {
        var paymentMethod: String
        Log.d("ROOM_TEST", "ADD Data Function Called")
        val frozenProducts = products.toList()

        withContext(Dispatchers.IO) {
                Log.d("ROOM_TEST", "Couroutine Started")
                try {

                    Log.d("ROOM_TEST", "Database created")

                    Log.d("ROOM_TEST", "Dao Created")
                    Log.d("ROOM_TEST", "Received products: $frozenProducts")


                    if (modeOP) {
                        paymentMethod = "UPI"
                    } else {
                        paymentMethod = "Cash"
                    }
                    val billno = billDao.insertBill(
                        BillEntity(
                            customerName = CustomerName,
                            customerPhone = CustomerPhone,
                            date = Date,
                            totalQty = TotalQty,
                            totalAmount = (TotalAmount * 100).toInt(),
                            modeOfPayment = paymentMethod,
                            taxableAmount = ((TotalAmount / 1.05) * 100).roundToInt(),
                            cgstAmount = ((((TotalAmount / 1.05) * 100) / 100) * 2.5).roundToInt(),
                            sgstAmount = ((((TotalAmount / 1.05) * 100) / 100) * 2.5).roundToInt()
                        )
                    )

                    Log.d("ROOM_TEST", "Insert Done")
                    Log.d("ROOM_TEST", "Received products: $products")
                    Log.d("ROOM_TEST", "Items Insert Start")
                    val listOfItems = products.map { product ->
                        BillInfoEntity(
                            billNo = billno,
                            productName = product.productName,
                            productQuantity = product.quantity,
                            productAmount = product.price,
                            productTotal = product.quantity * product.price
                        )
                    }
                    billDao.insertItems(listOfItems)
                    Log.d("ROOM_TEST", "Items Insert Done of ${listOfItems}")
                    Log.d("ROOM_TEST", "Mapped items size = ${listOfItems.size}")


                } catch (e: Exception) {
                    Log.e("ROOM_TEST", e.toString())
                }


            }

    }

   suspend fun deleteBill(bill: BillEntity) {
       withContext(Dispatchers.IO) {
                billDao.delete(bill)
            }

    }
   suspend fun deleteShop(shop: ShopEntity,context: Context) {
        withContext(Dispatchers.IO){
            billDao.delete(shop)}
            withContext(Dispatchers.Main){
                Toast.makeText(context,"Business Profile Deleted Successfully", Toast.LENGTH_SHORT).show()
            }

   }

    suspend fun billNo(): Long {
        val lastId = billDao.getLastGeneratedId() ?: 0L
        val nextId = lastId + 1
        return nextId
    }

    suspend fun addShop(
        shopName: String,
        address: String,
        shopPhone: String,
        shopGST: String,
        shopTerms: String,
        profileImgUri: String?,
        text : String,
        context: Context
    ) {
        withContext(Dispatchers.IO) {
            billDao.insertShop(
                ShopEntity(
                    shopName = shopName,
                    address = address,
                    phoneNo = shopPhone,
                    gstNo = shopGST,
                    shopTerms = shopTerms,
                    imageUri = profileImgUri

                )
            )
        }
        withContext(Dispatchers.Main){
            Toast.makeText(context,"Profile ${text} Successfully", Toast.LENGTH_SHORT).show()
        }



    }

}



