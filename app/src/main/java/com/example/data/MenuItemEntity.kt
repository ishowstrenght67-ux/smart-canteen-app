package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "menu_items")
data class MenuItemEntity(
  @PrimaryKey
  val id: String,
  val name: String,
  val description: String,
  val price: Double,
  val category: String,
  val isAvailable: Boolean = true,
  val isVeg: Boolean = true,
  val prepTime: String = "8-10 min",
  val rating: Double = 4.7,
  val calories: Int = 320,
  val badge: String = ""
)
