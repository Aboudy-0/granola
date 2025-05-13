package com.example.granola.ui.screens.custom

import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.granola.ui.theme.*
import com.navigation.ROUT_ABOUT
import com.navigation.ROUT_CONTACT
import com.navigation.ROUT_HOME
import com.navigation.ROUT_USER_PRODUCT_LIST
import kotlinx.coroutines.launch

// Color Scheme
val LightBrown = Color(0xFFD7CCC8)
val MediumBrown = Color(0xFFBCAAA4)
val DarkBrown = Color(0xFF8D6E63)
val LightBackground = Color(0xFFEFEBE9)
val AccentBrown = Color(0xFF5D4037)

data class GranolaFlavor(val name: String, val multiplier: Double, val emoji: String)
data class ExtraOption(val name: String, val price: Double, val emoji: String)

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun CustomScreen(navController: NavController) {
    val context = LocalContext.current
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val selectedItem = remember { mutableStateOf("Custom Granola") }
    val basePricePer100g = 150.0

    // Flavors and Extras
    val availableFlavors = listOf(
        GranolaFlavor("Honey Almond", 1.0, "🍯"),
        GranolaFlavor("Chocolate Chip", 1.2, "🍫"),
        GranolaFlavor("Dried Berries", 1.1, "🍓"),
        GranolaFlavor("Coconut Flakes", 0.9, "🥥"),
        GranolaFlavor("Peanut Butter", 1.3, "🥜")
    )

    val extras = listOf(
        ExtraOption("Chia Seeds", 30.0, "⚫"),
        ExtraOption("Sunflower Seeds", 25.0, "🌻"),
        ExtraOption("Dried Mango", 40.0, "🥭"),
        ExtraOption("Protein Boost", 50.0, "💪")
    )

    // State variables
    val selectedFlavors = remember { mutableStateListOf<String>() }
    val selectedExtras = remember { mutableStateListOf<String>() }
    var grams by remember { mutableStateOf(100f) }

    // Price calculation
    val selectedFlavorObjs = availableFlavors.filter { it.name in selectedFlavors }
    val selectedExtraObjs = extras.filter { it.name in selectedExtras }
    val averageMultiplier = if (selectedFlavorObjs.isNotEmpty()) selectedFlavorObjs.map { it.multiplier }.average() else 1.0
    val extraCost = selectedExtraObjs.sumOf { it.price }
    val baseCost = (grams / 100f) * basePricePer100g * averageMultiplier
    val totalPrice = baseCost + extraCost

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(DarkBrown)
                    .padding(16.dp)
            ) {
                // Drawer header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .padding(bottom = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "LujaGranola",
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = LightBrown,
                            fontWeight = FontWeight.ExtraBold
                        )
                    )
                }

                // Drawer items
                listOf(
                    "Home" to ROUT_HOME,
                    "Products" to ROUT_USER_PRODUCT_LIST,
                    "About" to ROUT_ABOUT,
                    "Contact" to ROUT_CONTACT,
                    "Custom" to "custom"
                ).forEach { (item, route) ->
                    NavigationDrawerItem(
                        label = {
                            Text(
                                text = item,
                                color = LightBrown,
                                fontWeight = if (selectedItem.value == item) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        selected = selectedItem.value == item,
                        onClick = {
                            selectedItem.value = item
                            navController.navigate(route) {
                                popUpTo("home") { inclusive = true }
                            }
                            scope.launch { drawerState.close() }
                        },
                        modifier = Modifier.padding(vertical = 4.dp),
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = MediumBrown.copy(alpha = 0.5f),
                            unselectedContainerColor = Color.Transparent,
                            selectedTextColor = LightBrown,
                            unselectedTextColor = LightBrown.copy(alpha = 0.8f)
                        )
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Custom Granola Builder", color = Color.White) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = AccentBrown)
                )
            },
            containerColor = LightBackground
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .padding(paddingValues)
            ) {
                Text("Select up to 3 Flavors:", style = MaterialTheme.typography.titleMedium, color = DarkBrown)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(availableFlavors) { flavor ->
                        FilterChip(
                            selected = selectedFlavors.contains(flavor.name),
                            onClick = {
                                if (selectedFlavors.size < 3 || selectedFlavors.contains(flavor.name)) {
                                    if (selectedFlavors.contains(flavor.name)) selectedFlavors.remove(flavor.name)
                                    else selectedFlavors.add(flavor.name)
                                }
                            },
                            label = { Text(flavor.name) },
                            leadingIcon = { Text(flavor.emoji, fontSize = 20.sp) },
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = MediumBrown)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text("Extras:", style = MaterialTheme.typography.titleMedium, color = DarkBrown)
                extras.forEach { extra ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = selectedExtras.contains(extra.name),
                            onCheckedChange = {
                                if (it) selectedExtras.add(extra.name) else selectedExtras.remove(extra.name)
                            },
                            colors = CheckboxDefaults.colors(checkedColor = AccentBrown)
                        )
                        Text("${extra.name} (+Ksh.${extra.price.toInt()})", color = DarkBrown)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text("Quantity: ${grams.toInt()} grams", color = DarkBrown)
                Slider(
                    value = grams,
                    onValueChange = { grams = it },
                    valueRange = 50f..500f,
                    colors = SliderDefaults.colors(thumbColor = AccentBrown)
                )

                Text("Total Price: Ksh.${"%.2f".format(totalPrice)}", color = DarkBrown, style = MaterialTheme.typography.titleLarge)

                Button(
                    onClick = {
                        val message = "Granola: ${grams.toInt()}g, Flavors: ${selectedFlavors.joinToString()}, Extras: ${selectedExtras.joinToString()}, Price: Ksh.${"%.2f".format(totalPrice)}"
                        val smsIntent = Intent(Intent.ACTION_VIEW).apply {
                            data = "sms:0741298978".toUri()
                            putExtra("sms_body", message)
                        }
                        context.startActivity(smsIntent)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = AccentBrown)
                ) {
                    Text("Submit Mix", color = Color.White)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.N)
@Preview(showBackground = true)
@Composable
fun CustomScreenPreview() {
    CustomScreen(rememberNavController())
}