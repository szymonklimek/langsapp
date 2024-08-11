package com.langsapp.android.ui.content

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun LoadingScreen() {
    Column {
        Text("Manage content loading...")
        CircularProgressIndicator()
    }
}
