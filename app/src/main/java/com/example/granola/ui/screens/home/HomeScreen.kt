package com.example.granola.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.granola.ui.theme.*
import com.google.accompanist.pager.*
import com.navigation.ROUT_ABOUT
import com.navigation.ROUT_CONTACT
import com.navigation.ROUT_CUSTOM
import com.navigation.ROUT_HOME
import com.navigation.ROUT_IDEA
import com.navigation.ROUT_USER_PRODUCT_LIST
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Brown color palette
internal val DarkBrown = Color(0xFF4E342E)
private val MediumBrown = Color(0xFF795548)
private val LightBrown = Color(0xFFD7CCC8)
internal val BackgroundBrown = Color(0xFFEFEBE9)
internal val TextOnBrown = Color.White
private val TextOnLight = Color(0xFF3E2723)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Composable
fun HomeScreen(navController: NavController) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val selectedItem = remember { mutableStateOf("Home") }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(navController, selectedItem) {
                scope.launch { drawerState.close() }
            }
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("LujaGranola", color = TextOnBrown) },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = DarkBrown,
                        titleContentColor = TextOnBrown,
                        navigationIconContentColor = TextOnBrown,
                        actionIconContentColor = TextOnBrown
                    ),
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            }
        ) { paddingValues ->
            HomeScreenContent(navController, paddingValues)
        }
    }
}

@Composable
fun DrawerContent(
    navController: NavController,
    selectedItem: MutableState<String>,
    onItemClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightBrown)
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
            Image(
                painter = painterResource(R.drawable.img_1),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .border(2.dp, MediumBrown, CircleShape)
            )
        }

        // Drawer items
        val drawerItems = listOf(
            "Home" to ROUT_HOME,
            "Products" to ROUT_USER_PRODUCT_LIST,
            "About" to ROUT_ABOUT,
            "Contact" to ROUT_CONTACT,
            "Custom" to ROUT_CUSTOM

        )

        drawerItems.forEach { (item, route) ->
            NavigationDrawerItem(
                label = {
                    Text(
                        text = item,
                        color = if (selectedItem.value == item) TextOnLight else DarkBrown,
                        fontWeight = if (selectedItem.value == item) FontWeight.Bold else FontWeight.Normal
                    )
                },
                selected = selectedItem.value == item,
                onClick = {
                    selectedItem.value = item
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                    onItemClicked()
                },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = MediumBrown.copy(alpha = 0.3f),
                    unselectedContainerColor = Color.Transparent,
                    selectedTextColor = DarkBrown,
                    unselectedTextColor = DarkBrown
                )
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeScreenContent(navController: NavController, paddingValues: PaddingValues) {
    val pagerState = rememberPagerState(initialPage = 0)
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        while (true) {
            delay(3000)
            val nextPage = (pagerState.currentPage + 1) % popularProducts.size
            scope.launch {
                pagerState.animateScrollToPage(nextPage)
            }
        }
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .background(BackgroundBrown)
            .padding(paddingValues)
    ) {
        // Hero Banner
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
                        color = TextOnBrown,
                        fontWeight = FontWeight.Bold
                    )
                )
                Button(
                    onClick = { navController.navigate(ROUT_USER_PRODUCT_LIST) },
                    modifier = Modifier.padding(top = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MediumBrown,
                        contentColor = TextOnBrown
                    )
                ) {
                    Text("Shop Now")
                }
            }
        }

        // Features
        Text(
            text = "Why Choose Us",
            style = MaterialTheme.typography.titleLarge,
            color = DarkBrown,
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

        // Carousel - Popular Choices
        Text(
            text = "Popular Choices",
            style = MaterialTheme.typography.titleLarge,
            color = DarkBrown,
            modifier = Modifier.padding(16.dp)
        )
        HorizontalPager(
            count = popularProducts.size,
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 32.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
        ) { page ->
            Image(
                painter = painterResource(popularProducts[page].imageRes),
                contentDescription = popularProducts[page].name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .padding(horizontal = 8.dp)
            )
        }

        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(8.dp),
            activeColor = MediumBrown,
            inactiveColor = LightBrown
        )

        // How It Works
        Text(
            text = "How It Works",
            style = MaterialTheme.typography.titleLarge,
            color = DarkBrown,
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

        // Granola Ideas Section
        Spacer(modifier = Modifier.height(16.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = LightBrown
            ),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Don't know how to eat your granola?",
                    style = MaterialTheme.typography.titleMedium,
                    color = DarkBrown,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Discover delicious ways to enjoy it!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MediumBrown,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Button(
                    onClick = { navController.navigate(ROUT_IDEA) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DarkBrown,
                        contentColor = TextOnBrown
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                ) {
                    Text("Get Granola Ideas")
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
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = LightBrown
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MediumBrown),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = feature.take(1),
                    style = MaterialTheme.typography.titleLarge,
                    color = TextOnBrown
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = feature,
                style = MaterialTheme.typography.bodyMedium,
                color = DarkBrown
            )
        }
    }
}

@Composable
fun HowItWorksStep(number: Int, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(MediumBrown, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = number.toString(),
                color = TextOnBrown,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            color = DarkBrown
        )
    }
}

data class Product(val name: String, val imageRes: Int, val price: String = "Ksh.650")

val features = listOf("Organic", "Creative", "Fresh", "Eco-Friendly")

val popularProducts = listOf(
    Product("Kivuli Cocoa", R.drawable.img_1),
    Product("Nazi Crunch", R.drawable.img_2),
    Product("Tunda Twist", R.drawable.img_6),
    Product("Peanut Butter", R.drawable.img_7)
)

val howItWorksSteps = listOf(
    "Choose your granola",
    "It gets safely packed",
    "It's brought to you!!"
)

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    GranolaTheme {
        HomeScreen(rememberNavController())
    }
}