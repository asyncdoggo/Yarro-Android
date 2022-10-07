package com.example.bitter.home

import android.content.SharedPreferences
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.bitter.data.Routes
import com.example.bitter.util.postForm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject


class NewPostViewModel(
    private val savedStateHandle: SavedStateHandle
) :ViewModel(){
    var uname:String? = ""
    var key:String? = ""
    var editor: SharedPreferences.Editor? = null
    var navController: NavController? = null


    var contentValue = savedStateHandle.getStateFlow("contentValue", "")

    fun addPost(): Boolean {
        if(contentValue.value.trim() == "") return false
        val postform = JSONObject()
        postform.put("subject", "sendpost")
        postform.put("uname", uname)
        postform.put("key", key)
        postform.put("content", contentValue.value)
        viewModelScope.launch(Dispatchers.IO) {

            postForm(postform) { ret ->
                println(ret)
                when (ret.getString("status")) {
                    "success" -> {
                        savedStateHandle["contentValue"] = ""
                        viewModelScope.launch(Dispatchers.Main) {
                            navController?.popBackStack()
                        }
                    }
                    "logout" -> {

                        editor?.clear()
                        editor?.commit()
                        viewModelScope.launch(Dispatchers.Main) {
                            navController?.navigate(Routes.LoginScreen.route + "/logout") {
                                popUpTo(Routes.MainScreen.route)
                            }
                        }
                    }
                    else -> {
                    }
                }
            }
        }
        return true
    }

    fun onContentValueChange(it: String) {
        savedStateHandle["contentValue"] = it
    }


}