package com.example.bitter

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.bitter.data.Routes
import com.example.bitter.util.ApiService
import kotlinx.coroutines.launch

@Composable
fun LoadingScreen(navController: NavController) {
    val context = LocalContext.current
    val keyPref = context.getSharedPreferences("authkey", Context.MODE_PRIVATE)
    val uname = keyPref.getString("uname", null)
    val token = keyPref.getString("token", null)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Loading",
            color = MaterialTheme.colors.onBackground,
            modifier = Modifier.padding(10.dp),
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
        CircularProgressIndicator()
    }


    val scope = rememberCoroutineScope()

    if(uname != null && token != null){
        LaunchedEffect(key1 = true){
            scope.launch{
                val response = ApiService.checkLogin(token)
                if(response.status == "success"){
                    navController.navigate(Routes.MainScreen.route)
                }
                else{
                    navController.navigate(Routes.LoginScreen.route)
                }
            }
        }
    }
    else{
        LaunchedEffect(key1 = true){
            scope.launch {
                navController.navigate(Routes.LoginScreen.route)
            }
        }
    }
}