package com.example.bitter.home

import Bitter.R
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbDown
import androidx.compose.material.icons.outlined.ThumbUp
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
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.bitter.data.PostItem
import com.example.bitter.data.Routes
import com.example.bitter.noRippleClickable
import com.example.bitter.postUrl
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch


@Composable
fun PostCard(
    item: PostItem,
    token:String,
    viewModel: PostCardViewModel = viewModel(),
    navController: NavController?
) {
    val scope = rememberCoroutineScope()
    Row{
        Column{
            AsyncImage(
                model = "$postUrl/images/${item.byuser}",
                contentDescription = "icon",
                placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
                modifier = Modifier
                    .padding(5.dp)
                    .clip(CircleShape)
                    .size(55.dp)
            )
        }
        Column() {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 3.dp, end = 5.dp)
                    .noRippleClickable {
                        navController?.navigate(Routes.ProfileScreen.route + "/${item.byuser}")
                    },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.byuser,
                    color = MaterialTheme.colors.onBackground,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.caption
                )
                Text(
                    text = item.datetime,
                    color = MaterialTheme.colors.onBackground,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Light
                )
            }
            Text(
                text = item.content,
                color = MaterialTheme.colors.onBackground.copy(0.8f),
                fontSize = 17.sp,
                fontFamily = FontFamily.SansSerif
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                IconButton(
                    onClick = {
                        viewModel.updateLike(item.postId, token)
                    }) {
                    Icon(
                        imageVector = if (item.isliked == 0) Icons.Outlined.ThumbUp else Icons.Filled.ThumbUp,
                        contentDescription = "Like Button"
                    )
                }
                Text(
                    text = item.lc.toString(),
                    color = MaterialTheme.colors.onBackground,
                    modifier = Modifier.padding(top = 15.dp)
                )
                IconButton(
                    onClick = {
                        scope.launch(IO) {
                            viewModel.updateDislike(item.postId, token)
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (item.isdisliked == 0) Icons.Outlined.ThumbDown else Icons.Filled.ThumbDown,
                        contentDescription = "dislike Button"
                    )
                }
                Text(
                    text = item.dlc.toString(),
                    color = MaterialTheme.colors.onBackground,
                    modifier = Modifier.padding(top = 15.dp)
                )
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
            PostItem(0,LoremIpsum(30).values.joinToString(),1,1,1,0,"username","11-11-111"),
            "",
            navController = NavController(LocalContext.current)
        )
    }
}