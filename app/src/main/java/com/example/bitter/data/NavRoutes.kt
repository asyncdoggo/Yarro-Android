package com.example.bitter.data

sealed class NavRoutes(
    val route:String
){
    object LoginPage : NavRoutes("loginPage")
    object RegisterPage : NavRoutes("registerPage")
    object UserDetailsPage : NavRoutes("userDetailsPage")
    object ForgotPassPage : NavRoutes("forgotPassPage")
    object MainPage : NavRoutes("MainPage")
    object UserProfilePage : NavRoutes("UserProfilePage")

}