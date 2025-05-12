package com.example.granola.ui.screens.products

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.granola.R
import com.example.granola.viewmodel.ProductViewModel
import com.navigation.ROUT_ADD_PRODUCT
import com.navigation.ROUT_HOME
import com.navigation.ROUT_PRODUCT_LIST

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(navController: NavController, viewModel: ProductViewModel) {
    // State variables remain the same
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var showMenu by remember { mutableStateOf(false) }

    // Brown color palette
    val darkBrown = Color(0xFF5D4037)
    val mediumBrown = Color(0xFF8D6E63)
    val lightBrown = Color(0xFFD7CCC8)
    val backgroundBrown = Color(0xFFEFEBE9)
    val accentBrown = Color(0xFFA1887F)
    val textOnBrown = Color.White
    val textOnLight = Color(0xFF3E2723)

    val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { imageUri = it }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Add Product",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = textOnBrown
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
                            Icons.Default.MoreVert,
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
        },
        bottomBar = { BottomNavigationBar(navController, darkBrown, mediumBrown) },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp)
                    .background(backgroundBrown),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                Text(
                    "Add New Product",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = darkBrown,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Image Picker
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .background(lightBrown, RoundedCornerShape(16.dp))
                        .clickable { imagePicker.launch("image/*") }
                        .border(2.dp, mediumBrown, RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    if (imageUri != null) {
                        Image(
                            painter = rememberAsyncImagePainter(imageUri),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Icon(
                                painterResource(R.drawable.image),
                                contentDescription = null,
                                tint = mediumBrown,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Tap to add product image",
                                color = mediumBrown,
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Form Fields
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Product Name", color = mediumBrown) },
                        leadingIcon = {
                            Icon(
                                painterResource(R.drawable.baseline_dynamic_feed_24),
                                contentDescription = null,
                                tint = mediumBrown
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = darkBrown,
                            focusedLabelColor = darkBrown,
                            focusedTextColor = textOnLight,
                            unfocusedBorderColor = lightBrown,
                            unfocusedLabelColor = mediumBrown,
                            cursorColor = darkBrown
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = price,
                        onValueChange = { price = it },
                        label = { Text("Product Price", color = mediumBrown) },
                        leadingIcon = {
                            Icon(
                                painterResource(R.drawable.baseline_architecture_24),
                                contentDescription = null,
                                tint = mediumBrown
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = darkBrown,
                            focusedLabelColor = darkBrown,
                            focusedTextColor = textOnLight,
                            unfocusedBorderColor = lightBrown,
                            unfocusedLabelColor = mediumBrown,
                            cursorColor = darkBrown
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Phone Number", color = mediumBrown) },
                        leadingIcon = {
                            Icon(
                                painterResource(R.drawable.phone),
                                contentDescription = null,
                                tint = mediumBrown
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = darkBrown,
                            focusedLabelColor = darkBrown,
                            focusedTextColor = textOnLight,
                            unfocusedBorderColor = lightBrown,
                            unfocusedLabelColor = mediumBrown,
                            cursorColor = darkBrown
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Submit Button
                Button(
                    onClick = {
                        val priceValue = price.toDoubleOrNull()
                        if (priceValue != null) {
                            imageUri?.toString()?.let {
                                viewModel.addProduct(name, priceValue, phone, it)
                            }
                            navController.navigate(ROUT_PRODUCT_LIST)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = mediumBrown,
                        contentColor = textOnBrown
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 2.dp
                    )
                ) {
                    Text(
                        "Add Product",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    )
}

@Composable
fun BottomNavigationBar(navController: NavController, darkBrown: Color, mediumBrown: Color) {
    NavigationBar(
        containerColor = darkBrown,
        contentColor = Color.White
    ) {
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate(ROUT_PRODUCT_LIST) },
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
            onClick = { navController.navigate(ROUT_HOME) },
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