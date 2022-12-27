package com.example.bitter.main

import Bitter.R
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import com.example.bitter.data.PostDatabase
import com.example.bitter.data.PostItem
import com.example.bitter.data.PostRepository
import com.example.bitter.data.Routes
import com.example.bitter.home.HomeScreen
import com.example.bitter.home.formatTo
import com.example.bitter.home.toDate
import com.example.bitter.postUrl
import com.example.bitter.userprofile.UserData
import com.example.bitter.util.ApiService
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive


@OptIn(ExperimentalAnimationApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun BottomNavHost(outerNavController: NavController) {
    val context = LocalContext.current
    val keyPref = context.getSharedPreferences("authkey", Context.MODE_PRIVATE)
    val editor = keyPref.edit()
    val token = keyPref.getString("token", null)
    val uname = keyPref.getString("uname", null)
    val coroutineScope = rememberCoroutineScope()
    val postDao = PostDatabase.instance?.postDao()
    val repository = postDao?.let { PostRepository(it) }
    val innerNavController = rememberAnimatedNavController()
    var expanded by remember {
        mutableStateOf(false)
    }
    val toast = Toast.makeText(
        LocalContext.current,
        "Cannot connect, please check your network connection",
        Toast.LENGTH_LONG
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "B-itter")
                },
                backgroundColor = Color.Transparent,
                actions = {
                    Box(
                        Modifier.wrapContentSize(Alignment.TopEnd)
                    ) {
                        IconButton(onClick = { expanded = true }) {
                            AsyncImage(
                                model = "$postUrl/images/$uname",
                                contentDescription = "icon",
                                placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(50.dp)
                            )
                        }
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(onClick = {
                            try {
                                logout(coroutineScope ,editor, outerNavController, token?:"")
                            } catch (e: Exception) {
                                toast.show()
                            }
                        }) {
                            Text(text = "Logout")
                        }
                    }
                },
                elevation = 0.dp,
            )
        },
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
                UserData(
                    it = PaddingValues(0.dp),
                    outerNavController = outerNavController,
                    uname = it.arguments?.getString("username")?:""
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


fun logout(coroutineScope: CoroutineScope, editor: SharedPreferences.Editor, navController: NavController, token: String) {
    val postDao = PostDatabase.instance?.postDao()
    editor.clear()
    editor.apply()
    coroutineScope.launch {
        ApiService.logout(token)
        postDao?.let { PostRepository(it) }?.deleteAll()
        navController.navigate(Routes.LoginScreen.route)
    }
}