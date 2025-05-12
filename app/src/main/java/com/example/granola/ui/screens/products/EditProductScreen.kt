import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.granola.R
import com.example.granola.model.Product
import com.navigation.ROUT_ADD_PRODUCT
import com.navigation.ROUT_PRODUCT_LIST
import com.example.granola.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductScreen(productId: Int?, navController: NavController, viewModel: ProductViewModel) {
    // Brown color palette
    val darkBrown = Color(0xFF4E342E)
    val mediumBrown = Color(0xFF795548)
    val lightBrown = Color(0xFFD7CCC8)
    val backgroundBrown = Color(0xFFEFEBE9)
    val accentBrown = Color(0xFFA1887F)
    val textOnBrown = Color.White
    val textOnLight = Color(0xFF3E2723)

    val context = LocalContext.current
    val productList by viewModel.allProducts.observeAsState(emptyList())
    val product = remember(productList) { productList.find { it.id == productId } }

    var name by remember { mutableStateOf(product?.name ?: "") }
    var price by remember { mutableStateOf(product?.price?.toString() ?: "") }
    var imagePath by remember { mutableStateOf(product?.imagePath ?: "") }
    var showMenu by remember { mutableStateOf(false) }

    val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            imagePath = it.toString()
            Toast.makeText(context, "Image Selected!", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Edit Product",
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
        bottomBar = {
            NavigationBar(
                containerColor = darkBrown,
                contentColor = textOnBrown
            ) {
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate(ROUT_PRODUCT_LIST) },
                    icon = {
                        Icon(
                            Icons.Default.Menu,
                            contentDescription = "Product List",
                            tint = textOnBrown
                        )
                    },
                    label = {
                        Text(
                            "Products",
                            color = textOnBrown
                        )
                    }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate(ROUT_ADD_PRODUCT) },
                    icon = {
                        Icon(
                            Icons.Default.Menu,
                            contentDescription = "Add Product",
                            tint = textOnBrown
                        )
                    },
                    label = {
                        Text(
                            "Add",
                            color = textOnBrown
                        )
                    }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(backgroundBrown)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (product != null) {
                // Header
                Text(
                    "Edit Product Details",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = darkBrown,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Form Card
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(16.dp))
                        .background(Color.White, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Product Name", color = mediumBrown) },
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
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
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

                // Image Section
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .shadow(4.dp, RoundedCornerShape(16.dp))
                        .background(lightBrown, RoundedCornerShape(16.dp))
                        .border(1.dp, mediumBrown, RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    if (imagePath.isNotEmpty()) {
                        Image(
                            painter = rememberAsyncImagePainter(model = Uri.parse(imagePath)),
                            contentDescription = "Product Image",
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
                                "No Image Selected",
                                color = mediumBrown,
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Change Image Button
                Button(
                    onClick = { imagePicker.launch("image/*") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
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
                    Text("Change Image")
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Update Button
                Button(
                    onClick = {
                        val updatedPrice = price.toDoubleOrNull()
                        if (updatedPrice != null) {
                            viewModel.updateProduct(product.copy(name = name, price = updatedPrice, imagePath = imagePath))
                            Toast.makeText(context, "Product Updated!", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        } else {
                            Toast.makeText(context, "Invalid price entered!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = darkBrown,
                        contentColor = textOnBrown
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 8.dp,
                        pressedElevation = 4.dp
                    )
                ) {
                    Text("Update Product", fontWeight = FontWeight.Bold)
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Product not found",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { navController.popBackStack() },
                        colors = ButtonDefaults.buttonColors(darkBrown),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Go Back", color = textOnBrown)
                    }
                }
            }
        }
    }
}