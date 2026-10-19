package com.example.billingapp.models

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnection
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections
import com.example.billingapp.database.BillEntity
import com.example.billingapp.database.BillWithDetails
import com.example.billingapp.printer.PrinterManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ProductsViewModel(): ViewModel() {

    var datePickerIsVisible by mutableStateOf(false)

    var isChecked by  mutableStateOf(false)
    private val  groupBills = MutableStateFlow<Map<String, Map<String , List<BillEntity>>>>(emptyMap())

    val uiOutput : StateFlow<Map<String, Map<String, List<BillEntity>>>> = groupBills

    var selectedImageUri by mutableStateOf<Uri?>(null)


    var selectedBill by mutableStateOf<BillEntity?>(value = null)


    var shopName by mutableStateOf("")
    fun updateshopName(value: String){
        shopName=value
    }
    var shopAdress by mutableStateOf("")
    fun updateshopAdress(value: String){
        shopAdress=value
    }
    var shopGST by mutableStateOf("")
    fun updateGST(value: String){
        shopGST=value
    }
    var shopPhone by mutableStateOf("")
    fun updateShopPhone(value: String){
        shopPhone=value
    }
    var shopTerms by mutableStateOf("")
    fun updateShopTerms(value: String){
        shopTerms=value
    }
    var newProduct by mutableStateOf("")
    private set

    fun updateNewProduct(value: String){
        newProduct=value
    }
    var newQty by mutableStateOf("")
        private set

    fun updateNewProductQty(value: String){
        newQty=value

    } var newPrice by mutableStateOf("")
        private set

    fun updateNewProductPrice(value: String){
        newPrice=value
    }

    var index: Int = 0

    private val _activeConnection = MutableStateFlow<BluetoothConnection?>(null)
    val activeConnection: StateFlow<BluetoothConnection?> = _activeConnection

    var edit by mutableStateOf(false)


    var isLoading by mutableStateOf(false)
        private set

    var billNo by mutableStateOf("")
        private set
    var customer by mutableStateOf(Customer("", ""))
        val products = mutableStateListOf<Products>()

        var name by mutableStateOf("")
        private set

        var quantity by mutableStateOf("")
            private set

        var price by mutableStateOf("")
            private set
    fun updateBillNo(value: Long){
        billNo=value.toString()
    }
    fun updateCustomerName(value: String){
        customer=customer.copy(customerName = value)
    }
    fun updateCustomerNo(value: String){
        customer=customer.copy(customerNo = value)
    }

        fun updateName(value: String) {
            name = value
        }

        fun updateQuantity(value: String) {
            quantity = value
        }

        fun updatePrice(value: String) {
            price = value
        }
    var dateState by mutableStateOf(LocalDate.now())

    val date = DateTimeFormatter.ofPattern("d MMM yyyy")


    fun addProducts(): String? {
        val qty = quantity.trim().toIntOrNull()

        if (qty == null ||name.isBlank()|| qty <= 0) return null

        val addedName = name

        products.add(
            Products(
                productName = name.trim(),
                quantity = qty,
                price = price.trim().toDoubleOrNull() ?: 0.0
            )
        )
        Log.d("BILL", products.toString())

        name = ""
        quantity = ""
        price = ""

        return addedName
    }
    fun getBillTotal(): Double{
return products.sumOf { it.quantity * it.price }
    }
    fun getTotalQuantity(): Int {
        return products.sumOf { it.quantity  }
    }
    fun printReceipt(
        context: Context,
        receipt: String,
        printerConnection: BluetoothConnection?,
        imagePath : String?
        ) {
            viewModelScope.launch(Dispatchers.IO) {
                PrinterManager.printTestReceipt(
                    context = context,
                    receipt = receipt,
                    printerConnection = printerConnection,
                    imagePath = imagePath
                )
            }
        }
    suspend fun connect(context: Context): BluetoothConnection? {
        return withContext(Dispatchers.IO) {
            isLoading = true
            try {
                activeConnection.value?.disconnect()
                _activeConnection.value = null

                val connection = BluetoothPrintersConnections.selectFirstPaired()

                // 2. Update the state variable here
                _activeConnection.value = connection
                if (_activeConnection.value!=null){
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Printer Connected! ✅", Toast.LENGTH_SHORT).show()
                }
                }else{
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Connection Failed ❌", Toast.LENGTH_SHORT).show()
                    }
                }

                return@withContext connection
            } catch (e: Exception) {
                _activeConnection.value = null

                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
                return@withContext null
            }finally {
                isLoading = false

            }
        }
    }
    fun refresh(){
        customer.customerName =""
        customer.customerNo =""
        products.clear()
        val result: Long = billNo.toLong()+1
        billNo = result.toString()
    }
    fun statechange(id: Int) {
        Log.d("STATE_CHANGE","stateChange Function Called")
        Log.d("STATE_CHANGE",id.toString())
        edit = true
        index=id-1
    }
    fun DialogClose(){
         edit=false
    }
    fun loadProductForEditing( i: Int) {
        newProduct = products.get(i).productName
        newQty = products.get(i).quantity.toString()
        newPrice = products.get(i).price.toString()
    // Set the default database text here
    }
    fun updateList(j:Int){
      val currentItem =  products.get(j)
        products[j] = currentItem.copy(productName = newProduct, quantity = newQty.toInt(), price = newPrice.toDouble())
    }
    fun groupBills(bills:List<BillEntity>){
        val group = bills.groupBy { it.date }.mapValues { dateEntry ->
            dateEntry.value.groupBy { it.modeOfPayment }
        }
        groupBills.value=group
    }

}

