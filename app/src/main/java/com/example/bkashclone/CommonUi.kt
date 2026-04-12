package com.example.bkashclone


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.KeyboardReturn
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.tooling.preview.Preview


// Light version of the Language Toggle for white backgrounds

@Composable
fun LightLanguageToggle(
    isEnglish: Boolean,
    onLanguageChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .background(Color.White, RoundedCornerShape(20.dp))
            .border(1.dp, BkashPink, RoundedCornerShape(20.dp))
            .padding(2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // English Option
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(18.dp))
                .clickable { onLanguageChange(true) }
                .background(if (isEnglish) BkashPink else Color.Transparent)
                .padding(horizontal = 16.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Eng",
                color = if (isEnglish) Color.White else BkashPink,
                fontSize = 14.sp,
                fontWeight = if (isEnglish) FontWeight.Bold else FontWeight.Medium
            )
        }

        // Bangla Option
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(18.dp))
                .clickable { onLanguageChange(false) }
                .background(if (!isEnglish) BkashPink else Color.Transparent)
                .padding(horizontal = 16.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            // Inside LightLanguageToggle -> Bangla Option Box
            Text(
                text = "বাং",
                // CHANGE THIS: It should be White when NOT English (meaning Bangla is selected)
                color = if (!isEnglish) Color.White else BkashPink,
                fontSize = 14.sp,
                fontWeight = if (!isEnglish) FontWeight.Bold else FontWeight.Medium
            )
        }
    }
}

// Reusable Top Bar for White Screens
// Reusable Top Bar for White Screens
@Composable
fun BkashTopBar(
    onBackClick: () -> Unit,
    showLanguageToggle: Boolean = false,
    isEnglish: Boolean = false,
    onLanguageChange: (Boolean) -> Unit = {},
    rightText: String? = null,
    onRightTextClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = 8.dp, end = 16.dp), // Adjusted padding slightly for the IconButton
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // FIX: Wrap the Icon in an IconButton to make the touch target bigger and add a ripple effect!
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = BkashPink,
                modifier = Modifier.size(28.dp)
            )
        }

        if (showLanguageToggle) {
            LightLanguageToggle(isEnglish, onLanguageChange)
        } else if (rightText != null) {
            Text(
                text = rightText,
                color = BkashPink,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .clickable { onRightTextClick() }
                    .padding(8.dp) // Makes the text easier to tap too
            )
        }
    }
}

// Reusable Bottom Pink Button
@Composable
fun BkashBottomButton(
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(BkashPink)
            .clickable { onClick() }
            .padding(16.dp),
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterStart)
        )
        Icon(
            imageVector = Icons.Default.ArrowForward,
            contentDescription = "Next",
            tint = Color.White,
            modifier = Modifier.align(Alignment.CenterEnd)
        )
    }
}





// Custom bKash Number Pad
@Composable
fun BkashNumberPad(
    isEnglish: Boolean,
    isNextEnabled: Boolean = true,
    buttonText: String? = null, // ✅ Added to support "Confirm" or "Next"
    onNumberClick: (String) -> Unit,
    onBackspaceClick: () -> Unit,
    onNextClick: () -> Unit
) {
    // Default text if none is provided
    val defaultText = if (isEnglish) "Next" else "পরবর্তী"
    val displayText = buttonText ?: defaultText

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF3F4F6))
    ) {
        // 1. The Top Bar (Next/Confirm Button)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                // ✅ Pink when enabled, Off-White/Light Gray when disabled
                .background(if (isNextEnabled) BkashPink else Color(0xFFE2E2E2))
                .then(if (isNextEnabled) Modifier.clickable { onNextClick() } else Modifier)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = displayText,
                color = if (isNextEnabled) Color.White else Color.Gray, // Text gets dim when disabled
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Next",
                tint = if (isNextEnabled) Color.White else Color.Gray
            )
        }

        // 2. The Number Grid
        val keys = listOf(
            listOf("1", "2", "3"),
            listOf("4", "5", "6"),
            listOf("7", "8", "9"),
            listOf("del", "0", "enter")
        )

        keys.forEach { row ->
            Row(modifier = Modifier.fillMaxWidth()) {
                row.forEach { key ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(65.dp)
                            .clickable(enabled = key.isNotEmpty()) {
                                when (key) {
                                    "del" -> onBackspaceClick()
                                    "enter" -> if (isNextEnabled) onNextClick()
                                    else -> onNumberClick(key)
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        when (key) {
                            "del" -> Icon(
                                imageVector = Icons.Default.Backspace,
                                contentDescription = "Delete",
                                tint = Color.Gray,
                                modifier = Modifier.size(28.dp)
                            )
                            "enter" -> Icon(
                                imageVector = Icons.Default.KeyboardReturn,
                                contentDescription = "Enter",
                                tint = Color.White,
                                modifier = Modifier
                                    .background(
                                        if (isNextEnabled) BkashPink else Color.Gray,
                                        CircleShape
                                    )
                                    .padding(8.dp)
                                    .size(20.dp)
                            )
                            else -> Text(
                                text = key,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.DarkGray
                            )
                        }
                    }
                }
            }
        }
    }
}




// --- COMPONENT PREVIEWS ---

@Preview(showBackground = true, name = "Light Toggle - English")
@Composable
fun PreviewLightToggleEng() {

    Box(Modifier
        .padding(20.dp)
        .background(Color.White)) {
        LightLanguageToggle(isEnglish = true, onLanguageChange = {})
    }
}

@Preview(showBackground = true, name = "Top Bar - With Toggle")
@Composable
fun PreviewTopBarToggle() {
    BkashTopBar(
        onBackClick = {},
        showLanguageToggle = true,
        isEnglish = true,
        onLanguageChange = {}
    )
}

@Preview(showBackground = true, name = "Top Bar - With Skip Action")
@Composable
fun PreviewTopBarSkip() {
    BkashTopBar(
        onBackClick = {},
        rightText = "Skip",
        onRightTextClick = {}
    )
}

@Preview(showBackground = true, name = "Bottom Pink Button")
@Composable
fun PreviewBottomBtn() {
    BkashBottomButton(
        text = "পরবর্তী",
        onClick = {}
    )
}

@Preview(showBackground = true, name = "Number Pad - Enabled")
@Composable
fun PreviewNumPadEnabled() {
    BkashNumberPad(
        isEnglish = true,
        isNextEnabled = true,
        onNumberClick = {},
        onBackspaceClick = {},
        onNextClick = {}
    )
}

@Preview(showBackground = true, name = "Number Pad - Disabled & Bangla")
@Composable
fun PreviewNumPadDisabled() {
    BkashNumberPad(
        isEnglish = false,
        isNextEnabled = false,
        buttonText = "নিশ্চিত করুন",
        onNumberClick = {},
        onBackspaceClick = {},
        onNextClick = {}
    )
}