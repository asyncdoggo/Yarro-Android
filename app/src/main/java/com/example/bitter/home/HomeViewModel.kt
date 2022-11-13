package com.example.bitter.home

import android.content.Context
import android.content.SharedPreferences.Editor
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.bitter.data.PostDatabase
import com.example.bitter.data.PostItem
import com.example.bitter.data.PostRepository
import com.example.bitter.data.Routes
import com.example.bitter.util.ApiService
import kotlinx.coroutines.launch
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.text.SimpleDateFormat
import java.util.*

class HomeViewModel : ViewModel() {
    var uname: String? = ""
    var token:String? = ""
    var editor: Editor? = null
    var navController:NavController? = null
    var isRefreshing = mutableStateOf(false)



    fun logout(context: Context) {
        val postDao = PostDatabase.getInstance(context).postDao()
        val repository = PostRepository(postDao)
        editor?.clear()
        editor?.apply()
        viewModelScope.launch {
            ApiService.logout(token)
            repository.deleteAll()
            navController?.navigate(Routes.LoginScreen.route)
        }
    }

    fun updatePosts(context: Context): LiveData<List<PostItem>> {
        val postDao = PostDatabase.getInstance(context).postDao()
        val repository = PostRepository(postDao)
        return repository.getAllPosts
    }

    fun fetchNewPosts(context:Context,latestPost:String){
        val postDao = PostDatabase.getInstance(context).postDao()
        val repository = PostRepository(postDao)


        viewModelScope.launch {
            val response = ApiService.getPosts(token,latestPost)
            if(response.status == "success"){
                val data = response.data
                if (data != null) {
                    var pid = "0"
                    for(i in data.keys){
                        val item = data.getValue(i)
                        var datetime = item.jsonObject["datetime"]?.jsonPrimitive?.content.toString()
                        datetime = datetime.toDate()?.formatTo("dd MMM yyyy,  K:mm a") ?: ""
                        repository.insert(
                            PostItem(
                                postId = i,
                                content = item.jsonObject["content"]?.jsonPrimitive?.content.toString(),
                                lc = item.jsonObject["lc"]?.jsonPrimitive?.int?:0,
                                dlc = item.jsonObject["dlc"]?.jsonPrimitive?.int?:0,
                                isliked = item.jsonObject["islike"]?.jsonPrimitive?.int?:0,
                                isdisliked = item.jsonObject["isdislike"]?.jsonPrimitive?.int?:0,
                                byuser = item.jsonObject["uname"]?.jsonPrimitive?.content.toString(),
                                datetime = datetime
                            )
                        )
                        pid = i
                    }
                    editor?.putString("post", pid)
                    editor?.apply()
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
                            pid = i,
                            lc = item.jsonObject["lc"]?.jsonPrimitive?.int?:0,
                            dlc = item.jsonObject["dlc"]?.jsonPrimitive?.int?:0,
                            isliked = item.jsonObject["islike"]?.jsonPrimitive?.int?:0,
                            isdisliked = item.jsonObject["isdislike"]?.jsonPrimitive?.int?:0,
                        )
                    }
                }
            }
        }

    }
}


fun String.toDate(
    dateFormat: String = "yyyy-MM-dd HH:mm:ss",
    timeZone: TimeZone = TimeZone.getTimeZone("UTC")
): Date? {
    val parser = SimpleDateFormat(dateFormat, Locale.getDefault())
    parser.timeZone = timeZone
    return parser.parse(this)
}

fun Date.formatTo(dateFormat: String, timeZone: TimeZone = TimeZone.getDefault()): String {
    val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
    formatter.timeZone = timeZone
    return formatter.format(this)
}