package com.example.granola.ui.screens.custom

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.granola.ui.theme.DarkBrown
import com.example.granola.ui.theme.GranolaTheme
import com.example.granola.ui.theme.LightBrown

data class GranolaFlavor(
    val name: String,
    val multiplier: Double,
    val imageUrl: String,
    val emoji: String
)

data class ExtraOption(
    val name: String,
    val price: Double,
    val emoji: String
)

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun CustomScreen(navController: NavController) {
    val context = LocalContext.current
    val basePricePer100g = 150.0

    val availableFlavors = listOf(
        GranolaFlavor("Honey Almond", 1.0, "https://cdn.pixabay.com/photo/2017/04/25/16/11/honey-2265039_1280.jpg", "🍯"),
        GranolaFlavor("Chocolate Chip", 1.2, "https://cdn.pixabay.com/photo/2015/07/02/10/14/chocolate-828416_1280.jpg", "🍫"),
        GranolaFlavor("Dried Berries", 1.1, "https://cdn.pixabay.com/photo/2017/01/20/15/06/berries-1995056_1280.jpg", "🍓"),
        GranolaFlavor("Coconut Flakes", 0.9, "https://cdn.pixabay.com/photo/2018/02/01/09/17/coconut-3129344_1280.jpg", "🥥"),
        GranolaFlavor("Peanut Butter", 1.3, "https://cdn.pixabay.com/photo/2020/04/01/16/35/peanut-butter-4991392_1280.jpg", "🥜")
    )

    val extras = listOf(
        ExtraOption("Chia Seeds", 30.0, "⚫"),
        ExtraOption("Sunflower Seeds", 25.0, "🌻"),
        ExtraOption("Dried Mango", 40.0, "🥭"),
        ExtraOption("Protein Boost", 50.0, "💪")
    )

    val selectedFlavors = remember { mutableStateMapOf<String, Boolean>() }
    val selectedExtras = remember { mutableStateMapOf<String, Boolean>() }

    availableFlavors.forEach { selectedFlavors.putIfAbsent(it.name, false) }
    extras.forEach { selectedExtras.putIfAbsent(it.name, false) }

    var grams by remember { mutableStateOf(100f) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val selectedFlavorObjs = availableFlavors.filter { selectedFlavors[it.name] == true }
    val selectedExtraObjs = extras.filter { selectedExtras[it.name] == true }

    val averageMultiplier = if (selectedFlavorObjs.isNotEmpty())
        selectedFlavorObjs.map { it.multiplier }.average()
    else 0.0

    val extraCost = selectedExtraObjs.sumOf { it.price }
    val baseCost = (grams / 100f) * basePricePer100g * averageMultiplier
    val totalPrice = baseCost + extraCost

    val canSubmit = grams >= 100 && selectedFlavorObjs.size <= 3 && selectedFlavorObjs.isNotEmpty()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Build Your Custom Granola")
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBrown,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Flavors
            Text("Choose up to 3 Flavors:")
            LazyColumn(modifier = Modifier.heightIn(max = 250.dp)) {
                items(availableFlavors.size) { index ->
                    val flavor = availableFlavors[index]
                    val isChecked = selectedFlavors[flavor.name] == true
                    val isLimitReached = selectedFlavorObjs.size >= 3 && !isChecked

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .toggleable(
                                enabled = !isLimitReached,
                                value = isChecked,
                                onValueChange = {
                                    selectedFlavors[flavor.name] = it
                                    errorMessage = null
                                }
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = null,
                            enabled = !isLimitReached
                        )
                        AsyncImage(
                            model = flavor.imageUrl,
                            contentDescription = flavor.name,
                            modifier = Modifier
                                .size(40.dp)
                                .padding(start = 8.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "${flavor.name} (x${flavor.multiplier})")
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Extras
            Text("Optional Extras:")
            extras.forEach { extra ->
                val isChecked = selectedExtras[extra.name] == true
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .toggleable(
                            value = isChecked,
                            onValueChange = {
                                selectedExtras[extra.name] = it
                            }
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = null
                    )
                    Text("${extra.name} (+Ksh.${extra.price.toInt()})")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Grams Slider
            Text("Select Quantity: ${grams.toInt()} grams")
            Slider(
                value = grams,
                onValueChange = {
                    grams = it
                    errorMessage = null
                },
                valueRange = 50f..500f,
                steps = 9
            )

            // Emoji Visual Preview
            val previewEmoji = buildString {
                selectedFlavorObjs.forEach { append(it.emoji) }
                selectedExtraObjs.forEach { append(it.emoji) }
            }
            Text("Preview: $previewEmoji", style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.height(8.dp))

            // Breakdown
            Text("Price Breakdown:", style = MaterialTheme.typography.titleMedium)
            Text("Base (x${"%.2f".format(averageMultiplier)}): Ksh.${"%.2f".format(baseCost)}")
            if (extraCost > 0) {
                Text("Extras: Ksh.${"%.2f".format(extraCost)}")
            }
            Text("Total Price: Ksh.${"%.2f".format(totalPrice)}", style = MaterialTheme.typography.titleLarge)

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = errorMessage!!, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (grams < 100) {
                        errorMessage = "Minimum quantity is 100 grams."
                    } else if (selectedFlavorObjs.size > 3) {
                        errorMessage = "You can only select up to 3 flavors."
                    } else if (selectedFlavorObjs.isEmpty()) {
                        errorMessage = "Please select at least one flavor."
                    } else {
                        errorMessage = null
                        Toast.makeText(
                            context,
                            "Granola created! Ksh.${"%.2f".format(totalPrice)}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                },
                enabled = canSubmit,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Submit Mix")
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.N)
@Preview(showBackground = true)
@Composable
fun CustomScreenPreview() {
    GranolaTheme {
        CustomScreen(navController = rememberNavController())
    }
}
