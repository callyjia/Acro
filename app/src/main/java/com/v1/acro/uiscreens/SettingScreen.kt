package com.v1.acro.uiscreens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.v1.acro.ui.theme.Currency
import com.v1.acro.ui.theme.LocalSettings
import com.v1.acro.ui.theme.UI_SCALE_OPTIONS

/**
 * ============================================================
 * SettingScreen.kt
 * ============================================================
 *
 * NEW FILE (this update). App settings, backed by LocalSettings (provided by
 * AcroTheme):
 *   - Currency: USD or IDR — instantly changes every money label in the app.
 *   - UI Scale: 100% / 125% / 150% / 175% / 200% — zooms the whole UI via density.
 *
 * NAVIGATION:
 *   Route: "settings"
 *   Entry: DrawerMenu → Settings
 * ============================================================
 */
@Composable
fun SettingScreen(navController: NavController) {
    val settings = LocalSettings.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                text = "Settings",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        // Currency
        SettingSectionLabel("Currency")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Currency.entries.forEach { c ->
                SettingPill(
                    text = "${c.code} (${c.symbol})",
                    selected = settings.currency == c,
                    onClick = { settings.setCurrency(c) }
                )
            }
        }

        // UI scale
        SettingSectionLabel("UI Scale")
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            UI_SCALE_OPTIONS.forEach { scale ->
                SettingPill(
                    text = "${(scale * 100).toInt()}%",
                    selected = settings.uiScale == scale,
                    onClick = { settings.setUiScale(scale) }
                )
            }
        }
        Text(
            text = "Changes apply immediately across the whole app.",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
    }
}

@Composable
private fun SettingSectionLabel(text: String) {
    Text(
        text = text,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
    )
}

/** A selectable pill — filled when selected, outlined otherwise. */
@Composable
private fun SettingPill(text: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(
                if (selected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.surface
            )
            .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Text(
            text = text,
            color = if (selected) MaterialTheme.colorScheme.onPrimary
            else MaterialTheme.colorScheme.onSurface,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
