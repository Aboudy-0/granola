package com.example.granola.ui.screens.custom

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.granola.ui.theme.*

@Composable
fun CustomGranolaScreen(navController: NavController) {
    // Oat and flavor state variables
    var selectedOat by remember { mutableStateOf("") }
    var selectedFlavor by remember { mutableStateOf("") }
    var totalPrice by remember { mutableStateOf(0) }

    // Placeholder data
    val oatTypes = listOf("Rolled Oats", "Steel-Cut Oats", "Quick Oats")
    val flavors = listOf("Honey", "Cinnamon", "Chocolate", "Berry Mix")
    val flavorPrices = mapOf("Honey" to 20, "Cinnamon" to 30, "Chocolate" to 40, "Berry Mix" to 50)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Custom Granola", style = MaterialTheme.typography.h4, color = DarkBrown)
        Spacer(modifier = Modifier.height(16.dp))

        // Oat selection
        DropdownMenu(expanded = true, onDismissRequest = {}) {
            oatTypes.forEach { oat ->
                DropdownMenuItem(onClick = { selectedOat = oat }) {
                    Text(oat)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Flavor selection
        DropdownMenu(expanded = true, onDismissRequest = {}) {
            flavors.forEach { flavor ->
                DropdownMenuItem(onClick = {
                    selectedFlavor = flavor
                    totalPrice = 150 + (flavorPrices[flavor] ?: 0)
                }) {
                    Text("$flavor (+${flavorPrices[flavor]} Ksh)")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Selected Oat: $selectedOat", color = MediumBrown)
        Text("Selected Flavor: $selectedFlavor", color = MediumBrown)
        Text("Total Price: $totalPrice Ksh", style = MaterialTheme.typography.h6, color = AccentBrown)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { /* Save to database */ }, colors = ButtonDefaults.buttonColors(backgroundColor = AccentBrown)) {
            Text("Confirm", color = LightBackground)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CustomGranolaScreenPreview() {
    CustomGranolaScreen(rememberNavController())
}
