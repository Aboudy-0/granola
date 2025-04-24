package com.example.granola.ui.screens.about

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.granola.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(navController: NavController) {
    val lightBrownColors = lightColorScheme(
        primary = Color(0xFF8D6E63),       // Warm brown
        onPrimary = Color.White,
        secondary = Color(0xFFA1887F),     // Lighter brown
        onSecondary = Color.White,
        surface = Color(0xFFEFEBE9),       // Very light brown
        onSurface = Color(0xFF3E2723),     // Dark brown text
        background = Color(0xFFF5F0ED),    // Creamy background
        onBackground = Color(0xFF3E2723)
    )

    MaterialTheme(
        colorScheme = lightBrownColors,
        content = {
            Box(modifier = Modifier.fillMaxSize()) {
                // Granola pattern background
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val granolaColor = lightBrownColors.primary.copy(alpha = 0.08f)
                    repeat(30) {
                        val x = (size.width * 0.1f) + (it % 6) * (size.width * 0.15f)
                        val y = (size.height * 0.1f) + (it / 6) * (size.height * 0.15f)
                        drawCircle(
                            color = granolaColor,
                            radius = 12.dp.toPx(),
                            center = Offset(x, y),
                            style = Stroke(width = 1.5.dp.toPx())
                        )
                    }
                }

                // Gradient overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    lightBrownColors.background.copy(alpha = 0.4f),
                                    lightBrownColors.background.copy(alpha = 0.9f)
                                )
                            )
                        )
                )

                Scaffold(
                    containerColor = Color.Transparent,
                    topBar = {
                        TopAppBar(
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Color.Transparent,
                                titleContentColor = lightBrownColors.onSurface
                            ),
                            title = {
                                Text(
                                    "About Us",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                            },
                            navigationIcon = {
                                IconButton(onClick = { navController.popBackStack() }) {
                                    Icon(
                                        Icons.Default.ArrowBack,
                                        contentDescription = "Back",
                                        tint = lightBrownColors.onSurface
                                    )
                                }
                            }
                        )
                    },
                    content = { innerPadding ->
                        Column(
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState()),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(modifier = Modifier.height(16.dp))

                            // Logo
                            Image(
                                painter = painterResource(R.drawable.img_3),
                                contentDescription = "Logo",
                                modifier = Modifier
                                    .size(200.dp)
                                    .padding(8.dp),
                                contentScale = ContentScale.Fit
                            )

                            // Content Card
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                shape = MaterialTheme.shapes.large,
                                color = lightBrownColors.surface.copy(alpha = 0.9f),
                                tonalElevation = 4.dp
                            ) {
                                Column(
                                    modifier = Modifier.padding(24.dp),
                                    verticalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    Text(
                                        "Our Story",
                                        style = MaterialTheme.typography.titleLarge.copy(
                                            fontWeight = FontWeight.SemiBold,
                                            color = lightBrownColors.primary
                                        )
                                    )

                                    Text(
                                        "Born in Mombasa, LujaGranola combines local flavors with " +
                                                "healthy ingredients to create delicious snacks that " +
                                                "everyone can enjoy.",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = lightBrownColors.onSurface
                                    )

                                    Divider(
                                        modifier = Modifier.padding(vertical = 8.dp),
                                        color = lightBrownColors.primary.copy(alpha = 0.1f)
                                    )

                                    Text(
                                        "Our Values",
                                        style = MaterialTheme.typography.titleLarge.copy(
                                            fontWeight = FontWeight.SemiBold,
                                            color = lightBrownColors.primary
                                        )
                                    )

                                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                        Text("• Locally sourced ingredients")
                                        Text("• No artificial additives")
                                        Text("• Eco-friendly packaging")
                                        Text("• Supporting local farmers")
                                    }
                                }
                            }

                            // Contact Info
                            Surface(
                                modifier = Modifier.padding(16.dp),
                                shape = MaterialTheme.shapes.small,
                                color = lightBrownColors.primary.copy(alpha = 0.1f),
                                border = BorderStroke(
                                    1.dp,
                                    lightBrownColors.primary.copy(alpha = 0.2f)
                                )
                            ) {
                                Text(
                                    "✉️ hello@lujagranola.com",
                                    modifier = Modifier.padding(16.dp),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = lightBrownColors.primary
                                )
                            }

                            Spacer(modifier = Modifier.height(32.dp))
                        }
                    }
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun AboutScreen() {
    AboutScreen(rememberNavController())
}