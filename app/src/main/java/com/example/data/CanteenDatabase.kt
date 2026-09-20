package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
  entities = [MenuItemEntity::class, OrderEntity::class],
  version = 2,
  exportSchema = false
)
abstract class CanteenDatabase : RoomDatabase() {
  abstract fun canteenDao(): CanteenDao

  companion object {
    @Volatile
    private var INSTANCE: CanteenDatabase? = null

    fun getInstance(context: Context): CanteenDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          CanteenDatabase::class.java,
          "smart_canteen_inr_db"
        )
          .fallbackToDestructiveMigration()
          .addCallback(object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
              super.onCreate(db)
              // Seed database with default items
              CoroutineScope(Dispatchers.IO).launch {
                getInstance(context).canteenDao().insertMenuItems(InitialData.defaultMenuItems)
                // Seed a couple sample orders for live token board / history showcase
                val initialOrders = listOf(
                  OrderEntity(
                    orderId = "ORD-2041",
                    tokenNumber = "CN-41",
                    studentName = "Rohan Sharma",
                    studentId = "STU-2024-118",
                    itemsSummary = "1x Executive Canteen Thali, 1x Masala Chai",
                    totalAmount = 7.90,
                    pickupTime = "12:45 PM",
                    notes = "Extra pickle please",
                    paymentMethod = "Campus Card Wallet",
                    status = "READY",
                    timestamp = System.currentTimeMillis() - 15 * 60 * 1000
                  ),
                  OrderEntity(
                    orderId = "ORD-2042",
                    tokenNumber = "CN-42",
                    studentName = "Alex Morgan",
                    studentId = "CS-2024-402",
                    itemsSummary = "1x Signature Cold Coffee Float, 1x Crispy Samosa Pav Duo",
                    totalAmount = 5.00,
                    pickupTime = "12:55 PM",
                    notes = "Less ice",
                    paymentMethod = "Campus Card Wallet",
                    status = "PREPARING",
                    timestamp = System.currentTimeMillis() - 8 * 60 * 1000
                  ),
                  OrderEntity(
                    orderId = "ORD-2040",
                    tokenNumber = "CN-40",
                    studentName = "Priya Patel",
                    studentId = "EE-2024-055",
                    itemsSummary = "2x Masala Dosa with Chutneys",
                    totalAmount = 7.00,
                    pickupTime = "12:30 PM",
                    notes = "",
                    paymentMethod = "UPI / QR",
                    status = "COMPLETED",
                    timestamp = System.currentTimeMillis() - 35 * 60 * 1000
                  )
                )
                initialOrders.forEach {
                  getInstance(context).canteenDao().insertOrder(it)
                }
              }
            }
          })
          .build()
        INSTANCE = instance
        instance
      }
    }
  }
}
