package com.example.bitter.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.SupervisedUserCircle
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    var title: String,
    var icon: ImageVector,
    var route:String
)

sealed class Routes(
    val route:String
){
    object LoginScreen : Routes("loginScreen")
    object RegisterScreen : Routes("registerScreen")
    object ForgotPassScreen : Routes("forgotPassScreen")
    object MainScreen : Routes("MainScreen")
    object EditUserProfileScreen : Routes("EditUserProfileScreen")
    object NewPostScreen : Routes("NewPostScreen")

    object Home: BottomNavItem("home", Icons.Default.Home,"home")
    object Profile: BottomNavItem("profile", Icons.Default.SupervisedUserCircle,"profile")
    object Chat: BottomNavItem("chat", Icons.Default.Chat,"chat")
}