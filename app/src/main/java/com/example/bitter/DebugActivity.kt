package com.example.bitter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview

class DebugActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            setContent{
                DebugPage()
            }
    }
}


@Preview(showSystemUi = true)
@Composable
fun DebugPage() {
    val keyPref = LocalContext.current.getSharedPreferences("authkey", Context.MODE_PRIVATE)

    var url by remember {
        mutableStateOf(keyPref.getString("url","").toString())
    }


    val context = LocalContext.current


    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
           modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                value = url,
                onValueChange = {url = it}
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ){
            Button(onClick = {
                val editor = keyPref.edit()
                editor.putString("url",url)
                editor.apply()
                val intent = Intent(context, MainActivity::class.java)
                intent.putExtra("url",url)

                context.startActivity(intent)
            }) {
                Text("next")
            }
        }
    }
}