package com.example.bitter

import Bitter.R
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.AndroidUiDispatcher.Companion.Main
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.bitter.data.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject

var globalKey: String = ""
var globalUsername: String = ""
var logout:Boolean = false


@Composable
fun MainPageStart(navController: NavController){

    var backPressedTime: Long = 0
    logout = false
//    val keyPref = LocalContext.current.getSharedPreferences("authkey",Context.MODE_PRIVATE)
//    globalUsername = keyPref.getString("uname",null).toString()
//    globalKey = keyPref.getString("key",null).toString()

    MainScreen(navController = navController)

    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current

    BackHandler {
        val t = System.currentTimeMillis()

        if (t - backPressedTime > 2000){
            backPressedTime = t
            Toast.makeText(context,"Press back again to logout",Toast.LENGTH_SHORT).show()
        }
        else{
            val postform = JSONObject()
            postform.put("subject","logout")
            postform.put("uname",globalUsername)
            postform.put("key",globalKey)

            postForm(postform){ ret->
                when (ret.getString("status")) {
                    "success" -> {
                        logout = true
                        coroutineScope.launch {
                            navController.navigate(NavRoutes.LoginPage.route)
                        }
                    }
                    else -> {
                    }
                }
            }
        }
    }
}

@Composable
fun MainScreen(navController: NavController) {
    var contentValue by remember {
        mutableStateOf("")
    }

    val postItems = remember {
        mutableStateListOf<PostItemData>()
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    val listState = rememberLazyListState()

    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .fillMaxWidth()
        ) {

            TopAppBar(
                title = {
                    Text(text = "B-itter")
                },
                backgroundColor = Color.White,
                contentColor = Color.Blue,
                actions = {
                    Box(
                        Modifier.wrapContentSize(Alignment.TopEnd)
                    ) {
                        IconButton(onClick = {
                            expanded = true
                        }) {
                            AsyncImage(
                                model = "$postUrl/images/$globalUsername",
                                contentDescription = "icon",
                                placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(50.dp)
                            )
                        }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            DropdownMenuItem(onClick = {
                                expanded = false
                                logout = true
                                navController.navigate(NavRoutes.UserProfilePage.route)

                            }) {
                                Text("Profile")
                            }
                            DropdownMenuItem(onClick = {
                                expanded = false
                                val postform = JSONObject()
                                postform.put("subject","logout")
                                postform.put("uname",globalUsername)
                                postform.put("key",globalKey)

                                postForm(postform){ ret ->
                                    when (ret.getString("status")) {
                                        "success" -> {
                                            logout = true
                                            coroutineScope.launch(Main) {
                                                navController.navigate(NavRoutes.LoginPage.route)
                                            }
                                        }
                                        else -> {
                                        }
                                    }
                                }
                            }) {
                                Text("Logout")
                            }
                        }
                    }
                },
                elevation = AppBarDefaults.TopAppBarElevation

            )
        }

        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .weight(1f)
        ) {
            LazyColumn(
                state = listState,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier)
            {

                items(postItems) { item ->
                    PostItem(
                        username = item.username,
                        content = item.content,
                        lc = item.lc,
                        key = globalKey,
                        postId = item.postId,
                        isliked = item.isliked,
                        byuser = item.byuser
                    )
                }

            }
        }

        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.padding(start = 7.dp, bottom = 7.dp)
            ) {
                Button(
                    onClick = {
                        if(contentValue != "") {
                            val postform = JSONObject()
                            postform.put("subject", "sendpost")
                            postform.put("uname", globalUsername)
                            postform.put("key", globalKey)
                            postform.put("content", contentValue)
                            coroutineScope.launch(IO) {

                                postForm(postform){ ret ->
                                    when (ret.getString("status")) {
                                        "success" -> {
                                            contentValue = ""
                                        }
                                        else -> {
                                        }
                                    }
                                }
                                listState.animateScrollToItem(postItems.size)
                            }
                        }
                    }
                ) {
                    Text(
                        text = "Post"
                    )
                }
            }
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                TextField(
                    value = contentValue,
                    onValueChange = { contentValue = it },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.White
                    )
                )
            }
        }
    }

    LaunchedEffect(key1 = null) {
        val postform = JSONObject()
        postform.put("subject", "getpost")
        postform.put("uname", globalUsername)
        postform.put("key", globalKey)

        coroutineScope.launch(IO) {
            while (true) {
                if (logout) break

                postForm(postform){ ret ->
                    when (ret.getString("status")) {
                        "success" -> {
                            val data = ret.getJSONObject("data")
                            postItems.clear()
                            for (i in data.keys()) {
                                val item = data.getJSONObject(i)
                                postItems.add(
                                    element = PostItemData(
                                        postId = i,
                                        username = item.getString("uname"),
                                        userId = item.getString("uid"),
                                        content = item.getString("content"),
                                        lc = item.getString("lc"),
                                        isliked = item.getInt("islike"),
                                        byuser = globalUsername
                                    )
                                )
                            }
                        }
                        else -> {
                            println(ret.getString("status"))
                        }
                    }
                }
                delay(2500)
            }
        }
    }

}
