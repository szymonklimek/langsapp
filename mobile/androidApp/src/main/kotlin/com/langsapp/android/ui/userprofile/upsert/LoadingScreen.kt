package com.langsapp.android.ui.userprofile.upsert

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
internal fun LoadingScreen() {
    Column {
        Text("Upserting profile...")
        CircularProgressIndicator()
    }
}
