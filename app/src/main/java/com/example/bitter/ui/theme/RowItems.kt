package com.example.bitter.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit


@Composable
fun TextItem(
    text: String,
    fontSize: TextUnit,
    fontWeight: FontWeight,
    style: TextStyle,
    fontFamily: FontFamily = FontFamily.Default,
    modifier: Modifier
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(
            text = text,
            fontSize = fontSize,
            fontWeight = fontWeight,
            style = style,
            fontFamily = fontFamily
        )
    }
}

@Composable
fun TextFieldItem(
    value :String,
    placeholder: String,
    onValueChange: (it:String) -> Unit,
    colors: TextFieldColors,
    modifier: Modifier

) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = modifier
    )
    {
        TextField(
            value = value, onValueChange = { onValueChange(it) },
            singleLine = true,
            placeholder = { Text(text = placeholder) },
            modifier = Modifier.fillMaxWidth(),
            colors = colors
        )
    }
}
