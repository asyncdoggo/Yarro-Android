package com.example.bitter

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.bitter.data.NavRoutes
import com.example.bitter.data.postForm
import com.example.bitter.ui.theme.TextFieldItem
import com.example.bitter.ui.theme.TextItem
import com.example.bitter.ui.theme.bgColorLight
import org.json.JSONObject

@Composable
fun forgotPassPage(navController: NavController) {

    BackHandler {
        navController.navigate(NavRoutes.LoginPage.route)
    }


    var email by remember {
        mutableStateOf("")
    }
    var errortext by remember {
        mutableStateOf("")
    }


    Box(
        modifier = Modifier
            .background(bgColorLight)
            .fillMaxSize()
            .padding(10.dp)
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Spacer(modifier = Modifier.padding(50.dp))

            TextItem(
                text = "Reset password",
                fontSize = 40.sp,
                fontWeight = FontWeight.Medium,
                style = MaterialTheme.typography.h1,
                fontFamily = FontFamily.Serif,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            )
            Spacer(modifier = Modifier.padding(20.dp))

            TextItem(
                text = "Email",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                style = MaterialTheme.typography.caption,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            )

            TextFieldItem(
                value = email,
                onValueChange = {email = it},
                placeholder = "Email",
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = bgColorLight
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )

            Button(
                onClick = {
                    val forgotPassForm = JSONObject()
                    forgotPassForm.put("subject", "forgotpass")
                    forgotPassForm.put("email", email)

                    postForm(forgotPassForm){ ret->
                        errortext = when (ret.getString("status")) {
                            "success" -> {
                                "Email sent successfully"
                            }
                            "noemail" -> {
                                "Email not found"
                            }
                            else -> {
                                "Unknown Error"
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
                    text = "Submit",
                    fontSize = 20.sp
                )
            }


            Text(
                text = errortext,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(40.dp)
            )

        }
    }
}

@Preview
@Composable
fun ForgPrev() {
    forgotPassPage(navController = NavController(LocalContext.current))
}

