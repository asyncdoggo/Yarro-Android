package com.example.bitter.main

import LoginScreen
import android.annotation.SuppressLint
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bitter.LoadingScreen
import com.example.bitter.chat.ChatScreen
import com.example.bitter.data.PostDatabase
import com.example.bitter.data.PostItem
import com.example.bitter.data.PostRepository
import com.example.bitter.data.Routes
import com.example.bitter.editprofile.EditProfileScreenSetup
import com.example.bitter.home.HomeScreen
import com.example.bitter.home.NewPostScreen
import com.example.bitter.home.formatTo
import com.example.bitter.home.toDate
import com.example.bitter.register.RegisterScreen
import com.example.bitter.resetpass.ForgotPassScreen
import com.example.bitter.userprofile.UserProfileScreen
import com.example.bitter.util.ApiService
import kotlinx.coroutines.launch
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

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

    val context = LocalContext.current
    val keyPref = context.getSharedPreferences("authkey", Context.MODE_PRIVATE)
    val token = keyPref.getString("token", null)
    val editor = keyPref.edit()
    val latestPost = keyPref.getString("post","0")

    val coroutineScope = rememberCoroutineScope()


    val postDao = PostDatabase.getInstance(context).postDao()
    val repository = PostRepository(postDao)

    LaunchedEffect(key1 = true){
        coroutineScope.launch {
            val response = ApiService.getPosts(token,latestPost?:"")
            if(response.status == "success"){
                val data = response.data
                if (data != null) {
                    var pid = "0"
                    for(i in data.keys){
                        val item = data.getValue(i)
                        var datetime = item.jsonObject["datetime"]?.jsonPrimitive?.content.toString()
                        datetime = datetime.toDate()?.formatTo("dd MMM yyyy,  K:mm a") ?: ""
                        repository.insert(
                            PostItem(
                                postId = i,
                                username = item.jsonObject["uname"]?.jsonPrimitive?.content.toString(),
                                content = item.jsonObject["content"]?.jsonPrimitive?.content.toString(),
                                lc = item.jsonObject["lc"]?.jsonPrimitive?.int?:0,
                                dlc = item.jsonObject["dlc"]?.jsonPrimitive?.int?:0,
                                isliked = item.jsonObject["islike"]?.jsonPrimitive?.int?:0,
                                isdisliked = item.jsonObject["isdislike"]?.jsonPrimitive?.int?:0,
                                byuser = item.jsonObject["uname"]?.jsonPrimitive?.content.toString(),
                                datetime = datetime
                            )
                        )
                        pid = i
                    }
                    editor.putString("post", pid)
                    editor.apply()
                }
            }
        }
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
