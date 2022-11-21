package com.example.bitter.editprofile

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bitter.util.ApiService
import kotlinx.coroutines.launch

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
    val bio = stateHandle.getStateFlow("bio","")


    fun setVal(key:String,value:Any){
        stateHandle[key] = value
    }

    fun saveButtonClick(
        token: String?,
    ) {
        viewModelScope.launch {
            val response = ApiService.updateUserDetails(fname.value,lname.value,gender.value,mob.value,dob.value,bio.value,token)

            when(response.status){
                "success" -> {
                    stateHandle["error"] = "Saved successfully"
                }
                else -> {
                    stateHandle["error"] = "Error"
                }
            }
        }

    }

    fun getUserDetails(
        token: String?
    ) {
        viewModelScope.launch {
            val response = ApiService.getUserDetails(token)
            when(response.status){
                "success" -> {
                    val data = response.data
                    stateHandle["fname"] = data.fname
                    stateHandle["lname"] = data.lname
                    stateHandle["gender"] = data.gender
                    stateHandle["mob"] = data.mob
                    stateHandle["dob"] = data.dob
                    stateHandle["details"] = false
                    stateHandle["bio"] = data.bio
                }
            }
        }
    }

    fun setImage(uri: Uri?) {
        imageUri = uri
    }
    fun setImageBitmap(bitmap: Bitmap){
        this.bitmap = bitmap
    }

}