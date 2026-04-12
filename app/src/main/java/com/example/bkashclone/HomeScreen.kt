package com.example.bkashclone

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.yield

// Data model for services
data class BkashService(
    val nameEn: String,
    val nameBn: String,
    val iconRes: Int
)

data class QuickContact(val name: String, val iconRes: Int)
data class FeatureCard(val nameEn: String, val nameBn: String, val iconRes: Int, val bgColor: Color)
data class OtherService(val nameEn: String, val nameBn: String, val iconRes: Int)

@Composable
fun HomeScreen(
    isEnglish: Boolean,
    onLanguageChange: (Boolean) -> Unit
) {
    // List of services based on your screenshots
    val mainServices = listOf(
        BkashService("Send Money", "সেন্ট মানি", R.drawable.ic_send_money),
        BkashService("Mobile Recharge", "মোবাইল রিচার্জ", R.drawable.ic_recharge),
        BkashService("Cash Out", "ক্যাশ আউট", R.drawable.ic_cash_out),
        BkashService("Make Payment", "পেমেন্ট", R.drawable.ic_payment),
        BkashService("Add Money", "অ্যাড মানি", R.drawable.ic_add_money),
        BkashService("Pay Bill", "পে বিল", R.drawable.ic_pay_bill),
        BkashService("Savings", "সেভিংস", R.drawable.ic_savings),
        BkashService("Loan", "লোন", R.drawable.ic_loan),
        BkashService("Remittance", "রেমিট্যান্স", R.drawable.ic_remittance),
        BkashService("Transfer Money", "ট্রান্সফার মানি", R.drawable.ic_transfer_money),
        BkashService("Education Fee", "এডুকেশন ফি", R.drawable.ic_education),
        BkashService("Microfinance", "মাইক্রোফাইন্যান্স", R.drawable.ic_microfinance),
        BkashService("bKash Bundle", "বিকাশ বান্ডেল", R.drawable.ic_bundle),
        BkashService("Donation", "অনুদান", R.drawable.ic_donation),
        BkashService("Insurance", "বীমা", R.drawable.ic_insurance),
        BkashService("Request Money", "রিকোয়েস্ট মানি", R.drawable.ic_request_money),
        BkashService("Toll", "টোল", R.drawable.ic_toll)

    )

    Scaffold(
        bottomBar = { BkashBottomNav(isEnglish) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                // 1. Header with Scenic Background
                item {
                    HomeHeader(isEnglish, onLanguageChange)
                }

                // 2. Main Services Card
                item {
                    MainServicesSection(isEnglish, mainServices)
                }

                // 3. Promotional Banner
                item {
                    PromotionalBanner()
                }

                // 4. Suggestions Section

                item { QuickFeaturesSection(isEnglish) }
                item { OtherServicesSection(isEnglish) }
//                item {
//                    SuggestionsSection(isEnglish)
//                }

                // Add extra space at the bottom for scrolling
                item { Spacer(modifier = Modifier.height(20.dp)) }
            }
        }
    }
}

@Composable
fun HomeHeader(isEnglish: Boolean, onLanguageChange: (Boolean) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
    ) {
        // The Scenic Background Image
        Image(
            painter = painterResource(id = R.drawable.bkash_bg_scenic),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(modifier = Modifier.fillMaxSize()) {
            // Top Row: Profile and Icons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Profile Picture
                Image(
                    painter = painterResource(id = R.drawable.user_profile),
                    contentDescription = "Profile",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .border(1.5.dp, Color.White, CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Name and Balance Button
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "kawser ahmed kazol",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // The Interactive Balance Button
                    var showBalance by remember { mutableStateOf(false) }
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { showBalance = !showBalance },
                        color = Color.White
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_bkash_logo),
                                contentDescription = null,
                                tint = BkashPink,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (showBalance) "৳ 2540.50"
                                else if (isEnglish) "Tap for Balance"
                                else "ব্যালেন্স দেখুন",
                                color = BkashPink,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Search and Bird Logo
                Icon(Icons.Default.Search, null, tint = Color.White, modifier = Modifier.size(28.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Image(
                    painter = painterResource(id = R.drawable.ic_bkash_bird),
                    contentDescription = null,
                    modifier = Modifier.size(35.dp)
                )
            }
        }
    }
}

@Composable
fun MainServicesSection(isEnglish: Boolean, services: List<BkashService>) {
    // 1. State to track if the grid is expanded or not
    var isExpanded by remember { mutableStateOf(false) }

    // 2. Decide which items to show: only 8 (2 rows) if collapsed, or all of them if expanded
    val visibleServices = if (isExpanded) services else services.take(8)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = (-25).dp)
            .padding(horizontal = 12.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(vertical = 16.dp)) {
            // 3. Create the grid rows
            val rows = visibleServices.chunked(4)
            rows.forEach { rowItems ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    rowItems.forEach { service ->
                        ServiceItem(isEnglish, service)
                    }

                    // Logic to maintain spacing if a row has less than 4 items
                    if (rowItems.size < 4) {
                        repeat(4 - rowItems.size) {
                            Spacer(modifier = Modifier.width(80.dp))
                        }
                    }
                }
            }

            // 4. "See More / See Less" Button logic
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .clickable { isExpanded = !isExpanded }, // Toggle the expansion state
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (isExpanded) {
                            if (isEnglish) "See Less" else "বন্ধ করুন"
                        } else {
                            if (isEnglish) "See More" else "আরো দেখুন"
                        },
                        color = BkashPink,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = BkashPink,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ServiceItem(isEnglish: Boolean, service: BkashService) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(80.dp)
    ) {
        Image(
            painter = painterResource(id = service.iconRes),
            contentDescription = null,
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = if (isEnglish) service.nameEn else service.nameBn,
            fontSize = 11.sp,
            textAlign = TextAlign.Center,
            lineHeight = 14.sp,
            color = Color.DarkGray
        )
    }
}

@Composable
fun PromotionalBanner() {
    // List of your banner images
    val bannerImages = listOf(
        R.drawable.promo_banner,
        R.drawable.promo_banner_2, // Add your other banner images here
        R.drawable.promo_banner_3,
        R.drawable.promo_banner_4,
        R.drawable.promo_banner_5
    )

    val pagerState = rememberPagerState(pageCount = { bannerImages.size })

    // --- AUTO-SLIDE LOGIC ---
    LaunchedEffect(Unit) {
        while (true) {
            yield()
            delay(4000) // Slide every 4 seconds
            val nextPage = (pagerState.currentPage + 1) % bannerImages.size
            pagerState.animateScrollToPage(nextPage)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // The Sliding Pager
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .padding(horizontal = 16.dp),
            pageSpacing = 8.dp
        ) { page ->
            Card(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    painter = painterResource(id = bannerImages[page]),
                    contentDescription = "Promo Banner",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // --- PAGE INDICATORS (The Dots) ---
        Row(
            modifier = Modifier.wrapContentHeight().fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color = if (pagerState.currentPage == iteration) BkashPink else Color.LightGray
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(6.dp)
                )
            }
        }
    }
}

@Composable
fun QuickFeaturesSection(isEnglish: Boolean) {
    val contacts = listOf(
        QuickContact("Kazol...", R.drawable.ic_send_money),
        QuickContact("Zobair...", R.drawable.ic_recharge),
        QuickContact("Ammu..", R.drawable.ic_send_money)
    )

    val featureCards = listOf(
        FeatureCard("My Offers", "মাই অফারস", R.drawable.ic_my_offers, Color(0xFFFFF8E1)), // Light Orange
        FeatureCard("Coupon", "কুপন", R.drawable.ic_coupon, Color(0xFFE0F2F1)),    // Light Teal
        FeatureCard("Rewards", "রিওয়ার্ডস", R.drawable.ic_rewards, Color(0xFFF9FBE7)) // Light Yellow
    )

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = if (isEnglish) "Quick Features" else "কুইক ফিচারসমূহ",
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Top Row: Contacts
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            contacts.forEach { contact ->
                Surface(
                    modifier = Modifier.weight(1f).height(60.dp),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color(0xFFE0E0E0))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
                        Image(painterResource(contact.iconRes), null, modifier = Modifier.size(30.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(contact.name, fontSize = 12.sp, maxLines = 1)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Bottom Row: Feature Cards
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            featureCards.forEach { card ->
                Surface(
                    modifier = Modifier.weight(1f).height(100.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = card.bgColor
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Image(painterResource(card.iconRes), null, modifier = Modifier.size(35.dp))
                        Text(
                            text = if (isEnglish) card.nameEn else card.nameBn,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OtherServicesSection(isEnglish: Boolean) {
    val allServices = listOf(
        OtherService("Tickets", "টিকিট ও ট্রাভেল", R.drawable.ic_tickets),
        OtherService("Recharge", "রিচার্জ প্ল্যান", R.drawable.ic_recharge_plan),
        OtherService("Tax", "বিডি ট্যাক্স", R.drawable.ic_tax),
        OtherService("Insurance", "ইন্স্যুরেন্স", R.drawable.ic_insurance_plan),
        OtherService("Games", "গেমস", R.drawable.ic_games),
        OtherService("Subs", "সাবস্ক্রিপশন", R.drawable.ic_subs),
        OtherService("Deen", "দ্বীন", R.drawable.ic_deen),
        OtherService("Invest", "বিনিয়োগ শিক্ষা", R.drawable.ic_invest),
        OtherService("Shopping", "শপিং", R.drawable.ic_shopping),
        OtherService("Binimoy", "বিনিময়", R.drawable.ic_binimoy),
        OtherService("NPSB", "এনপিএসবি", R.drawable.ic_npsb)
    )

    val pages = allServices.chunked(8) // 8 items per page (4x2 grid)
    val pagerState = rememberPagerState(pageCount = { pages.size })

    Column(modifier = Modifier.padding(16.dp)) {
        // Header with Dots on the Right
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isEnglish) "Other Services" else "অন্যান্য সেবাসমূহ",
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )

            // Pager Indicators (Dots)
            Row {
                repeat(pages.size) { index ->
                    Box(
                        modifier = Modifier
                            .padding(2.dp)
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(if (pagerState.currentPage == index) BkashPink else Color(0xFFFCE4EC))
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        HorizontalPager(state = pagerState) { pageIndex ->
            val pageItems = pages[pageIndex]
            Column {
                pageItems.chunked(4).forEach { rowItems ->
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                        rowItems.forEach { service ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.width(80.dp).padding(vertical = 8.dp)
                            ) {
                                Box(
                                    modifier = Modifier.size(50.dp).background(Color(0xFFF5F5F5), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(painterResource(service.iconRes), null, modifier = Modifier.size(30.dp))
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = if (isEnglish) service.nameEn else service.nameBn,
                                    fontSize = 11.sp,
                                    textAlign = TextAlign.Center,
                                    maxLines = 2
                                )
                            }
                        }
                        // Fill empty space if row has < 4 items
                        if (rowItems.size < 4) {
                            repeat(4 - rowItems.size) { Spacer(modifier = Modifier.width(80.dp)) }
                        }
                    }
                }
            }
        }
    }
}

//@Composable
//fun SuggestionsSection(isEnglish: Boolean) {
//    Column(modifier = Modifier.padding(16.dp)) {
//        Text(
//            text = if (isEnglish) "Suggestions" else "সাজেশন",
//            fontWeight = FontWeight.Bold,
//            fontSize = 16.sp
//        )
//        // You can add a horizontal row of suggested icons here
//    }
//}

@Composable
fun BkashBottomNav(isEnglish: Boolean) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        val items = listOf(
            Triple(if (isEnglish) "Home" else "হোম", Icons.Default.Home, true),
            Triple(if (isEnglish) "My bKash" else "আমার বিকাশ", Icons.Default.AccountBalanceWallet, false),
            Triple(if (isEnglish) "QR Scan" else "QR স্ক্যান", Icons.Default.QrCodeScanner, false),
            Triple(if (isEnglish) "Inbox" else "ইনবক্স", Icons.Default.Mail, false)
        )

        items.forEach { (label, icon, selected) ->
            NavigationBarItem(
                selected = selected,
                onClick = { },
                icon = { Icon(icon, contentDescription = label) },
                label = { Text(label, fontSize = 10.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = BkashPink,
                    selectedTextColor = BkashPink,
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}


//preview
@Preview(
    name = "Home Screen",
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp"
)
@Composable
fun HomeScreenPreview() {
    var isEnglish by remember { mutableStateOf(false) }
    MaterialTheme {
        HomeScreen(
            isEnglish = isEnglish,
            onLanguageChange = { isEnglish = it }
        )
    }
}