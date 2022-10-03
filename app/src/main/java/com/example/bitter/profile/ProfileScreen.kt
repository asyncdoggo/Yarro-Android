package com.example.bitter.profile

import Bitter.R
import android.content.Context
import android.graphics.ImageDecoder
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.bitter.data.Routes
import com.example.bitter.postUrl
import com.example.bitter.ui.theme.profPicColor
import com.example.bitter.userdetails.noRippleClickable
import com.example.bitter.util.postImage
import com.example.bitter.util.removeCoilCache
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState


@Composable
fun ProfileScreenSetup(navController: NavController) {

    removeCoilCache(LocalContext.current)
    ProfileScreen(navController)

    BackHandler {
        navController.navigate(Routes.MainScreen.route)
    }
}

@Composable
fun ProfileScreen(navController: NavController) {
    val viewModel = viewModel<ProfileViewModel>()
    val context = LocalContext.current
    val keyPref = context.getSharedPreferences("authkey", Context.MODE_PRIVATE)
    val uname = keyPref.getString("uname", null)
    val key = keyPref.getString("key", null)

    val fname by viewModel.fname.collectAsState()
    val lname by viewModel.lname.collectAsState()
    val gender by viewModel.gender.collectAsState()
    val mob by viewModel.mob.collectAsState()
    val dob by viewModel.dob.collectAsState()
    val errortext by viewModel.error.collectAsState()
    val checked by viewModel.checked.collectAsState()
    val details by viewModel.details.collectAsState()

    val imageUri = viewModel.imageUri

    val bitmap = viewModel.bitmap

    val launcher = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) viewModel.setVal("checked",false)
        viewModel.setImage(uri)
    }
    imageUri?.let {
        val source = ImageDecoder.createSource(context.contentResolver, it)
        viewModel.setImageBitmap(ImageDecoder.decodeBitmap(source))
    }

    val dialogState = rememberMaterialDialogState()


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.verticalScroll(rememberScrollState())
            .padding(start = 20.dp,end = 20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            if (checked) {
                AsyncImage(
                    model = "$postUrl/images/$uname",
                    contentDescription = "text",
                    placeholder = painterResource(id = R.drawable.ic_launcher_background),
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                )
            } else {
                bitmap?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                    )
                }
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
        ) {
            Text(
                "Change Profile Picture",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.profPicColor,
                modifier = Modifier
                    .clickable { launcher.launch("image/*") }
            )
        }


        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, bottom = 10.dp)
        ) {
            Text(text = uname?:"username")
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
                label = { Text(text = "Last Name") },
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
                viewModel.setVal("dob",date.toString())
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, top = 20.dp)
        ) {
            Button(onClick = {
                viewModel.saveButtonClick(uname,key,navController)
                bitmap?.let { postImage(context, it, uname?:"", key?:"") }
            }) {
                Text(text = "Save")
            }

            Text(
                text = errortext,
                modifier = Modifier.padding(start = 20.dp
                )
            )
        }
    }

    if (details) {
        viewModel.getUserDetails(uname,key,navController)
    }
}


