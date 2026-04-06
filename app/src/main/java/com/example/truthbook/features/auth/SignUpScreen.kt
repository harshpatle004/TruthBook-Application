package com.example.truthbook.features.auth

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.truthbook.R
import com.example.truthbook.data.models.SendOtpRequest
import com.example.truthbook.data.models.EmailRequest
import com.example.truthbook.data.models.SetPasswordRequest
import com.example.truthbook.data.models.SetUsernameRequest
import com.example.truthbook.data.models.VerifyOtpRequest
import com.example.truthbook.data.remote.RetrofitInstance
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject

// ─────────────────────────────────────────────────────────────────────────────
// Backend flow:
// Step 1 → fullName + email → sendOtp(fullName, email)
// Step 2 → OTP              → verifyOtp(email, otp)
// Step 3 → password         → setPassword(email, password)
// Step 4 → username         → setUsername(email, userName) ← creates user + returns token
// ─────────────────────────────────────────────────────────────────────────────


// ─────────────────────────────────────────────────────────────────────────────
// Input Field
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun InputField(
    label: String,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    borderColor: Color,
    enabled: Boolean = true,
    trailingContent: @Composable (() -> Unit)? = null
) {
    val shape = RoundedCornerShape(50.dp)
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text          = label,
            fontSize      = 11.sp,
            fontWeight    = FontWeight.Bold,
            letterSpacing = 1.1.sp,
            color         = colorResource(R.color.label_color)
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value         = value,
            onValueChange = onValueChange,
            enabled       = enabled,
            placeholder   = {
                Text(
                    text      = placeholder,
                    color     = colorResource(R.color.hint_color),
                    fontSize  = 15.sp,
                    fontStyle = FontStyle.Italic
                )
            },
            singleLine           = true,
            keyboardOptions      = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
            visualTransformation = visualTransformation,
            trailingIcon         = trailingContent,
            shape                = shape,
            colors = TextFieldDefaults.colors(
                focusedContainerColor   = colorResource(R.color.field_bg),
                unfocusedContainerColor = colorResource(R.color.field_bg),
                disabledContainerColor  = colorResource(R.color.field_bg),
                focusedIndicatorColor   = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor  = Color.Transparent,
                cursorColor             = colorResource(R.color.purple_primary),
                focusedTextColor        = colorResource(R.color.title_color),
                unfocusedTextColor      = colorResource(R.color.title_color)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .border(1.2.dp, borderColor, shape)
        )
    }
}


// ─────────────────────────────────────────────────────────────────────────────
// Header with step dots
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun Header(currentStep: Int, totalSteps: Int = 4) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(110.dp))
        Text(
            text       = "TruthBook",
            fontSize   = 60.sp,
            fontStyle  = FontStyle.Italic,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Cursive,
            color      = colorResource(R.color.title_color)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text      = "Your truth. Your story.",
            fontStyle = FontStyle.Italic,
            fontSize  = 13.sp,
            color     = colorResource(R.color.subtext_color)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .width(44.dp)
                .height(2.dp)
                .background(colorResource(R.color.purple_primary), RoundedCornerShape(2.dp))
        )
        Spacer(modifier = Modifier.height(22.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment     = Alignment.CenterVertically
        ) {
            repeat(totalSteps) { i ->
                val active = i + 1 == currentStep
                Box(
                    modifier = Modifier
                        .height(6.dp)
                        .width(if (active) 28.dp else 14.dp)
                        .clip(CircleShape)
                        .background(
                            if (active) colorResource(R.color.purple_primary)
                            else colorResource(R.color.dot_inactive)
                        )
                )
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
    }
}


// ─────────────────────────────────────────────────────────────────────────────
// NavRow
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun NavRow(
    onBack: () -> Unit,
    onNext: () -> Unit,
    nextLabel: String = "Continue  →",
    isLoading: Boolean = false,
    nextEnabled: Boolean = true
) {
    Row(
        modifier              = Modifier.fillMaxWidth(),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .border(1.5.dp, colorResource(R.color.field_border), CircleShape)
                .background(colorResource(R.color.field_bg))
                .clickable(enabled = !isLoading) { onBack() },
            contentAlignment = Alignment.Center
        ) {
            Text("←", fontSize = 20.sp, color = colorResource(R.color.purple_primary))
        }
        Button(
            onClick  = onNext,
            enabled  = nextEnabled && !isLoading,
            modifier = Modifier
                .weight(1f)
                .height(54.dp),
            shape  = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor         = colorResource(R.color.purple_primary),
                disabledContainerColor = colorResource(R.color.purple_primary).copy(alpha = 0.5f)
            ),
            elevation = ButtonDefaults.buttonElevation(0.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier    = Modifier.size(22.dp),
                    color       = Color.White,
                    strokeWidth = 2.5.dp
                )
            } else {
                Text(
                    text       = nextLabel,
                    fontSize   = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color      = Color.White
                )
            }
        }
    }
}


// ─────────────────────────────────────────────────────────────────────────────
// Background Circles
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun BoxScope.BgCircles() {
    Box(
        modifier = Modifier
            .size(150.dp)
            .offset(x = (-55).dp, y = 44.dp)
            .clip(CircleShape)
            .background(colorResource(R.color.deco_circle).copy(alpha = 0.55f))
    )
    Box(
        modifier = Modifier
            .size(130.dp)
            .align(Alignment.BottomEnd)
            .offset(x = 45.dp, y = 0.dp)
            .clip(CircleShape)
            .background(colorResource(R.color.deco_circle).copy(alpha = 0.55f))
    )
}


// ─────────────────────────────────────────────────────────────────────────────
// Password Requirement Row
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun Requirement(text: String, met: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text       = if (met) "✓  " else "○  ",
            color      = if (met) colorResource(R.color.green_valid) else colorResource(R.color.subtext_color),
            fontSize   = 12.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text     = text,
            color    = if (met) colorResource(R.color.title_color) else colorResource(R.color.subtext_color),
            fontSize = 13.sp
        )
    }
}


// ─────────────────────────────────────────────────────────────────────────────
// Login Row
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun LoginRow(onLoginClick: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        Text(
            text     = "Already have an account? ",
            fontSize = 13.sp,
            color    = colorResource(R.color.subtext_color)
        )
        TextButton(onClick = onLoginClick, contentPadding = PaddingValues(0.dp)) {
            Text(
                text       = "Log in",
                fontSize   = 13.sp,
                fontWeight = FontWeight.Bold,
                color      = colorResource(R.color.purple_primary)
            )
        }
    }
}


// ─────────────────────────────────────────────────────────────────────────────
// Info Card
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun InfoCard(title: String, body: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(colorResource(R.color.field_bg))
            .border(1.dp, colorResource(R.color.field_border), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("ℹ ", color = colorResource(R.color.purple_primary), fontSize = 14.sp)
                Text(
                    text       = title,
                    fontWeight = FontWeight.Bold,
                    color      = colorResource(R.color.purple_primary),
                    fontSize   = 13.sp
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text       = body,
                color      = colorResource(R.color.subtext_color),
                fontSize   = 13.sp,
                lineHeight = 18.sp
            )
        }
    }
}


// ─────────────────────────────────────────────────────────────────────────────
// Error Banner
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun ErrorBanner(message: String) {
    if (message.isNotEmpty()) {
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFFFEBEE))
                .border(1.dp, Color(0xFFF44336), RoundedCornerShape(12.dp))
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("⚠ ", color = Color(0xFFF44336), fontSize = 14.sp)
                Text(
                    text       = message,
                    color      = Color(0xFFC62828),
                    fontSize   = 13.sp,
                    lineHeight = 18.sp
                )
            }
        }
    }
}


// ─────────────────────────────────────────────────────────────────────────────
// Helper — parse backend JSON error message
// ─────────────────────────────────────────────────────────────────────────────
private fun parseError(errorBody: String?): String {
    return try {
        JSONObject(errorBody ?: "").getString("message")
    } catch (e: Exception) {
        "Something went wrong. Please try again."
    }
}


// ─────────────────────────────────────────────────────────────────────────────
// MAIN SCREEN
// Step 1 → fullName + email  → sendOtp(fullName, email)
// Step 2 → OTP               → verifyOtp(email, otp)
// Step 3 → password          → setPassword(email, password)
// Step 4 → username          → setUsername(email, userName) ← creates user
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun SignUpScreen(
    onLoginClick: () -> Unit = {},
    onSignUpComplete: () -> Unit = {}
) {
    val scope      = rememberCoroutineScope()
    val totalSteps = 4
    var currentStep by remember { mutableStateOf(1) }

    var isLoading    by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // Step 1
    var fullName by remember { mutableStateOf("") }
    var email    by remember { mutableStateOf("") }
    val isEmailValid = email.contains("@") && email.contains(".")
    val isStep1Valid = fullName.isNotBlank() && isEmailValid

    // Step 2 — OTP
    val otpLength       = 6
    val otpValues       = remember { mutableStateListOf(*Array(otpLength) { "" }) }
    val focusRequesters = remember { List(otpLength) { FocusRequester() } }
    val otpFilled       = otpValues.all { it.isNotEmpty() }

    // Step 3 — Password
    var password        by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showPassword    by remember { mutableStateOf(false) }
    var showConfirm     by remember { mutableStateOf(false) }
    val hasMin8    = password.length >= 8
    val hasUpper   = password.any { it.isUpperCase() }
    val hasNumber  = password.any { it.isDigit() }
    val hasSpecial = password.any { !it.isLetterOrDigit() }
    val pwdStrong  = hasMin8 && hasUpper && hasNumber && hasSpecial
    val pwdMatch   = password == confirmPassword && confirmPassword.isNotEmpty()
    val strength   = listOf(hasMin8, hasUpper, hasNumber, hasSpecial).count { it }
    val strengthLabel = when (strength) {
        4    -> "Strong 💪"
        3    -> "Good"
        2    -> "Fair"
        else -> "Weak"
    }
    val strengthColor = when (strength) {
        4    -> colorResource(R.color.green_valid)
        3    -> Color(0xFF8BC34A)
        2    -> Color(0xFFFFC107)
        else -> Color(0xFFF44336)
    }

    // Step 4 — Username
    var username by remember { mutableStateOf("") }
    val isUsernameValid = username.length >= 3 && username.all { it.isLetterOrDigit() || it == '_' }

    // Back handler
    BackHandler(enabled = currentStep > 1) {
        errorMessage = ""
        if (currentStep == 2) otpValues.forEachIndexed { i, _ -> otpValues[i] = "" }
        currentStep--
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.bg_color))
    ) {
        BgCircles()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Header(currentStep = currentStep, totalSteps = totalSteps)

            when (currentStep) {

                // ══════════════════════════════════════════════════════════════
                // STEP 1 — Full Name + Email → sendOtp(fullName, email)
                // ══════════════════════════════════════════════════════════════
                1 -> {
                    Text(
                        text          = "TELL US ABOUT YOU",
                        fontSize      = 15.sp,
                        fontWeight    = FontWeight.ExtraBold,
                        letterSpacing = 1.8.sp,
                        color         = colorResource(R.color.title_color),
                        modifier      = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    InputField(
                        label         = "FULL NAME",
                        value         = fullName,
                        onValueChange = { fullName = it; errorMessage = "" },
                        placeholder   = "Enter your full name",
                        imeAction     = ImeAction.Next,
                        borderColor   = colorResource(R.color.field_border),
                        enabled       = !isLoading
                    )
                    Spacer(modifier = Modifier.height(18.dp))

                    InputField(
                        label         = "EMAIL ADDRESS",
                        value         = email,
                        onValueChange = { email = it; errorMessage = "" },
                        placeholder   = "jane.doe@email.com",
                        keyboardType  = KeyboardType.Email,
                        imeAction     = ImeAction.Done,
                        enabled       = !isLoading,
                        borderColor   = when {
                            email.isEmpty() -> colorResource(R.color.field_border)
                            isEmailValid    -> colorResource(R.color.green_valid)
                            else            -> Color(0xFFF44336)
                        }
                    )
                    if (isEmailValid) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text     = "✓ Valid email address",
                            color    = colorResource(R.color.green_valid),
                            fontSize = 12.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Spacer(modifier = Modifier.height(14.dp))

                    InfoCard(
                        title = "Why we need this?",
                        body  = "We'll send a one-time code to verify your identity and keep your account secure."
                    )

                    ErrorBanner(errorMessage)
                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            when {
                                fullName.isBlank() -> errorMessage = "Please enter your full name."
                                !isEmailValid      -> errorMessage = "Please enter a valid email address."
                                else -> {
                                    errorMessage = ""
                                    isLoading    = true
                                    scope.launch {
                                        try {
                                            val response = RetrofitInstance.api.sendOtp(
                                                SendOtpRequest(fullName = fullName, email = email)
                                            )
                                            if (response.isSuccessful) {
                                                currentStep = 2
                                            } else {
                                                val errorBody = response.errorBody()?.string()
                                                Log.e("SIGNUP_STEP1", "Code: ${response.code()} Body: $errorBody")
                                                errorMessage = parseError(errorBody)
                                            }
                                        } catch (e: Exception) {
                                            errorMessage = "Network error. Check your connection."
                                        } finally {
                                            isLoading = false
                                        }
                                    }
                                }
                            }
                        },
                        enabled  = isStep1Valid && !isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape  = RoundedCornerShape(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor         = colorResource(R.color.purple_primary),
                            disabledContainerColor = colorResource(R.color.purple_primary).copy(alpha = 0.5f)
                        ),
                        elevation = ButtonDefaults.buttonElevation(0.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier    = Modifier.size(22.dp),
                                color       = Color.White,
                                strokeWidth = 2.5.dp
                            )
                        } else {
                            Text(
                                text       = "Send OTP  →",
                                fontSize   = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color      = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    LoginRow(onLoginClick)
                }

                // ══════════════════════════════════════════════════════════════
                // STEP 2 — OTP → verifyOtp(email, otp)
                // ══════════════════════════════════════════════════════════════
                2 -> {
                    var timerSeconds  by remember { mutableStateOf(60) }
                    var canResend     by remember { mutableStateOf(false) }
                    var resendTrigger by remember { mutableStateOf(0) }

                    LaunchedEffect(resendTrigger) {
                        timerSeconds = 60
                        canResend    = false
                        while (timerSeconds > 0) {
                            delay(1000L)
                            timerSeconds--
                        }
                        canResend = true
                    }

                    Text(
                        text          = "OTP VERIFICATION",
                        fontSize      = 15.sp,
                        fontWeight    = FontWeight.ExtraBold,
                        letterSpacing = 1.8.sp,
                        color         = colorResource(R.color.title_color),
                        modifier      = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(50.dp))
                            .background(Color(0xFFE8F5E9))
                            .padding(horizontal = 18.dp, vertical = 13.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("📱 ", fontSize = 14.sp)
                            Text(
                                text       = "Code sent to $email",
                                color      = colorResource(R.color.green_valid),
                                fontSize   = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text          = "ENTER 6-DIGIT CODE",
                        fontSize      = 11.sp,
                        fontWeight    = FontWeight.Bold,
                        letterSpacing = 1.1.sp,
                        color         = colorResource(R.color.label_color),
                        modifier      = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // ── OTP boxes ──────────────────────────────────────────
                    Row(
                        modifier              = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        repeat(otpLength) { i ->
                            val otpShape = RoundedCornerShape(12.dp)
                            val filled   = otpValues[i].isNotEmpty()
                            TextField(
                                value         = otpValues[i],
                                onValueChange = { v ->
                                    errorMessage = ""
                                    val digit = v.filter { it.isDigit() }.take(1)
                                    otpValues[i] = digit
                                    if (digit.isNotEmpty() && i < otpLength - 1) {
                                        focusRequesters[i + 1].requestFocus()
                                    }
                                },
                                enabled    = !isLoading,
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction    = if (i == otpLength - 1) ImeAction.Done else ImeAction.Next
                                ),
                                textStyle = LocalTextStyle.current.copy(
                                    textAlign  = TextAlign.Center,
                                    fontSize   = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color      = colorResource(R.color.title_color)
                                ),
                                shape  = otpShape,
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor   = colorResource(R.color.field_bg),
                                    unfocusedContainerColor = colorResource(R.color.field_bg),
                                    focusedIndicatorColor   = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor  = Color.Transparent,
                                    cursorColor             = colorResource(R.color.purple_primary)
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(54.dp)
                                    .focusRequester(focusRequesters[i])
                                    .onKeyEvent { event ->
                                        if (event.type == KeyEventType.KeyDown && event.key == Key.Backspace) {
                                            if (otpValues[i].isEmpty() && i > 0) {
                                                otpValues[i - 1] = ""
                                                focusRequesters[i - 1].requestFocus()
                                            } else {
                                                otpValues[i] = ""
                                            }
                                            true
                                        } else {
                                            false
                                        }
                                    }
                                    .border(
                                        width  = 1.5.dp,
                                        color  = if (filled) colorResource(R.color.purple_primary)
                                        else colorResource(R.color.field_border),
                                        shape  = otpShape
                                    )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))

                    // ── Resend row ─────────────────────────────────────────
                    Row(
                        modifier          = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text     = "Didn't receive it? ",
                            fontSize = 13.sp,
                            color    = colorResource(R.color.subtext_color)
                        )
                        Text(
                            text           = "Resend OTP",
                            fontSize       = 13.sp,
                            fontWeight     = FontWeight.Bold,
                            color          = if (canResend) colorResource(R.color.purple_primary)
                            else colorResource(R.color.subtext_color),
                            textDecoration = if (canResend) TextDecoration.Underline
                            else TextDecoration.None,
                            modifier       = Modifier.clickable(enabled = canResend && !isLoading) {
                                otpValues.forEachIndexed { i, _ -> otpValues[i] = "" }
                                errorMessage  = ""
                                resendTrigger++
                                scope.launch {
                                    try {
                                        RetrofitInstance.api.resendOtp(EmailRequest(email))
                                    } catch (e: Exception) {
                                        errorMessage = "Failed to resend OTP."
                                    }
                                }
                            }
                        )
                        if (!canResend) {
                            Text(
                                text     = "  ·  0:${timerSeconds.toString().padStart(2, '0')}",
                                fontSize = 13.sp,
                                color    = colorResource(R.color.subtext_color)
                            )
                        }
                    }

                    ErrorBanner(errorMessage)
                    Spacer(modifier = Modifier.height(24.dp))

                    NavRow(
                        onBack = {
                            currentStep = 1
                            errorMessage = ""
                            otpValues.forEachIndexed { i, _ -> otpValues[i] = "" }
                        },
                        nextLabel   = "Verify & Continue",
                        isLoading   = isLoading,
                        nextEnabled = otpFilled,
                        onNext = {
                            if (!otpFilled) {
                                errorMessage = "Please enter the complete 6-digit OTP."
                                return@NavRow
                            }
                            val otp  = otpValues.joinToString("")
                            errorMessage = ""
                            isLoading    = true
                            scope.launch {
                                try {
                                    val response = RetrofitInstance.api.verifyOtp(
                                        VerifyOtpRequest(email, otp)
                                    )
                                    if (response.isSuccessful) {
                                        currentStep = 3
                                    } else {
                                        val errorBody = response.errorBody()?.string()
                                        Log.e("SIGNUP_STEP2", "Code: ${response.code()} Body: $errorBody")
                                        errorMessage = parseError(errorBody)
                                    }
                                } catch (e: Exception) {
                                    errorMessage = "Network error. Check your connection."
                                } finally {
                                    isLoading = false
                                }
                            }
                        }
                    )
                }

                // ══════════════════════════════════════════════════════════════
                // STEP 3 — Password → setPassword(email, password)
                // ══════════════════════════════════════════════════════════════
                3 -> {
                    Text(
                        text          = "CREATE A PASSWORD",
                        fontSize      = 15.sp,
                        fontWeight    = FontWeight.ExtraBold,
                        letterSpacing = 1.8.sp,
                        color         = colorResource(R.color.title_color),
                        modifier      = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    InputField(
                        label         = "PASSWORD",
                        value         = password,
                        onValueChange = { password = it; errorMessage = "" },
                        placeholder   = "Enter password",
                        keyboardType  = KeyboardType.Password,
                        visualTransformation = if (showPassword) VisualTransformation.None
                        else PasswordVisualTransformation(),
                        borderColor     = colorResource(R.color.field_border),
                        trailingContent = {
                            TextButton(
                                onClick        = { showPassword = !showPassword },
                                contentPadding = PaddingValues(end = 12.dp)
                            ) {
                                Text(
                                    text       = if (showPassword) "Hide" else "Show",
                                    color      = colorResource(R.color.purple_primary),
                                    fontSize   = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    )

                    if (password.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier              = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            repeat(4) { i ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(5.dp)
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(
                                            if (i < strength) strengthColor
                                            else colorResource(R.color.field_border)
                                        )
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text       = strengthLabel,
                            color      = strengthColor,
                            fontSize   = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier   = Modifier.fillMaxWidth(),
                            textAlign  = TextAlign.End
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(colorResource(R.color.field_bg))
                            .border(1.dp, colorResource(R.color.field_border), RoundedCornerShape(16.dp))
                            .padding(14.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                            Requirement("At least 8 characters", hasMin8)
                            Requirement("One uppercase letter",  hasUpper)
                            Requirement("One number",            hasNumber)
                            Requirement("One special character", hasSpecial)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    InputField(
                        label         = "CONFIRM PASSWORD",
                        value         = confirmPassword,
                        onValueChange = { confirmPassword = it; errorMessage = "" },
                        placeholder   = "Re-enter password",
                        keyboardType  = KeyboardType.Password,
                        imeAction     = ImeAction.Done,
                        visualTransformation = if (showConfirm) VisualTransformation.None
                        else PasswordVisualTransformation(),
                        borderColor = when {
                            confirmPassword.isEmpty() -> colorResource(R.color.field_border)
                            pwdMatch                  -> colorResource(R.color.green_valid)
                            else                      -> Color(0xFFF44336)
                        },
                        trailingContent = {
                            TextButton(
                                onClick        = { showConfirm = !showConfirm },
                                contentPadding = PaddingValues(end = 12.dp)
                            ) {
                                Text(
                                    text       = if (showConfirm) "Hide" else "Show",
                                    color      = colorResource(R.color.purple_primary),
                                    fontSize   = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    )
                    if (pwdMatch) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text     = "✓ Passwords match",
                            color    = colorResource(R.color.green_valid),
                            fontSize = 12.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    ErrorBanner(errorMessage)
                    Spacer(modifier = Modifier.height(24.dp))

                    NavRow(
                        onBack      = { currentStep = 2; errorMessage = "" },
                        nextEnabled = pwdStrong && pwdMatch,
                        isLoading   = isLoading,
                        onNext = {
                            when {
                                !pwdStrong -> errorMessage = "Password must meet all requirements."
                                !pwdMatch  -> errorMessage = "Passwords do not match."
                                else -> {
                                    errorMessage = ""
                                    isLoading    = true
                                    scope.launch {
                                        try {
                                            val response = RetrofitInstance.api.setPassword(
                                                SetPasswordRequest(email = email, password = password)
                                            )
                                            if (response.isSuccessful) {
                                                currentStep = 4
                                            } else {
                                                val errorBody = response.errorBody()?.string()
                                                Log.e("SIGNUP_STEP3", "Code: ${response.code()} Body: $errorBody")
                                                errorMessage = parseError(errorBody)
                                            }
                                        } catch (e: Exception) {
                                            errorMessage = "Network error. Check your connection."
                                        } finally {
                                            isLoading = false
                                        }
                                    }
                                }
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                }

                // ══════════════════════════════════════════════════════════════
                // STEP 4 — Username → setUsername(email, userName)
                // ══════════════════════════════════════════════════════════════
                4 -> {
                    Text(
                        text          = "CREATE A USERNAME",
                        fontSize      = 15.sp,
                        fontWeight    = FontWeight.ExtraBold,
                        letterSpacing = 1.8.sp,
                        color         = colorResource(R.color.title_color),
                        modifier      = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    InputField(
                        label         = "USERNAME",
                        value         = username,
                        onValueChange = {
                            username     = it.lowercase().replace(" ", "")
                            errorMessage = ""
                        },
                        placeholder = "e.g. jane_doe",
                        imeAction   = ImeAction.Done,
                        enabled     = !isLoading,
                        borderColor = when {
                            username.isEmpty() -> colorResource(R.color.field_border)
                            isUsernameValid    -> colorResource(R.color.green_valid)
                            else               -> Color(0xFFF44336)
                        }
                    )
                    if (username.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        if (isUsernameValid) {
                            Text(
                                text     = "✓ Username looks good!",
                                color    = colorResource(R.color.green_valid),
                                fontSize = 12.sp,
                                modifier = Modifier.fillMaxWidth()
                            )
                        } else {
                            Text(
                                text     = "Min 3 chars · letters, numbers & underscores only",
                                color    = Color(0xFFF44336),
                                fontSize = 12.sp,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))

                    InfoCard(
                        title = "Choosing your username",
                        body  = "Your username is unique and how others will find you on TruthBook. You can change it later in settings."
                    )

                    ErrorBanner(errorMessage)
                    Spacer(modifier = Modifier.height(24.dp))

                    NavRow(
                        onBack      = { currentStep = 3; errorMessage = "" },
                        nextLabel   = "Create Account  →",
                        isLoading   = isLoading,
                        nextEnabled = isUsernameValid,
                        onNext = {
                            if (!isUsernameValid) {
                                errorMessage = "Please enter a valid username."
                                return@NavRow
                            }
                            errorMessage = ""
                            isLoading    = true
                            scope.launch {
                                try {
                                    val response = RetrofitInstance.api.setUsername(
                                        SetUsernameRequest(email = email, userName = username)
                                    )
                                    if (response.isSuccessful) {
                                        val token = response.body()?.token
                                        Log.d("SIGNUP", "Account created! Token: $token")
                                        // TODO: save token → SharedPreferences or DataStore
                                        onSignUpComplete()
                                    } else {
                                        val errorBody = response.errorBody()?.string()
                                        Log.e("SIGNUP_STEP4", "Code: ${response.code()} Body: $errorBody")
                                        errorMessage = parseError(errorBody)
                                    }
                                } catch (e: Exception) {
                                    errorMessage = "Network error. Check your connection."
                                } finally {
                                    isLoading = false
                                }
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    LoginRow(onLoginClick)
                }
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true, device = "spec:width=390dp,height=844dp,dpi=460")
@Composable
fun PreviewSignUp() {
    SignUpScreen()
}