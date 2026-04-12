package com.example.bkashclone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.request.ImageRequest
import kotlinx.coroutines.launch


// The bKash Pink Color
val BkashPink = Color(0xFFE2136E)

// All the screens in your screenshots
sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object LoginRegChoice : Screen("login_reg_choice")
    object PhoneEntry : Screen("phone_entry")
    object NameEntry : Screen("name_entry")

    object PinSetup : Screen("pin_setup")
    object ProfilePicEntry : Screen("profile_pic")
    object LoginPin : Screen("login_pin")
    object Home : Screen("home")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                val prefManager = remember { PrefManager(this) }
                val scope = rememberCoroutineScope()

                // Global States
                var isLoading by remember { mutableStateOf(false) }
                var globalIsEnglish by remember { mutableStateOf(false) }
                var globalRegisteredPhone by remember { mutableStateOf("") }

                // Root Container to allow Overlay on top of NavHost
                Box(modifier = Modifier.fillMaxSize()) {
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Splash.route
                    ) {

                        // 1. Splash Screen
                        composable(Screen.Splash.route) {
                            SplashScreen(onTimeout = {
                                val savedPhone = prefManager.getUserPhone()
                                if (!savedPhone.isNullOrEmpty()) {
                                    // If already registered, go to Login
                                    navController.navigate("${Screen.LoginPin.route}/$savedPhone") {
                                        popUpTo(Screen.Splash.route) { inclusive = true }
                                    }
                                } else {
                                    // New user flow
                                    navController.navigate(Screen.LoginRegChoice.route) {
                                        popUpTo(Screen.Splash.route) { inclusive = true }
                                    }
                                }
                            })
                        }

                        // 2. Choice Screen
                        composable(Screen.LoginRegChoice.route) {
                            LoginRegChoiceScreen(
                                isEnglish = globalIsEnglish,
                                onLanguageChange = { globalIsEnglish = it },
                                onLoginRegClick = { navController.navigate(Screen.PhoneEntry.route) }
                            )
                        }

                        // 3. Phone Entry
                        composable(Screen.PhoneEntry.route) {
                            PhoneEntryScreen(
                                isEnglish = globalIsEnglish,
                                onLanguageChange = { globalIsEnglish = it },
                                onBackClick = { navController.popBackStack() },
                                onNextClick = { phoneNum ->
                                    globalRegisteredPhone = phoneNum
                                    // Logic to check if user already exists in your "backend"
                                    if (phoneNum == "01712345678") {
                                        navController.navigate("${Screen.LoginPin.route}/$phoneNum")
                                    } else {
                                        navController.navigate(Screen.NameEntry.route)
                                    }
                                }
                            )
                        }

                        // 4. Name Entry
                        composable(Screen.NameEntry.route) {
                            NameEntryScreen(
                                isEnglish = globalIsEnglish,
                                onBackClick = { navController.popBackStack() },
                                onNextClick = { navController.navigate(Screen.ProfilePicEntry.route) }
                            )
                        }

                        // 5. Profile Pic
                        composable(Screen.ProfilePicEntry.route) {
                            ProfilePicScreen(
                                isEnglish = globalIsEnglish,
                                onBackClick = { navController.popBackStack() },
                                onNextClick = { navController.navigate(Screen.PinSetup.route) }
                            )
                        }

                        // 6. PIN Setup (Final Registration Step)
                        composable(Screen.PinSetup.route) {
                            PinSetupScreen(
                                isEnglish = globalIsEnglish,
                                onBackClick = { navController.popBackStack() },
                                onNextClick = { createdPin ->
                                    // Save credentials to storage
                                    prefManager.saveUserPhone(globalRegisteredPhone)
                                    prefManager.saveUserPin(createdPin)

                                    navController.navigate("${Screen.LoginPin.route}/$globalRegisteredPhone") {
                                        popUpTo(Screen.LoginRegChoice.route) { inclusive = true }
                                    }
                                }
                            )
                        }

                        // 7. Login PIN Screen (With Loading Transition)
                        composable("${Screen.LoginPin.route}/{phone}") { backStackEntry ->
                            val phone = backStackEntry.arguments?.getString("phone") ?: ""
                            val savedPin = prefManager.getUserPin()

                            LoginPinScreen(
                                phoneNumber = phone,
                                correctPin = savedPin,
                                isEnglish = globalIsEnglish,
                                onLanguageChange = { globalIsEnglish = it },
                                onBackClick = { navController.popBackStack() },
                                onNextClick = {
                                    // Trigger Loading sequence
                                    scope.launch {
                                        isLoading = true
                                        delay(2000) // 2 second delay for your GIF
                                        navController.navigate(Screen.Home.route) {
                                            popUpTo(0) { inclusive = true }
                                        }
                                        isLoading = false
                                    }
                                }
                            )
                        }

                        // 8. Home Screen
                        composable(Screen.Home.route) {
                            HomeScreen(
                                isEnglish = globalIsEnglish,
                                onLanguageChange = { globalIsEnglish = it }
                            )
                        }
                    }

                    // --- GLOBAL LOADING OVERLAY ---
                    LoadingOverlay(isLoading = isLoading)
                }
            }
        }
    }
}

//toggle lang
@Composable
fun LanguageToggle(
    isEnglish: Boolean,
    onLanguageChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .background(BkashPink, RoundedCornerShape(20.dp))
            .border(1.dp, Color.White, RoundedCornerShape(20.dp))
            .padding(2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // English Option
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(18.dp))
                .clickable { onLanguageChange(true) }
                .background(if (isEnglish) Color.White else Color.Transparent)
                .padding(horizontal = 16.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Eng",
                color = if (isEnglish) BkashPink else Color.White,
                fontSize = 14.sp,
                fontWeight = if (isEnglish) FontWeight.Bold else FontWeight.Medium
            )
        }

        // Bangla Option
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(18.dp))
                .clickable { onLanguageChange(false) }
                .background(if (!isEnglish) Color.White else Color.Transparent)
                .padding(horizontal = 16.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "বাং",
                color = if (!isEnglish) BkashPink else Color.White,
                fontSize = 14.sp,
                fontWeight = if (!isEnglish) FontWeight.Bold else FontWeight.Medium
            )
        }
    }
}

//splash screen

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "logo_scale_transition")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 350, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "logo_scale_animation"
    )

    LaunchedEffect(Unit) {
        delay(2000)
        onTimeout()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BkashPink),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_bkash_logo),
            contentDescription = "bKash Logo",
            modifier = Modifier
                .size(240.dp)
                .offset(y = (-40).dp)
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale
                ),
            contentScale = ContentScale.Fit
        )

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 60.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            repeat(3) {
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.5f))
                )
            }
        }
    }
}

//loading page
@Composable
fun LoadingOverlay(isLoading: Boolean) {
    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f)) // The "slightly transparent" dim
                .zIndex(10f)
                .clickable(enabled = false) { }, // Stops users from clicking buttons behind the GIF
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(R.drawable.loading_bird_gif) // Put your GIF name here (no extension)
                    .decoderFactory(GifDecoder.Factory())
                    .build(),
                contentDescription = "Loading...",
                modifier = Modifier.size(120.dp) // Adjust size as needed
            )
        }
    }
}

//login reg

@Composable
fun LoginRegChoiceScreen(
    isEnglish: Boolean,                  // ✅ Added parameter
    onLanguageChange: (Boolean) -> Unit, // ✅ Added parameter
    onLoginRegClick: () -> Unit
) {
    // ✅ REMOVED the local state var isEnglish by remember...

    val titleText = if (isEnglish) "Great services await you" else "দারুণ সব সার্ভিস আপনার অপেক্ষায়"
    val buttonText = if (isEnglish) "Log In / Registration" else "লগ ইন / রেজিস্ট্রেশন"
    val bottomText = if (isEnglish) "Change bKash Number" else "বিকাশ নাম্বার পরিবর্তন"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BkashPink)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp, end = 24.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            LanguageToggle(
                isEnglish = isEnglish,
                onLanguageChange = onLanguageChange // ✅ Passed the function up
            )
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .align(Alignment.Center),
            colors = CardDefaults.cardColors(containerColor = Color(0xF0C81060)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 30.dp, horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = titleText,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onLoginRegClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(buttonText, color = BkashPink, fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(20.dp))

                TextButton(onClick = { /* Handle number change */ }) {
                    Text(bottomText, color = Color.White, fontSize = 16.sp)
                }
            }
        }
    }
}



// --- COMPONENT PREVIEWS ---

@Preview(showBackground = true, name = "Toggle - English")
@Composable
fun PreviewLanguageToggleEng() {
    Box(Modifier.padding(20.dp)) {
        LanguageToggle(isEnglish = true, onLanguageChange = {})
    }
}

@Preview(showBackground = true, name = "Toggle - Bangla")
@Composable
fun PreviewLanguageToggleBng() {
    Box(Modifier.padding(20.dp)) {
        LanguageToggle(isEnglish = false, onLanguageChange = {})
    }
}


// Put this at the very bottom of MainActivity.kt
class PrefManager(context: android.content.Context) {
    private val prefs = context.getSharedPreferences("bkash_prefs", android.content.Context.MODE_PRIVATE)

    fun saveUserPhone(phone: String) {
        prefs.edit().putString("registered_phone", phone).apply()
    }

    fun getUserPhone(): String? = prefs.getString("registered_phone", null)

    // ✅ ADD THESE TWO FUNCTIONS
    fun saveUserPin(pin: String) {
        prefs.edit().putString("user_pin", pin).apply()
    }

    fun getUserPin(): String? = prefs.getString("user_pin", null)
}




// --- FULL SCREEN PREVIEWS ---

@Preview(device = "id:pixel_5", showSystemUi = true, name = "1. Splash Screen")
@Composable
fun PreviewSplashScreen() {
    SplashScreen(onTimeout = {})
}

@Preview(device = "id:pixel_5", showSystemUi = true, name = "2. Choice Screen - English")
@Composable
fun PreviewChoiceEng() {
    LoginRegChoiceScreen(isEnglish = true, onLanguageChange = {}, onLoginRegClick = {})
}

@Preview(device = "id:pixel_5", showSystemUi = true, name = "2. Choice Screen - Bangla")
@Composable
fun PreviewChoiceBng() {
    LoginRegChoiceScreen(isEnglish = false, onLanguageChange = {}, onLoginRegClick = {})
}

@Preview(device = "id:pixel_5", showSystemUi = true, name = "3. Phone Entry")
@Composable
fun PreviewPhoneEntry() {
    // Replace with your PhoneEntryScreen call
    PhoneEntryScreen(isEnglish = true, onLanguageChange = {}, onBackClick = {}, onNextClick = {})
}

@Preview(device = "id:pixel_5", showSystemUi = true, name = "4. Name Entry")
@Composable
fun PreviewNameEntry() {
    // Replace with your NameEntryScreen call
    NameEntryScreen(isEnglish = true, onBackClick = {}, onNextClick = {})
}

@Preview(device = "id:pixel_5", showSystemUi = true, name = "5. Profile Picture")
@Composable
fun PreviewProfilePic() {
    // Replace with your ProfilePicScreen call
    ProfilePicScreen(isEnglish = true, onBackClick = {}, onNextClick = {})
}

@Preview(device = "id:pixel_5", showSystemUi = true, name = "6. PIN Setup")
@Composable
fun PreviewPinSetup() {
    // Replace with your PinSetupScreen call
    PinSetupScreen(isEnglish = true, onBackClick = {}, onNextClick = {})
}

//@Preview(device = "id:pixel_5", showSystemUi = true, name = "7. Login PIN Screen")
//@Composable
//fun PreviewLoginPin() {
//    LoginPinScreen(
//        phoneNumber = "01712345678",
//        isEnglish = true,
//        onLanguageChange = {},
//        onBackClick = {},
//        onNextClick = {}
//    )
//}