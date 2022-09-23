package com.example.bitter

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import androidx.navigation.NavController
import com.example.bitter.data.NavRoutes
import com.example.bitter.data.postForm
import com.example.bitter.ui.theme.TextFieldItem
import com.example.bitter.ui.theme.TextItem
import com.example.bitter.ui.theme.bgColorLight
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import org.json.JSONObject


@Composable
fun SignUpPage(navController: NavController) {

    BackHandler {
        navController.navigate(NavRoutes.LoginPage.route)
    }

    val keyPref = LocalContext.current.getSharedPreferences("authkey", Context.MODE_PRIVATE)

    var username by remember {
        mutableStateOf("")
    }
    var password1 by remember {
        mutableStateOf("")
    }
    var password2 by remember {
        mutableStateOf("")
    }
    var email by remember {
        mutableStateOf("")
    }

    var password1Visible by remember {
        mutableStateOf(false)
    }
    var password2Visible by remember {
        mutableStateOf(false)
    }

    var errortext by remember {
        mutableStateOf("")
    }

    var coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current



    Box(
        modifier = Modifier
            .background(bgColorLight)
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

            TextItem(
                text = "Register",
                fontSize = 40.sp,
                fontWeight = FontWeight.Medium,
                style = MaterialTheme.typography.h1,
                fontFamily = FontFamily.Serif,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.padding(20.dp))

            TextItem(
                text = "Email",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                style = MaterialTheme.typography.caption,
                modifier = Modifier
                    .fillMaxWidth()
            )

            TextFieldItem(
                value = email,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                placeholder = "Enter your Email",
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = bgColorLight
                ),
                onValueChange = {
                    email = it
                }
            )

            TextItem(
                text = "Username",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                style = MaterialTheme.typography.caption,
                modifier = Modifier
                    .fillMaxWidth()
            )

            TextFieldItem(
                value = username,
                placeholder = "username",
                onValueChange = { username = it },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = bgColorLight
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )

            TextItem(
                text = "Password",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                style = MaterialTheme.typography.caption,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            )


            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
            )
            {
                TextField(
                    value = password1, onValueChange = { password1 = it },
                    singleLine = true,
                    placeholder = { Text(text = "password") },
                    modifier = Modifier
                        .fillMaxWidth(),
                    visualTransformation = if (password1Visible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        val image = if (password1Visible)
                            Icons.Filled.Visibility
                        else Icons.Filled.VisibilityOff

                        val description = if (password1Visible) "Hide password" else "Show password"

                        IconButton(onClick = { password1Visible = !password1Visible }) {
                            Icon(imageVector = image, description)
                        }
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = bgColorLight

                    )
                )
            }

            TextItem(
                text = "Reenter Password",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                style = MaterialTheme.typography.caption,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
            )
            {
                TextField(
                    value = password2, onValueChange = { password2 = it },
                    singleLine = true,
                    placeholder = { Text(text = "password") },
                    modifier = Modifier
                        .fillMaxWidth(),
                    visualTransformation = if (password2Visible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        val image = if (password2Visible)
                            Icons.Filled.Visibility
                        else Icons.Filled.VisibilityOff

                        val description = if (password2Visible) "Hide password" else "Show password"

                        IconButton(onClick = { password2Visible = !password2Visible }) {
                            Icon(imageVector = image, description)
                        }
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = bgColorLight,
                        focusedIndicatorColor = if (password1 == password2) Color.Green else Color.Red
                    )
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
                        errortext = ""
                        if (password1 == password2) {
                            val regForm = JSONObject()
                            regForm.put("subject", "register")
                            regForm.put("email", email)
                            regForm.put("uname", username)
                            regForm.put("passwd1", password1)

                            postForm(regForm){ ret->
                               errortext =  when (ret.getString("status")) {
                                    "success" -> {
                                        val uname = ret.getString("uname")
                                        val key = ret.getString("key")
                                        val editor = keyPref.edit()
                                        editor.putString("uname",uname)
                                        editor.putString("key",key)
                                        editor.apply()
                                        globalUsername = uname
                                        globalKey = key
                                        coroutineScope.launch(Main){
                                            navController.navigate(NavRoutes.UserDetailsPage.route)
                                        }
                                        ""
                                    }
                                    "alreadyuser" -> {
                                        "Username already exists"
                                    }
                                    "alreadyemail" -> {
                                        "Email already exists"
                                    }
                                    else -> {
                                        ret.getString("status")
                                    }
                                }
                            }
                        }
                        else {
                            errortext = "Passwords do not match"
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
                        text = "Register",
                        fontSize = 20.sp
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, bottom = 5.dp)
            ) {
                Text(text = errortext)
            }
        }
    }
}


@Preview
@Composable
fun SignPrev() {
    SignUpPage(navController = NavController(LocalContext.current))
}