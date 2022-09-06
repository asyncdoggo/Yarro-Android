package com.example.bitter

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bitter.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.SocketTimeoutException

class ResetPassActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            ResetPage()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ResetPage() {
    var email by remember {
        mutableStateOf("")
    }
    var errortext by remember {
        mutableStateOf("")
    }


    Box(
        modifier = Modifier
            .background(Color(0xFFEAFFE3))
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
                fontsSize = 50.sp,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.h1,
                fontFamily = FontFamily.Cursive,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            )
            Spacer(modifier = Modifier.padding(20.dp))

            TextItem(
                text = "Email",
                fontsSize = 20.sp,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.h3,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            )

            TextFieldItem(
                value = email,
                onValueChange = {email = it},
                placeholder = "Email",
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color(0xFFEAFFE3)
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

                        postForm(forgotPassForm, callback = object : Callback {
                            override fun onFailure(call: Call, e: IOException) {
                                e.printStackTrace()
                            }

                            override fun onResponse(call: Call, response: Response) {
                                val responseString = String(response.body.bytes())
                                val ret = JSONObject(responseString)
                                try {
                                    when (ret.getString("status")) {
                                        "success" -> {
                                            errortext = "Email sent successfully"
                                        }
                                        "noemail" -> {
                                            errortext = "Email not found"
                                        }
                                        else -> {
                                            errortext = "Unknown Error"
                                        }
                                    }
                                }
                                catch(e:JSONException){
                                    e.printStackTrace()
                                }
                                catch (e: SocketTimeoutException){
                                    errortext = "Network Error"
                                }
                            }

                        })
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

