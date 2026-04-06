package com.example.truthbook.features.auth

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.truthbook.R
import com.example.truthbook.features.auth.LoginState
import com.example.truthbook.features.auth.LoginViewModel


private val BackgroundColor = Color(0xFFF3EEF8)
private val BubbleLight     = Color(0xFFE6D9F2)
private val BubbleMid       = Color(0xFFD8C5EE)
private val ButtonPurple    = Color(0xFFC4A0DC)
val TextPrimary     = Color(0xFF3A2D5C)
private val TextSecondary   = Color(0xFF9B8BB0)
private val DividerColor    = Color(0xFFCCBBE0)

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onSignupClick: () -> Unit,
    loginViewModel: LoginViewModel
) {

    var identifier by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val loginState by loginViewModel.loginState.collectAsState()

//    val context = LocalContext.current

    LaunchedEffect(loginState) {
        if (loginState is LoginState.Success) {
            onLoginSuccess()
        }
    }

    Scaffold { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(BackgroundColor)
        ) {

            // Background blobs
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 50.dp, y = (-30).dp)
                    .clip(CircleShape)
                    .background(BubbleMid.copy(alpha = 0.6f))
            )

            Box(
                modifier = Modifier
                    .size(90.dp)
                    .align(Alignment.TopStart)
                    .offset(x = (-25).dp, y = 50.dp)
                    .clip(CircleShape)
                    .background(BubbleLight.copy(alpha = 0.8f))
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(Modifier.weight(1f))

                // Title
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Text(
                        "TruthBook",
                        fontSize = 60.sp,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        fontFamily = FontFamily.Cursive,
                        color = TextPrimary
                    )

                    Text(
                        "Your truth. Your story.",
                        fontSize = 14.sp,
                        color = TextSecondary,
                        fontStyle = FontStyle.Italic
                    )

                    Spacer(Modifier.height(10.dp))

                    Box(
                        modifier = Modifier
                            .width(44.dp)
                            .height(1.5.dp)
                            .background(DividerColor, RoundedCornerShape(50))
                    )
                }

                Spacer(Modifier.height(48.dp))

                // Inputs
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                ) {

                    Text("EMAIL OR USERNAME", fontSize = 11.sp, color = TextSecondary)
                    Spacer(Modifier.height(6.dp))

                    OutlinedTextField(
                        value = identifier,
                        onValueChange = { identifier = it },
                        placeholder = { Text("Enter your email or username ") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        shape = RoundedCornerShape(16.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor   = colorResource(R.color.field_bg),
                            unfocusedContainerColor = colorResource(R.color.field_bg),
                            disabledContainerColor  = colorResource(R.color.field_bg),
                            focusedIndicatorColor = colorResource(R.color.field_border),
                            unfocusedIndicatorColor = colorResource(R.color.field_border),
                            disabledIndicatorColor  = Color.Transparent,
                            cursorColor             = colorResource(R.color.purple_primary),
                            focusedTextColor        = colorResource(R.color.title_color),
                            unfocusedTextColor      = colorResource(R.color.title_color)
                        ),

                        modifier = Modifier.fillMaxWidth()

                    )

                    Spacer(Modifier.height(16.dp))

                    Text("PASSWORD", fontSize = 11.sp, color = TextSecondary)
                    Spacer(Modifier.height(6.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = { Text("Enter your password") },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor   = colorResource(R.color.field_bg),
                            unfocusedContainerColor = colorResource(R.color.field_bg),
                            disabledContainerColor  = colorResource(R.color.field_bg),
                            focusedIndicatorColor = colorResource(R.color.field_border),
                            unfocusedIndicatorColor = colorResource(R.color.field_border),
                            disabledIndicatorColor  = Color.Transparent,
                            cursorColor             = colorResource(R.color.purple_primary),
                            focusedTextColor        = colorResource(R.color.title_color),
                            unfocusedTextColor      = colorResource(R.color.title_color)
                        ),
                        singleLine = true,
                        visualTransformation =
                            if (passwordVisible) VisualTransformation.None
                            else PasswordVisualTransformation(),
                        trailingIcon = {
                            TextButton(onClick = {
                                passwordVisible = !passwordVisible
                            }) {
                                Text(
                                    if (passwordVisible) "Hide" else "Show",
                                    color = colorResource(R.color.purple_200))
                            }
                        },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(Modifier.height(12.dp))

                if (loginState is LoginState.Error) {
                    Text(
                        text = (loginState as LoginState.Error).message,
                        color = Color.Red,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(horizontal = 32.dp)
                    )
                }

                Spacer(Modifier.height(20.dp))

                // Login Button (UI only)
                Button(
                    onClick = {
                        loginViewModel.login(identifier, password)
                    },
                    enabled = loginState !is LoginState.Loading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                        .height(52.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = ButtonPurple)
                ) {
                    if (loginState is LoginState.Loading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(22.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Login", color = Color.White)
                    }
                }

                Spacer(Modifier.height(16.dp))

                // 🔥 Signup navigation
                Row (  verticalAlignment = Alignment.CenterVertically){
                    Text("Don't have an account? ", color = TextSecondary)
                    TextButton(
                        onClick = {
                            onSignupClick()
                        }
                    ) {
                        Text("Sign Up",color = colorResource(R.color.purple_primary))

                    }
                }

                Spacer(Modifier.weight(1f))
            }
        }
    }
}