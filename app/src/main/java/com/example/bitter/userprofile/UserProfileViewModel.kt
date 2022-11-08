package com.example.bitter.userprofile

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bitter.data.PostDatabase
import com.example.bitter.data.PostItem
import com.example.bitter.data.PostRepository
import com.example.bitter.util.ApiService
import kotlinx.coroutines.launch
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class UserProfileViewModel(
    private val stateHandle: SavedStateHandle
):ViewModel() {
    var fullname = stateHandle.getStateFlow("fullname","")
    val postItems = mutableStateListOf<PostItem>()


    private fun setVal(k:String, v:String){
        stateHandle[k] = v
    }

    fun getPosts(context: Context,uname: String): LiveData<List<PostItem>> {
        val postDao = PostDatabase.getInstance(context).postDao()
        val repository = PostRepository(postDao)
        return repository.getPosts(uname)
    }

    fun getName(token: String?){
        viewModelScope.launch {
            val response = ApiService.getFullName(token)
            when(response["status"]?.jsonPrimitive?.content.toString()){
                "success" -> {
                    stateHandle["fullname"] = response["name"]?.jsonPrimitive?.content
                }
                else -> {

                }
            }
        }
    }
    fun updateLikes(context: Context,token: String?) {
        val postDao = PostDatabase.getInstance(context).postDao()
        val repository = PostRepository(postDao)
        viewModelScope.launch {
            val response = ApiService.updateLikeData(token)
            if(response.status == "success") {
                val data = response.data
                if (data != null) {
                    for(i in data.keys) {
                        val item = data.getValue(i)
                        repository.update(
                            i,
                            item.jsonObject["lc"]?.jsonPrimitive?.content?.toInt()?:0,
                            item.jsonObject["dlc"]?.jsonPrimitive?.content?.toInt()?:0,
                            item.jsonObject["islike"]?.jsonPrimitive?.content?.toInt()?:0,
                            item.jsonObject["isdislike"]?.jsonPrimitive?.content?.toInt()?:0,
                        )
                    }
                }
            }
        }

    }
}