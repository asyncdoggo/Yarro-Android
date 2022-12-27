package com.example.bitter.main

import LoginScreen
import androidx.compose.animation.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.navigation.*
import com.example.bitter.LoadingScreen
import com.example.bitter.data.Routes
import com.example.bitter.editprofile.EditProfileScreenSetup
import com.example.bitter.home.NewPostScreen
import com.example.bitter.login.VerifyScreen
import com.example.bitter.register.RegisterScreen
import com.example.bitter.resetpass.ForgotPassScreen
import com.example.bitter.userprofile.UserProfileScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainNav() {
    val outerNavController = rememberAnimatedNavController()

    AnimatedNavHost(
        navController = outerNavController,
        startDestination = Routes.LoadingScreen.route,
        enterTransition = {
            slideInHorizontally()
        },
        exitTransition = {
            slideOutHorizontally()
        }
    ) {

        composable(route = Routes.LoadingScreen.route){
            LoadingScreen(outerNavController)
        }

        composable(route = Routes.LoginScreen.route) {
            LoginScreen(navController = outerNavController)
        }
        composable(Routes.RegisterScreen.route) {
            RegisterScreen(navController = outerNavController)
        }

        composable(Routes.ForgotPassScreen.route) {
            ForgotPassScreen(navController = outerNavController)
        }

        composable(Routes.BottomNav.route) {
            BottomNavHost(outerNavController)
        }

        composable(Routes.EditUserProfileScreen.route) {
            EditProfileScreenSetup(navController = outerNavController)
        }
        composable(Routes.NewPostScreen.route){
            NewPostScreen(navController = outerNavController)
        }
        composable(Routes.VerifyScreen.route){
            VerifyScreen(navController = outerNavController)
        }


        composable(
            Routes.ProfileScreen.route + "/{username}",
            arguments = listOf(navArgument("username") { type = NavType.StringType })
        ) {
            UserProfileScreen(
                outerNavController = outerNavController,
                username = it.arguments?.getString("username")?:""
            )
        }
    }
}