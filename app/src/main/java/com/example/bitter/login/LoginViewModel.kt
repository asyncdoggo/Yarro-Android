package com.example.bitter.login

import android.content.SharedPreferences.Editor
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.bitter.data.Routes
import com.example.bitter.util.ApiService
import kotlinx.coroutines.launch

class LoginViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {


    var username = savedStateHandle.getStateFlow("username", "")
    var password = savedStateHandle.getStateFlow("password", "")
    var passwordVisible = savedStateHandle.getStateFlow("passwordVisible", false)
    var errortext = savedStateHandle.getStateFlow("error", "")
    var loading = savedStateHandle.getStateFlow("loading",false)


    fun loginButtonOnClick(navController: NavController, editor: Editor) {
        savedStateHandle["loading"] = true
        viewModelScope.launch {
            val response = ApiService.login(username.value, password.value)
            when (response.status) {
                "success" -> {
                    editor.putString("token",response.token)
                        .putString("uname",response.uname)
                    editor.apply()
                    navController.navigate(Routes.MainScreen.route)
                }
                "failure" -> {
                    savedStateHandle["error"] = "An error occurred try again"
                    savedStateHandle["loading"] = false
                }
                "email" -> {
                    editor.putString("token",response.token)
                        .putString("uname",response.uname)
                    editor.apply()
                    navController.navigate(Routes.VerifyScreen.route)
                }
                else -> {
                    savedStateHandle["error"] = response.status
                    savedStateHandle["loading"] = false
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


}