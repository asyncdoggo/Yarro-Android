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
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.bitter.data.PostItem
import com.example.bitter.data.Routes
import com.example.bitter.noRippleClickable
import com.example.bitter.postUrl
import com.example.bitter.ui.theme.buttonColor
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
    Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(start = 5.dp, top = 10.dp)
                        .noRippleClickable {
                            navController?.navigate(Routes.Profile.route + "/${item.byuser}")
                        }

                ) {
                    AsyncImage(
                        model = "$postUrl/images/${item.byuser}",
                        contentDescription = "icon",
                        placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(50.dp)
                    )

                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 10.dp)
                        ) {
                            Text(
                                text = item.byuser,
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
                        ) {
                            Text(
                                text = item.datetime,
                                color = MaterialTheme.colors.onBackground,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Light
                            )
                        }
                    }
                }
            }


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 65.dp, end = 5.dp, top = 10.dp)
            ) {
                Text(
                    text = item.content,
                    color = MaterialTheme.colors.onBackground,
                    fontSize = 20.sp,
                    fontFamily = FontFamily.SansSerif
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 53.dp)
            ) {
                IconButton(
                    onClick = {
                        viewModel.updateLike(item.postId, token)
                    }) {
                    Icon(
                        imageVector = Icons.Default.ThumbUp,
                        contentDescription = "Like Button",
                        tint = if (item.isliked == 1) MaterialTheme.colors.buttonColor else MaterialTheme.colors.onBackground
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
                    }) {
                    Icon(
                        imageVector = Icons.Default.ThumbDown,
                        contentDescription = "dislike Button",
                        tint = if (item.isdisliked == 1) MaterialTheme.colors.buttonColor else MaterialTheme.colors.onBackground
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


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun postprev() {
    Card(
        elevation = 5.dp,
        shape = RoundedCornerShape(20.dp),
    ) {
        PostCard(
            PostItem(0,LoremIpsum(30).values.joinToString(),1,1,1,1,"username","11-11-111"),
            "",
            navController = NavController(LocalContext.current)
        )
    }
}