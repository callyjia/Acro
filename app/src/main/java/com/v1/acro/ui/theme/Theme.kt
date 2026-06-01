package com.v1.acro.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

/**
 * ============================================================
 * Theme.kt
 * ============================================================
 *
 * DESIGN INTENT:
 *   Light: blue-tinted surfaces, MidBlue brand text on cards
 *   Dark: neutral dark surfaces, white text on cards
 *   Brand (MidBlue) stays consistent in both modes
 *
 * STANDARD MATERIAL3 ROLES:
 *   background     → scaffold bg
 *   surface        → cards, drawer, QR button bg
 *   surfaceVariant → secondary surfaces (order btn, toggles)
 *   primary        → brand color (header, bottom bar, accents)
 *   onPrimary      → text/icons on primary
 *   onBackground   → text on scaffold
 *   onSurface      → text/icons on cards
 *   outline        → borders, dividers
 *   error          → out of stock, validation errors
 *
 * CUSTOM ROLES (non-standard):
 *   surfaceContainerHighest → used where a card needs
 *                             a stronger contrast from surface
 *
 * WHERE EACH IS USED:
 *   background      → Scaffold containerColor
 *   surface         → TaskCard bg, ProductCard bg, Drawer bg, QR button bg
 *   surfaceVariant  → OrderButton bg, toggle row bg, image placeholder
 *   primary         → Header bg, BottomBar bg, TodaySale card,
 *                      brand text/icons, borders, search focus
 *   onPrimary       → text on Header, BottomBar, TodaySale
 *   onSurface       → text/icons on cards (MidBlue in light, White in dark)
 *   outline         → dividers, unfocused borders, switch track
 *   error           → out of stock text
 * ============================================================
 */

// ── Theme state for dark mode toggle ──
data class ThemeState(
    val isDarkMode: Boolean,
    val toggleDarkMode: () -> Unit
)

val LocalThemeState = compositionLocalOf<ThemeState> {
    error("ThemeState not provided — wrap with AcroTheme")
}

// ── Color schemes ──
private val LightColorScheme = lightColorScheme(
    // Surfaces
    background = Color(0xFFEAF0FB),            // scaffold bg
    surface = Color(0xFFD8DEF3),              // btn, toggles, placeholders

    // Brand
    primary = MidBlue,                         // header, bottom bar, accents
    onPrimary = Color.White,                   // text on brand surfaces

    // Text on surfaces
    onBackground = MidBlue,                    // text on scaffold
    onSurface = MidBlue,                       // text/icons on cards
    onSurfaceVariant = MidBlue,                // text on order btn, toggles

    // Utility
    outline = Color(0xFFB0B0B0),               // borders, dividers
    outlineVariant = Color(0xFFD0D0D0),        // subtle borders
    error = Color(0xFFB3261E),                 // out of stock, errors
    onError = Color.White                      // text on error
)

private val DarkColorScheme = darkColorScheme(
    // Surfaces
    background = Color(0xFF242d46),            // scaffold bg
    surface = Color(0xFF2C396A),               // btn, toggles, placeholders

    // Brand
    primary = MidBlue,                         // header, bottom bar, accents
    onPrimary = Color.White,                   // text on brand surfaces

    // Text on surfaces
    onBackground = Color.White,                // text on scaffold
    onSurface = Color.White,                   // text/icons on cards
    onSurfaceVariant = Color.White,            // text on order btn, toggles

    // Utility
    outline = Color(0xFF444444),               // borders, dividers
    outlineVariant = Color(0xFF555555),        // subtle borders
    error = Color(0xFFFF6B6B),                 // brighter for dark bg
    onError = Color.White                      // text on error
)

// ── Theme wrapper ──
@Composable
fun AcroTheme(content: @Composable () -> Unit) {
    var isDarkMode by remember { mutableStateOf(false) }

    val themeState = remember(isDarkMode) {
        ThemeState(
            isDarkMode = isDarkMode,
            toggleDarkMode = { isDarkMode = !isDarkMode }
        )
    }

    val colorScheme = if (isDarkMode) DarkColorScheme else LightColorScheme

    CompositionLocalProvider(LocalThemeState provides themeState) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}