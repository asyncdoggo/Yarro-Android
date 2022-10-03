package com.example.bitter.userdetails

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState


@Composable
fun UserDetailScreen(navController: NavController) {

    val context = LocalContext.current
    val keyPref = context.getSharedPreferences("authkey", Context.MODE_PRIVATE)
    val editor = keyPref.edit()
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

    var backPressedTime: Long = 0
    BackHandler {
        val t = System.currentTimeMillis()

        if (t - backPressedTime > 2000){
            backPressedTime = t
            Toast.makeText(context,"Press back again to logout", Toast.LENGTH_SHORT).show()
        }
        else viewModel.logout(uname?:"",key?:"",editor,navController)
    }


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
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                        .padding(top = 20.dp,bottom = 25.dp)
                ) {
                    Text(
                        text = "User Details",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Medium,
                        style = MaterialTheme.typography.h1,
                        fontFamily = FontFamily.Serif
                    )
                }

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
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                )
                {
                    OutlinedTextField(
                        value = fname,
                        onValueChange = {
                            viewModel.setVal("fname", it)
                        },
                        singleLine = true,
                        label = { Text(text = "First Name") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next,
                            capitalization = KeyboardCapitalization.Words
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
                        value = lname,
                        onValueChange = {
                            viewModel.setVal("lname", it)
                        },
                        singleLine = true,
                        label = { Text(text = "Last Lame") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next,
                            capitalization = KeyboardCapitalization.Words

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
                        value = gender,
                        onValueChange = {
                            viewModel.setVal("gender", it)
                        },
                        singleLine = true,
                        label = { Text(text = "Gender") },
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
                        value = mob, onValueChange = { viewModel.setVal("mob", it) },
                        singleLine = true,
                        label = { Text(text = "Mobile Number")},
                        modifier = Modifier
                            .fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent
                        )
                    )
                }

                OutlinedTextField(
                    value = dob,
                    enabled = false,
                    onValueChange = {/* DO NOTHING */},
                    label = { Text(text = "Date of birth")},
                    modifier = Modifier.fillMaxWidth()
                        .noRippleClickable { dialogState.show() },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        disabledLabelColor = Color(0xFF636c6b),
                        disabledTextColor = Color.Black
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
                        shape = RoundedCornerShape(5.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 25.dp)
                            .size(50.dp),
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
                    Text(
                        text = errortext,
                        color = Color.Red
                    )
                }
            }
        }
    }
}


inline fun Modifier.noRippleClickable(crossinline onClick: () -> Unit): Modifier =
    composed {
        clickable(indication = null,
            interactionSource = remember { MutableInteractionSource() }) {
            onClick()
        }
    }

@Preview
@Composable
fun UserDetailsScreenprev() {
    UserDetailScreen(navController = NavController(LocalContext.current))
}