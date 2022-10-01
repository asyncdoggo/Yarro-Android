package com.example.bitter.data

sealed class Routes(
    val route:String
){
    object LoginScreen : Routes("loginScreen")
    object RegisterScreen : Routes("registerScreen")
    object UserDetailsScreen : Routes("userDetailsScreen")
    object ForgotPassScreen : Routes("forgotPassScreen")
    object MainScreen : Routes("MainScreen")
    object UserProfileScreen : Routes("UserProfileScreen")
}