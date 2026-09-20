package com.example.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.Kitchen
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.ui.CanteenScreen
import com.example.ui.UserRole

@Composable
fun CanteenBottomNav(
  currentScreen: CanteenScreen,
  cartCount: Int,
  activeOrderCount: Int,
  userRole: UserRole,
  onScreenSelect: (CanteenScreen) -> Unit,
  modifier: Modifier = Modifier
) {
  NavigationBar(
    modifier = modifier,
    containerColor = MaterialTheme.colorScheme.surface,
    tonalElevation = 6.dp
  ) {
    // Menu Tab
    NavigationBarItem(
      selected = currentScreen == CanteenScreen.MENU,
      onClick = { onScreenSelect(CanteenScreen.MENU) },
      icon = {
        Icon(
          imageVector = Icons.Default.Fastfood,
          contentDescription = "Menu"
        )
      },
      label = { Text("Menu") },
      modifier = Modifier.testTag("nav_menu_tab")
    )

    // Cart Tab
    NavigationBarItem(
      selected = currentScreen == CanteenScreen.CART,
      onClick = { onScreenSelect(CanteenScreen.CART) },
      icon = {
        BadgedBox(badge = {
          if (cartCount > 0) {
            Badge(containerColor = MaterialTheme.colorScheme.primary) {
              Text("$cartCount")
            }
          }
        }) {
          Icon(
            imageVector = Icons.Default.ShoppingCart,
            contentDescription = "Cart"
          )
        }
      },
      label = { Text("Cart") },
      modifier = Modifier.testTag("nav_cart_tab")
    )

    // Orders Tab
    NavigationBarItem(
      selected = currentScreen == CanteenScreen.ORDERS,
      onClick = { onScreenSelect(CanteenScreen.ORDERS) },
      icon = {
        BadgedBox(badge = {
          if (activeOrderCount > 0) {
            Badge(containerColor = MaterialTheme.colorScheme.tertiary) {
              Text("$activeOrderCount")
            }
          }
        }) {
          Icon(
            imageVector = Icons.Default.ReceiptLong,
            contentDescription = "My Orders"
          )
        }
      },
      label = { Text("My Orders") },
      modifier = Modifier.testTag("nav_orders_tab")
    )

    // Token Board Tab
    NavigationBarItem(
      selected = currentScreen == CanteenScreen.LIVE_BOARD,
      onClick = { onScreenSelect(CanteenScreen.LIVE_BOARD) },
      icon = {
        Icon(
          imageVector = Icons.Default.Tv,
          contentDescription = "Token Display"
        )
      },
      label = { Text("Token Board") },
      modifier = Modifier.testTag("nav_live_board_tab")
    )

    // Admin / Kitchen tab if admin, else Profile / Student Card
    if (userRole == UserRole.ADMIN) {
      NavigationBarItem(
        selected = currentScreen == CanteenScreen.ADMIN,
        onClick = { onScreenSelect(CanteenScreen.ADMIN) },
        icon = {
          Icon(
            imageVector = Icons.Default.Kitchen,
            contentDescription = "Kitchen Admin"
          )
        },
        label = { Text("Admin") },
        modifier = Modifier.testTag("nav_admin_tab"),
        colors = NavigationBarItemDefaults.colors(
          selectedIconColor = MaterialTheme.colorScheme.error,
          selectedTextColor = MaterialTheme.colorScheme.error
        )
      )
    } else {
      NavigationBarItem(
        selected = currentScreen == CanteenScreen.PROFILE,
        onClick = { onScreenSelect(CanteenScreen.PROFILE) },
        icon = {
          Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Profile"
          )
        },
        label = { Text("Campus ID") },
        modifier = Modifier.testTag("nav_profile_tab")
      )
    }
  }
}
