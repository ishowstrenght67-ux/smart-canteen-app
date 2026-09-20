package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Kitchen
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MenuItemEntity
import com.example.data.OrderEntity
import com.example.ui.theme.TokenGold
import com.example.ui.theme.VegGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
  orders: List<OrderEntity>,
  menuItems: List<MenuItemEntity>,
  onUpdateOrderStatus: (String, String) -> Unit,
  onToggleAvailability: (String, Boolean) -> Unit,
  onUpdatePrice: (String, Double) -> Unit,
  onAddNewItem: (String, String, Double, String, Boolean, String, Int, String) -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedTab by remember { mutableIntStateOf(0) }
  val tabTitles = listOf("Live Orders", "Menu & Stock", "Analytics")

  // State for Add Item Dialog
  var showAddDialog by remember { mutableStateOf(false) }

  // State for Price Edit Dialog
  var editingItem by remember { mutableStateOf<MenuItemEntity?>(null) }
  var newPriceText by remember { mutableStateOf("") }

  // Orders filters
  var orderFilter by remember { mutableStateOf("All") }

  val totalRevenue = orders.filter { it.status == "COMPLETED" || it.status == "READY" }.sumOf { it.totalAmount }
  val activeKitchenOrders = orders.filter { it.status == "PLACED" || it.status == "PREPARING" }
  val readyOrders = orders.filter { it.status == "READY" }

  Box(modifier = modifier.fillMaxSize()) {
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .testTag("admin_dashboard_screen"),
      contentPadding = PaddingValues(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Header & KPI Metrics Row
      item {
        Column {
          Text(
            text = "Canteen Admin & Kitchen Panel",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
          )
          Text(
            text = "Manage live kitchen workflow, item stock, prices, and revenue",
            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
          )
          Spacer(modifier = Modifier.height(14.dp))

          // 4 Metric cards in a 2x2 grid
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            MetricCard(
              title = "Today's Revenue",
              value = "₹${String.format("%.2f", totalRevenue)}",
              icon = Icons.Default.CurrencyRupee,
              color = MaterialTheme.colorScheme.primary,
              modifier = Modifier.weight(1f)
            )
            MetricCard(
              title = "Total Orders",
              value = "${orders.size}",
              icon = Icons.Default.RestaurantMenu,
              color = MaterialTheme.colorScheme.secondary,
              modifier = Modifier.weight(1f)
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            MetricCard(
              title = "Cooking Queue",
              value = "${activeKitchenOrders.size}",
              icon = Icons.Default.Kitchen,
              color = TokenGold,
              modifier = Modifier.weight(1f)
            )
            MetricCard(
              title = "Ready for Pickup",
              value = "${readyOrders.size}",
              icon = Icons.Default.CheckCircle,
              color = VegGreen,
              modifier = Modifier.weight(1f)
            )
          }
        }
      }

      // Tabs: Live Orders / Menu Management / Analytics
      item {
        PrimaryTabRow(
          selectedTabIndex = selectedTab,
          modifier = Modifier.clip(RoundedCornerShape(12.dp))
        ) {
          tabTitles.forEachIndexed { index, title ->
            Tab(
              selected = selectedTab == index,
              onClick = { selectedTab = index },
              text = { Text(title, fontWeight = FontWeight.Bold) },
              modifier = Modifier.testTag("admin_tab_$index")
            )
          }
        }
      }

      // TAB 0: LIVE ORDERS QUEUE
      if (selectedTab == 0) {
        // Quick filter pills for orders
        item {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            listOf("All", "PLACED", "PREPARING", "READY", "COMPLETED").forEach { filter ->
              val isSelected = orderFilter == filter
              OutlinedButton(
                onClick = { orderFilter = filter },
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                  containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                ),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                modifier = Modifier.testTag("filter_order_$filter")
              ) {
                Text(
                  text = when (filter) {
                    "All" -> "All (${orders.size})"
                    "PLACED" -> "New"
                    "PREPARING" -> "Cooking"
                    "READY" -> "Ready"
                    "COMPLETED" -> "Done"
                    else -> filter
                  },
                  style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                )
              }
            }
          }
        }

        val filteredOrders = if (orderFilter == "All") {
          orders
        } else {
          orders.filter { it.status == orderFilter }
        }

        if (filteredOrders.isEmpty()) {
          item {
            Card(
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(14.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(32.dp),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = "No orders under this status.",
                  style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )
              }
            }
          }
        } else {
          items(filteredOrders, key = { it.orderId }) { order ->
            AdminOrderCard(
              order = order,
              onUpdateStatus = { newStatus -> onUpdateOrderStatus(order.orderId, newStatus) }
            )
          }
        }
      }

      // TAB 1: MENU & AVAILABILITY MANAGEMENT
      if (selectedTab == 1) {
        item {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Live Stock & Price Controls (${menuItems.size} Items)",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Button(
              onClick = { showAddDialog = true },
              shape = RoundedCornerShape(10.dp),
              contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
              modifier = Modifier.testTag("admin_add_food_button")
            ) {
              Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Add Food Item", style = MaterialTheme.typography.labelMedium)
            }
          }
        }

        items(menuItems, key = { it.id }) { item ->
          AdminMenuItemCard(
            item = item,
            onToggleStock = { isAvailable -> onToggleAvailability(item.id, isAvailable) },
            onEditPrice = {
              editingItem = item
              newPriceText = String.format("%.2f", item.price)
            }
          )
        }
      }

      // TAB 2: ANALYTICS & INSIGHTS
      if (selectedTab == 2) {
        item {
          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.TrendingUp, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = "Canteen Rush Hours & Trends",
                  style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
              }
              Spacer(modifier = Modifier.height(12.dp))

              AnalyticsBar(label = "Breakfast (8:30 - 10:30 AM)", percent = 0.65f, count = "65 orders")
              Spacer(modifier = Modifier.height(8.dp))
              AnalyticsBar(label = "Lunch Break Peak (12:30 - 2:00 PM)", percent = 0.95f, count = "142 orders (Busiest)")
              Spacer(modifier = Modifier.height(8.dp))
              AnalyticsBar(label = "Evening Tea & Snacks (4:00 - 5:30 PM)", percent = 0.75f, count = "80 orders")
              Spacer(modifier = Modifier.height(8.dp))
              AnalyticsBar(label = "Dinner / Hostel Takeaway (7:00 - 8:30 PM)", percent = 0.40f, count = "38 orders")
            }
          }
        }

        item {
          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Text(
                text = "Most Ordered Campus Favorites",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
              )
              Spacer(modifier = Modifier.height(10.dp))
              PopularItemRow(rank = 1, name = "Signature Cold Coffee Float", count = 84, revenue = "₹5,040")
              PopularItemRow(rank = 2, name = "Crispy Samosa Pav Duo", count = 76, revenue = "₹3,040")
              PopularItemRow(rank = 3, name = "Paneer Butter Masala Meal Box", count = 58, revenue = "₹8,120")
              PopularItemRow(rank = 4, name = "Student Rush Hour Saver Combo", count = 52, revenue = "₹6,240")
            }
          }
        }
      }

      item {
        Spacer(modifier = Modifier.height(70.dp))
      }
    }
  }

  // DIALOG: EDIT ITEM PRICE
  if (editingItem != null) {
    AlertDialog(
      onDismissRequest = { editingItem = null },
      title = { Text("Update Price for ${editingItem?.name}") },
      text = {
        Column {
          Text("Enter new price in Indian Rupees (₹):")
          Spacer(modifier = Modifier.height(8.dp))
          OutlinedTextField(
            value = newPriceText,
            onValueChange = { newPriceText = it },
            prefix = { Text("₹") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            modifier = Modifier.testTag("edit_price_input")
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            val priceVal = newPriceText.toDoubleOrNull()
            if (priceVal != null && priceVal >= 0 && editingItem != null) {
              onUpdatePrice(editingItem!!.id, priceVal)
            }
            editingItem = null
          },
          modifier = Modifier.testTag("save_price_button")
        ) {
          Text("Save Price")
        }
      },
      dismissButton = {
        TextButton(onClick = { editingItem = null }) {
          Text("Cancel")
        }
      }
    )
  }

  // DIALOG: ADD NEW MENU ITEM
  if (showAddDialog) {
    AddNewItemDialog(
      onDismiss = { showAddDialog = false },
      onConfirm = { name, desc, price, cat, isVeg, prepTime, cals, badge ->
        onAddNewItem(name, desc, price, cat, isVeg, prepTime, cals, badge)
        showAddDialog = false
      }
    )
  }
}

@Composable
fun MetricCard(
  title: String,
  value: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  color: Color,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier,
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = title,
          style = MaterialTheme.typography.labelSmall.copy(
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Bold
          )
        )
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = color,
          modifier = Modifier.size(18.dp)
        )
      }
      Spacer(modifier = Modifier.height(6.dp))
      Text(
        text = value,
        style = MaterialTheme.typography.titleLarge.copy(
          fontWeight = FontWeight.ExtraBold,
          color = color
        )
      )
    }
  }
}

@Composable
fun AdminOrderCard(
  order: OrderEntity,
  onUpdateStatus: (String) -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("admin_order_card_${order.tokenNumber}"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      // Header: Token and customer
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = when (order.status) {
              "READY" -> VegGreen
              "PREPARING" -> TokenGold
              "COMPLETED" -> MaterialTheme.colorScheme.outlineVariant
              else -> MaterialTheme.colorScheme.primary
            }
          ) {
            Text(
              text = order.tokenNumber,
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                fontFamily = FontFamily.Monospace
              )
            )
          }

          Spacer(modifier = Modifier.width(10.dp))

          Column {
            Text(
              text = order.studentName,
              style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
            Text(
              text = "ID: ${order.studentId} • ${order.pickupTime}",
              style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )
          }
        }

        // Current status chip
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = MaterialTheme.colorScheme.surfaceVariant
        ) {
          Text(
            text = order.status,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Items
      Text(
        text = order.itemsSummary,
        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
      )

      if (order.notes.isNotBlank()) {
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "Note: ${order.notes}",
          style = MaterialTheme.typography.bodySmall.copy(
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold
          )
        )
      }

      Spacer(modifier = Modifier.height(8.dp))
      HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
      Spacer(modifier = Modifier.height(8.dp))

      // Actions for kitchen staff
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "₹${String.format("%.2f", order.totalAmount)} (${order.paymentMethod})",
          style = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          when (order.status) {
            "PLACED" -> {
              Button(
                onClick = { onUpdateStatus("PREPARING") },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TokenGold),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                modifier = Modifier.testTag("action_prepare_${order.tokenNumber}")
              ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Cook", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
              }
            }
            "PREPARING" -> {
              Button(
                onClick = { onUpdateStatus("READY") },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = VegGreen),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                modifier = Modifier.testTag("action_ready_${order.tokenNumber}")
              ) {
                Icon(Icons.Default.NotificationsActive, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Mark Ready", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
              }
            }
            "READY" -> {
              Button(
                onClick = { onUpdateStatus("COMPLETED") },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                modifier = Modifier.testTag("action_complete_${order.tokenNumber}")
              ) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Picked Up", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
              }
            }
            "COMPLETED" -> {
              Text(
                text = "Order Fulfilled",
                style = MaterialTheme.typography.labelSmall.copy(color = VegGreen, fontWeight = FontWeight.Bold)
              )
            }
          }
        }
      }
    }
  }
}

@Composable
fun AdminMenuItemCard(
  item: MenuItemEntity,
  onToggleStock: (Boolean) -> Unit,
  onEditPrice: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("admin_item_row_${item.id}"),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f))
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          if (item.isVeg) VegIcon(modifier = Modifier.size(14.dp)) else NonVegIcon(modifier = Modifier.size(14.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = item.name,
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
          )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = "${item.category} • ₹${String.format("%.2f", item.price)}",
          style = MaterialTheme.typography.bodySmall.copy(
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold
          )
        )
      }

      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        // Price Edit Button
        IconButton(
          onClick = onEditPrice,
          modifier = Modifier
            .size(36.dp)
            .testTag("admin_edit_price_${item.id}")
        ) {
          Icon(Icons.Default.Edit, contentDescription = "Edit Price", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
        }

        // Availability Toggle Switch
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Switch(
            checked = item.isAvailable,
            onCheckedChange = onToggleStock,
            colors = SwitchDefaults.colors(
              checkedThumbColor = VegGreen,
              checkedTrackColor = VegGreen.copy(alpha = 0.3f)
            ),
            modifier = Modifier.testTag("stock_switch_${item.id}")
          )
          Text(
            text = if (item.isAvailable) "In Stock" else "Sold Out",
            style = MaterialTheme.typography.labelSmall.copy(
              fontSize = 9.sp,
              fontWeight = FontWeight.Bold,
              color = if (item.isAvailable) VegGreen else MaterialTheme.colorScheme.error
            )
          )
        }
      }
    }
  }
}

@Composable
fun AnalyticsBar(label: String, percent: Float, count: String) {
  Column(modifier = Modifier.fillMaxWidth()) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Text(label, style = MaterialTheme.typography.bodySmall)
      Text(count, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
    }
    Spacer(modifier = Modifier.height(4.dp))
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(8.dp)
        .clip(RoundedCornerShape(4.dp))
        .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth(percent)
          .height(8.dp)
          .clip(RoundedCornerShape(4.dp))
          .background(MaterialTheme.colorScheme.primary)
      )
    }
  }
}

@Composable
fun PopularItemRow(rank: Int, name: String, count: Int, revenue: String) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 6.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      Surface(
        shape = RoundedCornerShape(6.dp),
        color = MaterialTheme.colorScheme.primaryContainer
      ) {
        Text(
          text = "#$rank",
          modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
          style = MaterialTheme.typography.labelSmall.copy(
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
          )
        )
      }
      Spacer(modifier = Modifier.width(8.dp))
      Column {
        Text(text = name, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium))
        Text(text = "$count ordered", style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
      }
    }
    Text(text = revenue, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary))
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewItemDialog(
  onDismiss: () -> Unit,
  onConfirm: (String, String, Double, String, Boolean, String, Int, String) -> Unit
) {
  var name by remember { mutableStateOf("") }
  var description by remember { mutableStateOf("") }
  var priceText by remember { mutableStateOf("") }
  var category by remember { mutableStateOf("Breakfast") }
  var isVeg by remember { mutableStateOf(true) }
  var prepTime by remember { mutableStateOf("8-10 min") }
  var caloriesText by remember { mutableStateOf("320") }
  var badge by remember { mutableStateOf("New") }

  val categories = listOf("Breakfast", "Lunch", "Snacks", "Beverages", "Combos")
  var categoryExpanded by remember { mutableStateOf(false) }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("Add New Food Item to Menu") },
    text = {
      LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        item {
          OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Item Name *") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag("add_item_name_input")
          )
        }
        item {
          OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            maxLines = 2,
            modifier = Modifier.fillMaxWidth().testTag("add_item_desc_input")
          )
        }
        item {
          OutlinedTextField(
            value = priceText,
            onValueChange = { priceText = it },
            label = { Text("Price (₹) *") },
            prefix = { Text("₹") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag("add_item_price_input")
          )
        }
        item {
          ExposedDropdownMenuBox(
            expanded = categoryExpanded,
            onExpandedChange = { categoryExpanded = !categoryExpanded }
          ) {
            OutlinedTextField(
              value = category,
              onValueChange = {},
              readOnly = true,
              label = { Text("Category") },
              trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
              modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
              expanded = categoryExpanded,
              onDismissRequest = { categoryExpanded = false }
            ) {
              categories.forEach { cat ->
                DropdownMenuItem(
                  text = { Text(cat) },
                  onClick = {
                    category = cat
                    categoryExpanded = false
                  }
                )
              }
            }
          }
        }
        item {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text("Dietary Type: ${if (isVeg) "Pure Veg" else "Non-Veg"}")
            Switch(
              checked = isVeg,
              onCheckedChange = { isVeg = it },
              colors = SwitchDefaults.colors(
                checkedThumbColor = VegGreen,
                checkedTrackColor = VegGreen.copy(alpha = 0.3f)
              )
            )
          }
        }
        item {
          OutlinedTextField(
            value = prepTime,
            onValueChange = { prepTime = it },
            label = { Text("Prep Time (e.g. 6-8 min)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          val price = priceText.toDoubleOrNull() ?: 3.50
          val calories = caloriesText.toIntOrNull() ?: 300
          if (name.isNotBlank()) {
            onConfirm(name, description, price, category, isVeg, prepTime, calories, badge)
          }
        },
        modifier = Modifier.testTag("submit_new_item_button")
      ) {
        Text("Add to Menu")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Cancel")
      }
    }
  )
}
