package com.example.bitter.main

import android.content.SharedPreferences.Editor
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.bitter.data.PostItemData
import com.example.bitter.data.Routes
import com.example.bitter.util.postForm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject

class MainViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val persistkey = mutableStateOf(true)

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
                    viewModelScope.launch {
                        navController.navigate(Routes.LoginScreen.route)
                    }
                }
                else -> {
                    println("logout error")
                }
            }
        }
    }

    fun onContentValueChange(it: String) {
        savedStateHandle["contentValue"] = it
    }

    fun setExpanded(state:Boolean) {
        savedStateHandle["expanded"] = state
    }

    fun dropDownClicked(navController: NavController) {
            setExpanded(false)
            savedStateHandle["logout"] = true
            navController.navigate(Routes.UserProfileScreen.route)
    }

    fun postButtonOnClick(uname: String?, key: String?) {
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
                    else -> {
                    }
                }
            }
        }
    }

    fun getPostLoop(uname: String?, key: String?) {
        val postform = JSONObject()
        postform.put("subject", "getpost")
        postform.put("uname", uname)
        postform.put("key", key)

        viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                if (logout.value) break

                postForm(postform){ ret ->
                    when (ret.getString("status")) {
                        "success" -> {
                            val data = ret.getJSONObject("data")
                            postItems.clear()
                            for (i in data.keys()) {
                                val item = data.getJSONObject(i)
                                postItems.add(
                                    element = PostItemData(
                                        postId = i,
                                        username = item.getString("uname"),
                                        userId = item.getString("uid"),
                                        content = item.getString("content"),
                                        lc = item.getString("lc"),
                                        isliked = item.getInt("islike"),
                                        byuser = uname?:""
                                    )
                                )
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


}