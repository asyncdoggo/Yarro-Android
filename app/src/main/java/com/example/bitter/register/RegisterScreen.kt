package com.example.bitter.register

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.bitter.ui.theme.buttonColor


@Composable
fun RegisterScreen(navController: NavController) {


    BackHandler {
        navController.popBackStack()
    }
    val viewModel = viewModel<RegisterViewModel>()
    val context = LocalContext.current
    val keyPref = context.getSharedPreferences("authkey", Context.MODE_PRIVATE)
    val editor = keyPref.edit()
    val username by viewModel.username.collectAsState()
    val password1 by viewModel.password1.collectAsState()
    val password2 by viewModel.password2.collectAsState()
    val email by viewModel.email.collectAsState()
    val error by viewModel.error.collectAsState()
    val password1Visible by viewModel.password1Visible.collectAsState()
    val password2Visible by viewModel.password2Visible.collectAsState()
    val loading by viewModel.loading.collectAsState()


    if (loading) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Loading",
                modifier = Modifier.padding(10.dp),
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )
            CircularProgressIndicator()
        }
    }

    else {

        Box(
            modifier = Modifier
                .background(MaterialTheme.colors.background)
                .fillMaxSize()
                .padding(10.dp)

        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .verticalScroll(rememberScrollState())
            ) {

                Spacer(modifier = Modifier.padding(40.dp))

                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Register",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Medium,
                        style = MaterialTheme.typography.h1,
                        fontFamily = FontFamily.Serif
                    )
                }

                Spacer(modifier = Modifier.padding(20.dp))


                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                )
                {
                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            viewModel.setVal("email", it)
                        },
                        singleLine = true,
                        label = { Text(text = "Enter your Email") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        )
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                )
                {
                    OutlinedTextField(
                        value = username,
                        onValueChange = {
                            viewModel.setVal("username", it)
                        },
                        singleLine = true,
                        label = { Text(text = "Enter your username") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        )
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                )
                {
                    OutlinedTextField(
                        value = password1, onValueChange = { viewModel.setVal("password1", it) },
                        singleLine = true,
                        label = { Text(text = "Enter your password") },
                        modifier = Modifier
                            .fillMaxWidth(),
                        visualTransformation = if (password1Visible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Next
                        ),
                        trailingIcon = {
                            val image = if (password1Visible)
                                Icons.Filled.Visibility
                            else Icons.Filled.VisibilityOff

                            val description =
                                if (password1Visible) "Hide password" else "Show password"

                            IconButton(onClick = {
                                viewModel.setVal(
                                    "password1Visible",
                                    !password1Visible
                                )
                            }) {
                                Icon(imageVector = image, description)
                            }
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent

                        )
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                )
                {
                    OutlinedTextField(
                        value = password2, onValueChange = { viewModel.setVal("password2", it) },
                        singleLine = true,
                        label = { Text(text = "Reenter your password") },
                        modifier = Modifier
                            .fillMaxWidth(),
                        visualTransformation = if (password2Visible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        trailingIcon = {
                            val image = if (password2Visible)
                                Icons.Filled.Visibility
                            else Icons.Filled.VisibilityOff

                            val description =
                                if (password2Visible) "Hide password" else "Show password"

                            IconButton(onClick = {
                                viewModel.setVal(
                                    "password2Visible",
                                    !password2Visible
                                )
                            }) {
                                Icon(imageVector = image, description)
                            }
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent,
                            focusedIndicatorColor = if (password1 == password2) Color.Green else Color.Red
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                viewModel.registerButtonOnClick(editor, navController)
                            }
                        )
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                {
                    Button(
                        onClick = {
                            viewModel.registerButtonOnClick(editor, navController)
                        },
                        shape = RoundedCornerShape(5.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 25.dp)
                            .size(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.buttonColor,
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
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, bottom = 5.dp)
                ) {
                    Text(
                        text = error,
                        color = Color.Red
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun SignPrev() {
    RegisterScreen(navController = NavController(LocalContext.current))
}