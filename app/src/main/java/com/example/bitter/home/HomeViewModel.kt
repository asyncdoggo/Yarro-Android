package com.example.bitter.home

import android.content.SharedPreferences.Editor
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.bitter.data.PostItem
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


    var postItems = mutableStateListOf<PostItem>()
        private set
    var isRefreshing = mutableStateOf(false)
    private set



    fun logout() {
        editor?.clear()
        editor?.apply()
        viewModelScope.launch {
            val response = ApiService.logout(token)
            navController?.navigate(Routes.LoginScreen.route)
        }
    }



    fun getPost() {
        if(uname == null){return}

        viewModelScope.launch {
            val response = ApiService.getPosts(token,"false")
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
//        val postform = JSONObject()
//        postform.put("subject", "getpost")
//        postform.put("uname", uname)
//        postform.put("key", key)
//        postform.put("self", "false")
//        viewModelScope.launch(IO) {
//            postForm(postform) { ret ->
//                when (ret.getString("status")) {
//                    "success" -> {
//                        val data = ret.getJSONObject("data")
//                        postItems.clear()
//                        for (i in data.keys()) {
//                            val item = data.getJSONObject(i)
//                            var datetime = item.getString("datetime")
//                            datetime =
//                                datetime.toDate()?.formatTo("dd MMM yyyy,  K:mm a") ?: ""
//
//                            postItems.add(
//                                element = PostItem(
//                                    postId = i,
//                                    username = item.getString("uname"),
//                                    content = item.getString("content"),
//                                    lc = item.getInt("lc"),
//                                    isliked = item.getInt("islike"),
//                                    byuser = uname?:"",
//                                    datetime = datetime
//                                )
//                            )
//                        }
//                        postItems.reverse()
//                    }
//                    "logout" -> {
//
////                        editor?.clear()
////                        editor?.commit()
////                        viewModelScope.launch(Main) {
////                            navController?.navigate(Routes.LoginScreen.route + "/logout")
////                        }
//
//                    }
//                    else -> {
//                        println(ret.getString("status"))
//                    }
//                }
//            }
//        }
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