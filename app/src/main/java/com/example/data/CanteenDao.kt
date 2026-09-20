package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CanteenDao {
  @Query("SELECT * FROM menu_items ORDER BY category, name ASC")
  fun getAllMenuItems(): Flow<List<MenuItemEntity>>

  @Query("SELECT * FROM menu_items WHERE id = :id")
  suspend fun getMenuItemById(id: String): MenuItemEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertMenuItems(items: List<MenuItemEntity>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertMenuItem(item: MenuItemEntity)

  @Update
  suspend fun updateMenuItem(item: MenuItemEntity)

  @Query("UPDATE menu_items SET isAvailable = :isAvailable WHERE id = :id")
  suspend fun updateAvailability(id: String, isAvailable: Boolean)

  @Query("UPDATE menu_items SET price = :price WHERE id = :id")
  suspend fun updatePrice(id: String, price: Double)

  @Delete
  suspend fun deleteMenuItem(item: MenuItemEntity)

  @Query("SELECT COUNT(*) FROM menu_items")
  suspend fun getMenuItemCount(): Int

  // Orders
  @Query("SELECT * FROM canteen_orders ORDER BY timestamp DESC")
  fun getAllOrders(): Flow<List<OrderEntity>>

  @Query("SELECT * FROM canteen_orders WHERE studentId = :studentId ORDER BY timestamp DESC")
  fun getStudentOrders(studentId: String): Flow<List<OrderEntity>>

  @Query("SELECT * FROM canteen_orders WHERE status = 'READY' ORDER BY timestamp DESC")
  fun getReadyTokens(): Flow<List<OrderEntity>>

  @Query("SELECT * FROM canteen_orders WHERE status = 'PREPARING' ORDER BY timestamp DESC")
  fun getPreparingTokens(): Flow<List<OrderEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertOrder(order: OrderEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertOrders(orders: List<OrderEntity>)

  @Query("SELECT COUNT(*) FROM canteen_orders")
  suspend fun getOrderCount(): Int

  @Query("UPDATE canteen_orders SET status = :status WHERE orderId = :orderId")
  suspend fun updateOrderStatus(orderId: String, status: String)

  @Query("SELECT * FROM canteen_orders WHERE orderId = :orderId")
  suspend fun getOrderById(orderId: String): OrderEntity?
}
