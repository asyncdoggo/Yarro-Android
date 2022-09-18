package com.example.bitter

import Bitter.R
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

var postUrl: String = "http://192.168.1.7:5005"


data class PostItemData(
    var postId: String,
    var username:String,
    var userId:String,
    var content: String,
    var lc :String,
    var isliked: Int,
    var byuser: String
)

@Composable
fun PostItem(
    username : String,
    content: String,
    lc: String,
    key: String,
    postId: String,
    isliked: Int,
    byuser: String
) {
    var _lc = lc
    Row(
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth()
            .border(border = BorderStroke(1.5.dp, color = Color.LightGray))
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(start = 10.dp,top = 17.dp)

        ){
            AsyncImage(
                model = "$postUrl/images/$username",
                contentDescription = "icon",
                placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
                modifier = Modifier
                    .clip(CircleShape)
                    .size(65.dp)
            )
        }

        Column(modifier = Modifier.padding(start = 10.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp)
            ){
                Text(
                    text = username,
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp)
            ) {
                Text(
                    text = content,
                    fontSize = 20.sp
                )
            }
            Row(
                modifier = Modifier

            ) {
                IconButton(onClick = {
                    val postform = JSONObject()
                    postform.put("subject","updatelc")
                    postform.put("uname",byuser)
                    postform.put("key",key)
                    postform.put("pid",postId)

                    postForm(postform,callback = object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            e.printStackTrace()
                        }
                        override fun onResponse(call: Call, response: Response) {
                            val responseString = String(response.body.bytes())
                            val ret = JSONObject(responseString)
                            try {
                                when (ret.getString("status")) {
                                    "success" -> {
                                        val temp = _lc.toInt() + 1
                                        _lc = temp.toString()
                                    }
                                    else -> {
                                    }
                                }
                            }
                            catch(e: JSONException){
                                e.printStackTrace()
                            }
                        }
                    })
                }) {
                    Icon(
                        imageVector = Icons.Default.ThumbUp,
                        contentDescription = "Like Button",
                        tint = if(isliked == 1) Color.Blue else Color.Black
                    )
                }
                Text(
                    text = _lc,
                    modifier = Modifier.padding(top = 15.dp)
                )
            }
        }
    }
}