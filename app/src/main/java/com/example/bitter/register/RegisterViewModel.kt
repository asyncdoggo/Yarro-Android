package com.example.bitter.register

import android.content.SharedPreferences.Editor
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.bitter.data.Routes
import com.example.bitter.util.postForm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

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
        if (password1.value == password2.value) {
            setVal("loading", true)
            val regForm = JSONObject()
            regForm.put("subject", "register")
            regForm.put("email", email.value)
            regForm.put("uname", username.value)
            regForm.put("passwd1", password1.value)
            try {
                postForm(regForm) { ret ->
                    val e = when (ret.getString("status")) {
                        "success" -> {
                            val retuname = ret.getString("uname")
                            val retkey = ret.getString("key")
                            editor.putString("uname", retuname)
                            editor.putString("key", retkey)
                            editor.apply()
                            viewModelScope.launch(Dispatchers.Main) {
                                navController.navigate(Routes.UserDetailsScreen.route)
                            }
                            ""
                        }
                        "alreadyuser" -> {
                            "Username already exists"
                        }
                        "alreadyemail" -> {
                            "Email already exists"
                        }
                        "failure" -> {
                            "Network Error"
                        }
                        else -> {
                            ret.getString("status")
                        }
                    }
                    stateHandle["error"] = e
                    setVal("loading", false)
                }
            } catch (_: Exception) {

            }
        }

        else {
            stateHandle["error"] = "Passwords do not match"
        }

    }
}