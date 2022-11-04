package com.example.bitter.chat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.bitter.postUrl

@Composable
fun ChatItem(uname:String) {
    Card(
        elevation = 5.dp,
        backgroundColor = MaterialTheme.colors.background,
        contentColor = MaterialTheme.colors.onBackground,
        modifier = Modifier.padding(5.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    // TODO: GOTO User Chat uname
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
            Text(
                text = "uname",
                modifier = Modifier.padding(start = 20.dp)
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun ChatPrev2() {
    LazyColumn{
        items(10){
            ChatItem("username")
        }
    }
}