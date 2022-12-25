package com.example.bitter.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun ChatScreen(
    navController: NavController,
    viewModel: ChatViewModel = viewModel()
) {
    val unameList = viewModel.unameList
    viewModel.getUnameList()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ){
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp)
            ) {
                Text(
                    text = "Messages",
                    color = MaterialTheme.colors.onBackground
                )
            }

            LazyColumn{
                item {
                    Spacer(
                        modifier = Modifier.fillMaxWidth()
                            .size(1.dp)
                            .background(Color.Gray)
                    )
                }
                items(unameList){ item ->
                    ChatItem(uname = item, message = "msg") {
                        navController.navigate("")
                    }
                    Spacer(
                        modifier = Modifier.fillMaxWidth()
                            .size(1.dp)
                            .background(Color.Gray)
                    )

                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatScreenPrev() {
    ChatScreen(navController = rememberNavController())
}