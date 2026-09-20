package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.CanteenRepository
import com.example.data.MenuItemEntity
import com.example.data.OrderEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.random.Random

enum class UserRole {
  STUDENT,
  ADMIN
}

data class UserProfile(
  val name: String = "Alex Morgan",
  val studentId: String = "CS-2024-402",
  val email: String = "alex.m@campus.edu",
  val department: String = "Computer Science & Engg",
  val walletBalance: Double = 450.0,
  val role: UserRole = UserRole.STUDENT
)

data class CartItem(
  val item: MenuItemEntity,
  val quantity: Int = 1,
  val notes: String = ""
)

enum class CanteenScreen {
  MENU,
  CART,
  ORDERS,
  LIVE_BOARD,
  ADMIN,
  PROFILE
}

class CanteenViewModel(application: Application) : AndroidViewModel(application) {

  private val repository = CanteenRepository.getInstance(application)

  private val _userProfile = MutableStateFlow(UserProfile())
  val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

  private val _currentScreen = MutableStateFlow(CanteenScreen.MENU)
  val currentScreen: StateFlow<CanteenScreen> = _currentScreen.asStateFlow()

  private val _darkTheme = MutableStateFlow(false)
  val darkTheme: StateFlow<Boolean> = _darkTheme.asStateFlow()

  private val _cart = MutableStateFlow<Map<String, CartItem>>(emptyMap())
  val cart: StateFlow<Map<String, CartItem>> = _cart.asStateFlow()

  private val _selectedCategory = MutableStateFlow("All")
  val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

  private val _vegOnlyFilter = MutableStateFlow(false)
  val vegOnlyFilter: StateFlow<Boolean> = _vegOnlyFilter.asStateFlow()

  private val _inStockOnlyFilter = MutableStateFlow(false)
  val inStockOnlyFilter: StateFlow<Boolean> = _inStockOnlyFilter.asStateFlow()

  private val _recentlyPlacedOrder = MutableStateFlow<OrderEntity?>(null)
  val recentlyPlacedOrder: StateFlow<OrderEntity?> = _recentlyPlacedOrder.asStateFlow()

  private val _notificationMessage = MutableStateFlow<String?>(null)
  val notificationMessage: StateFlow<String?> = _notificationMessage.asStateFlow()

  val allMenuItems: StateFlow<List<MenuItemEntity>> = repository.allMenuItems
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val allOrders: StateFlow<List<OrderEntity>> = repository.allOrders
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val readyTokens: StateFlow<List<OrderEntity>> = repository.readyTokens
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val preparingTokens: StateFlow<List<OrderEntity>> = repository.preparingTokens
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val studentOrders: StateFlow<List<OrderEntity>> = combine(
    repository.allOrders,
    _userProfile
  ) { orders, profile ->
    orders.filter { it.studentId == profile.studentId }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  // Filtered menu flow
  val filteredMenuItems: StateFlow<List<MenuItemEntity>> = combine(
    allMenuItems,
    _selectedCategory,
    _searchQuery,
    _vegOnlyFilter,
    _inStockOnlyFilter
  ) { items, cat, query, vegOnly, inStockOnly ->
    items.filter { item ->
      val matchesCat = cat == "All" || item.category.equals(cat, ignoreCase = true)
      val matchesQuery = query.isBlank() ||
          item.name.contains(query, ignoreCase = true) ||
          item.description.contains(query, ignoreCase = true)
      val matchesVeg = !vegOnly || item.isVeg
      val matchesStock = !inStockOnly || item.isAvailable
      matchesCat && matchesQuery && matchesVeg && matchesStock
    }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  init {
    viewModelScope.launch {
      repository.ensureMenuPopulated()
    }
  }

  fun setScreen(screen: CanteenScreen) {
    _currentScreen.value = screen
  }

  fun setCategory(category: String) {
    _selectedCategory.value = category
  }

  fun setSearchQuery(query: String) {
    _searchQuery.value = query
  }

  fun toggleVegFilter() {
    _vegOnlyFilter.value = !_vegOnlyFilter.value
  }

  fun toggleInStockFilter() {
    _inStockOnlyFilter.value = !_inStockOnlyFilter.value
  }

  fun toggleDarkTheme() {
    _darkTheme.value = !_darkTheme.value
  }

  fun addToCart(item: MenuItemEntity) {
    if (!item.isAvailable) {
      showNotification("${item.name} is currently sold out!")
      return
    }
    val current = _cart.value.toMutableMap()
    val existing = current[item.id]
    if (existing != null) {
      current[item.id] = existing.copy(quantity = existing.quantity + 1)
    } else {
      current[item.id] = CartItem(item = item, quantity = 1)
    }
    _cart.value = current
    showNotification("Added ${item.name} to cart")
  }

  fun removeFromCart(itemId: String) {
    val current = _cart.value.toMutableMap()
    current.remove(itemId)
    _cart.value = current
  }

  fun updateCartQuantity(itemId: String, delta: Int) {
    val current = _cart.value.toMutableMap()
    val existing = current[itemId] ?: return
    val newQty = existing.quantity + delta
    if (newQty <= 0) {
      current.remove(itemId)
    } else {
      current[itemId] = existing.copy(quantity = newQty)
    }
    _cart.value = current
  }

  fun updateCartNote(itemId: String, note: String) {
    val current = _cart.value.toMutableMap()
    val existing = current[itemId] ?: return
    current[itemId] = existing.copy(notes = note)
    _cart.value = current
  }

  fun clearCart() {
    _cart.value = emptyMap()
  }

  fun placeOrder(
    pickupTime: String,
    paymentMethod: String,
    notes: String
  ): Boolean {
    val items = _cart.value.values.toList()
    if (items.isEmpty()) return false

    val total = items.sumOf { it.item.price * it.quantity }
    val profile = _userProfile.value

    if (paymentMethod == "Campus Card Wallet" && profile.walletBalance < total) {
      showNotification("Insufficient wallet balance (₹${String.format("%.2f", profile.walletBalance)}). Please top up!")
      return false
    }

    // Deduct balance if paying with campus card
    if (paymentMethod == "Campus Card Wallet") {
      _userProfile.value = profile.copy(walletBalance = profile.walletBalance - total)
    }

    val tokenInt = Random.nextInt(100, 999)
    val tokenNumber = "CN-$tokenInt"
    val orderId = "ORD-${System.currentTimeMillis() % 100000}"
    val itemsSummary = items.joinToString(", ") { "${it.quantity}x ${it.item.name}" }

    val order = OrderEntity(
      orderId = orderId,
      tokenNumber = tokenNumber,
      studentName = profile.name,
      studentId = profile.studentId,
      itemsSummary = itemsSummary,
      totalAmount = total,
      pickupTime = pickupTime,
      notes = notes,
      paymentMethod = paymentMethod,
      status = "PLACED",
      timestamp = System.currentTimeMillis()
    )

    viewModelScope.launch {
      repository.placeOrder(order)
      _recentlyPlacedOrder.value = order
      clearCart()
      showNotification("Order placed! Your token is $tokenNumber")
    }
    return true
  }

  fun dismissOrderCelebration() {
    _recentlyPlacedOrder.value = null
  }

  fun topUpWallet(amount: Double) {
    val current = _userProfile.value
    _userProfile.value = current.copy(walletBalance = current.walletBalance + amount)
    showNotification("Added $${String.format("%.2f", amount)} to Campus Wallet!")
  }

  fun switchUserRole(role: UserRole) {
    _userProfile.value = _userProfile.value.copy(role = role)
    if (role == UserRole.ADMIN) {
      _currentScreen.value = CanteenScreen.ADMIN
      showNotification("Switched to Admin & Kitchen Mode")
    } else {
      _currentScreen.value = CanteenScreen.MENU
      showNotification("Switched to Student Mode")
    }
  }

  fun updateProfile(name: String, studentId: String, department: String) {
    _userProfile.value = _userProfile.value.copy(
      name = name,
      studentId = studentId,
      department = department
    )
    showNotification("Profile updated successfully")
  }

  fun loginStudent(name: String, studentId: String, department: String, balance: Double = 400.0) {
    _userProfile.value = UserProfile(
      name = name,
      studentId = studentId,
      department = department,
      walletBalance = balance,
      role = UserRole.STUDENT
    )
    _currentScreen.value = CanteenScreen.MENU
    showNotification("Welcome, $name! Logged in as $studentId")
  }

  fun logoutStudent() {
    _userProfile.value = UserProfile(
      name = "Guest Student",
      studentId = "GUEST-001",
      department = "Campus Guest",
      walletBalance = 150.00,
      role = UserRole.STUDENT
    )
    showNotification("Logged out. Switched to Guest Account.")
  }

  // Admin actions
  fun toggleItemAvailability(itemId: String, isAvailable: Boolean) {
    viewModelScope.launch {
      repository.toggleAvailability(itemId, isAvailable)
      showNotification(if (isAvailable) "Item marked In Stock" else "Item marked Sold Out")
    }
  }

  fun updateItemPrice(itemId: String, price: Double) {
    viewModelScope.launch {
      repository.updatePrice(itemId, price)
      showNotification("Price updated to $${String.format("%.2f", price)}")
    }
  }

  fun addNewMenuItem(
    name: String,
    description: String,
    price: Double,
    category: String,
    isVeg: Boolean,
    prepTime: String,
    calories: Int,
    badge: String
  ) {
    val newItem = MenuItemEntity(
      id = "m_${System.currentTimeMillis()}",
      name = name,
      description = description,
      price = price,
      category = category,
      isAvailable = true,
      isVeg = isVeg,
      prepTime = prepTime,
      rating = 4.8,
      calories = calories,
      badge = badge
    )
    viewModelScope.launch {
      repository.addMenuItem(newItem)
      showNotification("New item added to canteen menu!")
    }
  }

  fun updateOrderStatus(orderId: String, newStatus: String) {
    viewModelScope.launch {
      repository.updateOrderStatus(orderId, newStatus)
      showNotification("Order $orderId marked as $newStatus")
    }
  }

  fun showNotification(msg: String) {
    _notificationMessage.value = msg
  }

  fun dismissNotification() {
    _notificationMessage.value = null
  }
}
