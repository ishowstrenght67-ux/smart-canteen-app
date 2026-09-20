package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.OrderEntity
import com.example.ui.CanteenScreen
import com.example.ui.CanteenViewModel
import com.example.ui.UserRole
import com.example.ui.components.CanteenBottomNav
import com.example.ui.components.CanteenTopBar
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.CartScreen
import com.example.ui.screens.LiveBoardScreen
import com.example.ui.screens.MenuScreen
import com.example.ui.screens.OrdersScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.VegGreen

class MainActivity : ComponentActivity() {

  private val viewModel: CanteenViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      val isDarkTheme by viewModel.darkTheme.collectAsState()

      MyApplicationTheme(darkTheme = isDarkTheme) {
        SmartCanteenApp(viewModel = viewModel)
      }
    }
  }
}

@Composable
fun SmartCanteenApp(viewModel: CanteenViewModel) {
  val userProfile by viewModel.userProfile.collectAsState()
  val currentScreen by viewModel.currentScreen.collectAsState()
  val isDarkTheme by viewModel.darkTheme.collectAsState()
  val cart by viewModel.cart.collectAsState()
  val filteredMenuItems by viewModel.filteredMenuItems.collectAsState()
  val allMenuItems by viewModel.allMenuItems.collectAsState()
  val allOrders by viewModel.allOrders.collectAsState()
  val studentOrders by viewModel.studentOrders.collectAsState()
  val readyTokens by viewModel.readyTokens.collectAsState()
  val preparingTokens by viewModel.preparingTokens.collectAsState()
  val selectedCategory by viewModel.selectedCategory.collectAsState()
  val searchQuery by viewModel.searchQuery.collectAsState()
  val vegOnlyFilter by viewModel.vegOnlyFilter.collectAsState()
  val inStockOnlyFilter by viewModel.inStockOnlyFilter.collectAsState()
  val recentlyPlacedOrder by viewModel.recentlyPlacedOrder.collectAsState()
  val notificationMessage by viewModel.notificationMessage.collectAsState()

  val snackbarHostState = remember { SnackbarHostState() }

  LaunchedEffect(notificationMessage) {
    notificationMessage?.let { msg ->
      snackbarHostState.showSnackbar(msg)
      viewModel.dismissNotification()
    }
  }

  val cartCount = cart.values.sumOf { it.quantity }
  val activeOrdersCount = studentOrders.count { it.status in listOf("PLACED", "PREPARING", "READY") }

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    topBar = {
      CanteenTopBar(
        userProfile = userProfile,
        darkTheme = isDarkTheme,
        onToggleTheme = { viewModel.toggleDarkTheme() },
        onWalletClick = { viewModel.setScreen(CanteenScreen.PROFILE) },
        onRoleToggle = {
          viewModel.switchUserRole(
            if (userProfile.role == UserRole.STUDENT) UserRole.ADMIN else UserRole.STUDENT
          )
        },
        onProfileClick = { viewModel.setScreen(CanteenScreen.PROFILE) }
      )
    },
    bottomBar = {
      CanteenBottomNav(
        currentScreen = currentScreen,
        cartCount = cartCount,
        activeOrderCount = activeOrdersCount,
        userRole = userProfile.role,
        onScreenSelect = { viewModel.setScreen(it) }
      )
    },
    snackbarHost = { SnackbarHost(snackbarHostState) }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      when (currentScreen) {
        CanteenScreen.MENU -> {
          MenuScreen(
            menuItems = filteredMenuItems,
            cart = cart,
            selectedCategory = selectedCategory,
            searchQuery = searchQuery,
            vegOnly = vegOnlyFilter,
            inStockOnly = inStockOnlyFilter,
            onCategorySelected = { viewModel.setCategory(it) },
            onSearchChanged = { viewModel.setSearchQuery(it) },
            onToggleVeg = { viewModel.toggleVegFilter() },
            onToggleInStock = { viewModel.toggleInStockFilter() },
            onAddToCart = { viewModel.addToCart(it) },
            onUpdateCartQty = { id, delta -> viewModel.updateCartQuantity(id, delta) }
          )
        }

        CanteenScreen.CART -> {
          CartScreen(
            cart = cart,
            userProfile = userProfile,
            onUpdateQty = { id, delta -> viewModel.updateCartQuantity(id, delta) },
            onRemoveItem = { viewModel.removeFromCart(it) },
            onUpdateNote = { id, note -> viewModel.updateCartNote(id, note) },
            onPlaceOrder = { pickup, payment, notes ->
              viewModel.placeOrder(pickup, payment, notes)
            },
            onBrowseMenu = { viewModel.setScreen(CanteenScreen.MENU) },
            onTopUpWallet = { viewModel.topUpWallet(it) }
          )
        }

        CanteenScreen.ORDERS -> {
          OrdersScreen(
            orders = studentOrders,
            onOrderAgain = { viewModel.setScreen(CanteenScreen.MENU) }
          )
        }

        CanteenScreen.LIVE_BOARD -> {
          LiveBoardScreen(
            readyOrders = readyTokens,
            preparingOrders = preparingTokens
          )
        }

        CanteenScreen.ADMIN -> {
          AdminDashboardScreen(
            orders = allOrders,
            menuItems = allMenuItems,
            onUpdateOrderStatus = { orderId, status -> viewModel.updateOrderStatus(orderId, status) },
            onToggleAvailability = { itemId, avail -> viewModel.toggleItemAvailability(itemId, avail) },
            onUpdatePrice = { itemId, price -> viewModel.updateItemPrice(itemId, price) },
            onAddNewItem = { name, desc, price, cat, isVeg, prep, cals, badge ->
              viewModel.addNewMenuItem(name, desc, price, cat, isVeg, prep, cals, badge)
            }
          )
        }

        CanteenScreen.PROFILE -> {
          ProfileScreen(
            userProfile = userProfile,
            darkTheme = isDarkTheme,
            onToggleTheme = { viewModel.toggleDarkTheme() },
            onTopUpWallet = { viewModel.topUpWallet(it) },
            onSwitchRole = { viewModel.switchUserRole(it) },
            onLoginStudent = { name, id, dept, bal -> viewModel.loginStudent(name, id, dept, bal) },
            onLogoutStudent = { viewModel.logoutStudent() }
          )
        }
      }
    }
  }

  // DIALOG: ORDER PLACED CELEBRATION & TOKEN DISPLAY
  if (recentlyPlacedOrder != null) {
    OrderCelebrationDialog(
      order = recentlyPlacedOrder!!,
      onDismiss = { viewModel.dismissOrderCelebration() },
      onTrackOrder = {
        viewModel.dismissOrderCelebration()
        viewModel.setScreen(CanteenScreen.ORDERS)
      }
    )
  }
}

@Composable
fun OrderCelebrationDialog(
  order: OrderEntity,
  onDismiss: () -> Unit,
  onTrackOrder: () -> Unit
) {
  AlertDialog(
    onDismissRequest = onDismiss,
    modifier = Modifier.testTag("order_celebration_dialog"),
    title = {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.Celebration, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(8.dp))
        Text("Order Placed Successfully!", fontWeight = FontWeight.Bold)
      }
    },
    text = {
      Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(
          text = "YOUR PICKUP TOKEN",
          style = MaterialTheme.typography.labelSmall.copy(
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.2.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Prominent Token Badge
        Surface(
          shape = RoundedCornerShape(16.dp),
          color = MaterialTheme.colorScheme.primaryContainer,
          border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
        ) {
          Text(
            text = order.tokenNumber,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
            style = MaterialTheme.typography.displayMedium.copy(
              fontWeight = FontWeight.ExtraBold,
              fontFamily = FontFamily.Monospace,
              color = MaterialTheme.colorScheme.primary
            )
          )
        }

        Spacer(modifier = Modifier.height(14.dp))
        Text(
          text = "Estimated Pickup: ${order.pickupTime}",
          style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "Head to Counter 2 when your token is announced on the Token Display Board.",
          style = MaterialTheme.typography.bodySmall.copy(
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 16.sp
          )
        )

        Spacer(modifier = Modifier.height(10.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(10.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text("Total Paid:", style = MaterialTheme.typography.bodyMedium)
          Text(
            "₹${String.format("%.2f", order.totalAmount)}",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
          )
        }
      }
    },
    confirmButton = {
      Button(
        onClick = onTrackOrder,
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.testTag("track_order_btn")
      ) {
        Icon(Icons.Default.ConfirmationNumber, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text("Track Token Live")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Done")
      }
    }
  )
}

// Template greeting function for test verification
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
  Text(text = "Hello $name!", modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
  MyApplicationTheme { Greeting("Android") }
}
