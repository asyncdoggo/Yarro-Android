package com.example.bitter.passwordReset

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.bitter.util.postForm
import org.json.JSONObject

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
        val forgotPassForm = JSONObject()
        forgotPassForm.put("subject", "forgotpass")
        forgotPassForm.put("email", email)
        try {
            postForm(forgotPassForm) { ret ->
                val e = when (ret.getString("status")) {
                    "success" -> {
                        "Email sent successfully"
                    }
                    "noemail" -> {
                        "Email not found"
                    }
                    "failure" -> {
                        "Network Error"
                    }
                    else -> {
                        "Unknown Error"
                    }
                }
                stateHandle["error"] = e
                stateHandle["loading"] = false
            }
        }
        catch (_:Exception){
            stateHandle["loading"] = false
        }
    }


}