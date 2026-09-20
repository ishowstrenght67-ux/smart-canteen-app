package com.example.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class CanteenRepository(private val dao: CanteenDao) {

  val allMenuItems: Flow<List<MenuItemEntity>> = dao.getAllMenuItems()
  val allOrders: Flow<List<OrderEntity>> = dao.getAllOrders()
  val readyTokens: Flow<List<OrderEntity>> = dao.getReadyTokens()
  val preparingTokens: Flow<List<OrderEntity>> = dao.getPreparingTokens()

  fun getStudentOrders(studentId: String): Flow<List<OrderEntity>> {
    return dao.getStudentOrders(studentId)
  }

  suspend fun ensureMenuPopulated() {
    withContext(Dispatchers.IO) {
      val count = dao.getMenuItemCount()
      if (count == 0) {
        dao.insertMenuItems(InitialData.defaultMenuItems)
      }
      val orderCount = dao.getOrderCount()
      if (orderCount == 0) {
        dao.insertOrders(InitialData.defaultOrders)
      }
    }
  }

  suspend fun toggleAvailability(itemId: String, isAvailable: Boolean) {
    withContext(Dispatchers.IO) {
      dao.updateAvailability(itemId, isAvailable)
    }
  }

  suspend fun updatePrice(itemId: String, newPrice: Double) {
    withContext(Dispatchers.IO) {
      dao.updatePrice(itemId, newPrice)
    }
  }

  suspend fun addMenuItem(item: MenuItemEntity) {
    withContext(Dispatchers.IO) {
      dao.insertMenuItem(item)
    }
  }

  suspend fun updateMenuItem(item: MenuItemEntity) {
    withContext(Dispatchers.IO) {
      dao.updateMenuItem(item)
    }
  }

  suspend fun deleteMenuItem(item: MenuItemEntity) {
    withContext(Dispatchers.IO) {
      dao.deleteMenuItem(item)
    }
  }

  suspend fun placeOrder(order: OrderEntity) {
    withContext(Dispatchers.IO) {
      dao.insertOrder(order)
    }
  }

  suspend fun updateOrderStatus(orderId: String, newStatus: String) {
    withContext(Dispatchers.IO) {
      dao.updateOrderStatus(orderId, newStatus)
    }
  }

  suspend fun getOrderById(orderId: String): OrderEntity? {
    return withContext(Dispatchers.IO) {
      dao.getOrderById(orderId)
    }
  }

  companion object {
    @Volatile
    private var INSTANCE: CanteenRepository? = null

    fun getInstance(context: Context): CanteenRepository {
      return INSTANCE ?: synchronized(this) {
        val db = CanteenDatabase.getInstance(context)
        val instance = CanteenRepository(db.canteenDao())
        INSTANCE = instance
        instance
      }
    }
  }
}
