package com.example.bitter

import LoginScreenSetup
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bitter.data.Routes
import com.example.bitter.main.MainScreenSetup
import com.example.bitter.passwordReset.ForgotPassScreen
import com.example.bitter.profile.ProfileScreenSetup
import com.example.bitter.register.RegisterScreen
import com.example.bitter.ui.theme.BitterTheme
import com.example.bitter.userdetails.UserDetailScreen

var postUrl: String = ""

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
            BitterTheme {


                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Routes.LoginScreen.route + "/{error}"
                ) {
                    composable(
                        route = Routes.LoginScreen.route + "/{error}",
                        arguments = listOf(
                            navArgument("error") {
                                type = NavType.StringType
                            }
                        )
                    ) {
                        LoginScreenSetup(
                            navController = navController,
                            it.arguments?.getString("error")
                        )
                    }

                    composable(Routes.RegisterScreen.route) {
                        RegisterScreen(navController = navController)
                    }

                    composable(Routes.ForgotPassScreen.route) {
                        ForgotPassScreen(navController = navController)
                    }

                    composable(Routes.MainScreen.route) {
                        MainScreenSetup(navController = navController)
                    }

                    composable(Routes.UserDetailsScreen.route) {
                        UserDetailScreen(navController = navController)
                    }

                    composable(Routes.UserProfileScreen.route) {
                        ProfileScreenSetup(navController = navController)
                    }
                }

                BackHandler { finishAndRemoveTask() }
            }
        }
    }
}
