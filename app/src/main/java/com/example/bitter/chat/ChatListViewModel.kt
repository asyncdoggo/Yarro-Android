package com.example.bitter.chat

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class ChatListViewModel(
    private val stateHandle: SavedStateHandle
):ViewModel() {

    var msg = stateHandle.getStateFlow("msg","")

    var sender = mutableStateOf("uname")
    private set

    var messageList = mutableStateListOf<Message>()
    private set

    fun getMessages(){
        messageList.addAll(listOf(
            Message("user2",LoremIpsum((1..30).random()).values.joinToString(), self = listOf(true,false).random()),
            Message("user2",LoremIpsum((1..30).random()).values.joinToString(), self = listOf(true,false).random()),
            Message("user2",LoremIpsum((1..30).random()).values.joinToString(), self = listOf(true,false).random()),
            Message("user2",LoremIpsum((1..30).random()).values.joinToString(), self = listOf(true,false).random()),
            Message("user2",LoremIpsum((1..30).random()).values.joinToString(), self = listOf(true,false).random()),
            Message("user2",LoremIpsum((1..30).random()).values.joinToString(), self = listOf(true,false).random()),
            Message("user2",LoremIpsum((1..30).random()).values.joinToString(), self = listOf(true,false).random()),
            Message("user2",LoremIpsum((1..30).random()).values.joinToString(), self = listOf(true,false).random()),
            Message("user2",LoremIpsum((1..30).random()).values.joinToString(), self = listOf(true,false).random()),
            Message("user2",LoremIpsum((1..30).random()).values.joinToString(), self = listOf(true,false).random()),
            Message("user2",LoremIpsum((1..30).random()).values.joinToString(), self = listOf(true,false).random()),
        ))
    }

    fun setMsg(it: String) {
        stateHandle["msg"] = it
    }

    fun sendMessage() {
        TODO("Not yet implemented")
    }

}