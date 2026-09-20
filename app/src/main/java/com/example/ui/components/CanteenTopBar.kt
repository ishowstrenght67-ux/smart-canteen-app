package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Badge
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.CanteenScreen
import com.example.ui.UserProfile
import com.example.ui.UserRole

@Composable
fun CanteenTopBar(
  userProfile: UserProfile,
  darkTheme: Boolean,
  onToggleTheme: () -> Unit,
  onWalletClick: () -> Unit,
  onRoleToggle: () -> Unit,
  onProfileClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier.fillMaxWidth(),
    color = MaterialTheme.colorScheme.surface,
    tonalElevation = 3.dp,
    shadowElevation = 2.dp
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .statusBarsPadding()
        .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // App Title & Role
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onProfileClick() }
            .padding(4.dp)
        ) {
          Box(
            modifier = Modifier
              .size(40.dp)
              .clip(RoundedCornerShape(10.dp))
              .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = if (userProfile.role == UserRole.ADMIN) Icons.Default.AdminPanelSettings else Icons.Default.Restaurant,
              contentDescription = "Canteen Logo",
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(24.dp)
            )
          }

          Spacer(modifier = Modifier.width(10.dp))

          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "Smart Canteen",
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 17.sp
                )
              )
              Spacer(modifier = Modifier.width(6.dp))
              Surface(
                shape = RoundedCornerShape(4.dp),
                color = if (userProfile.role == UserRole.ADMIN) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.secondaryContainer
              ) {
                Text(
                  text = if (userProfile.role == UserRole.ADMIN) "ADMIN" else "CAMPUS",
                  modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp),
                  style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (userProfile.role == UserRole.ADMIN) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onSecondaryContainer
                  )
                )
              }
            }
            Text(
              text = if (userProfile.role == UserRole.ADMIN) "Kitchen & Staff Panel" else "Hi, ${userProfile.name.substringBefore(" ")}",
              style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )
          }
        }

        // Action controls: Wallet pill, Theme toggle, Mode switch
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          // Wallet Quick Balance pill (only shown for students)
          if (userProfile.role == UserRole.STUDENT) {
            Surface(
              shape = RoundedCornerShape(20.dp),
              color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.85f),
              modifier = Modifier
                .testTag("wallet_topbar_pill")
                .clip(RoundedCornerShape(20.dp))
                .clickable { onWalletClick() }
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                  imageVector = Icons.Default.AccountBalanceWallet,
                  contentDescription = "Wallet",
                  tint = MaterialTheme.colorScheme.onSecondaryContainer,
                  modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = "$${String.format("%.2f", userProfile.walletBalance)}",
                  style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                  )
                )
              }
            }
          }

          // Role Switch Button
          IconButton(
            onClick = onRoleToggle,
            modifier = Modifier
              .testTag("role_switch_button")
              .size(38.dp)
          ) {
            Icon(
              imageVector = if (userProfile.role == UserRole.ADMIN) Icons.Default.School else Icons.Default.AdminPanelSettings,
              contentDescription = "Switch Mode",
              tint = MaterialTheme.colorScheme.primary
            )
          }

          // Dark/Light Theme Button
          IconButton(
            onClick = onToggleTheme,
            modifier = Modifier
              .testTag("theme_toggle_button")
              .size(38.dp)
          ) {
            Icon(
              imageVector = if (darkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
              contentDescription = "Toggle Dark Mode",
              tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }
    }
  }
}
