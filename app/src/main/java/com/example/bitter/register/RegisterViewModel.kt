package com.example.bitter.register

import android.content.SharedPreferences.Editor
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.bitter.data.Routes
import com.example.bitter.util.ApiService
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val stateHandle: SavedStateHandle
):ViewModel() {
    val username = stateHandle.getStateFlow("username","")
    val password1 = stateHandle.getStateFlow("password1","")
    val password2 = stateHandle.getStateFlow("password2","")
    val email = stateHandle.getStateFlow("email","")
    val error = stateHandle.getStateFlow("error","")
    val password1Visible = stateHandle.getStateFlow("password1Visible",false)
    val password2Visible = stateHandle.getStateFlow("password2Visible",false)
    val loading = stateHandle.getStateFlow("loading",false)

    fun setVal(key:String,value:Any){
        stateHandle[key] = value
    }

    fun registerButtonOnClick(editor: Editor,navController:NavController) {
       stateHandle["error"] = ""
        stateHandle["loading"] = true
        if (password1.value == password2.value) {
            viewModelScope.launch {
                val response = ApiService.register(username.value,password1.value,email.value)
                when (response.status) {
                    "success" -> {
                        editor.putString("token",response.token)
                            .putString("uname",response.uname)
                        editor.apply()
                        navController.navigate(Routes.MainScreen.route)
                    }
                    "userexists" -> {
                        stateHandle["error"] = "username already exists"
                        stateHandle["loading"] = false

                    }
                    else -> {
                        stateHandle["error"] = "Error"
                        stateHandle["loading"] = false
                    }
                }
            }
        }
    }
}