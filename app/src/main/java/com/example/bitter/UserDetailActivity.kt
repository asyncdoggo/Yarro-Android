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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bitter.ui.theme.TextFieldItem
import com.example.bitter.ui.theme.TextItem
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import org.json.JSONObject

class UserDetailActivity : ComponentActivity() {
    var uname:String = ""
    var key :String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            val keyPref = LocalContext.current.getSharedPreferences("authkey", Context.MODE_PRIVATE)
            uname = keyPref.getString("uname",null).toString()
            key = keyPref.getString("key",null).toString()
            UserDetails()
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun UserDetails() {

        val keyPref = LocalContext.current.getSharedPreferences("authkey", Context.MODE_PRIVATE)
        val editor = keyPref.edit()

        var firstname by remember {
            mutableStateOf("")
        }
        var lastname by remember {
            mutableStateOf("")
        }
        var gender by remember {
            mutableStateOf("")
        }

        var mob by remember {
            mutableStateOf("")
        }

        var dob by remember {
            mutableStateOf("")
        }

        var errortext by remember {
            mutableStateOf("")
        }

        val dialogState = rememberMaterialDialogState()

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
                TextItem(
                    text = "User Details",
                    fontsSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.h1,
                    fontFamily = FontFamily.Cursive,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.padding(20.dp))

                TextItem(
                    text = "Enter your details (optional)",
                    fontsSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    style = TextStyle.Default,
                    modifier = Modifier.padding(10.dp)
                )

                TextItem(
                    text = "First name",
                    fontsSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.h3,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                TextFieldItem(
                    value = firstname,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    placeholder = "First name",
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color(0xFFEAFFE3)
                    ),
                    onValueChange = {
                        firstname = it
                    }
                )

                TextItem(
                    text = "Last name",
                    fontsSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.h3,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                TextFieldItem(
                    value = lastname,
                    placeholder = "Last name",
                    onValueChange = { lastname = it },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color(0xFFEAFFE3)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                )

                TextItem(
                    text = "Gender",
                    fontsSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.h3,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                )


                TextFieldItem(
                    value = gender,
                    placeholder = "Gender",
                    onValueChange = {gender = it},
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color(0xFFEAFFE3)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                )

                TextItem(
                    text = "mobile number",
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
                        value = mob, onValueChange = { mob = it },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color(0xFFEAFFE3)
                        )
                    )
                }

                TextItem(
                    text = "Date of birth",
                    fontsSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.h3,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                )

                TextField(
                    value = dob,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color(0xFFEAFFE3)
                    ),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "",
                            modifier = Modifier.clickable {
                                dialogState.show()
                            }
                        )
                    }
                )


                MaterialDialog(
                    dialogState = dialogState,
                    buttons = {
                        positiveButton("Ok")
                        negativeButton("Cancel")
                    }
                ) {
                    datepicker { date ->
                        dob = date.toString()
                    }
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
                            val regForm = JSONObject()
                            regForm.put("subject", "udetails")
                            regForm.put("key", key)
                            regForm.put("uname", uname)
                            regForm.put("fname", firstname)
                            regForm.put("lname", lastname)
                            regForm.put("gender", gender)
                            regForm.put("mob", mob)
                            regForm.put("dob", dob)

                            postForm(regForm){ ret->
                                when (ret.getString("status")) {
                                    "success" -> {
                                        val intent = Intent(context, PostActivity::class.java)
                                        editor.putString("uname",uname)
                                        editor.putString("key",key)
                                        editor.apply()
                                        context.startActivity(intent)
                                    }
                                    //TODO: on status badkey: logout
                                    else -> {
                                        errortext = ret.getString("status")
                                        println(errortext)
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
                    text = "Next",
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
}
