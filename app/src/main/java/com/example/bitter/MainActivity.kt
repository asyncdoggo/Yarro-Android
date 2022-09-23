package com.example.bitter

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
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
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.bitter.data.NavRoutes
import com.example.bitter.data.postForm
import com.example.bitter.data.postUrl
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import org.json.JSONObject
import kotlin.system.exitProcess


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
            Navigator()
            BackHandler { exitProcess(0) }
        }
    }
}

@Composable
fun LoginPageStart(navController: NavController) {
    val context = LocalContext.current
    val keyPref = context.getSharedPreferences("authkey", Context.MODE_PRIVATE)
    val uname = keyPref.getString("uname", null)
    val key = keyPref.getString("key", null)


    val loginForm = JSONObject()
    loginForm.put("subject", "login")
    loginForm.put("uname", uname)
    loginForm.put("key", key)

    LaunchedEffect(key1 = null){
        postForm(loginForm) {
            when (it.getString("status")) {
                "success" -> {
                    val retKey = it.getString("key")
                    val retUname = it.getString("uname")

                    val editor = keyPref.edit()
                    editor.putString("uname", retUname)
                    editor.putString("key", retKey)
                    editor.apply()
                    globalUsername = retUname
                    globalKey = retKey
                    navController.navigate(NavRoutes.MainPage.route)

                }
                else -> {
                    println(it.getString("status"))
                }
            }
        }
    }

    LoginPage(navController)
}

@Composable
fun LoginPage(navController: NavController) {
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
            .background(Color(0xFFF3FCFF))
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
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Text(
                    text = "B-itter",
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
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.caption,
                    fontFamily = FontFamily.Default
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
                        backgroundColor = Color(0xFFF3FCFF)
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
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.caption,
                    fontFamily = FontFamily.Default
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
                        backgroundColor = Color(0xFFF3FCFF)
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
                    fontSize = 15.sp,
                    modifier = Modifier.clickable {
                        navController.navigate(NavRoutes.ForgotPassPage.route)
                    },
                    color = Color(0xff0000ee)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, bottom = 5.dp)
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
                                        val keyPref = context.getSharedPreferences(
                                            "authkey",
                                            Context.MODE_PRIVATE
                                        )
                                        val editor = keyPref.edit()
                                        editor.putString("uname", uname)
                                        editor.putString("key", key)
                                        editor.apply()
                                        globalUsername = uname
                                        globalKey = key
                                        coroutineScope.launch(Main) {
                                            navController.navigate(NavRoutes.MainPage.route)
                                        }
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
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .padding(start = 15.dp, end = 15.dp)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xff0065ff),
                        contentColor = Color.White
                    )

                ) {
                    Text(
                        text = "Login",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
                    .clickable {
                        navController.navigate(NavRoutes.RegisterPage.route)
                    }
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("New to B-itter? ")
                        withStyle(style = SpanStyle(color = Color(0xff0000ee))) {
                            append("Register here")
                        }
                    },
                    fontSize = 15.sp
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

@Preview
@Composable
fun LoginPrev() {
    LoginPage(navController = NavController(LocalContext.current))
}