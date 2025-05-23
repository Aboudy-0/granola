package com.example.granola.ui.screens.products

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.granola.R
import com.example.granola.viewmodel.ProductViewModel
import com.example.granola.model.Product
import com.example.granola.ui.theme.AccentBrown
import com.example.granola.ui.theme.DarkBrown
import com.example.granola.ui.theme.LightBackground
import com.example.granola.ui.theme.LightBrown
import com.example.granola.ui.theme.MediumBrown
import com.navigation.ROUT_ABOUT
import com.navigation.ROUT_ADD_PRODUCT
import com.navigation.ROUT_CONTACT
import com.navigation.ROUT_CUSTOM
import com.navigation.ROUT_EDIT_PRODUCT
import com.navigation.ROUT_HOME
import com.navigation.ROUT_PRODUCT_LIST
import com.navigation.ROUT_USER_PRODUCT_LIST
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.OutputStream

// Color Definitions (add these to your theme package if not already there)
val darkBrown = Color(0xFF4E342E)
val mediumBrown = Color(0xFF795548)
val lightBrown = Color(0xFFD7CCC8)
val backgroundBrown = Color(0xFFEFEBE9)
val textOnBrown = Color.White
val textOnLight = Color(0xFF3E2723)

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProductListScreen(navController: NavController, viewModel: ProductViewModel) {
    val productList by viewModel.allProducts.observeAsState(emptyList())
    var searchQuery by remember { mutableStateOf("") }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val selectedItem = remember { mutableStateOf("Products") }

    val filteredProducts = productList.filter {
        it.name.contains(searchQuery, ignoreCase = true)
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(navController, selectedItem) {
                scope.launch { drawerState.close() }
            }
        }
    ) {
        Scaffold(
            topBar = {
                Column() {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                "LujaGranola",
                                color = textOnBrown,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = darkBrown,
                            navigationIconContentColor = textOnBrown,
                            actionIconContentColor = textOnLight
                        ),
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Default.Menu, contentDescription = "Menu")
                            }
                        }
                    )

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        placeholder = { Text("Search products...", color = mediumBrown) },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = mediumBrown
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = darkBrown,
                            unfocusedContainerColor = lightBrown,
                            focusedBorderColor = textOnLight,
                            unfocusedBorderColor = mediumBrown,
                            focusedTextColor = textOnLight,
                            unfocusedTextColor = mediumBrown
                        )
                    )
                }
            },
            containerColor = LightBackground
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundBrown)
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {
                items(filteredProducts.size) { index ->
                    ProductItem1(navController, filteredProducts[index], viewModel)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun DrawerContent(
    navController: NavController,
    selectedItem: MutableState<String>,
    onItemClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightBrown)
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .padding(bottom = 16.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(MediumBrown, LightBrown)
                    ),
                    shape = RoundedCornerShape(bottomEnd = 16.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.img_1),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .border(2.dp, AccentBrown, CircleShape)
            )
        }

        val drawerItems = listOf(
            "Home" to ROUT_HOME,
            "Products" to ROUT_USER_PRODUCT_LIST,
            "About" to ROUT_ABOUT,
            "Contact" to ROUT_CONTACT,
            "Custom" to ROUT_CUSTOM
        )

        drawerItems.forEach { (item, route) ->
            NavigationDrawerItem(
                label = {
                    Text(
                        text = item,
                        color = if (selectedItem.value == item) AccentBrown else DarkBrown,
                        fontWeight = FontWeight.Medium
                    )
                },
                selected = selectedItem.value == item,
                onClick = {
                    selectedItem.value = item
                    navController.navigate(route) {
                        popUpTo("home") { inclusive = true }
                    }
                    onItemClicked()
                },
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .fillMaxWidth(),
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = AccentBrown.copy(alpha = 0.2f),
                    unselectedContainerColor = Color.Transparent,
                    selectedTextColor = AccentBrown,
                    unselectedTextColor = DarkBrown
                ),
                shape = RoundedCornerShape(8.dp)
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun ProductItem1(navController: NavController, product: Product, viewModel: ProductViewModel) {
    val painter: Painter = rememberAsyncImagePainter(
        model = product.imagePath?.let { Uri.parse(it) } ?: Uri.EMPTY
    )
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (product.id != 0) {
                    navController.navigate(ROUT_EDIT_PRODUCT)
                }
            },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = LightBrown
        )
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = painter,
                contentDescription = "Product Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .align(Alignment.BottomStart)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                DarkBrown.copy(alpha = 0.8f)
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(
                    text = product.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Ksh${product.price}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Green
                )
            }

            Row(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilledTonalButton(
                    onClick = {
                        val smsIntent = Intent(Intent.ACTION_SENDTO)
                        smsIntent.data = "smsto:${product.phone}".toUri()
                        smsIntent.putExtra("sms_body", "Is the ${product.name} still available..?")
                        context.startActivity(smsIntent)
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = AccentBrown,
                        contentColor = Color.White
                    )
                ) {
                    Icon(Icons.Default.Send, contentDescription = "Message Seller")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Message")
                }

                IconButton(
                    onClick = { generateProductPDF1(context, product) },
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = AccentBrown,
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_file_download_24),
                        contentDescription = "Download PDF",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
fun generateProductPDF1(context: Context, product: Product) {
    val pdfDocument = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(300, 500, 1).create()
    val page = pdfDocument.startPage(pageInfo)
    val canvas = page.canvas
    val paint = android.graphics.Paint()

    val bitmap: Bitmap? = try {
        product.imagePath?.let {
            val uri = Uri.parse(it)
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream)
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    bitmap?.let {
        val scaledBitmap = Bitmap.createScaledBitmap(it, 250, 150, false)
        canvas.drawBitmap(scaledBitmap, 25f, 20f, paint)
    }

    paint.textSize = 16f
    paint.isFakeBoldText = true
    canvas.drawText("Product Details", 80f, 200f, paint)

    paint.textSize = 12f
    paint.isFakeBoldText = false
    canvas.drawText("Name: ${product.name}", 50f, 230f, paint)
    canvas.drawText("Price: Ksh${product.price}", 50f, 250f, paint)
    canvas.drawText("Seller Phone: ${product.phone}", 50f, 270f, paint)

    pdfDocument.finishPage(page)

    val fileName = "${product.name}_Details.pdf"
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
        put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
    }

    val contentResolver = context.contentResolver
    val uri = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

    if (uri != null) {
        try {
            val outputStream: OutputStream? = contentResolver.openOutputStream(uri)
            if (outputStream != null) {
                pdfDocument.writeTo(outputStream)
                Toast.makeText(context, "PDF saved to Downloads!", Toast.LENGTH_LONG).show()
            }
            outputStream?.close()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(context, "Failed to save PDF!", Toast.LENGTH_LONG).show()
        }
    } else {
        Toast.makeText(context, "Failed to create file!", Toast.LENGTH_LONG).show()
    }

    pdfDocument.close()
}