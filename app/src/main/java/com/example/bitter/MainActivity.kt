package com.example.bitter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import com.example.bitter.data.PostDatabase
import com.example.bitter.main.MainNav
import com.example.bitter.ui.theme.BitterTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController


var postUrl: String = "https://subpixel.pythonanywhere.com"

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PostDatabase.setInstance(this)
            // Remember a SystemUiController
            val systemUiController = rememberSystemUiController()
            val useDarkIcons = !isSystemInDarkTheme()

            DisposableEffect(systemUiController, useDarkIcons) {
                systemUiController.setSystemBarsColor(
                    color = if(useDarkIcons) Color.White else Color.Black,
                    darkIcons = useDarkIcons
                )
                onDispose {}
            }


            BitterTheme {
                MainNav()
                BackHandler { finishAndRemoveTask() }
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

