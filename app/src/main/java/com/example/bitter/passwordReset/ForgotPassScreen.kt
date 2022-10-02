package com.example.bitter.passwordReset

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.bitter.ui.theme.bgColorLight

@Composable
fun ForgotPassScreen(navController: NavController) {
    val viewModel = viewModel<ForgotPassViewModel>()

    val email by viewModel.email.collectAsState()

    val error by viewModel.error.collectAsState()

    val loading by viewModel.loading.collectAsState()

    BackHandler {
        navController.popBackStack()
    }

    if (loading) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x4DFFFFFF)),
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
                .background(bgColorLight)
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
                Spacer(modifier = Modifier.padding(50.dp))

                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Text(
                        text = "Reset password",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Medium,
                        style = MaterialTheme.typography.h1,
                        fontFamily = FontFamily.Serif
                    )
                }
                Spacer(modifier = Modifier.padding(20.dp))

                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                ) {
                    Text(
                        text = "Email",
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
                        value = email,
                        onValueChange = {
                            viewModel.onEmailChange(it)
                        },
                        singleLine = true,
                        placeholder = { Text(text = "Email") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = bgColorLight
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                viewModel.resetButtonOnClick()
                            }
                        )

                    )
                }

                Button(
                    onClick = {
                        viewModel.resetButtonOnClick()
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
                        text = "Submit",
                        fontSize = 20.sp
                    )
                }


                Text(
                    text = error,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .padding(40.dp)
                )

            }
        }
    }
}

@Preview
@Composable
fun ForgPrev() {
    ForgotPassScreen(navController = NavController(LocalContext.current))
}

