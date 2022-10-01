package com.example.bitter

import LoginScreenSetup
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bitter.data.Routes
import com.example.bitter.data.postUrl
import com.example.bitter.main.MainScreenSetup
import com.example.bitter.passwordReset.ForgotPassScreen
import com.example.bitter.profile.ProfileScreenSetup
import com.example.bitter.register.RegisterScreen
import com.example.bitter.userdetails.UserDetailScreen
import kotlin.system.exitProcess


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // url
        val url = intent.getStringExtra("url")
        if (url != null) {
            postUrl = "http://$url"
        }
        // url
        setContent {

            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = Routes.LoginScreen.route
            ){
                composable(Routes.LoginScreen.route){
                    LoginScreenSetup(navController = navController)
                }
                composable(Routes.RegisterScreen.route){
                    RegisterScreen(navController = navController)
                }
                composable(Routes.ForgotPassScreen.route){
                    ForgotPassScreen(navController = navController)
                }
                composable(Routes.MainScreen.route){
                    MainScreenSetup(navController = navController)
                }
                composable(Routes.UserDetailsScreen.route){
                    UserDetailScreen(navController = navController)
                }
                composable(Routes.UserProfileScreen.route){
                    ProfileScreenSetup(navController = navController)
                }
            }

            BackHandler { exitProcess(0) }
        }
    }
}
