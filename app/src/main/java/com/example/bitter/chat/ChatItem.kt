package com.example.bitter.chat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.bitter.postUrl
import kotlin.random.Random

@Composable
fun ChatItem(uname:String, message: String, navCallback: () -> Unit) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    navCallback()
                }
        ) {
            AsyncImage(
                model = "$postUrl/images/$uname",
                contentDescription = "User Image",
                placeholder = painterResource(id = androidx.appcompat.R.drawable.abc_ic_search_api_material),
                modifier = Modifier
                    .padding(5.dp)
                    .size(40.dp)
            )

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = uname,
                    fontSize = 17.sp,
                    modifier = Modifier.padding(start = 5.dp)
                )
                Text(
                    text = message,
                    fontSize = 15.sp,
                    color = MaterialTheme.colors.onBackground.copy(0.5f),
                    modifier = Modifier.padding(start = 5.dp)
                )
            }
        }

}

@Preview(showSystemUi = true)
@Composable
fun ChatPrev2() {
    LazyColumn{
        items(10){
            ChatItem(Random(5).nextInt().toString(), Random(10).nextInt().toString()){}
        }
    }
}