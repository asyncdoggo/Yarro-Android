package com.example.bitter.resetpass

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bitter.util.ApiService
import kotlinx.coroutines.launch

class ForgotPassViewModel(
    private val stateHandle: SavedStateHandle
):ViewModel() {

    val email = stateHandle.getStateFlow("email","")
    val error = stateHandle.getStateFlow("error","")
    val loading = stateHandle.getStateFlow("loading",false)

    fun onEmailChange(it: String) {
        stateHandle["email"] = it
    }

    fun resetButtonOnClick(){
        stateHandle["loading"] = true
        viewModelScope.launch {
            val response = ApiService.forgotPass(email.value)
            when (response.status) {
                "success" -> {
                    stateHandle["error"] = "Email sent successfully"
                }
                "noemail" -> {
                    stateHandle["error"] = "Email does not exists"
                }
                else -> {
                    stateHandle["error"] = "Error"
                }
            }
        }
    }
}