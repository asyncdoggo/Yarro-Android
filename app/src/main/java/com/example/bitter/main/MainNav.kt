package com.example.bitter.main

import LoginScreen
import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.animation.*
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.*
import com.example.bitter.LoadingScreen
import com.example.bitter.data.PostDatabase
import com.example.bitter.data.PostItem
import com.example.bitter.data.PostRepository
import com.example.bitter.data.Routes
import com.example.bitter.editprofile.EditProfileScreenSetup
import com.example.bitter.home.HomeScreen
import com.example.bitter.home.NewPostScreen
import com.example.bitter.home.formatTo
import com.example.bitter.home.toDate
import com.example.bitter.login.VerifyScreen
import com.example.bitter.register.RegisterScreen
import com.example.bitter.resetpass.ForgotPassScreen
import com.example.bitter.userprofile.UserProfileScreen
import com.example.bitter.util.ApiService
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import kotlinx.coroutines.launch
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

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



@OptIn(ExperimentalAnimationApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun BottomNavHost(outerNavController: NavController) {
    val context = LocalContext.current
    val keyPref = context.getSharedPreferences("authkey", Context.MODE_PRIVATE)
    val token = keyPref.getString("token", null)
    val coroutineScope = rememberCoroutineScope()
    val postDao = PostDatabase.instance?.postDao()
    val repository = postDao?.let { PostRepository(it) }
    val innerNavController = rememberAnimatedNavController()
    Scaffold(
        bottomBar = { BottomNav(navController = innerNavController) }
    ) {
        AnimatedNavHost(innerNavController, startDestination = Routes.HomeScreen.route) {
            composable(Routes.HomeScreen.route) {
                HomeScreen(outerNavController = outerNavController)
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
//            composable(Routes.Chat.route) {
//                ChatScreen(navController = innerNavController)
//            }
        }
    }

    LaunchedEffect(key1 = true){
        coroutineScope.launch {
            try {
                val response = ApiService.checkLogin(token?:"")
                when (response.status) {
                    "success" -> {
                    }
                    "email" -> {
                        outerNavController.navigate(Routes.VerifyScreen.route)
                    }
                    else -> {
                        outerNavController.navigate(Routes.LoginScreen.route)
                    }
                }

            } catch (_: Exception) {  }

        }

        coroutineScope.launch {
            val latestPost = repository?.getLatest()
            try {
                val response = ApiService.getPosts(token, latestPost?:0)
                if(response.status == "success"){
                    val data = response.data
                    if (data != null) {
                        for(i in data.keys){
                            val item = data.getValue(i)
                            var datetime = item.jsonObject["datetime"]?.jsonPrimitive?.content.toString()
                            datetime = datetime.toDate()?.formatTo("dd MMM yyyy,  K:mm a") ?: ""
                            repository?.insert(
                                PostItem(
                                    postId = i.toInt(),
                                    content = item.jsonObject["content"]?.jsonPrimitive?.content.toString(),
                                    lc = item.jsonObject["lc"]?.jsonPrimitive?.int?:0,
                                    dlc = item.jsonObject["dlc"]?.jsonPrimitive?.int?:0,
                                    isliked = item.jsonObject["islike"]?.jsonPrimitive?.int?:0,
                                    isdisliked = item.jsonObject["isdislike"]?.jsonPrimitive?.int?:0,
                                    byuser = item.jsonObject["uname"]?.jsonPrimitive?.content.toString(),
                                    datetime = datetime
                                )
                            )
                        }
                    }
                }
            }
            catch (_:Exception){}
        }
    }
}
