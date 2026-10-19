package com.example.billingapp.printer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.os.Environment
import android.util.Log
import android.widget.Toast
import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnection
import com.dantsu.escposprinter.textparser.PrinterTextParserImg
import com.example.billingapp.R
import com.example.billingapp.database.BillInfoEntity
import com.example.billingapp.database.BillWithDetails
import com.example.billingapp.database.DatabaseConnect
import com.example.billingapp.models.Products
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

object PrinterManager {
    fun smartWrap(text: String, max: Int = 32): List<String> {
        val words = text.split(" ")
        val lines = mutableListOf<String>()

        var current = ""

        for (word in words) {
            if ((current + " " + word).trim().length > max) {
                lines.add(current.trim())
                current = word
            } else {
                current += " $word"
            }
        }

        if (current.isNotBlank()) {
            lines.add(current.trim())
        }

        return lines
    }

    fun formatProductRow(srNo: Int, p: Products): String {

        return "[L]${srNo}: ${p.productName}"
    }

    fun formatValueRow(p: Products): String {

        val amount = p.quantity * p.price

        return "[L]${p.quantity}[C]${"%.2f".format(p.price)}[R]${"%.2f".format(amount)}"
    }
    fun SaveformatProductRow(srNo: Int, p: BillInfoEntity): String {

        return "[L]${srNo}: ${p.productName}"
    }

    fun SaveformatValueRow(p: BillInfoEntity): String {


        return "[L]${p.productQuantity}[C]${"%.2f".format(p.productAmount)}[R]${"%.2f".format(p.productTotal)}"
    }



    fun buildReceiptDantsu(
        mop: String,
        shopName: String,
        shopAddress: String,
        shopGST: String,
        shopMobile: String,
        date: String,
        billNo: String,
        cPhone: String,
        cName: String,
        gQty: Int,
        gTotal: Double,
        products: List<Products>,
        shopTerms: String
    ): String {

// 2. Perform math cleanly using standard decimals
        val taxableAmountDouble = (gTotal / 105.0) * 100.0
        val taxDouble = (taxableAmountDouble / 100.0) * 2.5

// 3. Format into strings for your receipt layout
        val taxableAmountStr = String.format(java.util.Locale.US, "%.2f", taxableAmountDouble)
        val taxStr = String.format(java.util.Locale.US, "%.2f", taxDouble)
        val totalStr = String.format(java.util.Locale.US, "%.2f", gTotal)

        val sb = StringBuilder()

        // ================= HEADER =================
        sb.appendLine("[C]{LOGO}")
        sb.appendLine()
        sb.appendLine("[C]<b>$shopName</b>")
        sb.appendLine()
        smartWrap(shopAddress).forEach {
            sb.appendLine("[L]$it")
        }
        sb.appendLine()
        sb.appendLine("[C]GST: ${shopGST}")
        sb.appendLine("[C]PHONE NO: ${shopMobile}")

        sb.appendLine("--------------------------------")

        // ================= CUSTOMER =================
        sb.appendLine("[L]NAME : $cName")
        sb.appendLine("[L]PHONE: $cPhone")

        sb.appendLine("--------------------------------")

        sb.appendLine("[L]BILL NO: $billNo")
        sb.appendLine("[L]DATE: $date")

        sb.appendLine("--------------------------------")

        // ================= TABLE HEADER =================
        sb.appendLine("[L]<b>SR.NO: ITEM NAME</b>")
        sb.appendLine("[L]QTY[C]PRICE[R]AMOUNT")
        sb.appendLine("--------------------------------")

        // ================= ITEMS =================
        products.forEachIndexed { index, p ->


            sb.appendLine(formatProductRow(index + 1, p))
            sb.appendLine(formatValueRow(p))
            sb.appendLine("--------------------------------")
        }

        // ================= FOOTER =================
        sb.appendLine("[L]TOTAL QTY : [R]$gQty")
        sb.appendLine("[L]TAXABLE AMOUNT : [R]${taxableAmountStr}")
        sb.appendLine("[L]SGST: [R]${taxStr}")
        sb.appendLine("[L]CGST: [R]${taxStr}")
        sb.appendLine("[L]TOTAL AMOUNT : [R]${totalStr}")
        sb.appendLine()
        sb.appendLine("[L]MODE OF PAYMENT: [R]$mop")
        sb.appendLine()
        sb.appendLine("[C]<b>TERMS AND CONDITIONS</b>")
        smartWrap(shopTerms).forEach {
            sb.appendLine("[L]$it")
        }

        sb.appendLine("--------------------------------")

        sb.appendLine("[C]<b>THANK YOU VISIT AGAIN!</b>")

        return sb.toString()
    }

    suspend fun printTestReceipt(
        printerConnection: BluetoothConnection?,
        context: Context,
        receipt: String,
        imagePath: String?
    ): Boolean {

        try {


            if (printerConnection == null) {

                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        "No Printer Found",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            if (printerConnection?.isConnected == false) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        "Connection Socket is dead ",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
            }

            if (printerConnection?.isConnected == false) {
                Toast.makeText(
                    context,
                    "Connection Socket is dead ",
                    Toast.LENGTH_SHORT
                ).show()
            }

//
            val printer = EscPosPrinter(
                printerConnection,
                203,
                48f,
                32
            )
            val original = if (imagePath != null) {
                BitmapFactory.decodeFile(imagePath)
            } else {
                BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.placeholder
                )
            }
            val blackWhite = makeCircleWithWhiteBackground(original)

            val logoHex = PrinterTextParserImg.bitmapToHexadecimalString(
                printer,
                blackWhite,
                true
            )

            val finalReceipt = receipt.replace(
                "{LOGO}",
                "<img>$logoHex</img>"
            )

            printer.printFormattedText(
                finalReceipt
            )
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    "Printing ",
                    Toast.LENGTH_SHORT
                ).show()
            }
            return true


        } catch (e: Exception) {
            Log.e("PRINT_ERROR", "Printing failed", e)

            withContext(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    e.stackTraceToString(),
                    Toast.LENGTH_LONG
                ).show()
            }

            try {
                printerConnection?.disconnect()
            } catch (_: Exception) {
            }

            return false
        }
    }
    fun makeCircleWithWhiteBackground(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val size = minOf(width, height)

        // 1. Create a temporary bitmap to remove dark backgrounds universally
        val cleanedSource = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        for (y in 0 until height) {
            for (x in 0 until width) {
                val pixel = bitmap.getPixel(x, y)
                val alpha = Color.alpha(pixel)

                if (alpha == 0) {
                    cleanedSource.setPixel(x, y, Color.WHITE)
                } else {
                    val r = Color.red(pixel)
                    val g = Color.green(pixel)
                    val b = Color.blue(pixel)

                    // Standard luminance formula to find overall brightness
                    val brightness = (0.299 * r + 0.587 * g + 0.114 * b).toInt()

                    // Any pixel darker than this threshold becomes white background.
                    // Brighter foreground colors (pink, yellow, red, etc.) are preserved.
                    if (brightness < 40) {
                        cleanedSource.setPixel(x, y, Color.WHITE)
                    } else {
                        cleanedSource.setPixel(x, y, Color.rgb(r, g, b))
                    }
                }
            }
        }

        // 2. Set up the final cropped canvas filled with a solid white background
        val finalBitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(finalBitmap)
        canvas.drawColor(Color.WHITE)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)

        // 3. Isolate the circle mask layering so corners stay white
        val layerId = canvas.saveLayer(0f, 0f, size.toFloat(), size.toFloat(), null)

        val radius = size / 2f
        canvas.drawCircle(radius, radius, radius, paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

        // 4. Center-crop and draw using the dynamically cleaned image
        val srcLeft = (width - size) / 2
        val srcTop = (height - size) / 2
        val srcRect = Rect(srcLeft, srcTop, srcLeft + size, srcTop + size)
        val destRect = Rect(0, 0, size, size)

        canvas.drawBitmap(cleanedSource, srcRect, destRect, paint)

        paint.xfermode = null
        canvas.restoreToCount(layerId)

        return finalBitmap
    }
    fun SavebuildReceiptDantsu(
        shopName: String,
        shopAddress: String,
        shopGST: String,
        shopMobile: String,
        details: List<BillWithDetails>,
        shopTerms: String
    ): String {
        val bill =details.firstOrNull()?.bill
        val items = details.firstOrNull()?.items
        val totalAmtDouble = (bill?.totalAmount ?: 0).toDouble() / 100.0

// 2. Perform math cleanly using standard decimals
        val taxableAmountDouble = (totalAmtDouble / 105.0) * 100.0
        val taxDouble = (taxableAmountDouble / 100.0) * 2.5

// 3. Format into strings for your receipt layout
        val taxableAmountStr = String.format(java.util.Locale.US, "%.2f", taxableAmountDouble)
        val taxStr = String.format(java.util.Locale.US, "%.2f", taxDouble)
        val totalStr = String.format(java.util.Locale.US, "%.2f", totalAmtDouble)

        val sb = StringBuilder()

        // ================= HEADER =================
        sb.appendLine("[C]{LOGO}")
        sb.appendLine()
        sb.appendLine("[C]<b>$shopName</b>")
        sb.appendLine()
        smartWrap(shopAddress).forEach {
            sb.appendLine("[L]$it")
        }
        sb.appendLine()
        sb.appendLine("[C]GST: ${shopGST}")
        sb.appendLine("[C]PHONE NO: ${shopMobile}")

        sb.appendLine("--------------------------------")

        // ================= CUSTOMER =================
        sb.appendLine("[L]NAME : ${bill?.customerName}")
        sb.appendLine("[L]PHONE: ${bill?.customerPhone}")

        sb.appendLine("--------------------------------")

        sb.appendLine("[L]BILL NO: ${bill?.billNo}")
        sb.appendLine("[L]DATE: ${bill?.date}")

        sb.appendLine("--------------------------------")

        // ================= TABLE HEADER =================
        sb.appendLine("[L]<b>SR.NO: ITEM NAME</b>")
        sb.appendLine("[L]QTY[C]PRICE[R]AMOUNT")
        sb.appendLine("--------------------------------")

        // ================= ITEMS =================
        items?.forEachIndexed { index, i ->


            sb.appendLine(SaveformatProductRow(index + 1, i))
            sb.appendLine(SaveformatValueRow(i))
            sb.appendLine("--------------------------------")
        }

        // ================= FOOTER =================
        sb.appendLine("[L]TOTAL QTY : [R]${bill?.totalQty}")
        sb.appendLine("[L]TAXABLE AMOUNT : [R]${taxableAmountStr}")
        sb.appendLine("[L]SGST: [R]${taxStr}")
        sb.appendLine("[L]CGST: [R]${taxStr}")
        sb.appendLine("[L]TOTAL AMOUNT : [R]${totalStr}")
        sb.appendLine("[L]MODE OF PAYMENT: [R]${bill?.modeOfPayment}")
        sb.appendLine()
        sb.appendLine("[C]<b>TERMS AND CONDITIONS</b>")
        smartWrap(shopTerms).forEach {
            sb.appendLine("[L]$it")
        }

        sb.appendLine("--------------------------------")

        sb.appendLine("[C]<b>THANK YOU VISIT AGAIN!</b>")

        return sb.toString()
    }


}