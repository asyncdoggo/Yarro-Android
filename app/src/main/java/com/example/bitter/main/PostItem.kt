package com.example.bitter.main

import Bitter.R
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.bitter.postUrl
import com.example.bitter.util.postForm
import org.json.JSONObject



data class PostItemData(
    var postId: String,
    var username:String,
    var content: String,
    var lc :String,
    var isliked: Int,
    var byuser: String,
    var datetime: String
)

@Composable
fun PostItem(
    username : String,
    content: String,
    lc: String,
    key: String,
    postId: String,
    isliked: Int,
    byuser: String,
    datetime:String
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
            modifier = Modifier.padding(start = 10.dp,top = 10.dp)

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
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.caption
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
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = {
                    val postform = JSONObject()
                    postform.put("subject","updatelc")
                    postform.put("uname",byuser)
                    postform.put("key",key)
                    postform.put("pid",postId)

                    postForm(postform){ ret->
                        when (ret.getString("status")) {
                            "success" -> {
                                val temp = _lc.toInt() + 1
                                _lc = temp.toString()
                            }
                            else -> {
                            }
                        }
                    }

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

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = datetime,
                        modifier = Modifier.padding(top = 30.dp, end = 15.dp),
                        fontSize = 11.sp
                    )
                }
            }

        }
    }
}


@Preview(showBackground = true)
@Composable
fun Text() {
    PostItem(
        username = "user1",
        content = "lorem ipsum dolor sit amet, col amit amze dfsdj fsdkfljsdf ksdfdsfhfjsdhd gfjsddgshf jdgsfhdsj gfshgff hgdsjfh gdsjfhg dsjfhg dsjfhsdgfdshgfdshf dshgfdsfhsgdjfshdgfshdgfsjdhgfsjdhfgshdfgdshfgdsjfhgdsjfasish",
        lc = "1",
        key = "12",
        postId = "11",
        isliked = 1,
        byuser = "",
        datetime = "13:34 pm, 12-01-2022"
    )
}