package com.v1.acro.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import java.util.Locale

/**
 * ============================================================
 * AppSettings.kt
 * ============================================================
 *
 * NEW FILE (this update). App-wide user settings exposed via a CompositionLocal,
 * provided by AcroTheme (like LocalThemeState):
 *   - currency: USD or IDR — drives all money formatting (see money()/formatPrice())
 *   - uiScale: global UI zoom (1.0 = 100% ... 2.0 = 200%), applied in AcroTheme
 *              by overriding LocalDensity.
 *
 * Read anywhere with: val settings = LocalSettings.current
 * Change from the Settings screen: settings.setCurrency(...), settings.setUiScale(...)
 * ============================================================
 */

enum class Currency(val code: String, val symbol: String) {
    USD("USD", "$"),
    IDR("IDR", "Rp")
}

/** Available UI scale steps shown in Settings (100%–200%). */
val UI_SCALE_OPTIONS = listOf(1.0f, 1.25f, 1.5f, 1.75f, 2.0f)

data class SettingsState(
    val currency: Currency,
    val uiScale: Float,
    val setCurrency: (Currency) -> Unit,
    val setUiScale: (Float) -> Unit
)

val LocalSettings = compositionLocalOf<SettingsState> {
    error("SettingsState not provided — wrap with AcroTheme")
}

/** Format a money amount with the given currency, e.g. "$ 1,250.00" or "Rp 1,250.00". */
fun formatPrice(value: Double, currency: Currency): String =
    "${currency.symbol} ${String.format(Locale.US, "%,.2f", value)}"

/** Composable convenience — formats using the app's currently selected currency. */
@Composable
fun money(value: Double): String = formatPrice(value, LocalSettings.current.currency)
