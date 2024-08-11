package com.langsapp.android

import android.os.Bundle
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.langsapp.android.identity.auth.AppAuthIdentityContract
import com.langsapp.android.identity.auth.AuthResult
import com.langsapp.android.logging.Log
import com.langsapp.android.ui.content.ManageContentScreen
import com.langsapp.android.ui.home.HomeScreen
import com.langsapp.android.ui.settings.language.LanguageSettingsScreen
import com.langsapp.android.ui.userprofile.upsert.UpsertProfileScreen
import com.langsapp.architecture.CommonSideEffect
import com.langsapp.architecture.StateTransition
import com.langsapp.content.ManageContentState
import com.langsapp.home.HomeNavigationSideEffect
import com.langsapp.home.HomeState
import com.langsapp.identity.IdentityAction
import com.langsapp.settings.language.LanguageSettingsState
import com.langsapp.userprofile.upsert.UpsertProfileState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.isActive

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { AppUi(AppViewModel()) }
    }
}

@Composable
fun AppUi(appViewModel: AppViewModel) {
    val uiStateChange by appViewModel.uiState.collectAsState()
    val context = LocalContext.current

    val loginLauncher = rememberLauncherForActivityResult(AppAuthIdentityContract()) {
        if (it is AuthResult.SignedIn) {
            appViewModel.sendAction(
                IdentityAction.UserSignedIn(
                    accessToken = it.accessToken,
                    refreshToken = it.refreshToken,
                    userId = it.userId,
                    accessTokenExpiresAtTimestampMs = it.accessTokenExpiresAtTimestampMs,
                ),
            )
        }
    }

    LaunchedEffect(Unit) {
        if (isActive) {
            appViewModel.sideEffects.collectLatest {
                when (it) {
                    is CommonSideEffect.ShowPopUpMessage -> Toast.makeText(context, it.message, LENGTH_SHORT).show()
                    is HomeNavigationSideEffect.SignUp -> loginLauncher.launch(it.authConfig)
                    else -> Unit
                }
            }
        }
    }

    Log.d("Transition not null!")
    AnimatedContent(
        targetState = uiStateChange,
        transitionSpec = {
            val transition = uiStateChange.second
            when (transition?.navigationType) {
                StateTransition.Type.BACKWARD -> {
                    slideInHorizontally(
                        animationSpec = tween(1000),
                        initialOffsetX = { -it },
                    ) togetherWith
                        slideOutHorizontally(
                            animationSpec = tween(1000),
                            targetOffsetX = { it },
                        )
                }

                StateTransition.Type.FORWARD -> {
                    slideInHorizontally(
                        animationSpec = tween(1000),
                        initialOffsetX = { it },
                    ) togetherWith
                        slideOutHorizontally(
                            animationSpec = tween(1000),
                            targetOffsetX = { -it },
                        )
                }

                else -> {
                    fadeIn(animationSpec = tween(0))
                        .togetherWith(fadeOut(animationSpec = tween(0)))
                }
            }
        },
        label = "Main content",
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            when (it.first) {
                is HomeState -> HomeScreen(
                    actionSender = appViewModel,
                    homeState = it.first as HomeState,
                )
                is LanguageSettingsState -> LanguageSettingsScreen(
                    actionSender = appViewModel,
                    state = it.first as LanguageSettingsState,
                )
                is UpsertProfileState -> UpsertProfileScreen(
                    actionSender = appViewModel,
                    state = it.first as UpsertProfileState,
                )
                is ManageContentState -> ManageContentScreen(
                    actionSender = appViewModel,
                    state = it.first as ManageContentState,
                )
            }
        }
    }
}
