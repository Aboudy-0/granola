package com.example.granola.ui.screens.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
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
import com.example.granola.ui.screens.home.BackgroundBrown
import com.example.granola.ui.screens.home.TextOnBrown
import com.example.granola.ui.theme.DarkBrown
import com.example.granola.ui.theme.LightBrown
import com.example.granola.ui.theme.MediumBrown
import com.navigation.ROUT_ABOUT
import com.navigation.ROUT_CONTACT
import com.navigation.ROUT_CUSTOM
import com.navigation.ROUT_HOME
import com.navigation.ROUT_USER_PRODUCT_LIST
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(navController: NavController) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val selectedItem = remember { mutableStateOf("About") } // Set "About" as default selected

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
                        containerColor = com.example.granola.ui.screens.home.DarkBrown,
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
            AboutScreenContent(navController, paddingValues)
        }
    }
}

@Composable
fun AboutScreenContent(navController: NavController, paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .background(LightBrown)
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
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "Our Story",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )

                Text(
                    "Born in Nairobi, LujaGranola combines local flavors with " +
                            "healthy ingredients to create delicious snacks that " +
                            "everyone can enjoy.",
                    style = MaterialTheme.typography.bodyLarge
                )

                Divider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                )

                Text(
                    "Our Values",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
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

        Spacer(modifier = Modifier.height(32.dp))
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
                        color = Color.White,
                        fontWeight = if (selectedItem.value == item) FontWeight.Bold else FontWeight.Normal
                    )
                },
                selected = selectedItem.value == item,
                onClick = {
                    selectedItem.value = item
                    navController.navigate(route) {
                        popUpTo("home") { inclusive = true }
                    }
                    onItemClicked()
                },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f),
                    unselectedContainerColor = LightBrown,
                    selectedTextColor = DarkBrown,
                    unselectedTextColor = MediumBrown
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AboutScreenPreview() {
    AboutScreen(rememberNavController())
}