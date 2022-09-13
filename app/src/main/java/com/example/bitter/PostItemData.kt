package com.example.bitter

import Bitter.R
import android.util.Log
import android.view.PointerIcon.load
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
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
    var cc:String
)

@Composable
fun PostItem(
    username : String,
    content: String,
    lc: String,
    cc:String,
    key: String,
    postId: String
) {
    var _lc = lc
    val _cc = cc
    Row(
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth()
            .border(border = BorderStroke(1.5.dp,color = Color.LightGray))
    ) {
        AsyncImage(
            model = "$postUrl/images/$username.png",
            contentDescription = "icon",
            placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
            modifier = Modifier
                .clip(CircleShape)
                .size(65.dp)
        )
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
                    .padding(start=10.dp)
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
                    postform.put("uname",username)
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
                    Icon(imageVector = Icons.Default.ThumbUp, contentDescription = "Like Button")
                }
                Text(
                    text = _lc,
                    modifier = Modifier.padding(top = 15.dp)
                )
                Spacer(modifier = Modifier.padding(start = 30.dp, end = 30.dp))

                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Default.Comment, contentDescription = "Comment Button")
                }
                Text(
                    text = _cc,
                    Modifier.padding(top = 15.dp)
                )
            }
        }
    }
}