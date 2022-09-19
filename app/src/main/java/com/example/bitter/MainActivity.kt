package com.example.bitter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import org.json.JSONObject


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // url
        val url = intent.getStringExtra("url")
        if (url != null) {
            postUrl = "http://$url"
        }
        // url

        setContent {
            val context = LocalContext.current
            val keyPref = context.getSharedPreferences("authkey", Context.MODE_PRIVATE)
            val uname = keyPref.getString("uname", null)
            val key = keyPref.getString("key", null)


            if (key != null && uname != null) {
                val loginForm = JSONObject()
                loginForm.put("subject", "login")
                loginForm.put("uname", uname)
                loginForm.put("key", key)

                postForm(loginForm) {
                    when (it.getString("status")) {
                        "success" -> {
                            val retKey = it.getString("key")
                            val retUname = it.getString("uname")
                            val intent = Intent(context, PostActivity::class.java)
                            val editor = keyPref.edit()
                            editor.putString("uname", retUname)
                            editor.putString("key", retKey)
                            editor.apply()
                            context.startActivity(intent)
                        }
                        else -> {
                            println(it.getString("status"))
                        }
                    }
                }
            }

            LoginPage()
        }
    }

}


@Preview(showBackground = true)
@Composable
fun LoginPage() {
    var username by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var passwordVisible by remember {
        mutableStateOf(false)
    }
    var errortext by remember {
        mutableStateOf("")
    }
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current


    Box(
        modifier = Modifier
            .background(Color(0xFFEAFFE3))
            .fillMaxSize()
            .padding(10.dp)

    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {

            Spacer(modifier = Modifier.padding(50.dp))

            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Text(
                    text = "Log-in",
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.h1,
                    fontFamily = FontFamily.Cursive
                )
            }
            Spacer(modifier = Modifier.padding(20.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            ) {
                Text(
                    text = "Username",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.h3,
                    fontFamily = FontFamily.Serif
                )

            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )
            {
                TextField(
                    value = username, onValueChange = { username = it },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color(0xFFEAFFE3)
                    )
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            ) {
                Text(
                    text = "Password",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.h3,
                    fontFamily = FontFamily.Serif
                )

            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
            )
            {
                TextField(
                    value = password, onValueChange = { password = it },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth(),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        val image = if (passwordVisible)
                            Icons.Filled.Visibility
                        else Icons.Filled.VisibilityOff

                        val description = if (passwordVisible) "Hide password" else "Show password"

                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = image, description)
                        }
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color(0xFFEAFFE3)
                    )
                )
            }
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(7.dp)
            ) {
                Text(
                    text = "Forgot password?",
                    fontSize = 17.sp,
                    modifier = Modifier.clickable {
                        val intent = Intent(context, ResetPassActivity::class.java)
                        context.startActivity(intent)
                    }
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 5.dp)
            )
            {
                Button(
                    onClick = {
                        val loginForm = JSONObject()
                        loginForm.put("subject", "login")
                        loginForm.put("uname", username)
                        loginForm.put("passwd", password)

                        coroutineScope.launch(IO) {

                            postForm(loginForm) { ret ->
                                when (ret.getString("status")) {
                                    "success" -> {
                                        val key = ret.getString("key")
                                        val uname = ret.getString("uname")
                                        val intent = Intent(context, PostActivity::class.java)
                                        val keyPref = context.getSharedPreferences(
                                            "authkey",
                                            Context.MODE_PRIVATE
                                        )
                                        val editor = keyPref.edit()
                                        editor.putString("uname", uname)
                                        editor.putString("key", key)
                                        editor.apply()
                                        context.startActivity(intent)
                                    }
                                    "badpasswd" -> {
                                        errortext = "Username or password is incorrect"
                                    }
                                    else -> {
                                        errortext = "Unknown Error"
                                    }
                                }
                            }
                        }

                    },
                    shape = RoundedCornerShape(40.dp),
                    modifier = Modifier
                        .padding(start = 15.dp, end = 15.dp)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xE81C0E1F),
                        contentColor = Color(0xFFFFF01B)

                    )

                ) {
                    Text(
                        text = "Login",
                        fontSize = 20.sp
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            ) {
                Text(
                    text = "Don't have an account? Click here to sign up",
                    fontSize = 15.sp,
                    modifier = Modifier.clickable {
                        val intent = Intent(context, SignUpActivity::class.java)
                        context.startActivity(intent)
                    }
                )

            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            ) {
                Text(text = errortext)
            }

        }


    }

}