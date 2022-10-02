package com.example.bitter.login

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

class LoginViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {


    var username = savedStateHandle.getStateFlow("username", "")
    var password = savedStateHandle.getStateFlow("password", "")
    var passwordVisible = savedStateHandle.getStateFlow("passwordVisible", false)
    var errortext = savedStateHandle.getStateFlow("error", "")
    var loading = savedStateHandle.getStateFlow("loading",false)


    fun loginButtonOnClick(navController: NavController, editor: Editor) {
        setLoading(true)
        val loginForm = JSONObject()
        loginForm.put("subject", "login")
        loginForm.put("uname", username.value)
        loginForm.put("passwd", password.value)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                postForm(loginForm) { ret ->
                    when (ret.getString("status")) {
                        "success" -> {
                            val key = ret.getString("key")
                            val uname = ret.getString("uname")

                            editor.putString("uname", uname)
                            editor.putString("key", key)
                            editor.apply()
                            viewModelScope.launch(Dispatchers.Main) {
                                navController.navigate(Routes.MainScreen.route)
                            }
                        }
                        "badpasswd" -> {
                            savedStateHandle["error"] = "Username or password is incorrect"
                        }
                        "failure" -> {
                            savedStateHandle["error"] = "Network Error"
                        }
                        else -> {
                            savedStateHandle["error"] = "Unknown Error"
                        }
                    }
                    setLoading(false)
                }
            }
            catch (_:Exception){
                setLoading(false)
            }
        }
    }

    fun autoLogin(uname: String, key: String, editor: Editor,navController: NavController) {
        val loginForm = JSONObject()
        loginForm.put("subject", "login")
        loginForm.put("uname", uname)
        loginForm.put("key", key)

        viewModelScope.launch(Dispatchers.IO) {
            postForm(loginForm) { ret ->
                when (ret.getString("status")) {
                    "success" -> {
                        val _key = ret.getString("key")
                        val _uname = ret.getString("uname")
                        editor.putString("uname", _uname)
                        editor.putString("key", _key)
                        editor.apply()

                        viewModelScope.launch(Dispatchers.Main) {
                            navController.navigate(Routes.MainScreen.route)
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    fun onUsernameChange(it: String) {
        savedStateHandle["username"] = it
    }

    fun onPasswordChanged(it: String) {
        savedStateHandle["password"] = it
    }

    fun onVisibleButtonClick() {
        savedStateHandle["passwordVisible"] = !passwordVisible.value
    }
    private fun setLoading(b: Boolean){
        savedStateHandle["loading"] = b
    }


}