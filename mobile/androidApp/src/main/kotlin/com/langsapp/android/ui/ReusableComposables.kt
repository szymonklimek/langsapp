package com.langsapp.android.ui

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ButtonPrimary(modifier: Modifier = Modifier, text: String, onClick: () -> Unit) {
    Button(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(50),
        contentPadding = ButtonDefaults.ContentPadding
    ) {
        Text(
            text = text.uppercase(),
            style = MaterialTheme.typography.titleSmall
        )
    }
}

@Composable
fun ButtonOutlined(modifier: Modifier = Modifier, text: String, onClick: () -> Unit) {
    OutlinedButton(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(50),
        contentPadding = ButtonDefaults.ContentPadding
    ) {
        Text(
            text = text.uppercase(),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}