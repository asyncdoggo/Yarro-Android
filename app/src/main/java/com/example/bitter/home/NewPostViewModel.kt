package com.example.bitter.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.bitter.util.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class NewPostViewModel(
    private val savedStateHandle: SavedStateHandle
) :ViewModel(){

    var contentValue = savedStateHandle.getStateFlow("contentValue", "")

    fun addPost(token: String, navController: NavController): Boolean {
        if (contentValue.value.trim() == "") return false
        viewModelScope.launch {
            val response = ApiService.sendPost(token, contentValue.value)
            when (response.status) {
                "success" -> {
                    savedStateHandle["contentValue"] = ""
                    viewModelScope.launch(Dispatchers.Main) {
                        navController.popBackStack()
                    }
                }
                else -> {
                }
            }
        }
        return true
    }

    fun onContentValueChange(it: String) {
        savedStateHandle["contentValue"] = it
    }


}