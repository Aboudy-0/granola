package com.example.granola.ui.screens.auth

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.granola.R
import com.example.granola.viewmodel.AuthViewModel
import com.navigation.ROUT_ABOUT
import com.navigation.ROUT_HOME
import com.navigation.ROUT_PRODUCT_LIST
import com.navigation.ROUT_REGISTER

// Brown color palette
private val DarkBrown = Color(0xFF4E342E)
private val MediumBrown = Color(0xFF795548)
private val LightBrown = Color(0xFFD7CCC8)
private val BackgroundBrown = Color(0xFFEFEBE9)
private val TextOnBrown = Color.White
private val TextOnLight = Color(0xFF3E2723)

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    navController: NavController,
    onLoginSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Observe login logic
    LaunchedEffect(authViewModel) {
        authViewModel.loggedInUser = { user ->
            if (user == null) {
                Toast.makeText(context, "Invalid Credentials", Toast.LENGTH_SHORT).show()
            } else {
                if (user.role == "admin") {
                    navController.navigate(ROUT_PRODUCT_LIST) {
                    }
                } else {
                    navController.navigate(ROUT_HOME) {
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundBrown)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Animated Welcome Text
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(animationSpec = tween(1000)),
                exit = fadeOut(animationSpec = tween(1000))
            ) {
                Text(
                    text = "Welcome To LujaGranola!",
                    fontSize = 30.sp,
                    fontFamily = FontFamily.Cursive,
                    color = DarkBrown
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Email Input
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                leadingIcon = { Icon(Icons.Filled.Email, contentDescription = "Email Icon") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DarkBrown,
                    unfocusedBorderColor = LightBrown,
                    focusedTextColor = TextOnLight,
                    unfocusedTextColor = TextOnLight,
                    cursorColor = DarkBrown,
                    focusedLabelColor = DarkBrown,
                    unfocusedLabelColor = MediumBrown,
                    focusedLeadingIconColor = DarkBrown,
                    unfocusedLeadingIconColor = MediumBrown
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Password Input
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Password Icon") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DarkBrown,
                    unfocusedBorderColor = LightBrown,
                    focusedTextColor = TextOnLight,
                    unfocusedTextColor = TextOnLight,
                    cursorColor = DarkBrown,
                    focusedLabelColor = DarkBrown,
                    unfocusedLabelColor = MediumBrown,
                    focusedLeadingIconColor = DarkBrown,
                    unfocusedLeadingIconColor = MediumBrown,
                    focusedTrailingIconColor = DarkBrown,
                    unfocusedTrailingIconColor = MediumBrown
                ),
                trailingIcon = {
                    val image = if (passwordVisible)
                        painterResource(R.drawable.visibility)
                    else
                        painterResource(R.drawable.visibilityoff)
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(painter = image, contentDescription = if (passwordVisible) "Hide password" else "Show password")
                    }
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Brown Gradient Login Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(8.dp, RoundedCornerShape(16.dp))
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(DarkBrown, MediumBrown)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        if (email.isBlank() || password.isBlank()) {
                            Toast.makeText(context, "Please enter email and password", Toast.LENGTH_SHORT).show()
                        } else {
                            authViewModel.loginUser(email, password)
                        }
                    },
                    modifier = Modifier.fillMaxSize(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 0.dp
                    )
                ) {
                    Text(
                        "Login",
                        color = TextOnBrown,
                        fontSize = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Register Navigation Button
            TextButton(
                onClick = { navController.navigate(ROUT_REGISTER) },
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    "Don't have an account? Register",
                    color = MediumBrown
                )
            }
        }
    }
}