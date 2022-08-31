package com.example.bitter

import Bitter.R
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class PostActivity : ComponentActivity() {
    private  var key: String = ""
    private  var username: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        key = intent.getStringExtra("key").toString()
        username = intent.getStringExtra("uname").toString()
        setContent {
            MainScreen()
        }
    }


    @Preview(showBackground = true)
    @Composable
    fun MainScreen() {
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
        var lkey by remember {
            mutableStateOf("")
        }
        val coroutineScope = rememberCoroutineScope()


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
                                    model = "$postUrl/images/$username.png",
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
                                    //TODO:GOTO PROFILE
                                }) {
                                    Text("Profile")
                                }
                                DropdownMenuItem(onClick = {
                                    expanded = false
                                    //TODO: GOTO LOGOUT
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
                    .padding(10.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    LazyColumn(
                        state = listState,
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.background(Color(0xFFA3CACD))

                    ){

                        items(postItems) { item ->
                            PostItem(
                                username = item.username,
                                content = item.content
                            )
                        }

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
                            /*TODO POST*/
                            postItems.add(
                                element = PostItemData(
                                    username = "opal",
                                    content = "This is a message"
                                )
                            )
                            coroutineScope.launch{
                                listState.animateScrollToItem(postItems.size)
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

        LaunchedEffect(key1 = lkey, block = {
            val postform = JSONObject()
            postform.put("subject", "getpost")
            postform.put("uname", username)
            postform.put("key", key)

            coroutineScope.launch(IO) {
                while(true){

                postForm(postform, callback = object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        e.printStackTrace()
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val responseString = String(response.body.bytes())
                        val ret = JSONObject(responseString)
                        when (ret.getString("status")) {
                            "success" -> {
                                val usr = ret.getJSONArray("users")
                                val posts = ret.getJSONArray("posts")
                                postItems.clear()
                                for(i in 0 until usr.length()){
                                    postItems.add(
                                        element = PostItemData(
                                            username = usr[i] as String,
                                            content = posts[i] as String
                                        )
                                    )
                                }
                            }
                            "badpasswd" -> {

                            }
                            else -> {

                            }
                        }
                    }

                })
                    delay(3000)
                }
            }
        })

    }


}

