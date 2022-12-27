package com.example.bitter

import Bitter.R
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.bitter.data.Routes
import com.example.bitter.util.ApiService
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Composable
fun LoadingScreen(navController: NavController) {
    val context = LocalContext.current
    val keyPref = context.getSharedPreferences("authkey", Context.MODE_PRIVATE)
    val uname = keyPref.getString("uname", null)
    val token = keyPref.getString("token", null)

    var update by remember{
        mutableStateOf(false)
    }
    var url by remember {
        mutableStateOf("")
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Checking for updates",
            color = MaterialTheme.colors.onBackground,
            modifier = Modifier.padding(10.dp),
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
        if (!update) {
            CircularProgressIndicator()
        }
        else{
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
                ) {
                Text(text = "Update available, Downloading")
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
//                val r = DownloadManager.Request(uri)
//                r.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"bitter.apk")
//                r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
//                val dm = LocalContext.current.getSystemService(Service.DOWNLOAD_SERVICE) as DownloadManager
//                dm.enqueue(r)
            }
        }
    }

    val version = stringResource(id = R.string.version)
    LaunchedEffect(key1 = true) {
        try {
            val resp = ApiService.checkUpdates()
            val appver = resp.jsonObject["tag_name"]?.jsonPrimitive?.content.toString()
            if (version != appver) {
                val downloadUrl =
                    resp.jsonObject["assets"]?.jsonArray?.get(0)?.jsonObject?.get("browser_download_url")?.jsonPrimitive?.content.toString()
                url = downloadUrl
                update = true

            } else {
                update = false
                if (uname != null && token != null) {
                    navController.navigate(Routes.BottomNav.route)
                } else {
                    navController.navigate(Routes.LoginScreen.route)
                }
            }
        }
        catch (_:Exception){
            navController.navigate(Routes.LoginScreen.route)
        }
    }
}