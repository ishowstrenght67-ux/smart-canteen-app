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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Kitchen
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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

@Composable
fun LiveBoardScreen(
  readyOrders: List<OrderEntity>,
  preparingOrders: List<OrderEntity>,
  modifier: Modifier = Modifier
) {
  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .testTag("live_board_screen"),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Top Canteen Display Board Header (TV screen style)
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(36.dp)
                  .clip(RoundedCornerShape(8.dp))
                  .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Tv,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.primary
                )
              }
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(
                  text = "Canteen Token Display Board",
                  style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                  text = "Real-time kitchen broadcast monitor",
                  style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )
              }
            }

            Surface(
              shape = RoundedCornerShape(8.dp),
              color = VegGreen.copy(alpha = 0.15f)
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
                  text = "LIVE SYNC",
                  style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = VegGreen,
                    fontSize = 10.sp
                  )
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(10.dp))
          HorizontalDivider()
          Spacer(modifier = Modifier.height(10.dp))

          // Counter allocation row
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            CounterPill(counterNum = "1", name = "Meals & Combos")
            CounterPill(counterNum = "2", name = "Fast Food & Snacks")
            CounterPill(counterNum = "3", name = "Drinks & Chai")
          }
        }
      }
    }

    // SECTION 1: NOW SERVING (READY)
    item {
      Column {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(VegGreen)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "NOW SERVING • READY FOR PICKUP",
              style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.ExtraBold,
                color = VegGreen,
                letterSpacing = 0.8.sp
              )
            )
          }

          Text(
            text = "${readyOrders.size} Ready",
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "Bring your phone or token number to the designated pickup counter.",
          style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
        )
      }
    }

    if (readyOrders.isEmpty()) {
      item {
        Surface(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(14.dp),
          color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
        ) {
          Box(
            modifier = Modifier.padding(24.dp),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "All completed tokens have been picked up!",
              style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )
          }
        }
      }
    } else {
      item {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          readyOrders.forEach { order ->
            ReadyTokenBoardCard(order = order)
          }
        }
      }
    }

    // SECTION 2: PREPARING IN KITCHEN
    item {
      Spacer(modifier = Modifier.height(10.dp))
      Column {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(TokenGold)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "IN THE KITCHEN • PREPARING",
              style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.ExtraBold,
                color = TokenGold,
                letterSpacing = 0.8.sp
              )
            )
          }

          Text(
            text = "${preparingOrders.size} In Queue",
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "Chefs are actively preparing these meals freshly.",
          style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
        )
      }
    }

    if (preparingOrders.isEmpty()) {
      item {
        Surface(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(14.dp),
          color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
        ) {
          Box(
            modifier = Modifier.padding(24.dp),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "Kitchen is idle! No pending orders right now.",
              style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )
          }
        }
      }
    } else {
      item {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          preparingOrders.forEach { order ->
            PreparingTokenBoardCard(order = order)
          }
        }
      }
    }

    // Announcement Ticker
    item {
      Spacer(modifier = Modifier.height(10.dp))
      Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
      ) {
        Row(
          modifier = Modifier.padding(12.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.Campaign,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
          )
          Spacer(modifier = Modifier.width(10.dp))
          Text(
            text = "Notice: Free extra sambar & chutney refills on all breakfast thalis today!",
            style = MaterialTheme.typography.bodySmall.copy(
              fontWeight = FontWeight.Medium,
              color = MaterialTheme.colorScheme.onPrimaryContainer
            )
          )
        }
      }
      Spacer(modifier = Modifier.height(60.dp))
    }
  }
}

@Composable
fun CounterPill(counterNum: String, name: String) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Surface(
      shape = RoundedCornerShape(6.dp),
      color = MaterialTheme.colorScheme.primaryContainer
    ) {
      Text(
        text = "Counter $counterNum",
        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
        style = MaterialTheme.typography.labelSmall.copy(
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onPrimaryContainer
        )
      )
    }
    Spacer(modifier = Modifier.height(2.dp))
    Text(
      text = name,
      style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    )
  }
}

@Composable
fun ReadyTokenBoardCard(order: OrderEntity) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("board_ready_token_${order.tokenNumber}"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    border = BorderStroke(1.5.dp, VegGreen),
    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = VegGreen
        ) {
          Text(
            text = order.tokenNumber,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            style = MaterialTheme.typography.headlineMedium.copy(
              fontWeight = FontWeight.ExtraBold,
              color = Color.White,
              fontFamily = FontFamily.Monospace
            )
          )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
          Text(
            text = order.studentName,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
          )
          Text(
            text = order.itemsSummary,
            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
            maxLines = 1
          )
        }
      }

      Surface(
        shape = RoundedCornerShape(8.dp),
        color = VegGreen.copy(alpha = 0.15f)
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(Icons.Default.CheckCircle, contentDescription = null, tint = VegGreen, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = "COUNTER 2",
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = VegGreen)
          )
        }
      }
    }
  }
}

@Composable
fun PreparingTokenBoardCard(order: OrderEntity) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("board_prep_token_${order.tokenNumber}"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = TokenGold
        ) {
          Text(
            text = order.tokenNumber,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            style = MaterialTheme.typography.headlineMedium.copy(
              fontWeight = FontWeight.ExtraBold,
              color = Color.White,
              fontFamily = FontFamily.Monospace
            )
          )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
          Text(
            text = order.studentName,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
          )
          Text(
            text = order.itemsSummary,
            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
            maxLines = 1
          )
        }
      }

      Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(Icons.Default.Kitchen, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = "Cooking",
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
          )
        }
      }
    }
  }
}
