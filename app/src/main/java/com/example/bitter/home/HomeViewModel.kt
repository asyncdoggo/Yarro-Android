package com.example.bitter.home

import android.content.SharedPreferences.Editor
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.bitter.data.Routes
import com.example.bitter.util.postForm
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class HomeViewModel : ViewModel() {


    var uname: String? = ""
    var key:String? = ""
    var editor: Editor? = null
    var navController:NavController? = null


    var postItems = mutableStateListOf<PostItemData>()
        private set
    var isRefreshing = mutableStateOf(false)
    private set

    private var delayms = 10000L


    fun logout() {
        
        delayms = 0L
        val postform = JSONObject()
        postform.put("subject", "logout")
        postform.put("uname", uname)
        postform.put("key", key)


        postForm(postform) { ret ->
            when (ret.getString("status")) {
                "success" -> {
                    
                }
                else -> {
                    println("logout error")
                }
            }
            editor?.clear()
            editor?.commit()
            viewModelScope.launch {
                navController?.navigate(Routes.LoginScreen.route + "/logout") {
                    popUpTo(Routes.MainScreen.route)
                }
            }
        }
    }



    fun getPost() {
        if(uname == null){return}
        val postform = JSONObject()
        postform.put("subject", "getpost")
        postform.put("uname", uname)
        postform.put("key", key)
        postform.put("self", "false")
        viewModelScope.launch(IO) {
            postForm(postform) { ret ->
                when (ret.getString("status")) {
                    "success" -> {
                        val data = ret.getJSONObject("data")
                        println(data)
                        postItems.clear()
                        for (i in data.keys()) {
                            val item = data.getJSONObject(i)
                            var datetime = item.getString("datetime")
                            datetime =
                                datetime.toDate()?.formatTo("dd MMM yyyy,  K:mm a") ?: ""

                            postItems.add(
                                element = PostItemData(
                                    postId = i,
                                    username = item.getString("uname"),
                                    content = item.getString("content"),
                                    lc = item.getString("lc"),
                                    isliked = item.getInt("islike"),
                                    byuser = uname?:"",
                                    datetime = datetime
                                )
                            )
                        }
                        postItems.reverse()
                    }
                    "logout" -> {
                        
//                        editor?.clear()
//                        editor?.commit()
//                        viewModelScope.launch(Main) {
//                            navController?.navigate(Routes.LoginScreen.route + "/logout")
//                        }

                    }
                    else -> {
                        println(ret.getString("status"))
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