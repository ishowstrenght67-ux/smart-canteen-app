package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "canteen_orders")
data class OrderEntity(
  @PrimaryKey
  val orderId: String,
  val tokenNumber: String,
  val studentName: String,
  val studentId: String,
  val itemsSummary: String,
  val totalAmount: Double,
  val pickupTime: String,
  val notes: String = "",
  val paymentMethod: String = "Campus Card Wallet",
  val status: String = "PLACED", // PLACED, PREPARING, READY, COMPLETED, CANCELLED
  val timestamp: Long = System.currentTimeMillis()
)
