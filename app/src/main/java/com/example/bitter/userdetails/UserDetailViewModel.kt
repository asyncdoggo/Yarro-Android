package com.example.bitter.userdetails

import android.content.SharedPreferences
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.bitter.data.Routes
import com.example.bitter.util.postForm
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import org.json.JSONObject

class UserDetailViewModel(
    private val stateHandle: SavedStateHandle
) :ViewModel(){
    val fname = stateHandle.getStateFlow("fname","")
    val lname = stateHandle.getStateFlow("lname","")
    val gender = stateHandle.getStateFlow("gender","")
    val mob = stateHandle.getStateFlow("mob","")
    val dob = stateHandle.getStateFlow("dob","")
    val error = stateHandle.getStateFlow("error","")
    val loading = stateHandle.getStateFlow("loading",false)

    fun setVal(key:String,value:Any){
        stateHandle[key] = value
    }

    fun saveButtonClick(
        uname: String?,
        key: String?,
        navController: NavController
    ) {
        val postform = JSONObject()
        postform.put("subject", "udetails")
        postform.put("key", key)
        postform.put("uname", uname)
        postform.put("fname", fname.value)
        postform.put("lname", lname.value)
        postform.put("gender", gender.value)
        postform.put("mob", mob.value)
        postform.put("dob", dob.value)
        setVal("loading",true)

        postForm(postform){ ret->
            when (ret.getString("status")) {
                "success" -> {
                    viewModelScope.launch(Main) {
                        navController.navigate(Routes.MainScreen.route)
                    }
                }
                "logout" -> {
                    viewModelScope.launch(Main) {
                        navController.navigate(Routes.LoginScreen.route + "/logout")
                    }
                }
                else -> {
                    stateHandle["error"] = ret.getString("status")
                }
            }
            setVal("loading",false)
        }
    }

    fun logout(uname: String, key: String, editor: SharedPreferences.Editor, navController: NavController) {
        val postform = JSONObject()
        postform.put("subject","logout")
        postform.put("uname", uname)
        postform.put("key", key)


        postForm(postform){ ret->
            when (ret.getString("status")) {
                "success" -> {
                    stateHandle["logout"] = true
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

}