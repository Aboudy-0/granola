package com.example.granola.ui.screens.custom

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.AnnotatedString
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext

@Composable
fun CustomGranolaScreen(navController: NavController) {
    val context = LocalContext.current
    var oatWeight by remember { mutableStateOf(100f) }
    val pricePer100g = 150
    val ingredientPrices = mapOf("Almonds" to 50, "Raisins" to 30, "Chia Seeds" to 40, "Honey" to 20)
    val availableIngredients = ingredientPrices.keys.toList()
    var selectedIngredients by remember { mutableStateOf(listOf<String>()) }

    val oatPrice = (oatWeight / 100 * pricePer100g).toInt()
    val ingredientsPrice = selectedIngredients.sumOf { ingredientPrices[it] ?: 0 }
    val totalPrice = oatPrice + ingredientsPrice

    fun initiatePayment(amount: Int) {
        Toast.makeText(context, "Initiating M-Pesa Payment of KSH $amount", Toast.LENGTH_SHORT).show()
        // Call M-Pesa STK Push API here
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Customize Your Granola", style = MaterialTheme.typography.h5)
        Spacer(modifier = Modifier.height(16.dp))

        Text("Oat Weight: ${oatWeight.toInt()}g")
        Slider(
            value = oatWeight,
            onValueChange = { oatWeight = it },
            valueRange = 50f..500f,
            steps = 8
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text("Ingredients:", style = MaterialTheme.typography.h6)

        availableIngredients.forEach { ingredient ->
            val isSelected = ingredient in selectedIngredients
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = {
                        selectedIngredients = if (isSelected) {
                            selectedIngredients - ingredient
                        } else {
                            selectedIngredients + ingredient
                        }
                    }
                )
                ClickableText(text = AnnotatedString("$ingredient (KSH ${ingredientPrices[ingredient]})"), onClick = {})
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Total Price: KSH $totalPrice", style = MaterialTheme.typography.h6)

        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = { initiatePayment(totalPrice) }) {
            Text("Purchase")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CustomGranolaScreenPreview() {
    CustomGranolaScreen(rememberNavController())
}
