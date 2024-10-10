package com.langsapp.android.ui.settings.language

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
internal fun LoadingScreen() {
    Column {
        Text("Language settings loading...")
        CircularProgressIndicator()
    }
}
