package com.example.bkashclone

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoginPinScreen(
    phoneNumber: String,
    correctPin: String?,
    isEnglish: Boolean,
    onLanguageChange: (Boolean) -> Unit,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit
) {
    var pin by remember { mutableStateOf("") }

    // ✅ 1. State to track if the user entered the wrong PIN
    var isError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .systemBarsPadding()
    ) {
        // Top Bar
        BkashTopBar(
            onBackClick = onBackClick,
            showLanguageToggle = true,
            isEnglish = isEnglish,
            onLanguageChange = onLanguageChange
        )

        // Middle Content Section
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_bkash_logo),
                    contentDescription = null,
                    modifier = Modifier.size(60.dp)
                )
                Icon(
                    imageVector = Icons.Default.QrCode,
                    contentDescription = "QR Code",
                    tint = BkashPink,
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = if (isEnglish) "Log In\nto your bKash account" else "আপনার বিকাশ একাউন্টে\nলগ ইন করুন",
                fontSize = 24.sp,
                fontWeight = FontWeight.Light
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(text = if (isEnglish) "Account Number" else "একাউন্ট নাম্বার", color = Color.Gray)
            Text(
                text = "+88 $phoneNumber",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // The PIN Field
            OutlinedTextField(
                value = pin,
                onValueChange = { },
                label = {
                    Text(
                        text = if (isEnglish) "bKash PIN" else "বিকাশ পিন",
                        color = if (isError) Color.Red else Color.Gray // ✅ Label turns red on error
                    )
                },
                placeholder = { Text(if (isEnglish) "Enter bKash PIN" else "বিকাশ পিন দিন", color = Color.LightGray) },
                visualTransformation = PasswordVisualTransformation(),
                readOnly = true,
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Fingerprint,
                        contentDescription = "Fingerprint",
                        tint = BkashPink,
                        modifier = Modifier.size(32.dp)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    // ✅ Underline turns red if there is an error
                    focusedIndicatorColor = if (isError) Color.Red else BkashPink,
                    unfocusedIndicatorColor = if (isError) Color.Red else Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent
                ),
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontSize = 24.sp,
                    letterSpacing = 8.sp // Adds spacing between the dots
                )
            )

            // ✅ 2. Show the warning text right below the PIN field if error is true
            if (isError) {
                Text(
                    text = if (isEnglish) "Incorrect PIN" else "পিন সঠিক নয়",
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = if (isEnglish) "Forgot PIN? Try PIN Reset" else "পিন ভুলে গিয়েছেন? পিন রিসেট করুন",
                color = BkashPink,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }

        // The Custom Keyboard at the very bottom
        BkashNumberPad(
            isEnglish = isEnglish,
            // Button is only clickable if they've typed a 5-digit PIN
            isNextEnabled = pin.length == 5,
            onNumberClick = { number ->
                if (pin.length < 5) {
                    pin += number
                    isError = false // ✅ Clear the error as soon as they start typing again
                }
            },
            onBackspaceClick = {
                if (pin.isNotEmpty()) {
                    pin = pin.dropLast(1)
                    isError = false // ✅ Clear the error on backspace
                }
            },
            onNextClick = {
                if (pin.length == 5) {
                    // ✅ CHANGE THIS: Compare with correctPin instead of "12345"
                    if (pin == correctPin) {
                        onNextClick()
                    } else {
                        isError = true
                        pin = ""
                    }
                }
            }
        )
    }
}


// --- UPDATED LOGIN PIN SCREEN PREVIEWS ---

@Preview(device = "id:pixel_5", showSystemUi = true, name = "7a. Login PIN - English")
@Composable
fun PreviewLoginPinEng() {
    MaterialTheme {
        LoginPinScreen(
            phoneNumber = "01712345678",
            correctPin = "12345", // Added required parameter
            isEnglish = true,
            onLanguageChange = {},
            onBackClick = {},
            onNextClick = {}
        )
    }
}

@Preview(device = "id:pixel_5", showSystemUi = true, name = "7b. Login PIN - Bangla")
@Composable
fun PreviewLoginPinBng() {
    MaterialTheme {
        LoginPinScreen(
            phoneNumber = "01911223344",
            correctPin = "12345", // Added required parameter
            isEnglish = false,
            onLanguageChange = {},
            onBackClick = {},
            onNextClick = {}
        )
    }
}

/**
 * Pro Tip: To see the "Incorrect PIN" error message in the preview,
 * you can use Interactive Mode (the finger icon) in Android Studio,
 * type anything other than "12345" and click the Next arrow.
 */