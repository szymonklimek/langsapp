package com.langsapp.android.identity.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.langsapp.android.logging.Log
import com.langsapp.identity.auth.AuthConfig

class AppAuthIdentityContract : ActivityResultContract<AuthConfig, AuthResult>() {

    override fun createIntent(context: Context, input: AuthConfig) =
        AppAuthIdentityActivity.createIntent(context, input)

    override fun parseResult(resultCode: Int, intent: Intent?): AuthResult {
        Log.d("Result code: $resultCode, intent: $intent")
        if (resultCode == Activity.RESULT_CANCELED) return AuthResult.Cancelled
        return runCatching {
            AuthResult.SignedIn(
                accessToken = intent!!.getStringExtra(AppAuthIdentityActivity.RESULT_ACCESS_TOKEN)!!,
                refreshToken = intent.getStringExtra(AppAuthIdentityActivity.RESULT_REFRESH_TOKEN)!!,
                userId = intent.getStringExtra(AppAuthIdentityActivity.RESULT_USER_ID)!!,
                accessTokenExpiresAtTimestampMs = intent.getLongExtra(
                    AppAuthIdentityActivity.ACCESS_TOKEN_EXPIRATION_MS,
                    0,
                ),
            )
        }
            .onFailure {
                Log.e("Failed to authenticate user. Reason: $it. ${it.stackTrace.take(2).joinToString(", ")}")
            }
            .getOrElse { AuthResult.Error }
    }
}
