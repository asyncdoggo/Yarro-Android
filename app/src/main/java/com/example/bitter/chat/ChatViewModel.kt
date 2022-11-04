package com.example.bitter.chat

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class ChatViewModel: ViewModel() {
    val unameList = mutableStateListOf<String>()

    fun getUnameList(){
        unameList.addAll(listOf("uname1","uname2","uname3","uname4","uname5","uname6","uname7","uname8"))
    }
}
