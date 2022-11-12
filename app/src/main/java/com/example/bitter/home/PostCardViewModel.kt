package com.example.bitter.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bitter.data.PostDatabase
import com.example.bitter.data.PostRepository
import com.example.bitter.util.ApiService
import kotlinx.coroutines.launch
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class PostCardViewModel : ViewModel() {
    fun updateLike(context: Context, postId: String, token: String) {
        val postDao = PostDatabase.getInstance(context).postDao()
        val repository = PostRepository(postDao)

        viewModelScope.launch {
            try {
                val response = ApiService.likePost(postId, token, true)
                when (response.status) {
                    "success" -> {
                        val data = response.data
                        if (data != null) {
                            repository.update(
                                postId,
                                data.jsonObject["lc"]?.jsonPrimitive?.content?.toInt()?:0,
                                data.jsonObject["dlc"]?.jsonPrimitive?.content?.toInt()?:0,
                                data.jsonObject["islike"]?.jsonPrimitive?.content?.toInt()?:0,
                                data.jsonObject["isdislike"]?.jsonPrimitive?.content?.toInt()?:0,
                            )
                        }
                    }
                }
            }
            catch (_:Exception){}
        }
    }

    fun updateDislike(context: Context, postId: String, token: String) {
        val postDao = PostDatabase.getInstance(context).postDao()
        val repository = PostRepository(postDao)

        viewModelScope.launch {
            try {
                val response = ApiService.likePost(postId, token, false)
                when (response.status) {
                    "success" -> {
                        val data = response.data
                        if (data != null) {
                            repository.update(
                                postId,
                                data.jsonObject["lc"]?.jsonPrimitive?.content?.toInt()?:0,
                                data.jsonObject["dlc"]?.jsonPrimitive?.content?.toInt()?:0,
                                data.jsonObject["islike"]?.jsonPrimitive?.content?.toInt()?:0,
                                data.jsonObject["isdislike"]?.jsonPrimitive?.content?.toInt()?:0,
                            )
                        }
                    }
                }
            }
            catch (_:Exception){}
        }
    }
}