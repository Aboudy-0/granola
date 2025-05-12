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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.example.granola.model.Product
import com.example.granola.viewmodel.ProductViewModel
import com.navigation.ROUT_ADD_PRODUCT
import com.navigation.ROUT_EDIT_PRODUCT
import com.navigation.ROUT_HOME
import com.navigation.ROUT_PRODUCT_LIST
import com.navigation.editProductRoute
import java.io.IOException
import java.io.OutputStream

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(navController: NavController, viewModel: ProductViewModel) {
    // Brown color palette
    val darkBrown = Color(0xFF4E342E)
    val mediumBrown = Color(0xFF795548)
    val lightBrown = Color(0xFFD7CCC8)
    val backgroundBrown = Color(0xFFEFEBE9)
    val textOnBrown = Color.White
    val textOnLight = Color(0xFF3E2723)

    val productList by viewModel.allProducts.observeAsState(emptyList())
    var showMenu by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredProducts = productList.filter {
        it.name.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            "Products",
                            fontSize = 20.sp,
                            color = textOnBrown,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = TopAppBarDefaults.mediumTopAppBarColors(
                        containerColor = darkBrown,
                        titleContentColor = textOnBrown
                    ),
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = textOnBrown
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Menu",
                                tint = textOnBrown
                            )
                        }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Product List") },
                                onClick = {
                                    navController.navigate(ROUT_PRODUCT_LIST)
                                    showMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Add Product") },
                                onClick = {
                                    navController.navigate(ROUT_ADD_PRODUCT)
                                    showMenu = false
                                }
                            )
                        }
                    }
                )

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    placeholder = { Text("Search products...", color = mediumBrown) },
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = mediumBrown
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = darkBrown,
                        unfocusedBorderColor = lightBrown,
                        focusedTextColor = textOnLight,
                        unfocusedTextColor = mediumBrown,
                        cursorColor = darkBrown
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
            }
        },
        bottomBar = { BottomNavigationBar(navController, darkBrown) },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundBrown)
                    .padding(paddingValues)
            ) {
                if (filteredProducts.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "No products found",
                            color = mediumBrown,
                            fontSize = 18.sp
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(filteredProducts) { product ->
                            ProductItem(navController, product, viewModel)
                        }
                    }
                }
            }
        }
    )
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun ProductItem(navController: NavController, product: Product, viewModel: ProductViewModel) {
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
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // Product Image
            Image(
                painter = painter,
                contentDescription = "Product Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                contentScale = ContentScale.Crop
            )

            // Gradient Overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .align(Alignment.BottomStart)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color(0xFF4E342E).copy(alpha = 0.8f))
                        )
                    )
            )

            // Product Info
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 16.dp, bottom = 60.dp)
            ) {
                Text(
                    text = product.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Price: Ksh${product.price}",
                    fontSize = 16.sp,
                    color = Color.Green
                )
                Text(
                    text = "Phone: ${product.phone}",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }

            // Action Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .align(Alignment.BottomEnd),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Message Seller
                OutlinedButton(
                    onClick = {
                        val smsIntent = Intent(Intent.ACTION_SENDTO)
                        smsIntent.data = "smsto:${product.phone}".toUri()
                        smsIntent.putExtra("sms_body", "Hello Seller,...?")
                        context.startActivity(smsIntent)
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White,
                        contentColor = Color(0xFF4E342E)
                    )
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Message Seller",
                            tint = Color(0xFF4E342E)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Message", color = Color(0xFF4E342E))
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Edit Button
                IconButton(
                    onClick = { navController.navigate(editProductRoute(product.id)) },
                    modifier = Modifier
                        .background(Color(0xFF795548), CircleShape)
                        .size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Delete Button
                IconButton(
                    onClick = { viewModel.deleteProduct(product) },
                    modifier = Modifier
                        .background(Color.Red.copy(alpha = 0.8f), CircleShape)
                        .size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Download PDF
                IconButton(
                    onClick = { generateProductPDF(context, product) },
                    modifier = Modifier
                        .background(Color(0xFF795548), CircleShape)
                        .size(40.dp)
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

@Composable
fun BottomNavigationBar(navController: NavController, darkBrown: Color) {
    NavigationBar(
        containerColor = darkBrown,
        contentColor = Color.White
    ) {
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate(ROUT_HOME) },
            icon = {
                Icon(
                    Icons.Default.Home,
                    contentDescription = "Product List",
                    tint = Color.White
                )
            },
            label = {
                Text(
                    "Home",
                    color = Color.White
                )
            }
        )
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate(ROUT_ADD_PRODUCT) },
            icon = {
                Icon(
                    Icons.Default.AddCircle,
                    contentDescription = "Add Product",
                    tint = Color.White
                )
            },
            label = {
                Text(
                    "Add",
                    color = Color.White
                )
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
fun generateProductPDF(context: Context, product: Product) {
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

    // Save PDF using MediaStore (Scoped Storage)
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