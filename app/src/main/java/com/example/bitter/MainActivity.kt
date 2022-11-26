package com.example.bitter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import com.example.bitter.data.PostDatabase
import com.example.bitter.main.MainNav
import com.example.bitter.ui.theme.BitterTheme


var postUrl: String = "http://192.168.1.4:5005"

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PostDatabase.setInstance(this)
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

