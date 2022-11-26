package com.example.bitter.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
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
    object LoadingScreen : Routes("LoadingScreen")

    object HomeScreen: BottomNavItem("Home", Icons.Default.Home,"HomeScreen")
    object ProfileScreen: BottomNavItem("Profile", Icons.Default.Person,"ProfileScreen")
    object ChatScreen: BottomNavItem("Chat", Icons.Default.Chat,"ChatScreen")


    object BottomNav : Routes("BottomNav")
    object EditUserProfileScreen : Routes("EditUserProfileScreen")
    object NewPostScreen : Routes("NewPostScreen")
    object VerifyScreen : Routes("verifyScreen")


}