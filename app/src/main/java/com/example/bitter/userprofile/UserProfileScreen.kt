package com.example.bitter.userprofile

import Bitter.R
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.bitter.home.PostCard
import com.example.bitter.postUrl
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.bitter.data.Routes
import com.example.bitter.ui.theme.buttonColor

@Composable
fun UserProfileScreen(viewModel: UserProfileViewModel = viewModel(), outerNavController: NavController,innerNavController:NavController) {
    val context = LocalContext.current
    val keyPref = context.getSharedPreferences("authkey", Context.MODE_PRIVATE)
    val uname = keyPref.getString("uname", null)
    val key = keyPref.getString("key", null)
    val editor = keyPref.edit()

    val postItems = viewModel.postItems
    val fullname = viewModel.fullname.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, bottom = 10.dp, top = 20.dp, end = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                AsyncImage(
                    model = "$postUrl/images/$uname",
                    contentDescription = "image",
                    placeholder = painterResource(id = R.drawable.ic_launcher_background),
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                )

                Text(
                    text = fullname.value,
                    modifier = Modifier.padding(30.dp),
                    fontSize = 16.sp
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 45.dp, bottom = 20.dp)
            ) {
                Text(
                    text = uname?:"username",
                    color = MaterialTheme.colors.onBackground,
                    modifier = Modifier.padding(end = 20.dp)
                )
                
                OutlinedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 25.dp, end = 25.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.buttonColor,
                        contentColor = Color.White
                    ),
                    onClick = { outerNavController.navigate(Routes.EditUserProfileScreen.route) }
                ) {
                    Text(text = "Edit profile")
                }
            }
            
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Your Posts",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 20.dp, bottom = 10.dp)
                )
            }

            LazyColumn{
                items(postItems) { item ->
                    PostCard(
                        index = postItems.indexOf(item),
                        username = item.username,
                        content = item.content,
                        lc = item.lc,
                        key = key?:"",
                        postId = item.postId,
                        isLiked = item.isliked,
                        byUser = item.byuser,
                        datetime = item.datetime
                    ){
                        if(postItems[it].isliked == 0){
                            postItems[it].lc += 1
                            postItems[it].isliked = 1
                        }
                        else{
                            postItems[it].lc -= 1
                            postItems[it].isliked = 0
                        }
                        listOf(postItems[it].lc,postItems[it].isliked)
                    }
                }
            }

            LaunchedEffect(key1 = true){
                viewModel.getPosts(uname,key,innerNavController, editor)
                viewModel.getName(uname,key)
            }

        }
    }
}
