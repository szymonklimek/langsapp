package com.langsapp.android.identity.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.langsapp.identity.auth.AuthConfig
import java.text.SimpleDateFormat
import java.util.Date

class AppAuthIdentityActivity : ComponentActivity() {

    private lateinit var authConfig: AuthConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authConfig = intent.parseAuthConfig()
        setContent {
            Column {
                Text("Temporary Auth screen")
                Spacer(Modifier.height(16.dp))

                Button(onClick = {
                    finish()
                }) {
                    Text("Finish with error")
                }

                Button(onClick = {
                    setResult(RESULT_CANCELED)
                    finish()
                }) {
                    Text("Finish with cancel")
                }

                Spacer(Modifier.height(16.dp))
                Button(onClick = {
                    setSuccessResult()
                    finish()
                }) {
                    Text("Finish auth")
                }
                Spacer(Modifier.height(16.dp))
            }
        }
    }

    @Suppress("SimpleDateFormat")
    private fun setSuccessResult() {
        setResult(
            RESULT_OK,
            Intent().apply {
                val dateTimeFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                putExtra(RESULT_ACCESS_TOKEN, "accessToken-${dateTimeFormatter.format(Date())}")
                putExtra(RESULT_REFRESH_TOKEN, "refreshToken-${dateTimeFormatter.format(Date())}")
                putExtra(RESULT_USER_ID, "userId1")
                putExtra(ACCESS_TOKEN_EXPIRATION_MS, System.currentTimeMillis() + 60000)
            },
        )
    }

    companion object {
        const val RESULT_ACCESS_TOKEN = "RESULT_ACCESS_TOKEN"
        const val RESULT_REFRESH_TOKEN = "RESULT_REFRESH_TOKEN"
        const val RESULT_USER_ID = "RESULT_USER_ID"
        const val ACCESS_TOKEN_EXPIRATION_MS = "ACCESS_TOKEN_EXPIRATION_MS"

        private const val AUTHORIZATION_ENDPOINT = "AUTHORIZATION_ENDPOINT"
        private const val TOKEN_ENDPOINT = "TOKEN_ENDPOINT"
        private const val REGISTRATION_ENDPOINT = "REGISTRATION_ENDPOINT"
        private const val END_SESSION_ENDPOINT = "END_SESSION_ENDPOINT"
        private const val CLIENT_ID = "CLIENT_ID"
        private const val REDIRECT_URI = "REDIRECT_URI"

        fun createIntent(context: Context, authConfig: AuthConfig) =
            Intent(context, AppAuthIdentityActivity::class.java).apply {
                putExtra(AUTHORIZATION_ENDPOINT, authConfig.authorizationEndpoint)
                putExtra(TOKEN_ENDPOINT, authConfig.tokenEndpoint)
                putExtra(REGISTRATION_ENDPOINT, authConfig.registrationEndpoint)
                putExtra(END_SESSION_ENDPOINT, authConfig.endSessionEndpoint)
                putExtra(CLIENT_ID, authConfig.clientId)
                putExtra(REDIRECT_URI, authConfig.redirectUri)
            }

        fun Intent.parseAuthConfig() = AuthConfig(
            authorizationEndpoint = getStringExtra(AUTHORIZATION_ENDPOINT)!!,
            tokenEndpoint = getStringExtra(TOKEN_ENDPOINT)!!,
            registrationEndpoint = getStringExtra(REGISTRATION_ENDPOINT)!!,
            endSessionEndpoint = getStringExtra(END_SESSION_ENDPOINT)!!,
            clientId = getStringExtra(CLIENT_ID)!!,
            redirectUri = getStringExtra(REDIRECT_URI)!!,
        )
    }
}
