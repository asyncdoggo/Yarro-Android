package com.example.bitter.userprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bitter.data.PostDatabase
import com.example.bitter.data.PostItem
import com.example.bitter.data.PostRepository
import com.example.bitter.util.ApiService
import kotlinx.coroutines.launch
import kotlinx.serialization.json.jsonPrimitive

class UserProfileViewModel(
    private val stateHandle: SavedStateHandle
):ViewModel() {
    var fullname = stateHandle.getStateFlow("fullname","")
    var bio = stateHandle.getStateFlow("bio","")


    fun getPosts(uname: String): LiveData<List<PostItem>>? {
        val postDao = PostDatabase.instance?.postDao()
        val repository = postDao?.let { PostRepository(it) }
        return repository?.getPosts(uname)
    }

    fun getName(token: String?, uname: String?){
        viewModelScope.launch {
            val response = ApiService.getFullName(token,uname)
            when(response["status"]?.jsonPrimitive?.content.toString()){
                "success" -> {
                    stateHandle["fullname"] = response["name"]?.jsonPrimitive?.content
                    stateHandle["bio"] = response["bio"]?.jsonPrimitive?.content
                }
                else -> {

                }
            }
        }
    }
}