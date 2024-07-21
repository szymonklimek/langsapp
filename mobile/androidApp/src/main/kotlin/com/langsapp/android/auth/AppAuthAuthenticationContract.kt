package com.langsapp.android.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.langsapp.AuthConfig
import com.langsapp.android.logging.Log

class AppAuthAuthenticationContract : ActivityResultContract<AuthConfig, AuthResult>() {

    override fun createIntent(context: Context, input: AuthConfig) = AppAuthActivity.createIntent(context, input)

    override fun parseResult(resultCode: Int, intent: Intent?): AuthResult {
        Log.d("Result code: $resultCode, intent: $intent")
        if (resultCode == Activity.RESULT_CANCELED) return AuthResult.Cancelled
        return runCatching {
            AuthResult.SignedIn(
                accessToken = intent!!.getStringExtra(AppAuthActivity.RESULT_ACCESS_TOKEN)!!,
                refreshToken = intent.getStringExtra(AppAuthActivity.RESULT_REFRESH_TOKEN)!!,
                userId = intent.getStringExtra(AppAuthActivity.RESULT_USER_ID)!!,
                accessTokenExpiresAtTimestampMs = intent.getLongExtra(AppAuthActivity.ACCESS_TOKEN_EXPIRATION_MS, 0)!!,
            )
        }
            .getOrElse { AuthResult.Error }
    }
}
