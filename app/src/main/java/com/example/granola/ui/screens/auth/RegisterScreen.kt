package com.example.granola.ui.screens.auth

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import com.example.granola.model.User
import com.example.granola.viewmodel.AuthViewModel
import com.navigation.ROUT_LOGIN

// Brown color palette
private val DarkBrown = Color(0xFF4E342E)
private val MediumBrown = Color(0xFF795548)
private val LightBrown = Color(0xFFD7CCC8)
private val BackgroundBrown = Color(0xFFEFEBE9)
private val TextOnBrown = Color.White
private val TextOnLight = Color(0xFF3E2723)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    authViewModel: AuthViewModel,
    navController: NavController,
    onRegisterSuccess: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val animatedAlpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 1500, easing = LinearEasing),
        label = "Animated Alpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundBrown)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            AnimatedVisibility(
                visible = true,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Text(
                    "Create Your Account",
                    fontSize = 36.sp,
                    fontFamily = FontFamily.Cursive,
                    color = DarkBrown
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Username Field
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username", color = MediumBrown) },
                leadingIcon = {
                    Icon(
                        Icons.Filled.Person,
                        contentDescription = "Username Icon",
                        tint = MediumBrown
                    )
                },
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

            Spacer(modifier = Modifier.height(16.dp))

            // Email Field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email", color = MediumBrown) },
                leadingIcon = {
                    Icon(
                        Icons.Filled.Email,
                        contentDescription = "Email Icon",
                        tint = MediumBrown
                    )
                },
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

            Spacer(modifier = Modifier.height(16.dp))

            // Role Selection
            var role by remember { mutableStateOf("user") }
            val roleOptions = listOf("user", "admin")
            var expanded by remember { mutableStateOf(false) }

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = role,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Select Role", color = MediumBrown) },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expanded
                        )
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DarkBrown,
                        unfocusedBorderColor = LightBrown,
                        focusedTextColor = TextOnLight,
                        unfocusedTextColor = TextOnLight,
                        cursorColor = DarkBrown,
                        focusedLabelColor = DarkBrown,
                        unfocusedLabelColor = MediumBrown,
                        focusedTrailingIconColor = DarkBrown,
                        unfocusedTrailingIconColor = MediumBrown
                    )
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(LightBrown)
                ) {
                    roleOptions.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    selectionOption,
                                    color = DarkBrown
                                )
                            },
                            onClick = {
                                role = selectionOption
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Password Field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password", color = MediumBrown) },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                leadingIcon = {
                    Icon(
                        Icons.Filled.Lock,
                        contentDescription = "Password Icon",
                        tint = MediumBrown
                    )
                },
                trailingIcon = {
                    val image = if (passwordVisible)
                        painterResource(R.drawable.visibility)
                    else
                        painterResource(R.drawable.visibilityoff)
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            painter = image,
                            contentDescription = if (passwordVisible) "Hide Password" else "Show Password",
                            tint = MediumBrown
                        )
                    }
                },
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
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Confirm Password Field
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password", color = MediumBrown) },
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                leadingIcon = {
                    Icon(
                        Icons.Filled.Lock,
                        contentDescription = "Confirm Password Icon",
                        tint = MediumBrown
                    )
                },
                trailingIcon = {
                    val image = if (confirmPasswordVisible)
                        painterResource(R.drawable.visibility)
                    else
                        painterResource(R.drawable.visibilityoff)
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(
                            painter = image,
                            contentDescription = if (confirmPasswordVisible) "Hide Password" else "Show Password",
                            tint = MediumBrown
                        )
                    }
                },
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
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Register Button
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
                        if (username.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                            Toast.makeText(context, "All fields are required", Toast.LENGTH_SHORT).show()
                        } else if (password != confirmPassword) {
                            Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                        } else {
                            authViewModel.registerUser(User(username = username, email = email, role = role, password = password))
                            onRegisterSuccess()
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
                        "Register",
                        color = TextOnBrown,
                        fontSize = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Login Navigation
            TextButton(
                onClick = { navController.navigate(ROUT_LOGIN) },
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    "Already have an account? Login",
                    color = MediumBrown
                )
            }
        }
    }
}