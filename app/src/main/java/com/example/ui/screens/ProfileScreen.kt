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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.UserProfile
import com.example.ui.UserRole
import com.example.ui.theme.VegGreen

@Composable
fun ProfileScreen(
  userProfile: UserProfile,
  darkTheme: Boolean,
  onToggleTheme: () -> Unit,
  onTopUpWallet: (Double) -> Unit,
  onSwitchRole: (UserRole) -> Unit,
  onLoginStudent: (String, String, String, Double) -> Unit,
  onLogoutStudent: () -> Unit,
  modifier: Modifier = Modifier
) {
  var showNoCodeGuide by remember { mutableStateOf(false) }
  var showLoginDialog by remember { mutableStateOf(false) }

  if (showLoginDialog) {
    StudentLoginDialog(
      onDismiss = { showLoginDialog = false },
      onLogin = { name, id, dept, balance ->
        onLoginStudent(name, id, dept, balance)
        showLoginDialog = false
      }
    )
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .testTag("profile_screen"),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Top Campus ID Card
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("student_id_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.School, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "CAMPUS SMART ID",
                style = MaterialTheme.typography.labelMedium.copy(
                  fontWeight = FontWeight.Bold,
                  letterSpacing = 1.sp,
                  color = MaterialTheme.colorScheme.onPrimaryContainer
                )
              )
            }

            Surface(
              shape = RoundedCornerShape(8.dp),
              color = VegGreen
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = "AUTHENTICATED",
                  style = MaterialTheme.typography.labelSmall.copy(color = Color.White, fontWeight = FontWeight.Bold)
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Avatar",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
              )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column {
              Text(
                text = userProfile.name,
                style = MaterialTheme.typography.titleLarge.copy(
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onPrimaryContainer
                )
              )
              Text(
                text = "ID: ${userProfile.studentId}",
                style = MaterialTheme.typography.bodyMedium.copy(
                  fontFamily = FontFamily.Monospace,
                  color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )
              )
              Text(
                text = userProfile.department,
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f))
              )
            }
          }

          Spacer(modifier = Modifier.height(16.dp))
          HorizontalDivider(color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f))
          Spacer(modifier = Modifier.height(12.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Button(
              onClick = { showLoginDialog = true },
              shape = RoundedCornerShape(10.dp),
              colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
              modifier = Modifier
                .weight(1.3f)
                .testTag("student_login_dialog_btn")
            ) {
              Icon(Icons.Default.Login, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Student Login", fontWeight = FontWeight.Bold)
            }

            OutlinedButton(
              onClick = onLogoutStudent,
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier
                .weight(1f)
                .testTag("student_logout_btn")
            ) {
              Icon(Icons.Default.Logout, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Log Out")
            }
          }
        }
      }
    }

    // Campus Canteen Digital Wallet Card
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("wallet_card"),
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
              Icon(
                imageVector = Icons.Default.AccountBalanceWallet,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Canteen Digital Wallet",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
              )
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          Text(
            text = "Current Balance",
            style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
          )
          Text(
            text = "₹${String.format("%.2f", userProfile.walletBalance)}",
            style = MaterialTheme.typography.displaySmall.copy(
              fontWeight = FontWeight.ExtraBold,
              color = MaterialTheme.colorScheme.primary
            )
          )

          Spacer(modifier = Modifier.height(12.dp))
          Text(
            text = "Instant Recharge (Simulated)",
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
          )
          Spacer(modifier = Modifier.height(8.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            listOf(50.0, 100.0, 200.0).forEach { amount ->
              OutlinedButton(
                onClick = { onTopUpWallet(amount) },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                  .weight(1f)
                  .testTag("top_up_button_${amount.toInt()}")
              ) {
                Text("+₹${amount.toInt()}", fontWeight = FontWeight.Bold)
              }
            }
          }
        }
      }
    }

    // Role Switcher Card
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("role_switcher_card"),
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
              Icon(Icons.Default.SwapHoriz, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "App Mode & Permissions",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
              )
            }

            Surface(
              shape = RoundedCornerShape(6.dp),
              color = MaterialTheme.colorScheme.secondaryContainer
            ) {
              Text(
                text = if (userProfile.role == UserRole.STUDENT) "Student View" else "Admin View",
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSecondaryContainer
                )
              )
            }
          }

          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "Switch between Student Ordering view and Admin Kitchen dashboard to manage live orders and stock.",
            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
          )

          Spacer(modifier = Modifier.height(14.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Button(
              onClick = { onSwitchRole(UserRole.STUDENT) },
              shape = RoundedCornerShape(10.dp),
              colors = ButtonDefaults.buttonColors(
                containerColor = if (userProfile.role == UserRole.STUDENT) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
              ),
              modifier = Modifier
                .weight(1f)
                .testTag("switch_to_student_btn")
            ) {
              Icon(
                Icons.Default.School,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = if (userProfile.role == UserRole.STUDENT) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                "Student",
                color = if (userProfile.role == UserRole.STUDENT) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
              )
            }

            Button(
              onClick = { onSwitchRole(UserRole.ADMIN) },
              shape = RoundedCornerShape(10.dp),
              colors = ButtonDefaults.buttonColors(
                containerColor = if (userProfile.role == UserRole.ADMIN) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
              ),
              modifier = Modifier
                .weight(1f)
                .testTag("switch_to_admin_btn")
            ) {
              Icon(
                Icons.Default.AdminPanelSettings,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = if (userProfile.role == UserRole.ADMIN) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                "Admin Staff",
                color = if (userProfile.role == UserRole.ADMIN) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }
      }
    }

    // Appearance / Dark Mode Toggle
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = if (darkTheme) Icons.Default.DarkMode else Icons.Default.LightMode,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = "Dark Mode Theme",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
              )
              Text(
                text = if (darkTheme) "Enabled (Night/OLED friendly)" else "Disabled (Bright Clean UI)",
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
              )
            }
          }

          Switch(
            checked = darkTheme,
            onCheckedChange = { onToggleTheme() },
            modifier = Modifier.testTag("profile_dark_mode_switch")
          )
        }
      }
    }

    // STEP-BY-STEP CANTEEN GUIDE & ARCHITECTURE FOR ZERO-CODERS
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("no_code_guide_card"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clickable { showNoCodeGuide = !showNoCodeGuide },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.HelpOutline, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(
                  text = "Zero-Code Architecture & Step-by-Step Guide",
                  style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                  text = "How each feature works & how to test it step-by-step",
                  style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )
              }
            }

            Icon(
              imageVector = if (showNoCodeGuide) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
              contentDescription = null
            )
          }

          if (showNoCodeGuide) {
            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(14.dp))

            GuideSection(
              title = "Step 1: Student Login & Authentication",
              body = "• Tap 'Student Login' above to switch between student accounts (Alex Morgan, Sarah Chen, Priya Patel) or enter your custom Student ID and Name.\n• Your wallet balance, token numbers, and order history are automatically linked to your logged-in student account."
            )

            Spacer(modifier = Modifier.height(10.dp))

            GuideSection(
              title = "Step 2: Browse Menu, Live Prices & Availability",
              body = "• Go to the 'Menu' tab to browse breakfast, lunch, snacks, beverages, and combos.\n• Each card shows live pricing, prep time, calories, and vegetarian badge.\n• When kitchen staff marks an item 'Sold Out', ordering is disabled and a red 'SOLD OUT' badge appears immediately."
            )

            Spacer(modifier = Modifier.height(10.dp))

            GuideSection(
              title = "Step 3: Cart, Quantities & Special Kitchen Notes",
              body = "• Tap '+ Add' on any dish to populate your cart.\n• In the 'Cart' tab, adjust quantities (+/-), select your pickup time slot (e.g. Lunch break or Immediate), add custom instructions (e.g. 'extra chutney'), and choose your payment method."
            )

            Spacer(modifier = Modifier.height(10.dp))

            GuideSection(
              title = "Step 4: Placing Order & High-Visibility Digital Token",
              body = "• Tap 'Place Campus Order' to generate an official digital token (e.g. CN-103).\n• An animated celebration popup displays your token number and estimated ready time.\n• This token is saved to your 'My Orders' history and sent to the kitchen."
            )

            Spacer(modifier = Modifier.height(10.dp))

            GuideSection(
              title = "Step 5: Live 4-Stage Status Tracker & Token Display Board",
              body = "• Open 'My Orders' to see your active order progress bar: Placed -> Cooking -> Ready -> Picked Up.\n• Open 'Token Board' to see a cafeteria TV display showing which tokens are cooking and which are ready for pickup at Counter 2."
            )

            Spacer(modifier = Modifier.height(10.dp))

            GuideSection(
              title = "Step 6: Admin & Kitchen Staff Dashboard",
              body = "• Tap the role switcher in the top bar or profile to enter 'Admin' mode.\n• As canteen staff: click 'Cook' or 'Mark Ready' on orders, toggle dish availability on/off, change menu prices, or add new daily specials."
            )

            Spacer(modifier = Modifier.height(10.dp))

            GuideSection(
              title = "No-Code Build Comparison (FlutterFlow vs Bubble)",
              body = "• FlutterFlow is ideal for mobile student apps with offline caching and camera token scanning.\n• Bubble is best for a desktop browser admin panel with visual relational workflows.\n• This app is built natively in Jetpack Compose + SQLite Room for instantaneous reactive performance."
            )
          }
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(60.dp))
    }
  }
}

@Composable
fun StudentLoginDialog(
  onDismiss: () -> Unit,
  onLogin: (name: String, studentId: String, department: String, balance: Double) -> Unit
) {
  var selectedTab by remember { mutableStateOf(0) }
  var customName by remember { mutableStateOf("") }
  var customStudentId by remember { mutableStateOf("") }
  var customDept by remember { mutableStateOf("") }
  var customPin by remember { mutableStateOf("") }

  val sampleStudents = listOf(
    StudentAccount("Alex Morgan", "CS-2024-402", "Computer Science & Engg", 450.00),
    StudentAccount("Sarah Chen", "EE-2023-118", "Electrical Engineering", 350.00),
    StudentAccount("Priya Patel", "BT-2024-089", "Biotechnology & Food Tech", 500.00),
    StudentAccount("Rohan Mehta", "ME-2023-314", "Mechanical Engineering", 280.00)
  )

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.School, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(8.dp))
        Text("Student Campus Login", fontWeight = FontWeight.Bold)
      }
    },
    text = {
      Column(modifier = Modifier.fillMaxWidth()) {
        TabRow(selectedTabIndex = selectedTab) {
          Tab(
            selected = selectedTab == 0,
            onClick = { selectedTab = 0 },
            text = { Text("Quick Profiles") }
          )
          Tab(
            selected = selectedTab == 1,
            onClick = { selectedTab = 1 },
            text = { Text("Custom Login") }
          )
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (selectedTab == 0) {
          Text(
            text = "Select a student account to switch profiles and view their orders and wallet:",
            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
          )
          Spacer(modifier = Modifier.height(10.dp))

          Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            sampleStudents.forEach { student ->
              Card(
                modifier = Modifier
                  .fillMaxWidth()
                  .clickable {
                    onLogin(student.name, student.id, student.department, student.balance)
                  }
                  .testTag("sample_student_${student.id}"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
              ) {
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Column {
                    Text(text = student.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                    Text(
                      text = "ID: ${student.id} • ${student.department}",
                      style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                  }

                  Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                  ) {
                    Text(
                      text = "₹${String.format("%.2f", student.balance)}",
                      modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                      style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                      )
                    )
                  }
                }
              }
            }
          }
        } else {
          Text(
            text = "Enter your college credentials to authenticate:",
            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
          )
          Spacer(modifier = Modifier.height(10.dp))

          OutlinedTextField(
            value = customName,
            onValueChange = { customName = it },
            label = { Text("Full Name") },
            placeholder = { Text("e.g. David Kim") },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("login_name_input"),
            singleLine = true
          )

          Spacer(modifier = Modifier.height(8.dp))

          OutlinedTextField(
            value = customStudentId,
            onValueChange = { customStudentId = it },
            label = { Text("Student Roll ID") },
            placeholder = { Text("e.g. CS-2024-501") },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("login_id_input"),
            singleLine = true
          )

          Spacer(modifier = Modifier.height(8.dp))

          OutlinedTextField(
            value = customDept,
            onValueChange = { customDept = it },
            label = { Text("Department") },
            placeholder = { Text("e.g. Information Technology") },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("login_dept_input"),
            singleLine = true
          )

          Spacer(modifier = Modifier.height(8.dp))

          OutlinedTextField(
            value = customPin,
            onValueChange = { customPin = it },
            label = { Text("Campus PIN / Password") },
            placeholder = { Text("4-digit pin") },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("login_pin_input"),
            singleLine = true
          )
        }
      }
    },
    confirmButton = {
      if (selectedTab == 1) {
        Button(
          onClick = {
            if (customName.isNotBlank() && customStudentId.isNotBlank()) {
              onLogin(
                customName.trim(),
                customStudentId.trim(),
                if (customDept.isNotBlank()) customDept.trim() else "Campus Student",
                300.00
              )
            }
          },
          enabled = customName.isNotBlank() && customStudentId.isNotBlank(),
          modifier = Modifier.testTag("login_submit_btn")
        ) {
          Text("Log In")
        }
      }
    },
    dismissButton = {
      OutlinedButton(onClick = onDismiss) {
        Text("Cancel")
      }
    }
  )
}

data class StudentAccount(
  val name: String,
  val id: String,
  val department: String,
  val balance: Double
)

@Composable
fun GuideSection(title: String, body: String) {
  Column {
    Text(
      text = title,
      style = MaterialTheme.typography.titleSmall.copy(
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary
      )
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
      text = body,
      style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurface)
    )
  }
}
