package com.example.bitter.home

import Bitter.R
import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.bitter.data.Routes
import com.example.bitter.postUrl
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState


@Composable
fun StartHomeScreen(navController: NavController) {
    HomeScreen(outerNavController = navController)
}



@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    outerNavController: NavController,
    viewModel: HomeViewModel = viewModel()
) {
    val context = LocalContext.current

    val keyPref = context.getSharedPreferences("authkey", Context.MODE_PRIVATE)
    val uname = keyPref.getString("uname", null)
    val key = keyPref.getString("key", null)
    val editor = keyPref.edit()
    viewModel.uname = uname
    viewModel.key = key
    viewModel.editor = editor
    viewModel.navController = outerNavController

    val postItems = viewModel.postItems
    val isRefreshing = viewModel.isRefreshing.value

    val listState = rememberLazyListState()


    Scaffold(
        scaffoldState = rememberScaffoldState(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "B-itter")
                },
                backgroundColor = MaterialTheme.colors.background,
                contentColor = MaterialTheme.colors.onBackground,
                actions = {
                    Box(
                        Modifier.wrapContentSize(Alignment.TopEnd)
                    ) {
                        AsyncImage(
                            model = "$postUrl/images/$uname",
                            contentDescription = "icon",
                            placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(50.dp)
                        )
                    }
                },
                elevation = AppBarDefaults.TopAppBarElevation
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    outerNavController.navigate(Routes.NewPostScreen.route)
                }
            ) {
                Icon(Icons.Filled.Add, "New Post")
            }
        },
        modifier = Modifier
            .background(MaterialTheme.colors.background)
            .fillMaxWidth()
            .fillMaxHeight(0.9f)

    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(MaterialTheme.colors.background)
                .fillMaxSize()
        ) {


            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing),
                onRefresh = {
                    viewModel.getPost()
                },
            ) {
                LazyColumn(
                    state = listState,
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.fillMaxWidth()
                )
                {
                    items(postItems) { item ->
                        PostItem(
                            username = item.username,
                            content = item.content,
                            lc = item.lc,
                            key = key ?: "",
                            postId = item.postId,
                            isliked = item.isliked,
                            byuser = item.byuser,
                            datetime = item.datetime
                        )
                    }

                }
            }
        }
    }

    LaunchedEffect(key1 = true){
        viewModel.getPost()
    }

    var backPressedTime: Long = 0
    BackHandler {
        val t = System.currentTimeMillis()

        if (t - backPressedTime > 2000) {
            backPressedTime = t
            Toast.makeText(context, "Press back again to logout", Toast.LENGTH_SHORT).show()
        } else viewModel.logout()
    }



}