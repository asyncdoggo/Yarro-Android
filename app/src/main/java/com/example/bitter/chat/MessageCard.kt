package com.example.bitter.chat

import Bitter.R
import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.bitter.postUrl
import com.example.bitter.ui.theme.BitterTheme
import com.example.bitter.ui.theme.chatCardColor

@Composable
fun MessageCard(message: Message,self: Boolean) {
    if(self) {
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.padding(start = 70.dp)
        ){
            Surface(
                shape = RoundedCornerShape(20.dp),
                elevation = 5.dp,
                modifier = Modifier.fillMaxWidth()
                    .padding(8.dp),
                color = MaterialTheme.colors.chatCardColor
            ) {
                Text(
                    text = message.content,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
    else {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(start = 5.dp, end = 40.dp)
        ) {
            Column(
                modifier = Modifier.padding(top = 10.dp)
            ) {
                AsyncImage(
                    model = "$postUrl/images/${message.user}",
                    placeholder = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = "User Image",
                    modifier = Modifier
                        .size(35.dp)
                        .clip(RoundedCornerShape(20.dp))
                )
            }
            Column(
                modifier = Modifier.padding(5.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    elevation = 5.dp
                ) {
                    Text(
                        text = message.content,
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}

@Preview(
    name = "Light Mode"
)
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun MessageCardPrev() {
    BitterTheme {
        Surface{
            Column {
                MessageCard(Message("Abcde",LoremIpsum(10).values.joinToString(),true),true)
                MessageCard(Message("Abcde",LoremIpsum(10).values.joinToString(),false),false)
            }
        }
    }
}

data class Message(
    val user: String,
    val content: String,
    val self: Boolean
)