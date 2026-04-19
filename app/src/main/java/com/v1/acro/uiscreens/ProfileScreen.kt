package com.v1.acro.uiscreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.v1.acro.R
import com.v1.acro.ui.theme.Violet


@Composable
fun ProfileScreen() {
    Box(
        modifier = Modifier.fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.aklogo),
                contentDescription = null,
                modifier = Modifier.size(96.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Store Management System",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(24.dp))

            MenuOption(
                icon = Icons.Default.Person,
                title = "Personal Details"
            )
            MenuOption(
                icon = Icons.Default.LocationOn,
                title = "Manage Address & Payment"
            )
            MenuOption(
                icon = Icons.Default.AccountCircle,
                title = "Accounts"
            )
            MenuOption(
                icon = Icons.Default.Settings,
                title = "Settings"
            )

            Card(
                modifier = Modifier.fillMaxWidth()
                    .clickable{},
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Violet
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.ExitToApp,
                        contentDescription = null,
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = "Logout",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold)
                }
            }
        }
        FloatingActionButton(
            onClick = {},
            modifier = Modifier.align(Alignment.BottomEnd),
            containerColor = Violet,
            shape = CircleShape
        ) {
            Icon(
                Icons.Default.Message,
                contentDescription = "Chat",
                tint = Color.White
            )
        }
    }
}

@Composable
fun MenuOption(icon: ImageVector,
               title: String
) {
    Card(
        modifier = Modifier.fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable{},
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(3.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon,
                contentDescription = null,
                tint = Violet
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}