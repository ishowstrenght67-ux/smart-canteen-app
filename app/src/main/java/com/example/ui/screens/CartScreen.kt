package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocalAtm
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.CartItem
import com.example.ui.UserProfile
import com.example.ui.theme.VegGreen

@Composable
fun CartScreen(
  cart: Map<String, CartItem>,
  userProfile: UserProfile,
  onUpdateQty: (String, Int) -> Unit,
  onRemoveItem: (String) -> Unit,
  onUpdateNote: (String, String) -> Unit,
  onPlaceOrder: (String, String, String) -> Unit,
  onBrowseMenu: () -> Unit,
  onTopUpWallet: (Double) -> Unit,
  modifier: Modifier = Modifier
) {
  val cartList = cart.values.toList()
  val subtotal = cartList.sumOf { it.item.price * it.quantity }
  val studentDiscount = if (subtotal > 5.0) 0.50 else 0.00
  val total = (subtotal - studentDiscount).coerceAtLeast(0.0)

  val pickupSlots = listOf(
    "Immediate (Next 10-15 mins)",
    "Short Break (11:15 AM)",
    "Lunch Rush (1:15 PM)",
    "Evening Break (4:30 PM)"
  )
  var selectedSlot by remember { mutableStateOf(pickupSlots.first()) }

  val paymentMethods = listOf("Campus Card Wallet", "UPI / QR Pay", "Cash at Counter")
  var selectedPayment by remember { mutableStateOf(paymentMethods.first()) }
  var orderNotes by remember { mutableStateOf("") }

  if (cartList.isEmpty()) {
    Box(
      modifier = modifier
        .fillMaxSize()
        .padding(24.dp)
        .testTag("empty_cart_container"),
      contentAlignment = Alignment.Center
    ) {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
      ) {
        Box(
          modifier = Modifier
            .size(80.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.ShoppingCart,
            contentDescription = "Empty Cart",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(40.dp)
          )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
          text = "Your Cart is Empty",
          style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "Add delicious dishes from the canteen menu to pre-order and generate your pickup token.",
          style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
          modifier = Modifier.padding(horizontal = 24.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
          onClick = onBrowseMenu,
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier.testTag("browse_menu_from_empty_cart_button")
        ) {
          Icon(Icons.Default.Restaurant, contentDescription = null)
          Spacer(modifier = Modifier.width(8.dp))
          Text("Browse Canteen Menu")
        }
      }
    }
    return
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .testTag("cart_screen_list"),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Header
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Your Order Cart",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
          )
          Text(
            text = "${cartList.sumOf { it.quantity }} items ready for smart checkout",
            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
          )
        }
      }
    }

    // Cart Items
    items(cartList, key = { it.item.id }) { cartItem ->
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("cart_item_${cartItem.item.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
          ) {
            Row(modifier = Modifier.weight(1f)) {
              if (cartItem.item.isVeg) {
                VegIcon(modifier = Modifier.size(16.dp))
              } else {
                NonVegIcon(modifier = Modifier.size(16.dp))
              }
              Spacer(modifier = Modifier.width(8.dp))
              Column {
                Text(
                  text = cartItem.item.name,
                  style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                  text = "₹${String.format("%.2f", cartItem.item.price)} each",
                  style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )
              }
            }

            // Total per item
            Text(
              text = "₹${String.format("%.2f", cartItem.item.price * cartItem.quantity)}",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
              )
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          // Quantity controls and delete
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            // Note input toggle or quick note
            Text(
              text = if (cartItem.notes.isNotBlank()) "Note: ${cartItem.notes}" else "Prep time: ${cartItem.item.prepTime}",
              style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
              modifier = Modifier.weight(1f)
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
              IconButton(
                onClick = { onRemoveItem(cartItem.item.id) },
                modifier = Modifier.size(32.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.Delete,
                  contentDescription = "Remove item",
                  tint = MaterialTheme.colorScheme.error,
                  modifier = Modifier.size(18.dp)
                )
              }

              Spacer(modifier = Modifier.width(6.dp))

              Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.primaryContainer
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  IconButton(
                    onClick = { onUpdateQty(cartItem.item.id, -1) },
                    modifier = Modifier.size(30.dp)
                  ) {
                    Icon(
                      imageVector = Icons.Default.Remove,
                      contentDescription = "Decrease",
                      tint = MaterialTheme.colorScheme.primary,
                      modifier = Modifier.size(14.dp)
                    )
                  }

                  Text(
                    text = "${cartItem.quantity}",
                    style = MaterialTheme.typography.titleSmall.copy(
                      fontWeight = FontWeight.Bold,
                      color = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.padding(horizontal = 6.dp)
                  )

                  IconButton(
                    onClick = { onUpdateQty(cartItem.item.id, 1) },
                    modifier = Modifier.size(30.dp)
                  ) {
                    Icon(
                      imageVector = Icons.Default.Add,
                      contentDescription = "Increase",
                      tint = MaterialTheme.colorScheme.primary,
                      modifier = Modifier.size(14.dp)
                    )
                  }
                }
              }
            }
          }
        }
      }
    }

    // Pickup Time Selection Card
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Schedule, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Pickup Time Slot",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
          }
          Spacer(modifier = Modifier.height(8.dp))
          pickupSlots.forEach { slot ->
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .clickable { selectedSlot = slot }
                .padding(vertical = 4.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              RadioButton(
                selected = selectedSlot == slot,
                onClick = { selectedSlot = slot }
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(text = slot, style = MaterialTheme.typography.bodyMedium)
            }
          }
        }
      }
    }

    // Payment Method Selection Card
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Payment Option",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          // Option 1: Campus Card Wallet
          Surface(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(12.dp))
              .clickable { selectedPayment = "Campus Card Wallet" },
            shape = RoundedCornerShape(12.dp),
            color = if (selectedPayment == "Campus Card Wallet") MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
            border = if (selectedPayment == "Campus Card Wallet") BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary) else null
          ) {
            Column(modifier = Modifier.padding(12.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                  Spacer(modifier = Modifier.width(8.dp))
                  Column {
                    Text(
                      text = "Campus Card Wallet (Recommended)",
                      style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                      text = "Current Balance: ₹${String.format("%.2f", userProfile.walletBalance)}",
                      style = MaterialTheme.typography.bodySmall.copy(
                        color = if (userProfile.walletBalance >= total) VegGreen else MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Medium
                      )
                    )
                  }
                }
                RadioButton(
                  selected = selectedPayment == "Campus Card Wallet",
                  onClick = { selectedPayment = "Campus Card Wallet" }
                )
              }

              // If insufficient balance, show quick top up
              if (userProfile.walletBalance < total && selectedPayment == "Campus Card Wallet") {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = "Insufficient balance!",
                    style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.error)
                  )
                  OutlinedButton(
                    onClick = { onTopUpWallet(100.0) },
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                  ) {
                    Text("+ Top Up ₹100", style = MaterialTheme.typography.labelSmall)
                  }
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          // Option 2: UPI / QR
          Surface(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(12.dp))
              .clickable { selectedPayment = "UPI / QR Pay" },
            shape = RoundedCornerShape(12.dp),
            color = if (selectedPayment == "UPI / QR Pay") MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
            border = if (selectedPayment == "UPI / QR Pay") BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary) else null
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.QrCode2, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                  Text(text = "Campus UPI & QR Pay", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                  Text(text = "Scan at counter kiosk", style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
                }
              }
              RadioButton(
                selected = selectedPayment == "UPI / QR Pay",
                onClick = { selectedPayment = "UPI / QR Pay" }
              )
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          // Option 3: Cash at Counter
          Surface(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(12.dp))
              .clickable { selectedPayment = "Cash at Counter" },
            shape = RoundedCornerShape(12.dp),
            color = if (selectedPayment == "Cash at Counter") MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
            border = if (selectedPayment == "Cash at Counter") BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary) else null
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocalAtm, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                  Text(text = "Pay Cash at Counter", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                  Text(text = "Pay physical cash when token is called", style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
                }
              }
              RadioButton(
                selected = selectedPayment == "Cash at Counter",
                onClick = { selectedPayment = "Cash at Counter" }
              )
            }
          }
        }
      }
    }

    // Special Kitchen Notes
    item {
      OutlinedTextField(
        value = orderNotes,
        onValueChange = { orderNotes = it },
        modifier = Modifier
          .fillMaxWidth()
          .testTag("cart_special_notes_field"),
        label = { Text("Kitchen instructions (e.g. less spicy, extra chutney)") },
        shape = RoundedCornerShape(14.dp)
      )
    }

    // Bill Breakdown Card
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "Bill Summary",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
          )
          Spacer(modifier = Modifier.height(10.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text("Subtotal", style = MaterialTheme.typography.bodyMedium)
            Text("₹${String.format("%.2f", subtotal)}", style = MaterialTheme.typography.bodyMedium)
          }

          if (studentDiscount > 0) {
            Spacer(modifier = Modifier.height(4.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text("Student Campus Discount", style = MaterialTheme.typography.bodyMedium.copy(color = VegGreen))
              Text("-₹${String.format("%.2f", studentDiscount)}", style = MaterialTheme.typography.bodyMedium.copy(color = VegGreen))
            }
          }

          Spacer(modifier = Modifier.height(4.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text("Canteen Platform Fee", style = MaterialTheme.typography.bodyMedium)
            Text("FREE (₹0.00)", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
          }

          Spacer(modifier = Modifier.height(10.dp))
          HorizontalDivider()
          Spacer(modifier = Modifier.height(10.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Total Payable",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Text(
              text = "₹${String.format("%.2f", total)}",
              style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
              )
            )
          }
        }
      }
    }

    // Checkout / Order Button
    item {
      Button(
        onClick = {
          onPlaceOrder(selectedSlot, selectedPayment, orderNotes)
        },
        modifier = Modifier
          .fillMaxWidth()
          .height(54.dp)
          .testTag("confirm_order_button"),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center
        ) {
          Text(
            text = "Place Order & Get Token • ₹${String.format("%.2f", total)}",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Icon(Icons.Default.ArrowForward, contentDescription = null)
        }
      }
      Spacer(modifier = Modifier.height(40.dp))
    }
  }
}
