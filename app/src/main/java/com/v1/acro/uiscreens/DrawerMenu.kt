package com.v1.acro.uiscreens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.v1.acro.ui.theme.*

/**
 * ============================================================
 * DrawerMenu.kt
 * ============================================================
 *
 * Right-side drawer menu accessed via Header menu button.
 *
 * STRUCTURE:
 *   DrawerMenu
 *     ├── Store Info        → store name and address
 *     ├── Dark/Light toggle → direct switch via LocalThemeState
 *     ├── Settings          → navigates to settings screen
 *     └── About             → navigates to about screen
 *
 * COLORS:
 *   - All colors use MaterialTheme for auto dark/light switching
 *   - Toggle uses primary color for brand consistency
 *
 * UPDATE NOTE: unchanged this update. Dark/light toggle works via LocalThemeState.
 *
 * ============================================================
 */

@Composable
fun DrawerMenu(navController: NavController) {
    val themeState = LocalThemeState.current

    ModalDrawerSheet(
        modifier = Modifier
            .clip(RectangleShape)
            .fillMaxWidth(0.6f),
        drawerShape = RectangleShape,
        drawerContainerColor = MaterialTheme.colorScheme.background
    ) {
        Box(modifier = Modifier.fillMaxSize()){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            )
            {
                // Store Info
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Acro",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )


                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                Spacer(modifier = Modifier.height(16.dp))

                // Dark Mode Toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(shape = RoundedCornerShape(4.dp), color = MaterialTheme.colorScheme.surface)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = if (themeState.isDarkMode) Icons.Default.DarkMode
                            else Icons.Default.LightMode,
                            contentDescription = "Theme toggle",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = if (themeState.isDarkMode) "Dark Mode" else "Light Mode",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Switch(
                        checked = themeState.isDarkMode,
                        onCheckedChange = { themeState.toggleDarkMode() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                            checkedTrackColor = MaterialTheme.colorScheme.primary,
                            uncheckedThumbColor = MaterialTheme.colorScheme.primary,
                            uncheckedTrackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Menu Items
                DrawerMenuItem(
                    icon = Icons.Default.Settings,
                    label = "Settings",
                    onClick = { navController.navigate("settings") }
                )
                DrawerMenuItem(
                    icon = Icons.Default.Info,
                    label = "About",
                    onClick = { navController.navigate("about") }
                )
            }
                // App version
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Acro v1.0.0",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(20.dp)
                )
        }

    }
}

/**
 * Reusable drawer menu item
 */
@Composable
fun DrawerMenuItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = label,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}