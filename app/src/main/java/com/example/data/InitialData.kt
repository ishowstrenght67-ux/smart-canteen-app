package com.example.data

object InitialData {
  val defaultMenuItems = listOf(
    // Breakfast
    MenuItemEntity(
      id = "m_1",
      name = "Masala Dosa with Chutneys",
      description = "Crispy golden fermented crepe served with spicy potato mash, fresh coconut chutney, and piping hot sambar.",
      price = 70.0,
      category = "Breakfast",
      isAvailable = true,
      isVeg = true,
      prepTime = "6-8 min",
      rating = 4.8,
      calories = 310,
      badge = "Bestseller"
    ),
    MenuItemEntity(
      id = "m_2",
      name = "Steamed Idli & Medu Vada",
      description = "Two fluffy steamed rice cakes and one crunchy lentil fritter paired with tangy gun powder and podi dip.",
      price = 50.0,
      category = "Breakfast",
      isAvailable = true,
      isVeg = true,
      prepTime = "5 min",
      rating = 4.6,
      calories = 240,
      badge = "Popular"
    ),
    MenuItemEntity(
      id = "m_3",
      name = "Spicy Egg Bhurji & Butter Toast",
      description = "Double farm-fresh scrambled eggs tossed with diced onions, green chilies, herbs, and toasted butter pav.",
      price = 60.0,
      category = "Breakfast",
      isAvailable = true,
      isVeg = false,
      prepTime = "7 min",
      rating = 4.7,
      calories = 380,
      badge = "Protein Pick"
    ),
    MenuItemEntity(
      id = "m_4",
      name = "Homestyle Poha with Peanuts",
      description = "Flattened rice flakes tempered with mustard seeds, curry leaves, roasted crunchy peanuts, and lemon.",
      price = 40.0,
      category = "Breakfast",
      isAvailable = true,
      isVeg = true,
      prepTime = "4 min",
      rating = 4.5,
      calories = 220,
      badge = "Quick Bite"
    ),

    // Lunch & Meals
    MenuItemEntity(
      id = "m_5",
      name = "Paneer Butter Masala Meal Box",
      description = "Tender cottage cheese cubes simmered in creamy makhani gravy, served with 2 butter naans, jeera rice, and salad.",
      price = 140.0,
      category = "Lunch",
      isAvailable = true,
      isVeg = true,
      prepTime = "10-12 min",
      rating = 4.9,
      calories = 620,
      badge = "Chef's Special"
    ),
    MenuItemEntity(
      id = "m_6",
      name = "Chicken Dum Biryani Bowl",
      description = "Fragrant long-grain basmati rice slow-cooked with aromatic spices and juicy marinated chicken, with cool raita.",
      price = 160.0,
      category = "Lunch",
      isAvailable = true,
      isVeg = false,
      prepTime = "8-10 min",
      rating = 4.9,
      calories = 680,
      badge = "Bestseller"
    ),
    MenuItemEntity(
      id = "m_7",
      name = "Rajma Chawal Comfort Bowl",
      description = "Rich Punjabi red kidney bean curry on steamed basmati rice with pickled red onions and roasted papad.",
      price = 90.0,
      category = "Lunch",
      isAvailable = true,
      isVeg = true,
      prepTime = "5-7 min",
      rating = 4.7,
      calories = 490,
      badge = "Comfort Food"
    ),
    MenuItemEntity(
      id = "m_8",
      name = "Veg Hakka Noodles & Manchurian",
      description = "Wok-tossed noodles with crisp bell peppers, cabbage, and shredded carrots alongside tangy Manchurian gravy.",
      price = 110.0,
      category = "Lunch",
      isAvailable = false, // demo sold-out item
      isVeg = true,
      prepTime = "10 min",
      rating = 4.6,
      calories = 510,
      badge = "Restocking"
    ),

    // Snacks
    MenuItemEntity(
      id = "m_9",
      name = "Crispy Samosa Pav Duo",
      description = "Two golden spiced potato pastries stuffed inside warm buttered buns with sweet date chutney and fiery garlic podi.",
      price = 40.0,
      category = "Snacks",
      isAvailable = true,
      isVeg = true,
      prepTime = "3 min",
      rating = 4.8,
      calories = 340,
      badge = "Campus Favorite"
    ),
    MenuItemEntity(
      id = "m_10",
      name = "Peri Peri Cheesy Fries",
      description = "Golden crisp skin-on potato fries dusted with zesty African bird's eye chili seasoning and melted cheddar drizzle.",
      price = 70.0,
      category = "Snacks",
      isAvailable = true,
      isVeg = true,
      prepTime = "6-8 min",
      rating = 4.7,
      calories = 420,
      badge = "Crunchy"
    ),
    MenuItemEntity(
      id = "m_11",
      name = "Grilled Paneer Tikka Wrap",
      description = "Char-grilled marinated paneer strips rolled in whole wheat tortilla with mint yogurt dressing and fresh iceberg.",
      price = 95.0,
      category = "Snacks",
      isAvailable = true,
      isVeg = true,
      prepTime = "8-10 min",
      rating = 4.8,
      calories = 460,
      badge = "Trending"
    ),

    // Beverages
    MenuItemEntity(
      id = "m_12",
      name = "Signature Cold Coffee Float",
      description = "Velvety brewed espresso blended with rich chilled milk, topped with a scoop of vanilla ice cream and chocolate drizzle.",
      price = 60.0,
      category = "Beverages",
      isAvailable = true,
      isVeg = true,
      prepTime = "3 min",
      rating = 4.9,
      calories = 260,
      badge = "Bestseller"
    ),
    MenuItemEntity(
      id = "m_13",
      name = "Kadak Masala Cutting Chai",
      description = "Steaming brewed Assam black tea infused with fresh crushed ginger, cardamom pods, and cinnamon bark.",
      price = 15.0,
      category = "Beverages",
      isAvailable = true,
      isVeg = true,
      prepTime = "2 min",
      rating = 4.9,
      calories = 80,
      badge = "Daily Boost"
    ),
    MenuItemEntity(
      id = "m_14",
      name = "Fresh Mint Lime Cooler",
      description = "Crushed garden spearmint muddled with freshly squeezed key limes, soda fizz, black salt, and roasted cumin.",
      price = 35.0,
      category = "Beverages",
      isAvailable = true,
      isVeg = true,
      prepTime = "3 min",
      rating = 4.6,
      calories = 90,
      badge = "Refreshing"
    ),

    // Combos
    MenuItemEntity(
      id = "m_15",
      name = "Student Rush Hour Saver Combo",
      description = "Grilled Cheese Sandwich + Crispy French Fries + Chilled Lemonade or Iced Tea. Perfect for exam fuel!",
      price = 120.0,
      category = "Combos",
      isAvailable = true,
      isVeg = true,
      prepTime = "8-10 min",
      rating = 4.9,
      calories = 590,
      badge = "Best Value"
    ),
    MenuItemEntity(
      id = "m_16",
      name = "Executive Canteen Thali",
      description = "Paneer Sabzi + Dal Makhani + 2 Butter Rotis + Basmati Rice + Gulab Jamun dessert + Crisp Papad.",
      price = 150.0,
      category = "Combos",
      isAvailable = true,
      isVeg = true,
      prepTime = "12 min",
      rating = 4.8,
      calories = 740,
      badge = "Complete Meal"
    )
  )

  val defaultOrders = listOf(
    OrderEntity(
      orderId = "ORD-901",
      tokenNumber = "CN-101",
      studentName = "Alex Morgan",
      studentId = "CS-2024-402",
      itemsSummary = "1x Paneer Butter Masala Meal Box, 1x Signature Cold Coffee Float",
      totalAmount = 200.0,
      pickupTime = "Lunch Rush (1:15 PM)",
      notes = "Extra butter on naan please",
      paymentMethod = "Campus Card Wallet",
      status = "READY",
      timestamp = System.currentTimeMillis() - 15 * 60 * 1000
    ),
    OrderEntity(
      orderId = "ORD-902",
      tokenNumber = "CN-102",
      studentName = "Sarah Chen",
      studentId = "EE-2023-118",
      itemsSummary = "2x Crispy Samosa Pav Duo, 1x Kadak Masala Cutting Chai",
      totalAmount = 95.0,
      pickupTime = "Immediate (Next 10-15 mins)",
      notes = "Spicy garlic chutney",
      paymentMethod = "UPI / QR Pay",
      status = "PREPARING",
      timestamp = System.currentTimeMillis() - 8 * 60 * 1000
    ),
    OrderEntity(
      orderId = "ORD-900",
      tokenNumber = "CN-098",
      studentName = "Alex Morgan",
      studentId = "CS-2024-402",
      itemsSummary = "1x Masala Dosa with Chutneys",
      totalAmount = 70.0,
      pickupTime = "Short Break (11:15 AM)",
      notes = "Well roasted dosa",
      paymentMethod = "Campus Card Wallet",
      status = "COMPLETED",
      timestamp = System.currentTimeMillis() - 90 * 60 * 1000
    )
  )
}
