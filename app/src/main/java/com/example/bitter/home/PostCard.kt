package com.example.bitter.home

import Bitter.R
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.bitter.postUrl
import com.example.bitter.ui.theme.buttonColor
import kotlinx.coroutines.launch


@Composable
fun PostCard(
    content: String,
    lc: Int,
    dlc:Int,
    token: String,
    postId: String,
    isLiked: Int,
    isDisliked: Int,
    byUser: String,
    datetime: String,
    viewModel: PostCardViewModel = viewModel(),
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current


    Surface(
        elevation = 5.dp,
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.padding(5.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
            //.border(border = BorderStroke(1.5.dp, color = Color.LightGray))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(start = 10.dp, top = 10.dp)

            ) {
                AsyncImage(
                    model = "$postUrl/images/$byUser",
                    contentDescription = "icon",
                    placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(60.dp)
                )
            }

            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, top = 5.dp)
                ) {
                    Text(
                        text = byUser,
                        color = MaterialTheme.colors.onBackground,
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.caption
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp)
                ){
                    Text(
                        text = datetime,
                        color = MaterialTheme.colors.onBackground,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Light
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 1.dp)
                ) {
                    Text(
                        text = content,
                        color = MaterialTheme.colors.onBackground,
                        fontSize = 20.sp,
                        fontFamily = FontFamily.SansSerif
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        onClick = {
                            viewModel.updateLike(context,postId,token)
//                            likeCallback()
                        }) {
                        Icon(
                            imageVector = Icons.Default.ThumbUp,
                            contentDescription = "Like Button",
                            tint = if (isLiked == 1) MaterialTheme.colors.buttonColor else MaterialTheme.colors.onBackground
                        )
                    }
                    Text(
                        text = lc.toString(),
                        color = MaterialTheme.colors.onBackground,
                        modifier = Modifier.padding(top = 15.dp)
                    )
                    IconButton(
                        onClick = {
                            scope.launch {
                                viewModel.updateDislike(context,postId,token)
//                                likeCallback()
                            }
                        }) {
                        Icon(
                            imageVector = Icons.Default.ThumbDown,
                            contentDescription = "dislike Button",
                            tint = if (isDisliked == 1) MaterialTheme.colors.buttonColor else MaterialTheme.colors.onBackground
                        )
                    }
                    Text(
                        text = dlc.toString(),
                        color = MaterialTheme.colors.onBackground,
                        modifier = Modifier.padding(top = 15.dp)
                    )
                }

            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun postprev() {
    Card(
        elevation = 5.dp,
        shape = RoundedCornerShape(20.dp),
    ) {
        PostCard(
            content = LoremIpsum(30).values.joinToString(),
            lc = 1,
            dlc = 1,
            token = "",
            postId = "1",
            isLiked = 1,
            isDisliked = 1,
            byUser = "username",
            datetime = "11111"
        )
    }
}