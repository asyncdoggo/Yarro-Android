package com.example.bitter.ui.theme

import androidx.compose.material.Colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)

val Colors.linkColor: Color
@Composable
get() = if(isLight) Color(0xff0000ee) else Color(0xFF00EFFF)

val Colors.buttonColor: Color
@Composable
get() = Color(0xff0065ff)

val Colors.chatCardColor: Color
@Composable
get() = if(isLight) Color(0xffefefef) else Color(0xff5647d4)
