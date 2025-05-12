package com.example.granola.ui.screens.contact

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.granola.R
import com.example.granola.ui.theme.DarkBrown
import com.example.granola.ui.theme.LightBrown
import com.example.granola.ui.theme.MediumBrown
import com.navigation.ROUT_ABOUT
import com.navigation.ROUT_CONTACT
import com.navigation.ROUT_HOME
import com.navigation.ROUT_USER_PRODUCT_LIST
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactScreen(navController: NavController) {
    val context = LocalContext.current
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val selectedItem = remember { mutableStateOf("Contact") }

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
                    title = { Text("LujaGranola", color = LightBrown) },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = DarkBrown,
                        titleContentColor = LightBrown,
                        navigationIconContentColor = LightBrown,
                        actionIconContentColor = LightBrown
                    ),
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            }
        ) { paddingValues ->
            ContactScreenContent(navController, paddingValues, context)
        }
    }
}

@Composable
fun ContactScreenContent(
    navController: NavController,
    paddingValues: PaddingValues,
    context: android.content.Context
) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MediumBrown)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Image(
            painter = painterResource(R.drawable.img_3),
            contentDescription = "Contact us",
            modifier = Modifier.size(180.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "We're here to help!",
            style = MaterialTheme.typography.headlineMedium,
            color = DarkBrown
        )

        Text(
            "Reach out to our friendly team for any questions or feedback",
            style = MaterialTheme.typography.bodyMedium,
            color = MediumBrown,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Contact Options
        ContactCard(
            icon = Icons.Default.Email,
            title = "Email Support",
            subtitle = "support@granola.com",
            description = "Typically responds within 24 hours",
            onClick = {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:support@granola.com")
                    putExtra(Intent.EXTRA_SUBJECT, "Support Request")
                }
                context.startActivity(intent)
            }
        )

        ContactCard(
            icon = Icons.Default.Phone,
            title = "Call Us",
            subtitle = "+254 741 298978",
            description = "Mon-Fri, 9AM-5PM",
            onClick = {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:+254741298978")
                }
                context.startActivity(intent)
            }
        )

        ContactCard(
            icon = Icons.Default.LocationOn,
            title = "Visit Us",
            subtitle = "Nairobi, Kenya",
            description = "123 Granola Street, Westlands",
            onClick = {
                val gmmIntentUri = Uri.parse("geo:0,0?q=123+Granola+Street+Westlands+Nairobi")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                context.startActivity(mapIntent)
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "Follow us on social media",
            style = MaterialTheme.typography.titleSmall,
            color = MediumBrown
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(bottom = 32.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.facebook),
                contentDescription = "Facebook",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .clickable {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://facebook.com/granola"))
                        context.startActivity(intent)
                    },
                contentScale = ContentScale.Fit
            )

            Image(
                painter = painterResource(R.drawable.instagram),
                contentDescription = "Instagram",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .clickable {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://instagram.com/granola"))
                        context.startActivity(intent)
                    },
                contentScale = ContentScale.Fit
            )

            Image(
                painter = painterResource(R.drawable.twitter),
                contentDescription = "Twitter",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .clickable {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/granola"))
                        context.startActivity(intent)
                    },
                contentScale = ContentScale.Fit
            )
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

        val drawerItems = listOf(
            "Home" to ROUT_HOME,
            "Products" to ROUT_USER_PRODUCT_LIST,
            "About" to ROUT_ABOUT,
            "Contact" to ROUT_CONTACT
        )

        drawerItems.forEach { (item, route) ->
            NavigationDrawerItem(
                label = {
                    Text(
                        text = item,
                        color = if (selectedItem.value == item) LightBrown else DarkBrown,
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
                    selectedContainerColor = MediumBrown.copy(alpha = 0.3f),
                    unselectedContainerColor = Color.Transparent,
                    selectedTextColor = DarkBrown,
                    unselectedTextColor = DarkBrown
                )
            )
        }
    }
}

@Composable
fun ContactCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    description: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = LightBrown
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MediumBrown)
                    .padding(8.dp),
                tint = LightBrown
            )

            Column {
                Text(
                    title,
                    style = MaterialTheme.typography.titleMedium,
                    color = DarkBrown
                )
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MediumBrown
                )
                Text(
                    description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MediumBrown
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Open",
                tint = MediumBrown
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContactScreenPreview() {
    ContactScreen(rememberNavController())
}