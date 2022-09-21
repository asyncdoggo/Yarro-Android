package com.example.bitter

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bitter.data.NavRoutes

@Composable
fun Navigator(){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.LoginPage.route
    ){
        composable(NavRoutes.LoginPage.route){
            LoginPageStart(navController = navController)
        }
        composable(NavRoutes.RegisterPage.route){
            SignUpPage(navController = navController)
        }
        composable(NavRoutes.ForgotPassPage.route){
            forgotPassPage(navController = navController)
        }
        composable(NavRoutes.MainPage.route){
            MainPageStart(navController = navController)
        }
        composable(NavRoutes.UserDetailsPage.route){
            UserDetails(navController = navController)
        }
        composable(NavRoutes.UserProfilePage.route){
            ProfilePageStart(navController = navController)
        }


    }
}