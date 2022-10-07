import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.bitter.data.Routes
import com.example.bitter.login.LoginViewModel
import com.example.bitter.ui.theme.buttonColor
import com.example.bitter.ui.theme.linkColor

@Composable
fun LoginScreenSetup(navController: NavController, error: String?) {
    val context = LocalContext.current
    val keyPref = context.getSharedPreferences("authkey", Context.MODE_PRIVATE)
    val uname = keyPref.getString("uname", null)
    val key = keyPref.getString("key", null)

    val viewModel = viewModel<LoginViewModel>()

    if(uname != null && key != null) {
        val editor = keyPref.edit()
        LaunchedEffect(key1 = true) {
            viewModel.autoLogin(uname, key,editor,navController)
        }
    }
    LoginScreen(navController,error)
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(navController: NavController,error: String?) {

    val viewModel = viewModel<LoginViewModel>()

    val username by viewModel.username.collectAsState()
    val password by viewModel.password.collectAsState()
    val passwordVisible by viewModel.passwordVisible.collectAsState()
    val errortext by viewModel.errortext.collectAsState(if(error == "logout") "You have been logged out" else "")
    val keyPref = LocalContext.current.getSharedPreferences("authkey", Context.MODE_PRIVATE)
    val editor = keyPref.edit()
    val keyboardController = LocalSoftwareKeyboardController.current
    val loading by viewModel.loading.collectAsState()

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
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier.padding(10.dp),
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )
            CircularProgressIndicator()
        }
    }
   else{
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

            Spacer(modifier = Modifier.padding(50.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Text(
                    text = "B-itter",
                    fontSize = 50.sp,
                    color = MaterialTheme.colors.onBackground,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.h1,
                    fontFamily = FontFamily.Cursive
                )
            }
            Spacer(modifier = Modifier.padding(20.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 25.dp)
            )
            {
                OutlinedTextField(
                    value = username,
                    onValueChange = { viewModel.onUsernameChange(it) },
                    label = { Text(text = "Enter your username")},
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        textColor = MaterialTheme.colors.onBackground
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
            )
            {
                OutlinedTextField(
                    value = password,
                    onValueChange = { viewModel.onPasswordChanged(it) },
                    label = { Text(text = "Enter your password")},
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth(),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                            viewModel.loginButtonOnClick(navController, editor = editor)
                        }
                    ),
                    trailingIcon = {
                        val image = if (passwordVisible)
                            Icons.Filled.Visibility
                        else Icons.Filled.VisibilityOff

                        val description = if (passwordVisible) "Hide password" else "Show password"

                        IconButton(onClick = { viewModel.onVisibleButtonClick() }) {
                            Icon(imageVector = image, description)
                        }
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        textColor = MaterialTheme.colors.onBackground
                    )
                )
            }
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                Text(
                    text = "Forgot password?",
                    fontSize = 15.sp,
                    modifier = Modifier.clickable {
                        navController.navigate(Routes.ForgotPassScreen.route)
                    },
                    color = MaterialTheme.colors.linkColor
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 80.dp, bottom = 5.dp)
            )
            {
                Button(
                    onClick = {
                        viewModel.loginButtonOnClick(navController, editor)
                    },
                    shape = RoundedCornerShape(size = 5.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.buttonColor,
                        contentColor = Color.White
                    )

                ) {
                    Text(
                        text = "Login",
                        fontSize = 20.sp,
                        color = MaterialTheme.colors.onBackground,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
                    .clickable {
                        navController.navigate(Routes.RegisterScreen.route)
                    }
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("New to B-itter? ")
                        withStyle(style = SpanStyle(color = MaterialTheme.colors.linkColor)) {
                            append("Register here")
                        }
                    },
                    color = MaterialTheme.colors.onBackground,
                    fontSize = 15.sp
                )

            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            ) {
                Text(
                    text = errortext,
                    color = MaterialTheme.colors.error
                )
            }
        }
    }
}
}

@Preview
@Composable
fun LoginPrev() {
    LoginScreen(navController = NavController(LocalContext.current),"")
}