package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.MenuItemEntity
import com.example.ui.CartItem
import com.example.ui.theme.NonVegRed
import com.example.ui.theme.TokenGold
import com.example.ui.theme.VegGreen

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MenuScreen(
  menuItems: List<MenuItemEntity>,
  cart: Map<String, CartItem>,
  selectedCategory: String,
  searchQuery: String,
  vegOnly: Boolean,
  inStockOnly: Boolean,
  onCategorySelected: (String) -> Unit,
  onSearchChanged: (String) -> Unit,
  onToggleVeg: () -> Unit,
  onToggleInStock: () -> Unit,
  onAddToCart: (MenuItemEntity) -> Unit,
  onUpdateCartQty: (String, Int) -> Unit,
  modifier: Modifier = Modifier
) {
  val categories = listOf("All", "Breakfast", "Lunch", "Snacks", "Beverages", "Combos")

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .testTag("menu_screen_list"),
    contentPadding = PaddingValues(bottom = 80.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // Hero Banner Card
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 6.dp)
          .testTag("canteen_hero_card"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(130.dp)
          ) {
            Image(
              painter = painterResource(id = R.drawable.banner_canteen_hero),
              contentDescription = "Canteen Food Hall Banner",
              modifier = Modifier.fillMaxSize(),
              contentScale = ContentScale.Crop
            )
            // Overlay badge
            Surface(
              modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(8.dp),
              shape = RoundedCornerShape(8.dp),
              color = Color.Black.copy(alpha = 0.7f)
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
                  text = "Kitchen Active • Live Orders",
                  color = Color.White,
                  style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                )
              }
            }
          }

          Column(modifier = Modifier.padding(14.dp)) {
            Text(
              text = "Campus Food Hall & Smart Kitchen",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "Skip the rush hour lines! Place your order now, get a digital token, and collect when ready.",
              style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )
          }
        }
      }
    }

    // Search bar
    item {
      Box(modifier = Modifier.padding(horizontal = 16.dp)) {
        OutlinedTextField(
          value = searchQuery,
          onValueChange = onSearchChanged,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("menu_search_field"),
          placeholder = { Text("Search meals, snacks, drinks...") },
          leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = "Search")
          },
          trailingIcon = {
            if (searchQuery.isNotEmpty()) {
              IconButton(onClick = { onSearchChanged("") }) {
                Icon(Icons.Default.Clear, contentDescription = "Clear search")
              }
            }
          },
          singleLine = true,
          shape = RoundedCornerShape(14.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface
          )
        )
      }
    }

    // Category Selector
    item {
      LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.testTag("category_row")
      ) {
        items(categories) { cat ->
          val isSelected = cat == selectedCategory
          Surface(
            shape = RoundedCornerShape(20.dp),
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier
              .clip(RoundedCornerShape(20.dp))
              .clickable { onCategorySelected(cat) }
              .testTag("category_chip_$cat")
          ) {
            Text(
              text = cat,
              modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
              style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
              )
            )
          }
        }
      }
    }

    // Filter toggle chips (Veg Only, In Stock Only)
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        FilterChip(
          selected = vegOnly,
          onClick = onToggleVeg,
          label = { Text("Pure Veg Only") },
          leadingIcon = {
            if (vegOnly) {
              Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
            } else {
              VegIcon(modifier = Modifier.size(14.dp))
            }
          },
          shape = RoundedCornerShape(12.dp),
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = VegGreen.copy(alpha = 0.15f),
            selectedLabelColor = VegGreen
          ),
          modifier = Modifier.testTag("filter_veg_chip")
        )

        FilterChip(
          selected = inStockOnly,
          onClick = onToggleInStock,
          label = { Text("In-Stock Only") },
          leadingIcon = if (inStockOnly) {
            { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
          } else null,
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.testTag("filter_instock_chip")
        )
      }
    }

    // Results Header
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = if (selectedCategory == "All") "Available Menu (${menuItems.size})" else "$selectedCategory (${menuItems.size})",
          style = MaterialTheme.typography.titleSmall.copy(
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        )
      }
    }

    // Empty state if no items match
    if (menuItems.isEmpty()) {
      item {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text(
              text = "No Items Found",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "Try adjusting your search query or filter tags.",
              style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )
          }
        }
      }
    } else {
      // Menu Items List
      items(menuItems, key = { it.id }) { item ->
        val cartItem = cart[item.id]
        FoodItemCard(
          item = item,
          cartQuantity = cartItem?.quantity ?: 0,
          onAddToCart = { onAddToCart(item) },
          onIncrement = { onUpdateCartQty(item.id, 1) },
          onDecrement = { onUpdateCartQty(item.id, -1) },
          modifier = Modifier.padding(horizontal = 16.dp)
        )
      }
    }
  }
}

@Composable
fun FoodItemCard(
  item: MenuItemEntity,
  cartQuantity: Int,
  onAddToCart: () -> Unit,
  onIncrement: () -> Unit,
  onDecrement: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("food_item_${item.id}"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (item.isAvailable) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
    ),
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
    elevation = CardDefaults.cardElevation(defaultElevation = if (item.isAvailable) 2.dp else 0.dp)
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      // Header: Veg indicator, Badge, and Stock status
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          if (item.isVeg) {
            VegIcon(modifier = Modifier.size(16.dp))
          } else {
            NonVegIcon(modifier = Modifier.size(16.dp))
          }
          Spacer(modifier = Modifier.width(6.dp))
          if (item.badge.isNotBlank()) {
            Surface(
              shape = RoundedCornerShape(6.dp),
              color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f)
            ) {
              Text(
                text = item.badge,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 10.sp,
                  color = MaterialTheme.colorScheme.onPrimaryContainer
                )
              )
            }
          }
        }

        // Availability Tag
        Surface(
          shape = RoundedCornerShape(6.dp),
          color = if (item.isAvailable) VegGreen.copy(alpha = 0.12f) else MaterialTheme.colorScheme.error.copy(alpha = 0.12f)
        ) {
          Text(
            text = if (item.isAvailable) "IN STOCK" else "SOLD OUT",
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 9.sp,
              color = if (item.isAvailable) VegGreen else MaterialTheme.colorScheme.error
            )
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Item Name
      Text(
        text = item.name,
        style = MaterialTheme.typography.titleMedium.copy(
          fontWeight = FontWeight.Bold,
          color = if (item.isAvailable) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
      )

      Spacer(modifier = Modifier.height(4.dp))

      // Description
      Text(
        text = item.description,
        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
      )

      Spacer(modifier = Modifier.height(8.dp))

      // Meta Info: Rating, Prep Time, Calories
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Star,
            contentDescription = "Rating",
            tint = TokenGold,
            modifier = Modifier.size(15.dp)
          )
          Spacer(modifier = Modifier.width(3.dp))
          Text(
            text = "${item.rating}",
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
          )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.AccessTime,
            contentDescription = "Prep Time",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(14.dp)
          )
          Spacer(modifier = Modifier.width(3.dp))
          Text(
            text = item.prepTime,
            style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
          )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.LocalFireDepartment,
            contentDescription = "Calories",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(14.dp)
          )
          Spacer(modifier = Modifier.width(3.dp))
          Text(
            text = "${item.calories} kcal",
            style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Price & Action Button
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "₹${String.format("%.2f", item.price)}",
            style = MaterialTheme.typography.titleLarge.copy(
              fontWeight = FontWeight.ExtraBold,
              color = MaterialTheme.colorScheme.primary
            )
          )
        }

        if (!item.isAvailable) {
          Surface(
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.surfaceVariant
          ) {
            Text(
              text = "Unavailable",
              modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
              style = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )
          }
        } else if (cartQuantity == 0) {
          Button(
            onClick = onAddToCart,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
            modifier = Modifier.testTag("add_button_${item.id}")
          ) {
            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Add to Cart")
          }
        } else {
          // Stepper: - [Qty] +
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.primaryContainer,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
            ) {
              IconButton(
                onClick = onDecrement,
                modifier = Modifier
                  .size(32.dp)
                  .testTag("decrement_${item.id}")
              ) {
                Icon(
                  imageVector = Icons.Default.Remove,
                  contentDescription = "Decrease",
                  tint = MaterialTheme.colorScheme.primary,
                  modifier = Modifier.size(16.dp)
                )
              }

              Text(
                text = "$cartQuantity",
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.padding(horizontal = 8.dp)
              )

              IconButton(
                onClick = onIncrement,
                modifier = Modifier
                  .size(32.dp)
                  .testTag("increment_${item.id}")
              ) {
                Icon(
                  imageVector = Icons.Default.Add,
                  contentDescription = "Increase",
                  tint = MaterialTheme.colorScheme.primary,
                  modifier = Modifier.size(16.dp)
                )
              }
            }
          }
        }
      }
    }
  }
}

@Composable
fun VegIcon(modifier: Modifier = Modifier) {
  Box(
    modifier = modifier
      .size(16.dp)
      .border(1.5.dp, VegGreen, RoundedCornerShape(3.dp)),
    contentAlignment = Alignment.Center
  ) {
    Box(
      modifier = Modifier
        .size(7.dp)
        .clip(CircleShape)
        .background(VegGreen)
    )
  }
}

@Composable
fun NonVegIcon(modifier: Modifier = Modifier) {
  Box(
    modifier = modifier
      .size(16.dp)
      .border(1.5.dp, NonVegRed, RoundedCornerShape(3.dp)),
    contentAlignment = Alignment.Center
  ) {
    Box(
      modifier = Modifier
        .size(7.dp)
        .clip(CircleShape)
        .background(NonVegRed)
    )
  }
}
