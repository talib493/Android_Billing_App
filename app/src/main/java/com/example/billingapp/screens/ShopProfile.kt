package com.example.billingapp.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.billingapp.R
import com.example.billingapp.database.DatabaseConnect
import com.example.billingapp.models.ProductsViewModel
import com.example.billingapp.ui.theme.CoffeeBrown
import java.io.File
import androidx.core.graphics.scale
import coil.compose.AsyncImagePainter
import com.canhub.cropper.CropImage
import com.canhub.cropper.CropImageActivity
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopProfile(
    Database: DatabaseConnect
) {
 val context = LocalContext.current
    val  Viewmodel: ProductsViewModel= viewModel()
    val shop by Database.shop.collectAsState(initial = null)
    var deleteShop by remember { mutableStateOf(false)}
    var updateShop by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    LaunchedEffect(shop) {
        shop?.let {
            Viewmodel.updateshopName(it.shopName)
            Viewmodel.updateshopAdress(it.address)
            Viewmodel.updateGST(it.gstNo)
            Viewmodel.updateShopPhone(it.phoneNo)
            Viewmodel.updateShopTerms(it.shopTerms)
        }
    }
        val cropImageLauncher = rememberLauncherForActivityResult(
            contract = CropImageContract() // Resolves 'CropImageContract'
            ) { result ->
        if (result.isSuccessful) {
            // SUCCESS: Resolves 'uriContent' cleanly!
            Viewmodel.selectedImageUri = result.uriContent
        } else {
            val error = result.error
        }
    }

// Launcher B: Your standard Image Picker
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            cropImageLauncher.launch(
                CropImageContractOptions(
                    uri = uri,
                    cropImageOptions = CropImageOptions().apply {
                        // --- THE FIX FOR A CIRCLE ---
                        cropShape = CropImageView.CropShape.OVAL // Turns the overlay shape into a circle

                        // Keep these to ensure it remains a perfect circle and stays large
                        aspectRatioX = 1
                        aspectRatioY = 1
                        fixAspectRatio = true
                        initialCropWindowPaddingRatio = 0f

                        // Toolbar configurations
                        activityTitle = "Crop Profile Picture"
                        cropMenuCropButtonTitle = "SAVE"
                    }
                )
            )
        }
    }

    val painter = rememberAsyncImagePainter(
        model = when {
            Viewmodel.selectedImageUri != null -> Viewmodel.selectedImageUri

            shop?.imageUri != null -> File(shop!!.imageUri!!)

            else -> R.drawable.placeholder
        }
    )
    // 1. Get the current underlying source model from your painter state
    val painterState = painter.state

    val currentPainterUriString = if (painterState is AsyncImagePainter.State.Success) {
        // Extract the raw request data model (e.g. your File path, Uri, or Resource Int)
        val rawData = painterState.result.request.data

        // Convert it to a predictable clean string format
        rawData.toString()
    } else {
        R.drawable.placeholder
        // Fallback if the painter is still loading or hasn't finished processing
    }


    val text = if (shop==null){
        "Save Profile"
    }else{ " Update"}

    Scaffold(
        containerColor = Color.White,
        contentColor = Color.Black,
        topBar = { TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = CoffeeBrown, titleContentColor = Color.Black, actionIconContentColor = Color.Black
            ),
            title ={Text(text = "Business Profile")},
            actions = {
                IconButton(
                    onClick = {deleteShop=true
                        Viewmodel.selectedImageUri=null
                    }

                ) {
                    Icon(painter = painterResource(R.drawable.delete_24px),
                    contentDescription = "Delete shop")
                }
            }
        ) }
    ) {innerPadding->

        Column(
            modifier = Modifier
                .fillMaxSize().padding(innerPadding)
                .padding( start = 20.dp, end = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier.padding(top = 20.dp)
            ) {

                Image(
                    modifier = Modifier
                        .size(200.dp)
                        .border(
                            width = 2.dp,
                            color = Color.Black,
                            shape = CircleShape
                        )
                        .clip(CircleShape)
                        .background(
                            color = Color.Transparent,
                            shape = CircleShape
                        ),
                    painter = painter,
                    contentDescription = "Profile Picture",
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .background(
                            color = Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(
                            start = 15.dp
                        )
                ) {

                    Icon(
                        painter = painterResource(R.drawable.image_arrow_up_24px),
                        contentDescription = "Add To Favorites",
                        tint = CoffeeBrown,
                        modifier = Modifier
                            .size(30.dp)
                            .clickable {
                                imagePickerLauncher.launch("image/*")
                            }
                    )
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

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
                value = Viewmodel.shopName,
                onValueChange = { Viewmodel.updateshopName(it) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Shop Name") },
                singleLine = true
            )

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
                value = Viewmodel.shopAdress,
                onValueChange = { Viewmodel.updateshopAdress(it) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Adress") },
                maxLines = 2
            )

            OutlinedTextField(
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = CoffeeBrown,
                    unfocusedTextColor = Color.Black,
                    focusedBorderColor = CoffeeBrown,
                    unfocusedBorderColor = Color.Black,
                    focusedLabelColor = CoffeeBrown,
                    unfocusedLabelColor = Color.Black,
                    cursorColor = CoffeeBrown
                ),
                value = Viewmodel.shopGST,
                onValueChange = { Viewmodel.updateGST(it) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("GST No.") },
                singleLine = true
            )

            OutlinedTextField(
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = CoffeeBrown,
                    unfocusedTextColor = Color.Black,
                    focusedBorderColor = CoffeeBrown,
                    unfocusedBorderColor = Color.Black,
                    focusedLabelColor = CoffeeBrown,
                    unfocusedLabelColor = Color.Black,
                    cursorColor = CoffeeBrown
                ),
                value = Viewmodel.shopPhone,
                onValueChange = { Viewmodel.updateShopPhone(it) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Phone Number") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone
                )
            )
            OutlinedTextField(
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = CoffeeBrown,
                    unfocusedTextColor = Color.Black,
                    focusedBorderColor = CoffeeBrown,
                    unfocusedBorderColor = Color.Black,
                    focusedLabelColor = CoffeeBrown,
                    unfocusedLabelColor = Color.Black,
                    cursorColor = CoffeeBrown
                ),
                value = Viewmodel.shopTerms,
                onValueChange = { Viewmodel.updateShopTerms(it) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Terms & Conditions") },
                singleLine = true
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                modifier = Modifier.fillMaxWidth().height(50.dp),
                onClick = {

                    if (shop != null){
                        updateShop=true
                } else {
                        val imagePath = if (Viewmodel.selectedImageUri != null) {

                            val originalBitmap = context.contentResolver
                                .openInputStream(Viewmodel.selectedImageUri!!)
                                ?.use { BitmapFactory.decodeStream(it) }

                            if (originalBitmap != null) {

                                val maxSize = 1024

                                val width = originalBitmap.width
                                val height = originalBitmap.height

                                val resizedBitmap =
                                    if (width <= maxSize && height <= maxSize) {
                                        originalBitmap
                                    } else {
                                        val scale = minOf(
                                            maxSize.toFloat() / width,
                                            maxSize.toFloat() / height
                                        )

                                        originalBitmap.scale(
                                            (width * scale).toInt(),
                                            (height * scale).toInt()
                                        )
                                    }

                                val file = File(context.filesDir, "shop_logo.jpg")

                                file.outputStream().use { output ->
                                    resizedBitmap.compress(
                                        Bitmap.CompressFormat.JPEG,
                                        90,
                                        output
                                    )
                                }

                                if (resizedBitmap !== originalBitmap) {
                                    originalBitmap.recycle()
                                    resizedBitmap.recycle()
                                } else {
                                    originalBitmap.recycle()
                                }

                                file.absolutePath

                            } else {
                                shop?.imageUri
                            }

                        } else {
                            shop?.imageUri
                        }
                        if (
                            Viewmodel.shopName == "" &&
                            Viewmodel.shopAdress == "" &&
                            Viewmodel.shopPhone == "" &&
                            Viewmodel.shopGST == "" &&
                            Viewmodel.shopTerms == "" &&
                            (imagePath == null || imagePath == "")
                        ) {
                            Toast.makeText(
                                context,
                                "Fill at least one field to save business profile",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            scope.launch {
                                Database.addShop(
                                    shopName = Viewmodel.shopName,
                                    address = Viewmodel.shopAdress,
                                    shopPhone = Viewmodel.shopPhone,
                                    shopGST = Viewmodel.shopGST,
                                    shopTerms = Viewmodel.shopTerms,
                                    profileImgUri = imagePath,
                                    text = "Saved",
                                    context = context
                                )
                            }
                        }

                    } },
                colors = ButtonDefaults.buttonColors(
                    containerColor = CoffeeBrown,
                    contentColor = Color.Black
                )
            ) {
                Text(text = text)
            }

        }
    }
    if(deleteShop){
        AlertDialog(
            containerColor = CoffeeBrown, titleContentColor = Color.Black, textContentColor = Color.Black,
            onDismissRequest = {
                deleteShop=false },
            title = {
                Text(text = "Delete Profile")
            },
            text = {Text(text="Are You Sure You Want To Delete Your Shop Profile")},
            confirmButton = {
                TextButton(
                    colors = ButtonDefaults.buttonColors(
                        contentColor = CoffeeBrown,
                        containerColor = Color.White
                    ),
                    onClick = {
                    if (shop!=null){
                        try {
                            scope.launch {
                                Database.deleteShop(shop!!,context)
                            }
                            Viewmodel.updateshopName("")
                            Viewmodel.updateshopAdress("")
                            Viewmodel.updateGST("")
                            Viewmodel.updateShopPhone("")
                            Viewmodel.updateShopTerms("")
                        }catch (e: Exception){
                            Log.d("DELETE",e.message.toString())
                        }


                    }else{
                        Toast.makeText(context,"Business Profile is already Empty", Toast.LENGTH_SHORT).show()
                    }
                    deleteShop=false
                }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(
                    colors = ButtonDefaults.buttonColors(
                    contentColor = CoffeeBrown,
                    containerColor = Color.White
                ), onClick = { deleteShop=false
                }) {
                    Text("Cancel")
                }
            }
        )
    }
    if(updateShop){
        AlertDialog(
            containerColor = CoffeeBrown, titleContentColor = Color.Black, textContentColor = Color.Black,
            onDismissRequest = {
                updateShop=false },
            title = {
                Text(text = "Update Profile")
            },
            text = {Text(text="Are You Sure You Want To Update Your Shop Profile")},
            confirmButton = {
                TextButton(
                    colors = ButtonDefaults.buttonColors(
                        contentColor = CoffeeBrown,
                        containerColor = Color.White
                    ),onClick = {
                    if (shop != null){
                        updateShop=true
                    }
                    val imagePath = if (Viewmodel.selectedImageUri != null) {

                        val originalBitmap = context.contentResolver
                            .openInputStream(Viewmodel.selectedImageUri!!)
                            ?.use { BitmapFactory.decodeStream(it) }

                        if (originalBitmap != null) {

                            val maxSize = 1024

                            val width = originalBitmap.width
                            val height = originalBitmap.height

                            val resizedBitmap =
                                if (width <= maxSize && height <= maxSize) {
                                    originalBitmap
                                } else {
                                    val scale = minOf(
                                        maxSize.toFloat() / width,
                                        maxSize.toFloat() / height
                                    )

                                    originalBitmap.scale(
                                        (width * scale).toInt(),
                                        (height * scale).toInt()
                                    )
                                }

                            val file = File(context.filesDir, "shop_logo.jpg")

                            file.outputStream().use { output ->
                                resizedBitmap.compress(
                                    Bitmap.CompressFormat.JPEG,
                                    90,
                                    output
                                )
                            }

                            if (resizedBitmap !== originalBitmap) {
                                originalBitmap.recycle()
                                resizedBitmap.recycle()
                            } else {
                                originalBitmap.recycle()
                            }

                            file.absolutePath

                        } else {
                            shop?.imageUri
                        }

                    } else {
                        shop?.imageUri
                    }
                    if (
                        Viewmodel.shopName == shop!!.shopName &&
                        Viewmodel.shopAdress == shop!!.address &&
                        Viewmodel.shopPhone == shop!!.phoneNo &&
                        Viewmodel.shopGST == shop!!.gstNo &&
                        Viewmodel.shopTerms == shop!!.shopTerms&&
                        currentPainterUriString==shop!!.imageUri
                        &&currentPainterUriString== R.drawable.placeholder
                    ) {
                        Toast.makeText(
                            context,
                            "No changes detected to update",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        scope.launch {
                            Database.addShop(
                                shopName = Viewmodel.shopName,
                                address = Viewmodel.shopAdress,
                                shopPhone = Viewmodel.shopPhone,
                                shopGST = Viewmodel.shopGST,
                                shopTerms = Viewmodel.shopTerms,
                                profileImgUri = imagePath,
                                text = "Updated",
                                context =context
                            )
                        }

                    }
                    updateShop=false
                }) {
                    Text("Update")
                }
            },
            dismissButton = {
                TextButton(
                    colors = ButtonDefaults.buttonColors(
                        contentColor = CoffeeBrown,
                        containerColor = Color.White
                    ),onClick = { updateShop=false
                }) {
                    Text("Cancel")
                }
            }
        )
    }
}
