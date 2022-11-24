package com.example.bitter.login

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.bitter.data.Routes
import com.example.bitter.ui.theme.linkColor
import com.example.bitter.util.ApiService
import kotlinx.coroutines.launch

@Composable
fun VerifyScreen(navController: NavHostController) {
    val keyPref = LocalContext.current.getSharedPreferences("authkey", Context.MODE_PRIVATE)
    val token = remember { keyPref.getString("token", "") }
    val scope = rememberCoroutineScope()
    var loading by remember {
        mutableStateOf(false)
    }
    var msg by remember {
        mutableStateOf("")
    }
    var done by remember {
        mutableStateOf(false)
    }



    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "Email verification required, check your  email for a verification link",
                textAlign = TextAlign.Center
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .size(50.dp)
                .clickable(enabled = true) {
                    scope.launch {
                        if  (!done) {
                            loading = true
                            ApiService.resendConfirm(token)
                            loading = false
                            msg = "Email sent successfully"
                            done = true
                        }
                    }
                }
        ) {
            Text(text = buildAnnotatedString {
                append("Didn't receive it? ")
                withStyle(style = SpanStyle(color = MaterialTheme.colors.linkColor)) {
                    append("click here")
                }
            })
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            if (loading){
                CircularProgressIndicator()
            }
            else{
                Text(text = msg)
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Button(onClick = { navController.navigate(Routes.LoginScreen.route) }) {
                Text(text = "continue")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun VerifyPrev() {
    VerifyScreen(navController = rememberNavController())
}