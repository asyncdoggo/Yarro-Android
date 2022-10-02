package com.example.bitter.main

import android.content.SharedPreferences.Editor
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.bitter.data.Routes
import com.example.bitter.util.postForm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val persistkey = savedStateHandle.getStateFlow("persist",true)

    var contentValue = savedStateHandle.getStateFlow("contentValue", "")
    val postItems = mutableStateListOf<PostItemData>()

    var expanded = savedStateHandle.getStateFlow("expanded", false)
    var logout = savedStateHandle.getStateFlow("logout",false)

    fun logout(uname: String, key: String, editor: Editor,navController: NavController) {
        val postform = JSONObject()
        postform.put("subject","logout")
        postform.put("uname", uname)
        postform.put("key", key)


        postForm(postform){ ret->
            when (ret.getString("status")) {
                "success" -> {
                    savedStateHandle["logout"] = true
                    editor.clear()
                    editor.apply()
                }
                else -> {
                    println("logout error")
                }
            }
            viewModelScope.launch {
                navController.navigate(Routes.LoginScreen.route + "/logout")
            }
        }
    }

    fun onContentValueChange(it: String) {
        savedStateHandle["contentValue"] = it
    }

    fun setExpanded(state:Boolean) {
        savedStateHandle["expanded"] = state
    }

    fun profileButtonClicked(navController: NavController) {
            setExpanded(false)
            savedStateHandle["logout"] = true
            navController.navigate(Routes.UserProfileScreen.route)
    }

    fun postButtonOnClick(uname: String?, key: String?,navController: NavController) {
        val postform = JSONObject()
        postform.put("subject", "sendpost")
        postform.put("uname", uname)
        postform.put("key", key)
        postform.put("content", contentValue.value)
        viewModelScope.launch(Dispatchers.IO) {

            postForm(postform){ ret ->
                when (ret.getString("status")) {
                    "success" -> {
                        savedStateHandle["contentValue"] = ""
                    }
                    "logout" -> {
                        savedStateHandle["logout"] = true
                        viewModelScope.launch(Dispatchers.Main) {
                            navController.navigate(Routes.LoginScreen.route + "/logout")
                        }
                    }
                    else -> {
                    }
                }
            }
        }
    }

    fun getPostLoop(uname: String?, key: String?,navController: NavController) {
        val postform = JSONObject()
        postform.put("subject", "getpost")
        postform.put("uname", uname)
        postform.put("key", key)

        viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                if (logout.value) break
                postForm(postform) { ret ->
                    when (ret.getString("status")) {
                        "success" -> {
                            val data = ret.getJSONObject("data")
                            postItems.clear()
                            for (i in data.keys()) {
                                val item = data.getJSONObject(i)
                                var datetime = item.getString("datetime")
                                datetime =
                                    datetime.toDate()?.formatTo("dd MMM yyyy,  K:mm a") ?: ""
                                println(datetime)

                                postItems.add(
                                    element = PostItemData(
                                        postId = i,
                                        username = item.getString("uname"),
                                        content = item.getString("content"),
                                        lc = item.getString("lc"),
                                        isliked = item.getInt("islike"),
                                        byuser = uname ?: "",
                                        datetime = datetime
                                    )
                                )
                            }
                        }
                        "logout" -> {
                            viewModelScope.launch(Dispatchers.Main) {
                                savedStateHandle["logout"] = true
                                navController.navigate(Routes.LoginScreen.route + "/logout")
                            }
                        }
                        else -> {
                            println(ret.getString("status"))
                        }
                    }
                }
                delay(2500)
            }
        }
    }

    fun setPersist(b: Boolean) {
        savedStateHandle["persist"] = b
    }
}

fun String.toDate(dateFormat: String = "yyyy-MM-dd HH:mm:ss", timeZone: TimeZone = TimeZone.getTimeZone("UTC")): Date? {
    val parser = SimpleDateFormat(dateFormat, Locale.getDefault())
    parser.timeZone = timeZone
    return parser.parse(this)
}

fun Date.formatTo(dateFormat: String, timeZone: TimeZone = TimeZone.getDefault()): String {
    val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
    formatter.timeZone = timeZone
    return formatter.format(this)
}