package com.example.bitter.userprofile

import android.content.SharedPreferences.Editor
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.bitter.data.Routes
import com.example.bitter.data.PostItem
import com.example.bitter.home.formatTo
import com.example.bitter.home.toDate
import com.example.bitter.util.postForm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import org.json.JSONObject

class UserProfileViewModel(
    private val stateHandle: SavedStateHandle
):ViewModel() {
    var fullname = stateHandle.getStateFlow("fullname","")
    val postItems = mutableStateListOf<PostItem>()


    private fun setVal(k:String, v:String){
        stateHandle[k] = v
    }

    fun getName(uname: String?,key: String?){
        val postform = JSONObject()
            .put("subject","getfullname")
            .put("uname",uname)
            .put("key",key)

        viewModelScope.launch(IO) {
            postForm(postform){
                ret ->
                when (ret.getString("status")) {
                    "success" -> {
                        val name = ret.getString("name")
                        setVal("fullname",name)
                    }
                    else -> {
                        println(ret.getString("status"))
                    }
                }

            }
        }
    }

    fun getPosts(uname: String?, key: String?,navController: NavController,editor:Editor) {
        val postform = JSONObject()
            .put("subject", "getpost")
            .put("uname", uname)
            .put("key", key)
            .put("self","true")

        viewModelScope.launch(IO) {
                postForm(postform) { ret ->
                    when (ret.getString("status")) {
                        "success" -> {
                            val data = ret.getJSONObject("data")
                            postItems.clear()
                            for (i in data.keys()) {
                                val item = data.getJSONObject(i)
                                var datetime = item.getString("datetime")
                                datetime =
                                    datetime.toDate()?.formatTo("dd MMM yyyy,  K:mm a") ?: ""

                                postItems.add(
                                    element = PostItem(
                                        postId = i,
                                        username = item.getString("uname"),
                                        content = item.getString("content"),
                                        lc = item.getInt("lc"),
                                        isliked = item.getInt("islike"),
                                        byuser = uname ?: "",
                                        datetime = datetime
                                    )
                                )
                            }
                            postItems.reverse()
                        }
                        "logout" -> {
                            stateHandle["logout"] = true
                            editor.clear()
                            editor.commit()
                            viewModelScope.launch(Dispatchers.Main) {
                                navController.navigate(Routes.LoginScreen.route + "/logout"){
                                    popUpTo(Routes.MainScreen.route)
                                }
                            }
                        }
                        else -> {
                            println(ret.getString("status"))
                        }
                    }
                }

        }
    }
}