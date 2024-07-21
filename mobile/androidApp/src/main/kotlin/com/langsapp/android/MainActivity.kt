package com.langsapp.android

import android.os.Bundle
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import com.langsapp.android.auth.AppAuthAuthenticationContract
import com.langsapp.android.auth.AuthResult
import com.langsapp.android.logging.Log
import com.langsapp.android.ui.home.HomeScreen
import com.langsapp.android.ui.learn.LearnScreen
import com.langsapp.architecture.CommonSideEffect
import com.langsapp.architecture.State
import com.langsapp.home.HomeNavigationSideEffect
import com.langsapp.home.HomeState
import com.langsapp.identity.IdentityAction
import com.langsapp.learn.LearnState
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
    val (uiState, transition) = uiStateChange

    val loginLauncher = rememberLauncherForActivityResult(AppAuthAuthenticationContract()) {
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
                    is HomeNavigationSideEffect.NavigateToLogin -> loginLauncher.launch(it.authConfig)
                    else -> Unit
                }
            }
        }
    }

        Log.d("Transition not null!")
        AnimatedContent(
            targetState = uiState,
            contentKey = { transition },
            transitionSpec = {

                slideIn(animationSpec = tween(3000), initialOffset = {
                    IntOffset(-it.width, 0)
                }) togetherWith slideOut(
                    animationSpec = tween(3000),
                    targetOffset = { IntOffset(it.width, 0) }
                )
            }, label = "Main content"
        ) {
            when (uiState) {
                is HomeState -> HomeScreen(
                    actionSender = appViewModel,
                    homeState = uiState,
                )

                is LearnState -> LearnScreen(
                    actionSender = appViewModel,
                    learnState = uiState,
                )
            }
        }
}

@Composable
fun ScreenContent(appViewModel: AppViewModel, state: State) {
    when (state) {
        is HomeState -> HomeScreen(
            actionSender = appViewModel,
            homeState = state,
        )

        is LearnState -> LearnScreen(
            actionSender = appViewModel,
            learnState = state,
        )
    }
}
