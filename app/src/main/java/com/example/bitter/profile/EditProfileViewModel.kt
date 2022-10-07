package com.example.bitter.profile

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.bitter.data.Routes
import com.example.bitter.util.postForm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class EditProfileViewModel(
    private val stateHandle: SavedStateHandle
) :ViewModel(){
    val checked = stateHandle.getStateFlow("checked",true)
    val details = stateHandle.getStateFlow("details",true)
    var imageUri by mutableStateOf<Uri?>(null)
    var bitmap by mutableStateOf<Bitmap?>(null)
    val fname = stateHandle.getStateFlow("fname","")
    val lname = stateHandle.getStateFlow("lname","")
    val gender = stateHandle.getStateFlow("gender","")
    val mob = stateHandle.getStateFlow("mob","")
    val dob = stateHandle.getStateFlow("dob","")
    val error = stateHandle.getStateFlow("error","")


    fun setVal(key:String,value:Any){
        stateHandle[key] = value
    }

    fun saveButtonClick(
        uname: String?,
        key: String?,
        navController:NavController,
        editor:Editor
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

        postForm(postform){ ret->
            val e = when (ret.getString("status")) {
                "success" -> {
                    "Saved Successfully"
                }
                "mob" -> {
                    "Mobile number should be 10 digit number"
                }
                "logout" -> {
                    editor.clear()
                    editor.commit()
                    viewModelScope.launch(Dispatchers.Main) {
                        navController.navigate(Routes.LoginScreen.route + "/logout"){
                            popUpTo(Routes.MainScreen.route)
                        }
                    }
                    ""
                }
                else -> {
                    ret.getString("status")
                }
            }
            stateHandle["error"] = e
        }
    }

    fun getUserDetails(
        uname: String?,
        key: String?,
        navController: NavController,
        editor: Editor
    ) {
        val postform = JSONObject()
        postform.put("subject", "getudetails")
        postform.put("key", key)
        postform.put("uname", uname)

        postForm(postform){ ret->
            val e = when (ret.getString("status")) {
                "success" -> {
                    val data = ret.getJSONObject("data")
                    stateHandle["fname"] = data.getString("fname")
                    stateHandle["lname"] = data.getString("lname")
                    stateHandle["gender"] = data.getString("gender")
                    val m = data.getString("mob")
                    stateHandle["mob"] = if(m == "0") "" else m
                    val d = data.getString("dob")
                    stateHandle["dob"] = if(m == "0000-00-00") "" else d
                    stateHandle["details"] = false
                    ""
                }
                "logout" -> {
                    editor.clear()
                    editor.commit()
                    viewModelScope.launch(Dispatchers.Main) {
                        navController.navigate(Routes.LoginScreen.route + "/logout"){
                            popUpTo(Routes.MainScreen.route)
                        }
                    }
                    ""
                }
                else -> {
                    ret.getString("status")
                }
            }
            stateHandle["error"] = e

        }
    }

    fun setImage(uri: Uri?) {
        imageUri = uri
    }
    fun setImageBitmap(bitmap: Bitmap){
        this.bitmap = bitmap
    }

}