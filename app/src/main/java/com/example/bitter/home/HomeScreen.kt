package com.example.bitter.home

import Bitter.R
import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
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


@OptIn(ExperimentalAnimationApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    outerNavController: NavController,
    viewModel: HomeViewModel = viewModel()
) {
    val context = LocalContext.current

    val keyPref = context.getSharedPreferences("authkey", Context.MODE_PRIVATE)
    val uname = keyPref.getString("uname", null)
    val token = keyPref.getString("token", null)
    val editor = keyPref.edit()
    viewModel.uname = uname
    viewModel.token = token
    viewModel.editor = editor
    viewModel.navController = outerNavController

    val postItems = viewModel.postItems
    val isRefreshing = viewModel.isRefreshing.value

    val listState = rememberLazyListState()
    var showFloatingAction by remember {
        mutableStateOf(true)
    }


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
            AnimatedVisibility(visible = showFloatingAction, enter = scaleIn(), exit = scaleOut()) {
                FloatingActionButton(
                    onClick = {
                        outerNavController.navigate(Routes.NewPostScreen.route)
                    }
                ) {
                    Icon(Icons.Filled.Add, "New Post")
                }
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
                        .padding(5.dp)
                )
                {
                    items(postItems) { item ->
                        PostCard(
                            index = postItems.indexOf(item),
                            content = item.content,
                            lc = item.lc,
                            token = token ?: "",
                            postId = item.postId,
                            isLiked = item.isliked,
                            byUser = item.byuser,
                            datetime = item.datetime
                        ){
                            if(postItems[it].isliked == 0){
                                postItems[it].lc += 1
                                postItems[it].isliked = 1
                            }
                            else{
                                postItems[it].lc -= 1
                                postItems[it].isliked = 0
                            }
                            listOf(postItems[it].lc,postItems[it].isliked)
                        }
                    }
                }


                showFloatingAction = listState.isScrollingDown()
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

@Composable
private fun LazyListState.isScrollingDown(): Boolean {
    var previousIndex by remember(this) { mutableStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableStateOf(firstVisibleItemScrollOffset) }
    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousScrollOffset >= firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}