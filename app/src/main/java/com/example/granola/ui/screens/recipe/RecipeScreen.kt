package com.example.granola.ui.screens.recipe

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.granola.R

// Brown color palette
private val DarkBrown = Color(0xFF4E342E)
private val MediumBrown = Color(0xFF795548)
private val LightBrown = Color(0xFFD7CCC8)
private val BackgroundBrown = Color(0xFFEFEBE9)
private val TextOnBrown = Color.White
private val TextOnLight = Color(0xFF3E2723)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeScreen(navController: NavController) {
    var selectedOption by remember { mutableStateOf<RecipeOption?>(null) }

    if (selectedOption != null) {
        RecipeDetailScreen(selectedOption!!) { selectedOption = null }
    } else {
        RecipeListScreen(navController) { option -> selectedOption = option }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeListScreen(
    navController: NavController,
    onOptionSelected: (RecipeOption) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Granola Recipes",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextOnBrown
                        )
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = DarkBrown,
                    titleContentColor = TextOnBrown
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = TextOnBrown
                        )
                    }
                }
            )
        },
        containerColor = BackgroundBrown
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Header image with overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_5),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(DarkBrown.copy(alpha = 0.5f))
                )
                Text(
                    text = "Delicious Granola Recipes",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = TextOnBrown,
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp
                    ),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 24.dp),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                val options = listOf(
                    RecipeOption(
                        "Yogurt Bowl",
                        "Greek yogurt with granola and fresh berries",
                        R.drawable.granola_yogurt,
                        "Ingredients:\n- 1 cup Greek yogurt\n- ½ cup granola\n- Fresh berries (strawberries, blueberries, raspberries)\n- Honey drizzle\n\nInstructions:\n1. Scoop yogurt into a bowl\n2. Top with granola\n3. Add fresh berries\n4. Drizzle with honey"
                    ),
                    RecipeOption(
                        "Smoothie Topper",
                        "Adds crunch and texture to any smoothie",
                        R.drawable.granola_smoothie,
                        "Ingredients:\n- Your favorite smoothie\n- ¼ cup granola\n\nInstructions:\n1. Prepare your smoothie\n2. Pour into glass\n3. Sprinkle granola on top\n4. Enjoy the crunchy texture"
                    ),
                    RecipeOption(
                        "Ice Cream",
                        "Perfect contrast of temperatures",
                        R.drawable.granola_icecream,
                        "Ingredients:\n- 1 scoop vanilla ice cream\n- ¼ cup granola\n- Chocolate syrup (optional)\n\nInstructions:\n1. Scoop ice cream into bowl\n2. Sprinkle granola on top\n3. Drizzle with chocolate syrup if desired"
                    ),
                    RecipeOption(
                        "Trail Mix",
                        "Mix with nuts and dried fruit",
                        R.drawable.granola_snack,
                        "Ingredients:\n- 1 cup granola\n- ½ cup mixed nuts\n- ½ cup dried fruit\n- ¼ cup chocolate chips\n\nInstructions:\n1. Mix all ingredients together\n2. Store in airtight container\n3. Great for on-the-go snacking"
                    ),
                    RecipeOption(
                        "Baked Goods",
                        "Topping for muffins or crust for bars",
                        R.drawable.granola_baked,
                        "Ingredients:\n- 1 cup granola\n- 2 tbsp melted butter\n- 1 tbsp honey\n\nInstructions for Crust:\n1. Mix ingredients\n2. Press into baking dish\n3. Bake at 350°F for 10 minutes\n4. Let cool before adding filling"
                    )
                )

                options.forEach { option ->
                    GranolaOption(option) { onOptionSelected(option) }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun GranolaOption(
    option: RecipeOption,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = LightBrown
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(MediumBrown)
            ) {
                Image(
                    painter = painterResource(id = option.imageRes),
                    contentDescription = option.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = option.title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = DarkBrown
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = option.description,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextOnLight,
                        fontSize = 14.sp
                    )
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(
    option: RecipeOption,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = option.title,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextOnBrown
                        )
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = DarkBrown,
                    titleContentColor = TextOnBrown
                ),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = TextOnBrown
                        )
                    }
                }
            )
        },
        containerColor = BackgroundBrown
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            ) {
                Image(
                    painter = painterResource(id = option.imageRes),
                    contentDescription = option.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(DarkBrown.copy(alpha = 0.3f))
                )
            }

            Column(
                modifier = Modifier
                    .padding(24.dp)
            ) {
                Text(
                    text = option.title,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = DarkBrown
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = option.details,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = TextOnLight,
                        lineHeight = 24.sp
                    )
                )
            }
        }
    }
}

data class RecipeOption(
    val title: String,
    val description: String,
    val imageRes: Int,
    val details: String
)

@Preview(showBackground = true)
@Composable
fun RecipeScreenPreview() {
    RecipeScreen(rememberNavController())
}