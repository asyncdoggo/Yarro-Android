package com.example.bitter

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import com.example.bitter.ui.theme.*
import org.json.JSONObject

class SignUpActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            SignUp()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUp() {

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

            TextItem(
                text = "Register",
                fontsSize = 50.sp,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.h1,
                fontFamily = FontFamily.Cursive,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.padding(20.dp))

            TextItem(
                text = "Email",
                fontsSize = 20.sp,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.h3,
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
                    backgroundColor = Color(0xFFEAFFE3)
                ),
                onValueChange = {
                    email = it
                }
            )

            TextItem(
                text = "Username",
                fontsSize = 20.sp,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.h3,
                modifier = Modifier
                    .fillMaxWidth()
            )

            TextFieldItem(
                value = username,
                placeholder = "username",
                onValueChange = { username = it },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color(0xFFEAFFE3)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )

            TextItem(
                text = "Password",
                fontsSize = 20.sp,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.h3,
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
                        backgroundColor = Color(0xFFEAFFE3)

                    )
                )
            }

            TextItem(
                text = "Reenter Password",
                fontsSize = 20.sp,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.h3,
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
                        backgroundColor = Color(0xFFEAFFE3),
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

                            val ret = postForm(regForm)

                            when(ret.getString("status")){
                                "success" -> {
                                    val uname = ret.getString("uname")
                                    val key = ret.getString("key")
                                    val intent = Intent(context,MainActivity::class.java)
                                    intent.putExtra("uname",uname)
                                    intent.putExtra("key",key)
                                    //TODO: Start nextactivity
                                }
                                "alreadyuser" -> {
                                    errortext = "Username already exists"
                                }
                                "alreadyemail" -> {
                                    errortext = "Email already exists"
                                }
                                else -> {
                                    errortext = ret.getString("status")
                                }
                            }

                        } else {
                            errortext = "Passwords do not match"
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


