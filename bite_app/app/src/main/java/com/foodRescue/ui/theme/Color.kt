// bite_app/app/src/main/java/com/foodRescue/ui/theme/Color.kt
package com.foodRescue.ui.theme

import androidx.compose.ui.graphics.Color

// ── Original dark / neon palette (used by Login, DonorHome, NGO, Volunteer) ──
val PrimaryGreen  = Color(0xFF1F3A33)
val NeonGreen     = Color(0xFF00FF94)
val AIDeepBlue    = Color(0xFF0A192F)
val GlassWhite    = Color(0x33FFFFFF)
val UrgencyRed    = Color(0xFFFF4B4B)
val WarningYellow = Color(0xFFFFD600)
val DarkBg        = Color(0xFF050B13)

// ── Warm editorial palette — used by Post Food metadata form ─────────────────
// Background
val Cream         = Color(0xFFF9F5F0)   // off-white / cream page background
val PaperWhite    = Color(0xFFFFFDF9)   // card surface
val DividerBeige  = Color(0xFFE8E0D6)   // subtle dividers

// Typography
val InkBrown      = Color(0xFF2B1F14)   // headlines — dark warm brown
val InkMid        = Color(0xFF5C4A38)   // body / secondary text
val InkLight      = Color(0xFF9C8878)   // placeholders / labels

// Accents
val SageGreen     = Color(0xFF6B8F71)   // primary CTA, veg badge
val CoralRed      = Color(0xFFD4694A)   // non-veg badge, high urgency
val AmberYellow   = Color(0xFFC8922A)   // medium urgency, warning
val SoftSage      = Color(0xFFDDE8DE)   // veg chip fill (light sage)
val SoftCoral     = Color(0xFFF5E0DA)   // non-veg chip fill (light coral)
val SoftAmber     = Color(0xFFF5ECDA)   // medium urgency chip fill

// Urgency chip colors (label / fill pairs)
val UrgencyLow    = Color(0xFF6B8F71)   // sage — calm
val UrgencyMedium = Color(0xFFC8922A)   // amber — caution
val UrgencyHigh   = Color(0xFFD4694A)   // coral — urgent

// Allergen / misc chips
val ChipFill      = Color(0xFFEEE8E2)   // unselected chip background
val ChipSelected  = Color(0xFF2B1F14)   // selected chip background (ink)
val ChipTextSel   = Color(0xFFF9F5F0)   // selected chip text (cream)
