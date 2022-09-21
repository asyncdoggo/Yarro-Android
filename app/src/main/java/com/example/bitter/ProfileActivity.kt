package com.example.bitter

import Bitter.R
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.bitter.data.*
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import org.json.JSONObject


@Composable
fun ProfilePageStart(navController: NavController) {
//    val keyPref = LocalContext.current.getSharedPreferences("authkey", Context.MODE_PRIVATE)
//    globalUsername = keyPref.getString("uname",null).toString()
//    globalKey = keyPref.getString("key",null).toString()

    removeCoilCache(LocalContext.current)
    ProfilePage(navController = navController)

    BackHandler {
        navController.navigate(NavRoutes.MainPage.route)
    }
}

@Composable
fun ProfilePage(navController: NavController) {

    var checked by remember {
        mutableStateOf(true)
    }
    var details by remember {
        mutableStateOf(true)
    }

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val context = LocalContext.current
    var bitmap by remember {
        mutableStateOf<Bitmap?>(null)
    }
    val launcher = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) checked = false
        imageUri = uri
    }
    imageUri?.let {
        val source = ImageDecoder
            .createSource(context.contentResolver, it)
        bitmap = ImageDecoder.decodeBitmap(source)
    }

    var fname by remember {
        mutableStateOf("")
    }
    var lname by remember {
        mutableStateOf("")
    }
    var gender by remember {
        mutableStateOf("")
    }
    var mob by remember {
        mutableStateOf(" ")
    }
    var dob by remember {
        mutableStateOf("")
    }

    var editable by remember {
        mutableStateOf(false)
    }

    var errortext by remember {
        mutableStateOf("")
    }

    val dialogState = rememberMaterialDialogState()


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
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
                    model = "$postUrl/images/$globalUsername",
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
                Text(text = globalUsername)
            }
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.fillMaxWidth(1f)
            ) {
                IconButton(onClick = { editable = !editable }) {
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
                    onValueChange = { fname = it },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        unfocusedIndicatorColor = if (!editable) Color.Transparent else Color.Black,
                        focusedIndicatorColor = if (!editable) Color.Transparent else Color.Black
                    )
                )
            }
        }

        ItemRow(
            text = "Last Name",
            value = lname,
            onValueChange = { lname = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, top = 20.dp),
            editable = editable
        )

        ItemRow(
            text = "Gender",
            value = gender,
            onValueChange = { gender = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, top = 20.dp),
            editable = editable
        )

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
                    onValueChange = { mob = it },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        unfocusedIndicatorColor = if (!editable) Color.Transparent else Color.Black,
                        focusedIndicatorColor = if (!editable) Color.Transparent else Color.Black
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
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
                        contentDescription = "",
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
                        dob = date.toString()
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
                val postform = JSONObject()
                postform.put("subject", "udetails")
                postform.put("key", globalKey)
                postform.put("uname", globalUsername)
                postform.put("fname", fname)
                postform.put("lname", lname)
                postform.put("gender", gender)
                postform.put("mob", mob)
                postform.put("dob", dob)

                postForm(postform){ ret->
                    errortext = when (ret.getString("status")) {
                        "success" -> {
                            "Saved Successfully"
                        }
                        "mob" -> {
                            "Mobile number should be 10 digit number"
                        }
                        else -> {
                            ret.getString("status")
                        }
                    }
                }

                bitmap?.let { postImage(context, it, globalUsername, globalKey) }

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
        val postform = JSONObject()
        postform.put("subject", "getudetails")
        postform.put("key", globalKey)
        postform.put("uname", globalUsername)

        postForm(postform){ ret->
            errortext = when (ret.getString("status")) {
                "success" -> {
                    val data = ret.getJSONObject("data")
                    fname = data.getString("fname")
                    lname = data.getString("lname")
                    gender = data.getString("gender")
                    mob = data.getString("mob")
                    dob = data.getString("dob")
                    details = false
                    "" // return

                }
                else -> {
                    ret.getString("status")
                }
            }
        }
    }
}


@Composable
fun ItemRow(
    text: String,
    value: String,
    onValueChange: (it: String) -> Unit,
    modifier: Modifier,
    editable: Boolean


) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.fillMaxWidth(0.2f)
        ) {
            Text(text = text, fontWeight = FontWeight.Bold)
        }
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(top = 5.dp)
        ) {
            TextField(
                value = value,
                onValueChange = { onValueChange(it) },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    unfocusedIndicatorColor = if (!editable) Color.Transparent else Color.Black,
                    focusedIndicatorColor = if (!editable) Color.Transparent else Color.Black
                )
            )
        }
    }
}

