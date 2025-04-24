package com.example.granola.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.granola.R
import com.example.granola.ui.theme.GranolaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("LujaGranola") },
                actions = {
                    IconButton(onClick = { navController.navigate("cart") }) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
        ) {
            // 1. Hero Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.img_1),
                    contentDescription = "Granola with fruits",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Fresh Organic Granola",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Button(
                        onClick = { navController.navigate("products") },
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text("Shop Now")
                    }
                }
            }

            // 2. Features Section
            Text(
                text = "Why Choose Us",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp)
            )

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(features) { feature ->
                    FeatureItem(feature)
                }
            }

            // 3. Popular Products
            Text(
                text = "Popular Choices",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp)
            )

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(popularProducts) { product ->
                    ProductCard(product)
                }
            }

            // 4. How It Works
            Text(
                text = "How It Works",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp)
            )

            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                howItWorksSteps.forEachIndexed { index, step ->
                    HowItWorksStep(index + 1, step)
                }
            }

        }
    }
}

@Composable
fun FeatureItem(feature: String) {
    Card(
        modifier = Modifier
            .width(140.dp)
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = feature.take(1),
                    style = MaterialTheme.typography.titleLarge
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = feature,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun ProductCard(product: String) {
    Card(
        modifier = Modifier.width(160.dp)
    ) {
        Column {
            Image(
                painter = painterResource(R.drawable.img_3),
                contentDescription = product,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = product,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Ksh.650",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun HowItWorksStep(number: Int, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(MaterialTheme.colorScheme.primary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = number.toString(),
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text)
    }
}

// Sample Data
val features = listOf(
    "Organic", "Custom", "Fresh", "Eco-Friendly"
)

val popularProducts = listOf(
    "Kivulo Cocoa", "Nazi Crunch", "Tunda twist", "Peanut Butter"
)

val howItWorksSteps = listOf(
    "Choose your granola",
    "It gets safely packed",
    "Its brought to you!!"
)

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    GranolaTheme {
        HomeScreen(rememberNavController())
    }
}