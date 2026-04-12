package com.example.bkashclone

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.compose.foundation.clickable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.ui.tooling.preview.Preview


// Reg Page 2: Phone Entry
@Composable
fun PhoneEntryScreen(
    isEnglish: Boolean,
    onLanguageChange: (Boolean) -> Unit,
    onBackClick: () -> Unit,
    onNextClick: (String) -> Unit
) {
    var phoneNumber by remember { mutableStateOf("") }

    // ✅ Logic to check if the number is exactly 11 digits
    val isNumberValid = phoneNumber.length == 11

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .systemBarsPadding()
    ) {
        BkashTopBar(
            onBackClick = onBackClick,
            showLanguageToggle = true,
            isEnglish = isEnglish,
            onLanguageChange = onLanguageChange
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(24.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_bkash_logo),
                contentDescription = null,
                modifier = Modifier.size(60.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = if (isEnglish) "Log In / Register\nwith Mobile Number" else "মোবাইল নাম্বার দিয়ে\nলগ ইন / রেজিস্ট্রেশন করুন",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(32.dp))
            Text(text = if (isEnglish) "Country Code" else "দেশের কোড", color = Color.Gray)
            Text(text = "🇧🇩 Bangladesh", fontSize = 18.sp, modifier = Modifier.padding(vertical = 8.dp))

            Spacer(modifier = Modifier.height(16.dp))
            Text(text = if (isEnglish) "Mobile Number" else "মোবাইল নাম্বার", color = Color.Gray)

            TextField(
                value = phoneNumber,
                onValueChange = { },
                prefix = { Text("+88  ", fontSize = 20.sp, color = Color.Black) },
                readOnly = true,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    // ✅ Underline turns red if invalid, pink if valid
                    unfocusedIndicatorColor = if (phoneNumber.isNotEmpty() && !isNumberValid) Color.Red else BkashPink,
                    focusedIndicatorColor = if (phoneNumber.isNotEmpty() && !isNumberValid) Color.Red else BkashPink
                ),
                textStyle = androidx.compose.ui.text.TextStyle(fontSize = 20.sp),
                modifier = Modifier.fillMaxWidth()
            )

            // ✅ Warning Text: Shows up only if they started typing but haven't reached 11 digits
            if (phoneNumber.isNotEmpty() && !isNumberValid) {
                Text(
                    text = if (isEnglish) "Number is not valid" else "সঠিক নাম্বার দিন",
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        BkashNumberPad(
            isEnglish = isEnglish,
            isNextEnabled = isNumberValid, // ✅ Passes the true/false state to the pad
            onNumberClick = { number ->
                if (phoneNumber.length < 11) {
                    phoneNumber += number
                }
            },
            onBackspaceClick = {
                if (phoneNumber.isNotEmpty()) {
                    phoneNumber = phoneNumber.dropLast(1)
                }
            },
            onNextClick = {
                if (isNumberValid) {
                    onNextClick(phoneNumber)
                }
            }
        )
    }
}

// Reg Page 3: Name Entry
@Composable
fun NameEntryScreen(
    isEnglish: Boolean,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .systemBarsPadding() // ✅ Added here too
    ) {
        BkashTopBar(
            onBackClick = onBackClick,
            rightText = if (isEnglish) "Do it later" else "পরে করুন"
        )

        Column(modifier = Modifier.weight(1f).padding(24.dp)) {
            Image(painter = painterResource(id = R.drawable.ic_bkash_logo), contentDescription = null, modifier = Modifier.size(60.dp))
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = if (isEnglish) "Set your name" else "আপনার নাম সেট করুন",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = if (isEnglish) "Only you will see this name on the home screen" else "আপনার নাম বিকাশ অ্যাপ-এর হোম স্ক্রিনে শুধুমাত্র আপনিই দেখতে পাবেন",
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(32.dp))
            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text(if (isEnglish) "First Name" else "নামের প্রথম অংশ") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text(if (isEnglish) "Last Name" else "নামের শেষ অংশ") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        BkashBottomButton(
            text = if (isEnglish) "Next" else "পরবর্তী",
            onClick = onNextClick
        )
    }
}

// Reg Page 4: Profile Pic Entry
@Composable
fun ProfilePicScreen(
    isEnglish: Boolean,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit
) {
    var isChecked by remember { mutableStateOf(true) }

    // ✅ State to hold the selected Image URI
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // ✅ The launcher that opens the phone's gallery
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri // Save the selected image URI
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .systemBarsPadding()
    ) {
        BkashTopBar(
            onBackClick = onBackClick,
            rightText = if (isEnglish) "Skip" else "পরে করুন"
        )

        Column(
            modifier = Modifier.weight(1f).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_bkash_logo),
                contentDescription = null,
                modifier = Modifier.size(60.dp).align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = if (isEnglish) "Add profile picture" else "প্রোফাইল ছবি সংযুক্ত করুন",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(64.dp))

            // Profile Picture Box
            Box(contentAlignment = Alignment.BottomEnd) {

                // ✅ Display selected image OR placeholder
                if (imageUri != null) {
                    AsyncImage(
                        model = imageUri,
                        contentDescription = "Selected Profile Picture",
                        contentScale = ContentScale.Crop, // Crops it perfectly into a circle
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                    )
                } else {
                    Surface(
                        shape = CircleShape,
                        color = Color.LightGray,
                        modifier = Modifier.size(100.dp)
                    ) {}
                }

                // Edit Icon (Triggers Gallery)
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    tint = Color.White,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(BkashPink)
                        .clickable {
                            galleryLauncher.launch("image/*") // ✅ Open Gallery to pick an image
                        }
                        .padding(6.dp)
                        .size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { isChecked = it },
                    colors = CheckboxDefaults.colors(checkedColor = BkashPink)
                )
                Text(if (isEnglish) "Make your profile picture visible to other app users" else "অন্যান্য অ্যাপ ব্যবহারকারীর কাছে আপনার প্রোফাইল ছবি দৃশ্যমান করুন")
            }
        }

        BkashBottomButton(
            text = if (isEnglish) "Get Started" else "শুরু করুন",
            onClick = onNextClick
        )
    }
}






// Reg Page 3.5: PIN Setup Entry
@Composable
fun PinSetupScreen(
    isEnglish: Boolean,
    onBackClick: () -> Unit,
    onNextClick: (String) -> Unit
) {
    var pin1 by remember { mutableStateOf("") }
    var pin2 by remember { mutableStateOf("") }

    // Tracks which field is currently active (1 or 2)
    var activeField by remember { mutableStateOf(1) }

    val isPin1Complete = pin1.length == 5
    val isPin2Complete = pin2.length == 5
    val doPinsMatch = pin1 == pin2
    val isNextEnabled = isPin1Complete && isPin2Complete && doPinsMatch

    // Show warning if they finished typing pin2 but it doesn't match pin1
    val showWarning = isPin2Complete && !doPinsMatch

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .systemBarsPadding()
    ) {
        // Top section pushes keypad to bottom
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
                    modifier = Modifier.size(50.dp)
                )
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Info",
                    tint = BkashPink,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = if (isEnglish) "5-digit" else "৫ সংখ্যার",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = BkashPink
            )
            Text(
                text = if (isEnglish) "Set new PIN" else "নতুন পিন সেট করুন",
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(32.dp))

            // FIELD 1: New PIN
            Text(text = if (isEnglish) "New bKash PIN" else "নতুন বিকাশ পিন", color = Color.Gray, fontSize = 14.sp)

            // Custom click handler for read-only text fields
            val interactionSource1 = remember { MutableInteractionSource() }
            if (interactionSource1.collectIsPressedAsState().value) { activeField = 1 }

            TextField(
                value = pin1,
                onValueChange = { },
                readOnly = true,
                visualTransformation = PasswordVisualTransformation(),
                interactionSource = interactionSource1,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = if (activeField == 1) BkashPink else Color.LightGray,
                    unfocusedIndicatorColor = if (activeField == 1) BkashPink else Color.LightGray
                ),
                textStyle = androidx.compose.ui.text.TextStyle(fontSize = 24.sp, letterSpacing = 8.sp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // FIELD 2: Confirm PIN
            Text(text = if (isEnglish) "Confirm new bKash PIN" else "নতুন বিকাশ পিন কনফার্ম করুন", color = Color.Gray, fontSize = 14.sp)

            val interactionSource2 = remember { MutableInteractionSource() }
            if (interactionSource2.collectIsPressedAsState().value) { activeField = 2 }

            TextField(
                value = pin2,
                onValueChange = { },
                readOnly = true,
                visualTransformation = PasswordVisualTransformation(),
                interactionSource = interactionSource2,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = if (showWarning) Color.Red else if (activeField == 2) BkashPink else Color.LightGray,
                    unfocusedIndicatorColor = if (showWarning) Color.Red else if (activeField == 2) BkashPink else Color.LightGray
                ),
                textStyle = androidx.compose.ui.text.TextStyle(fontSize = 24.sp, letterSpacing = 8.sp),
                modifier = Modifier.fillMaxWidth()
            )

            if (showWarning) {
                Text(
                    text = if (isEnglish) "PINs do not match" else "পিন মিলছে না",
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }

        // Custom Number Pad
        BkashNumberPad(
            isEnglish = isEnglish,
            isNextEnabled = isNextEnabled,
            buttonText = if (isEnglish) "Confirm" else "নিশ্চিত করুন", // Custom Text!
            onNumberClick = { number ->
                if (activeField == 1 && pin1.length < 5) {
                    pin1 += number
                    // Auto-switch to second field when first is full
                    if (pin1.length == 5) activeField = 2
                } else if (activeField == 2 && pin2.length < 5) {
                    pin2 += number
                }
            },
            onBackspaceClick = {
                if (activeField == 1 && pin1.isNotEmpty()) {
                    pin1 = pin1.dropLast(1)
                } else if (activeField == 2 && pin2.isNotEmpty()) {
                    pin2 = pin2.dropLast(1)
                }
            },
            onNextClick = {
                if (isNextEnabled) {
                    onNextClick(pin1)
                }
            }
        )
    }
}





// --- REGISTRATION FLOW PREVIEWS ---

@Preview(device = "id:pixel_5", showSystemUi = true, name = "3. Phone Entry - English")
@Composable
fun PreviewPhoneEntryEng() {
    PhoneEntryScreen(
        isEnglish = true,
        onLanguageChange = {},
        onBackClick = {},
        onNextClick = {}
    )
}

@Preview(device = "id:pixel_5", showSystemUi = true, name = "4. Name Entry - Bangla")
@Composable
fun PreviewNameEntryBng() {
    NameEntryScreen(
        isEnglish = false,
        onBackClick = {},
        onNextClick = {}
    )
}

@Preview(device = "id:pixel_5", showSystemUi = true, name = "5. Profile Picture Selection")
@Composable
fun PreviewProfilePicSelection() {
    // Note: Coil AsyncImage will show a placeholder in Preview
    ProfilePicScreen(
        isEnglish = true,
        onBackClick = {},
        onNextClick = {}
    )
}

@Preview(device = "id:pixel_5", showSystemUi = true, name = "6. PIN Setup - English")
@Composable
fun PreviewPinSetupEng() {
    PinSetupScreen(
        isEnglish = true,
        onBackClick = {},
        onNextClick = {}
    )
}

@Preview(device = "id:pixel_5", showSystemUi = true, name = "6. PIN Setup - Bangla")
@Composable
fun PreviewPinSetupBng() {
    PinSetupScreen(
        isEnglish = false,
        onBackClick = {},
        onNextClick = {}
    )
}

// --- SHARED COMPONENTS PREVIEW ---

@Preview(showBackground = true, name = "Bottom Button Custom")
@Composable
fun PreviewBottomButton() {
    Column(Modifier.padding(16.dp)) {
        BkashBottomButton(
            text = "পরবর্তী",
            onClick = {}
        )
    }
}