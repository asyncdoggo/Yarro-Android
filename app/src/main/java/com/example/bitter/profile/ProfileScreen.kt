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
import androidx.compose.material.icons.filled.Edit
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
    val editable by viewModel.editable.collectAsState()

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
                color = Color(0xFFA00BCD),
                modifier = Modifier
                    .clickable { launcher.launch("image/*") }
            )
        }


        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, top = 20.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.fillMaxWidth(0.2f)
            ) {
                Text(text = "username", fontWeight = FontWeight.Bold)
            }
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(start = 16.dp)

            ) {
                Text(text = uname?:"")
            }
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.fillMaxWidth(1f)
            ) {
                IconButton(onClick = { viewModel.setVal("editable",!editable)}) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "edit"
                    )
                }
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, top = 20.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.fillMaxWidth(0.2f)
            ) {
                Text(text = "First Name", fontWeight = FontWeight.Bold)
            }
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(top = 5.dp)
            ) {
                TextField(
                    value = fname,
                    onValueChange = { viewModel.setVal("fname",it) },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        unfocusedIndicatorColor = if (!editable) Color.Transparent else Color.Black,
                        focusedIndicatorColor = if (!editable) Color.Transparent else Color.Black
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next,
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                    readOnly = !editable
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, top = 20.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.fillMaxWidth(0.2f)
            ) {
                Text(text = "Last Name",
                    fontWeight = FontWeight.Bold)
            }
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(top = 5.dp)
            ) {
                TextField(
                    value = lname,
                    onValueChange = {
                        viewModel.setVal("lname", it)
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        unfocusedIndicatorColor = if (!editable
                        ) Color.Transparent else Color.Black,
                        focusedIndicatorColor = if (!editable) Color.Transparent else Color.Black
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next,
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                    readOnly = !editable
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, top = 20.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.fillMaxWidth(0.2f)
            ) {
                Text(text = "Gender",
                    fontWeight = FontWeight.Bold)
            }
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(top = 5.dp)
            ) {
                TextField(
                    value = gender,
                    onValueChange = {
                        viewModel.setVal("gender", it)
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        unfocusedIndicatorColor = if (!editable
                        ) Color.Transparent else Color.Black,
                        focusedIndicatorColor = if (!editable) Color.Transparent else Color.Black
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next,
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                    readOnly = !editable
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, top = 20.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.fillMaxWidth(0.2f)
            ) {
                Text(text = "Mobile", fontWeight = FontWeight.Bold)
            }
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(top = 5.dp)
            ) {
                TextField(
                    value = mob,
                    onValueChange = { viewModel.setVal("mob",it) },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        unfocusedIndicatorColor = if (!editable) Color.Transparent else Color.Black,
                        focusedIndicatorColor = if (!editable) Color.Transparent else Color.Black
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    readOnly = !editable
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, top = 20.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.fillMaxWidth(0.2f)
            ) {
                Text(text = "D.O.B", fontWeight = FontWeight.Bold)
            }
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(start = 16.dp)
            ) {
                Text(text = dob)
            }
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.fillMaxWidth(1f)
            ) {
                IconButton(onClick = { dialogState.show() }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "DOB",
                        modifier = Modifier.clickable {
                            dialogState.show()
                        }
                    )
                }
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


