package com.example.bitter.home

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NewPostScreen(navController: NavController) {
    val context = LocalContext.current
    val keyPref = context.getSharedPreferences("authkey", Context.MODE_PRIVATE)
    val token = keyPref.getString("token", null)
    val toast = Toast.makeText(LocalContext.current,"Cannot connect, please check your network connection",Toast.LENGTH_LONG)
    val viewModel = viewModel<NewPostViewModel>()
    val contentValue by viewModel.contentValue.collectAsState()

    BackHandler {
        navController.popBackStack()
    }


    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val toast1 = Toast.makeText(context,"Content can't be empty",Toast.LENGTH_SHORT)
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(text = "New Post") },
                        navigationIcon = {
                            IconButton(onClick = {
                                navController.popBackStack()
                            }) {
                                Icon(Icons.Default.ArrowBack, "exit")
                            }
                        },
                        actions = {
                            Box(
                                Modifier.wrapContentSize(Alignment.TopEnd)
                            ) {
                                IconButton(onClick = {
                                    try {
                                        if(!viewModel.addPost(token?:"",navController)){
                                            toast1.show()
                                        }
                                    }
                                    catch (e:Exception){
                                        toast.show()
                                    }
                                }) {
                                    Icon(
                                        imageVector = Icons.Filled.Check,
                                        contentDescription = "Done"
                                    )
                                }
                            }
                        },
                        backgroundColor = MaterialTheme.colors.primary,
                        contentColor = MaterialTheme.colors.onPrimary,
                        elevation = 10.dp,
                    )
                }
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextField(
                        value = contentValue,
                        onValueChange = { viewModel.onContentValueChange(it) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

