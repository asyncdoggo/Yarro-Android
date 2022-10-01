package com.example.bitter.main

import Bitter.R
import android.content.Context
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.bitter.data.*
import kotlinx.coroutines.launch


@Composable
fun MainScreenSetup(navController: NavController){

    val context = LocalContext.current
    val keyPref = context.getSharedPreferences("authkey", Context.MODE_PRIVATE)
    val uname = keyPref.getString("uname", null)
    val key = keyPref.getString("key", null)
    val editor = keyPref.edit()
    val viewModel = viewModel<MainViewModel>()



    MainScreen(navController = navController,uname,key)

    var backPressedTime: Long = 0
    BackHandler {
        val t = System.currentTimeMillis()

        if (t - backPressedTime > 2000){
            backPressedTime = t
            Toast.makeText(context,"Press back again to logout",Toast.LENGTH_SHORT).show()
        }
        else viewModel.logout(uname?:"",key?:"",editor,navController)
    }
}


@Composable
fun MainScreen(navController: NavController, uname: String?, key: String?) {

    val viewModel = viewModel<MainViewModel>()
    val contentValue by viewModel.contentValue.collectAsState()

    val postItems = viewModel.postItems

    val expanded by viewModel.expanded.collectAsState()

    val context = LocalContext.current
    val keyPref = context.getSharedPreferences("authkey", Context.MODE_PRIVATE)
    val editor = keyPref.edit()

    val listState = rememberLazyListState()

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
                            viewModel.setExpanded(true)
                        }) {
                            AsyncImage(
                                model = "$postUrl/images/$uname",
                                contentDescription = "icon",
                                placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(50.dp)
                            )
                        }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { viewModel.setExpanded(false) }
                        ) {
                            DropdownMenuItem(onClick = {
                                viewModel.dropDownClicked(navController)
                            }) {
                                Text("Profile")
                            }
                            DropdownMenuItem(onClick = {
                                viewModel.setExpanded(false)
                                viewModel.logout(uname?:"",key?:"",editor,navController)
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
                        key = key?:"",
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
                            viewModel.postButtonOnClick(uname,key)
                            coroutineScope.launch {
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
                    onValueChange = { viewModel.onContentValueChange(it) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.White
                    )
                )
            }
        }
    }
    if(viewModel.persistkey.value){
        LaunchedEffect(key1 = true) {
            viewModel.persistkey.value = false
            viewModel.getPostLoop(uname,key)
        }
    }

}

@Preview
@Composable
fun MainPreview() {
    MainScreenSetup(navController = NavController(LocalContext.current))
}