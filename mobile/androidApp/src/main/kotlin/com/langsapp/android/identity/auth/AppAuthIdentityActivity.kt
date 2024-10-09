package com.langsapp.android.identity.auth

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import com.auth0.android.jwt.JWT
import com.langsapp.android.logging.Log
import com.langsapp.identity.auth.AuthConfig
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ResponseTypeValues

class AppAuthIdentityActivity : ComponentActivity() {

    private lateinit var authConfig: AuthConfig
    private lateinit var authorizationService: AuthorizationService

    private val browserAuthenticationLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            Log.d("Browser auth result: $it")
            val data = it.data
            if (data == null) {
                Log.e("Something went wrong with browser authentication")
                finish()
                return@registerForActivityResult
            }
            val response = AuthorizationResponse.fromIntent(data)
            val exception = AuthorizationException.fromIntent(data)
            Log.d("Auth response: $response")
            Log.d("Auth exception: $exception")
            if (exception != null && exception.type == AuthorizationException.TYPE_GENERAL_ERROR) {
                Log.e("Error when signing in. Finishing")
                finish()
                return@registerForActivityResult
            }
            if (response == null) {
                Log.e(
                    "Error when signing in. " +
                        "AuthorizationResponse shouldn't be null at this moment. Finishing",
                )
                finish()
                return@registerForActivityResult
            }
            exchangeAuthResponseForToken(response)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authConfig = runCatching { intent.parseAuthConfig() }
            .onFailure {
                Log.e("Failed to parse config. Reason: $it")
            }
            .getOrElse {
                finish()
                return
            }
        authorizationService = AuthorizationService(this)
        browserAuthenticationLauncher.launch(
            authorizationService
                .getAuthorizationRequestIntent(
                    AuthorizationRequest.Builder(
                        AuthorizationServiceConfiguration(
                            Uri.parse(authConfig.authorizationEndpoint),
                            Uri.parse(authConfig.tokenEndpoint),
                            Uri.parse(authConfig.registrationEndpoint),
                            Uri.parse(authConfig.endSessionEndpoint),
                        ),
                        authConfig.clientId,
                        ResponseTypeValues.CODE,
                        Uri.parse(authConfig.redirectUri),
                    )
                        .build(),
                ),
        )
    }

    private fun exchangeAuthResponseForToken(authorizationResponse: AuthorizationResponse) {
        // exchange for token
        authorizationService.performTokenRequest(authorizationResponse.createTokenExchangeRequest()) { tokenResponse, tokenRequestException ->
            Log.d(
                """
                Token response:
                    accessToken: ${tokenResponse?.accessToken},
                    expires in: ${tokenResponse?.accessTokenExpirationTime},
                    id token: ${tokenResponse?.idToken},
                    refreshToken: ${tokenResponse?.refreshToken},
                    scope: ${tokenResponse?.scope},
                    additionalParameters: ${tokenResponse?.additionalParameters},
                    exception: $tokenRequestException
                """.trimIndent(),
            )

            tokenResponse?.accessToken?.let { accessToken ->
                val userId =
                    runCatching { JWT(accessToken).subject }
                        .onFailure {
                            Log.e("Failed decoding token. Reason: $it")
                        }
                        .getOrNull()
                if (userId != null) {
                    setResult(
                        RESULT_OK,
                        Intent().apply {
                            putExtra(RESULT_ACCESS_TOKEN, accessToken)
                            putExtra(RESULT_REFRESH_TOKEN, tokenResponse.refreshToken)
                            putExtra(RESULT_USER_ID, userId)
                            putExtra(ACCESS_TOKEN_EXPIRATION_MS, tokenResponse.accessTokenExpirationTime ?: 0)
                        },
                    )
                    finish()
                    return@performTokenRequest
                }
            }
            setResult(RESULT_OK, Intent())
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        authorizationService.dispose()
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
