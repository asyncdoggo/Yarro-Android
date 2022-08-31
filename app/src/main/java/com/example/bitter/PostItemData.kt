package com.example.bitter

import Bitter.R
import android.view.PointerIcon.load
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
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

const val postUrl: String = "http://192.168.1.7:5005"


data class PostItemData(
    var username:String,
    var content: String
)

@Composable
fun PostItem(
    username : String,
    content: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        AsyncImage(
            model = "$postUrl/images/$username.png",
            contentDescription = "icon",
            placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
            modifier = Modifier
                .clip(CircleShape)
                .size(40.dp)
        )
        Spacer(modifier = Modifier.size(10.dp))
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
            .padding(start = 20.dp)
    ) {
        Text(
            text = content,
            fontSize = 20.sp
        )
    }
}