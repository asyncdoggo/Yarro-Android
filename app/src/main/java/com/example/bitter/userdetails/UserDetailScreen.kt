package com.example.bitter.userdetails

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.bitter.ui.theme.bgColorLight
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState


@Composable
fun UserDetailScreen(navController: NavController) {

    val context = LocalContext.current
    val keyPref = context.getSharedPreferences("authkey", Context.MODE_PRIVATE)
    val uname = keyPref.getString("uname", null)
    val key = keyPref.getString("key", null)

    val viewModel = viewModel<UserDetailViewModel>()

    val fname by viewModel.fname.collectAsState()
    val lname by viewModel.lname.collectAsState()
    val gender by viewModel.gender.collectAsState()
    val mob by viewModel.mob.collectAsState()
    val dob by viewModel.dob.collectAsState()
    val errortext by viewModel.error.collectAsState()

    val dialogState = rememberMaterialDialogState()
    val loading by viewModel.loading.collectAsState()

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
                .background(Color(0xFFF3FCFF))
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
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "User Details",
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
                    modifier = Modifier.padding(10.dp)
                ) {
                    Text(
                        text = "Enter your details (optional)",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        style = TextStyle.Default,
                        fontFamily = FontFamily.Default
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "First name",
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
                        value = fname,
                        onValueChange = {
                            viewModel.setVal("fname", it)
                        },
                        singleLine = true,
                        placeholder = { Text(text = "First name") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = bgColorLight
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next,
                            capitalization = KeyboardCapitalization.Words
                        )
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Last name",
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
                        value = lname,
                        onValueChange = {
                            viewModel.setVal("lname", it)
                        },
                        singleLine = true,
                        placeholder = { Text(text = "Last name") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = bgColorLight
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next,
                            capitalization = KeyboardCapitalization.Words

                        )
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    Text(
                        text = "Gender",
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
                        value = gender,
                        onValueChange = {
                            viewModel.setVal("gender", it)
                        },
                        singleLine = true,
                        placeholder = { Text(text = "Gender") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = bgColorLight
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        )
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    Text(
                        text = "mobile number",
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
                        value = mob, onValueChange = { viewModel.setVal("mob", it) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = bgColorLight
                        )
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    Text(
                        text = "Date of birth",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        style = MaterialTheme.typography.caption,
                        fontFamily = FontFamily.Default
                    )
                }

                TextField(
                    value = dob,
                    readOnly = true,
                    onValueChange = {/* DO NOTHING */},
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = bgColorLight
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
                        viewModel.setVal("dob", date.toString())
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
                            viewModel.saveButtonClick(uname, key, navController)
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
