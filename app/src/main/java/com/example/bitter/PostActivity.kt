package com.example.bitter

import Bitter.R
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
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
import androidx.compose.ui.platform.LocalContext
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
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.SocketTimeoutException

class PostActivity : ComponentActivity() {
    private var key: String = ""
    private var username: String = ""
    private var logout:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var backPressedTime: Long = 0
        logout = false
        setContent {
            val keyPref = LocalContext.current.getSharedPreferences("authkey",Context.MODE_PRIVATE)
            username = keyPref.getString("uname",null).toString()
            key = keyPref.getString("key",null).toString()
            MainScreen()

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
                    postform.put("uname",username)
                    postform.put("key",key)

                    postForm(postform,callback = object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            e.printStackTrace()
                        }
                        override fun onResponse(call: Call, response: Response) {
                            val responseString = String(response.body.bytes())
                            val ret = JSONObject(responseString)
                            try {
                                when (ret.getString("status")) {
                                    "success" -> {
                                        logout = true
                                        val intent = Intent(context,MainActivity::class.java)
                                        context.startActivity(intent)
                                    }
                                    else -> {
                                    }
                                }
                            }
                            catch(e: JSONException){
                                e.printStackTrace()
                            }
                        }
                    })
                }
            }
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
                                    val intent = Intent(context,ProfileActivity::class.java)
                                    intent.putExtra("uname",username)
                                    intent.putExtra("key",key)
                                    logout = true
                                    context.startActivity(intent)

                                }) {
                                    Text("Profile")
                                }
                                DropdownMenuItem(onClick = {
                                    expanded = false
                                    val postform = JSONObject()
                                    postform.put("subject","logout")
                                    postform.put("uname",username)
                                    postform.put("key",key)

                                    postForm(postform,callback = object : Callback {
                                        override fun onFailure(call: Call, e: IOException) {
                                            e.printStackTrace()
                                        }
                                        override fun onResponse(call: Call, response: Response) {
                                            val responseString = String(response.body.bytes())
                                            val ret = JSONObject(responseString)
                                            try {
                                                when (ret.getString("status")) {
                                                    "success" -> {
                                                        logout = true
                                                        val intent = Intent(context,MainActivity::class.java)
                                                        context.startActivity(intent)
                                                    }
                                                    else -> {
                                                    }
                                                }
                                            }
                                            catch(e: JSONException){
                                                e.printStackTrace()
                                            }
                                        }
                                    })
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
                            key = key,
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
                                postform.put("uname", username)
                                postform.put("key", key)
                                postform.put("content", contentValue)
                                coroutineScope.launch(IO) {
                                    postForm(postform, callback = object : Callback {
                                        override fun onFailure(call: Call, e: IOException) {
                                            e.printStackTrace()
                                        }

                                        override fun onResponse(call: Call, response: Response) {
                                            val responseString = String(response.body.bytes())
                                            val ret = JSONObject(responseString)
                                            try {
                                                when (ret.getString("status")) {
                                                    "success" -> {
                                                        contentValue = ""
                                                    }
                                                    else -> {
                                                    }
                                                }
                                            } catch (e: JSONException) {
                                                e.printStackTrace()
                                            } catch (e: SocketTimeoutException) {
                                                Toast.makeText(
                                                    context,
                                                    "Network Error",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                                logout = true
                                            }
                                        }
                                    })
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

        LaunchedEffect(key1 = true) {
            val postform = JSONObject()
            postform.put("subject", "getpost")
            postform.put("uname", username)
            postform.put("key", key)

            coroutineScope.launch(IO) {
                while (true) {
                    if (logout) break
                    postForm(postform, callback = object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            e.printStackTrace()
                        }

                        override fun onResponse(call: Call, response: Response) {
                            val responseString = String(response.body.bytes())
                            val ret = JSONObject(responseString)
                            try {
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
                                                    byuser = username
                                                )
                                            )
                                        }
                                    }
                                    else -> {
                                        println(ret.getString("status"))
                                    }
                                }
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                        }
                    })
                    delay(2500)
                }
            }
        }

    }

}