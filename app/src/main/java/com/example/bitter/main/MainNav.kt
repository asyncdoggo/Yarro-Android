package com.example.bitter.main

import LoginScreen
import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bitter.LoadingScreen
import com.example.bitter.chat.ChatScreen
import com.example.bitter.data.Routes
import com.example.bitter.home.HomeScreen
import com.example.bitter.home.NewPostScreen
import com.example.bitter.editprofile.EditProfileScreenSetup
import com.example.bitter.register.RegisterScreen
import com.example.bitter.resetpass.ForgotPassScreen
import com.example.bitter.userprofile.UserProfileScreen

@Composable
fun MainNav() {
    val outerNavController = rememberNavController()

    NavHost(
        navController = outerNavController,
        startDestination = "loadingscreen"
    ) {

        composable(route = "loadingscreen"){
            LoadingScreen(outerNavController)
        }


        composable(route = Routes.LoginScreen.route) {
            LoginScreen(
                navController = outerNavController
            )
        }

        composable(Routes.RegisterScreen.route) {
            RegisterScreen(navController = outerNavController)
        }

        composable(Routes.ForgotPassScreen.route) {
            ForgotPassScreen(navController = outerNavController)
        }

        composable(Routes.MainScreen.route) {
            BottomNavHost(outerNavController)
        }

        composable(Routes.EditUserProfileScreen.route) {
            EditProfileScreenSetup(navController = outerNavController)
        }
        composable(Routes.NewPostScreen.route){
            NewPostScreen(navController = outerNavController)
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun BottomNavHost(outerNavController: NavController) {
    val innerNavController:NavHostController = rememberNavController()

    BackHandler {
        outerNavController.popBackStack()
    }

    Scaffold(
        bottomBar = { BottomNav(navController = innerNavController) }
    ) {
        NavHost(innerNavController, startDestination = Routes.Home.route) {
            composable(Routes.Home.route) {
                HomeScreen(outerNavController = outerNavController)
            }
            composable(Routes.Profile.route) {
                UserProfileScreen(outerNavController = outerNavController, innerNavController = innerNavController)
            }
            composable(Routes.Chat.route) {
                ChatScreen(navController = innerNavController)
            }
        }
    }
}


