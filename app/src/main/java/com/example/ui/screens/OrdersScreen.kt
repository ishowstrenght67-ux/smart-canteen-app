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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Kitchen
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.TakeoutDining
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.OrderEntity
import com.example.ui.theme.TokenGold
import com.example.ui.theme.VegGreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun OrdersScreen(
  orders: List<OrderEntity>,
  onOrderAgain: () -> Unit,
  modifier: Modifier = Modifier
) {
  val activeOrders = orders.filter { it.status in listOf("PLACED", "PREPARING", "READY") }
  val pastOrders = orders.filter { it.status !in listOf("PLACED", "PREPARING", "READY") }

  if (orders.isEmpty()) {
    Box(
      modifier = modifier
        .fillMaxSize()
        .padding(24.dp)
        .testTag("empty_orders_container"),
      contentAlignment = Alignment.Center
    ) {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
      ) {
        Box(
          modifier = Modifier
            .size(72.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Receipt,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(36.dp)
          )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
          text = "No Orders Placed Yet",
          style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "Once you place an order, your live digital token and real-time kitchen status will appear here.",
          style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
          modifier = Modifier.padding(horizontal = 20.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
          onClick = onOrderAgain,
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.testTag("start_ordering_button")
        ) {
          Text("Order From Canteen")
        }
      }
    }
    return
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .testTag("orders_screen_list"),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Active Orders Header
    if (activeOrders.isNotEmpty()) {
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Active Canteen Tokens (${activeOrders.size})",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
          )
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.primaryContainer
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(8.dp)
                  .clip(CircleShape)
                  .background(VegGreen)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "Live Tracking",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onPrimaryContainer
                )
              )
            }
          }
        }
      }

      items(activeOrders, key = { it.orderId }) { order ->
        ActiveTokenCard(order = order)
      }
    }

    // Past Orders Section
    if (pastOrders.isNotEmpty()) {
      item {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "Order History (${pastOrders.size})",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
      }

      items(pastOrders, key = { it.orderId }) { order ->
        PastOrderCard(order = order, onReorder = onOrderAgain)
      }
    }

    item {
      Spacer(modifier = Modifier.height(50.dp))
    }
  }
}

@Composable
fun ActiveTokenCard(order: OrderEntity) {
  val isReady = order.status == "READY"
  val isPreparing = order.status == "PREPARING"

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("active_token_card_${order.tokenNumber}"),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (isReady) MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.4f) else MaterialTheme.colorScheme.surface
    ),
    border = BorderStroke(
      width = if (isReady) 2.dp else 1.dp,
      color = if (isReady) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
  ) {
    Column(modifier = Modifier.padding(18.dp)) {
      // Header: Token Number & Live status badge
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "TOKEN NUMBER",
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              letterSpacing = 1.2.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          )
          Text(
            text = order.tokenNumber,
            style = MaterialTheme.typography.displaySmall.copy(
              fontWeight = FontWeight.ExtraBold,
              color = MaterialTheme.colorScheme.primary
            )
          )
        }

        // Status Badge
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = when (order.status) {
            "READY" -> VegGreen
            "PREPARING" -> TokenGold
            else -> MaterialTheme.colorScheme.primary
          }
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = when (order.status) {
                "READY" -> Icons.Default.NotificationsActive
                "PREPARING" -> Icons.Default.Kitchen
                else -> Icons.Default.Schedule
              },
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = when (order.status) {
                "READY" -> "READY FOR PICKUP!"
                "PREPARING" -> "PREPARING IN KITCHEN"
                else -> "ORDER RECEIVED"
              },
              style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White
              )
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Status 4-step progress timeline
      OrderStepProgressBar(currentStatus = order.status)

      Spacer(modifier = Modifier.height(14.dp))
      HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
      Spacer(modifier = Modifier.height(14.dp))

      // Order Summary Details
      Text(
        text = order.itemsSummary,
        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
      )

      if (order.notes.isNotBlank()) {
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "Note: ${order.notes}",
          style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Pickup instruction and barcode simulation
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(12.dp))
          .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f))
          .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = if (isReady) "Collect now at Counter 2" else "Estimated: ${order.pickupTime}",
            style = MaterialTheme.typography.labelLarge.copy(
              fontWeight = FontWeight.Bold,
              color = if (isReady) VegGreen else MaterialTheme.colorScheme.onSurface
            )
          )
          Text(
            text = "Show this screen or token #${order.tokenNumber} to counter staff",
            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
          )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Simulated QR Code Icon Box
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = Color.White,
          border = BorderStroke(1.dp, Color.LightGray)
        ) {
          Column(
            modifier = Modifier.padding(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Icon(
              imageVector = Icons.Default.QrCode,
              contentDescription = "Token QR",
              tint = Color.Black,
              modifier = Modifier.size(32.dp)
            )
            Text(
              text = order.tokenNumber,
              style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 8.sp,
                fontFamily = FontFamily.Monospace,
                color = Color.Black
              )
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(
          text = "Payment: ${order.paymentMethod}",
          style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
        )
        Text(
          text = "Total: ₹${String.format("%.2f", order.totalAmount)}",
          style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
        )
      }
    }
  }
}

@Composable
fun OrderStepProgressBar(currentStatus: String) {
  val steps = listOf("Received", "Preparing", "Ready", "Picked Up")
  val activeIndex = when (currentStatus) {
    "PLACED" -> 0
    "PREPARING" -> 1
    "READY" -> 2
    "COMPLETED" -> 3
    else -> 0
  }

  Row(
    modifier = Modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    steps.forEachIndexed { index, stepName ->
      val isReached = index <= activeIndex
      val isCurrent = index == activeIndex

      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.weight(1f)
      ) {
        Box(
          modifier = Modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(
              when {
                isCurrent -> MaterialTheme.colorScheme.primary
                isReached -> VegGreen
                else -> MaterialTheme.colorScheme.surfaceVariant
              }
            ),
          contentAlignment = Alignment.Center
        ) {
          if (isReached && !isCurrent) {
            Icon(
              imageVector = Icons.Default.CheckCircle,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(16.dp)
            )
          } else {
            Text(
              text = "${index + 1}",
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = if (isReached) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
              )
            )
          }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = stepName,
          style = MaterialTheme.typography.labelSmall.copy(
            fontSize = 9.sp,
            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
            color = if (isCurrent) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
          )
        )
      }

      if (index < steps.size - 1) {
        Box(
          modifier = Modifier
            .weight(0.6f)
            .height(3.dp)
            .background(
              if (index < activeIndex) VegGreen else MaterialTheme.colorScheme.outlineVariant
            )
        )
      }
    }
  }
}

@Composable
fun PastOrderCard(order: OrderEntity, onReorder: () -> Unit) {
  val dateFormat = SimpleDateFormat("MMM dd, yyyy • hh:mm a", Locale.getDefault())
  val dateStr = dateFormat.format(Date(order.timestamp))

  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Token #${order.tokenNumber}",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
          )
          Text(
            text = dateStr,
            style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
          )
        }

        Surface(
          shape = RoundedCornerShape(8.dp),
          color = if (order.status == "COMPLETED") VegGreen.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant
        ) {
          Text(
            text = order.status,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              color = if (order.status == "COMPLETED") VegGreen else MaterialTheme.colorScheme.onSurfaceVariant
            )
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))
      Text(
        text = order.itemsSummary,
        style = MaterialTheme.typography.bodyMedium
      )

      Spacer(modifier = Modifier.height(8.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Paid: ₹${String.format("%.2f", order.totalAmount)} (${order.paymentMethod})",
          style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
        )

        OutlinedButton(
          onClick = onReorder,
          shape = RoundedCornerShape(10.dp),
          contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
        ) {
          Icon(Icons.Default.TakeoutDining, contentDescription = null, modifier = Modifier.size(14.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Order Again", style = MaterialTheme.typography.labelSmall)
        }
      }
    }
  }
}
