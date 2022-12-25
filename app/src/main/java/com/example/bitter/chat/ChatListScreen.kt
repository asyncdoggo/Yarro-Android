package com.example.bitter.chat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.bitter.ui.theme.BitterTheme


@Composable
fun UserChatScreen(
    navController: NavController,
    viewModel: ChatListViewModel = viewModel()
) {
    val messages = viewModel.messageList
    val msg by viewModel.msg.collectAsState()

    viewModel.getMessages()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = viewModel.sender.value,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { navController.popBackStack() }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                )
            },
            bottomBar = {
                BottomAppBar(
                    backgroundColor = MaterialTheme.colors.surface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(60.dp)
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = msg, onValueChange = { viewModel.setMsg(it) },
                        trailingIcon = {
                            IconButton(
                                onClick = { viewModel.sendMessage() },
                                enabled = msg.trim().isNotEmpty()
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Send,
                                    contentDescription = "Send"
                                )
                            }
                        }
                    )
                }
            }
        ) {
            BitterTheme {
                Surface(
                    modifier = Modifier.padding(it)
                ) {
                    Box(modifier = Modifier.padding(top = 10.dp))
                    {
                        LazyColumn(reverseLayout = true) {
                            items(messages) { item ->
                                MessageCard(message = item, self = item.self)
                            }
                        }
                    }
                }
            }
        }
    }
}