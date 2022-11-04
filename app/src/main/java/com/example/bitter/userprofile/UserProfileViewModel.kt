package com.example.bitter.userprofile

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.bitter.data.PostItem
import com.example.bitter.home.formatTo
import com.example.bitter.home.toDate
import com.example.bitter.util.ApiService
import kotlinx.coroutines.launch
import kotlinx.serialization.json.int
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

    fun getName(uname: String?,token: String?){
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

    fun getPosts(token: String?,navController: NavController) {

        viewModelScope.launch {
            val response = ApiService.getPosts(token,"true")
            if(response.status == "success"){
                val data = response.data
                if (data != null) {
                    postItems.clear()
                    for(i in data.keys){
                        val item = data.getValue(i)
                        var datetime = item.jsonObject["datetime"]?.jsonPrimitive?.content.toString()
                        datetime = datetime.toDate()?.formatTo("dd MMM yyyy,  K:mm a") ?: ""
                        postItems.add(
                            element = PostItem(
                                postId = i,
                                username = item.jsonObject["uname"]?.jsonPrimitive?.content.toString(),
                                content = item.jsonObject["content"]?.jsonPrimitive?.content.toString(),
                                lc = item.jsonObject["lc"]?.jsonPrimitive?.int?:0,
                                isliked = item.jsonObject["islike"]?.jsonPrimitive?.int?:0,
                                byuser = item.jsonObject["uname"]?.jsonPrimitive?.content.toString(),
                                datetime = datetime
                            )
                        )
                    }
                }
            }
        }
    }
}