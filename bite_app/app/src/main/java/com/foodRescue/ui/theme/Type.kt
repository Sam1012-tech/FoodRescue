// bite_app/app/src/main/java/com/foodRescue/ui/theme/Type.kt
package com.foodRescue.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Editorial typography for the warm "Post Food" screens.
 *
 * Headline font: system serif (falls back to Default if no bundled font).
 * Body font: system sans-serif for maximum legibility.
 *
 * If you want to bundle a custom serif (e.g. Playfair Display or Lora),
 * add the TTF to res/font/ and replace FontFamily.Serif below.
 */

// Use the platform serif for headlines — feels editorial without bundling a font file.
// Swap to a bundled font (e.g. Font(R.font.playfair_display)) when assets are added.
val SerifHeadline = FontFamily.Serif
val SansBody      = FontFamily.Default   // system sans-serif

val WarmTypography = Typography(
    // Large headline — screen title, e.g. "Share your food"
    headlineLarge = TextStyle(
        fontFamily = SerifHeadline,
        fontWeight = FontWeight.Bold,
        fontSize   = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = (-0.5).sp
    ),
    // Medium headline — section titles
    headlineMedium = TextStyle(
        fontFamily = SerifHeadline,
        fontWeight = FontWeight.Bold,
        fontSize   = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = (-0.3).sp
    ),
    // Small headline — card titles
    headlineSmall = TextStyle(
        fontFamily = SerifHeadline,
        fontWeight = FontWeight.Medium,
        fontSize   = 20.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    // Body — primary text
    bodyLarge = TextStyle(
        fontFamily = SansBody,
        fontWeight = FontWeight.Normal,
        fontSize   = 16.sp,
        lineHeight = 24.sp
    ),
    // Body — secondary/description text
    bodyMedium = TextStyle(
        fontFamily = SansBody,
        fontWeight = FontWeight.Normal,
        fontSize   = 14.sp,
        lineHeight = 20.sp
    ),
    // Labels / helper text
    labelLarge = TextStyle(
        fontFamily = SansBody,
        fontWeight = FontWeight.Medium,
        fontSize   = 14.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontFamily = SansBody,
        fontWeight = FontWeight.Normal,
        fontSize   = 12.sp,
        letterSpacing = 0.2.sp
    ),
    // Button text
    titleMedium = TextStyle(
        fontFamily = SansBody,
        fontWeight = FontWeight.SemiBold,
        fontSize   = 16.sp,
        letterSpacing = 0.5.sp
    )
)
