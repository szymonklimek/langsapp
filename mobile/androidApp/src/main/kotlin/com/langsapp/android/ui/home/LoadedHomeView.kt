package com.langsapp.android.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.langsapp.architecture.Action
import com.langsapp.architecture.ActionSender
import com.langsapp.home.HomeAction
import com.langsapp.home.HomeState
import com.langsapp.home.onboarding.OnBoardingInfo
import com.langsapp.home.onboarding.UserProfileInfo

@Composable
internal fun LoadedHomeView(
    actionSender: ActionSender<Action>,
    state: HomeState.Loaded,
) {
    Column {
        val userProfileInfo = state.userProfileInfo
        val onBoardingInfo = state.onBoardingInfo

        Text("Home loaded")
        Spacer(Modifier.height(32.dp))

        Text("User profile info: $userProfileInfo")
        userProfileInfo.fold(
            onSuccess = {
                if (it is UserProfileInfo.Anonymous) {
                    Button(onClick = {
                        actionSender.sendAction(HomeAction.SignUpTapped)
                    }) {
                        Text("Sign up")
                    }
                }
            },
            onFailure = {
                Text("Failed to load user profile info")
            },
        )

        Spacer(Modifier.height(32.dp))

        onBoardingInfo.fold(
            onSuccess = {
                Text("On-boarding finished: ${it.isFinished}")
                Text("On-boarding sections:")
                it.sections.forEachIndexed { index, section ->
                    Spacer(Modifier.height(8.dp))
                    Text("Section ${index + 1}: ${section.rootStep}")
                    StepInfoView(actionSender, section.rootStep)
                    section.childSteps.forEach { stepInfo -> StepInfoView(actionSender, stepInfo) }
                }
            },
            onFailure = {
                Text("Failed to load on-boarding info")
            },
        )
    }
}

@Composable
internal fun StepInfoView(
    actionSender: ActionSender<Action>,
    stepInfo: OnBoardingInfo.StepInfo,
) {
    Text("${stepInfo.step}, required: ${stepInfo.required}, done: ${stepInfo.done}, enabled: ${stepInfo.enabled}")
    if (!stepInfo.done) {
        Button(
            onClick = {
                when (stepInfo.step) {
                    OnBoardingInfo.OnBoardingStep.SELECT_LANGUAGES -> actionSender.sendAction(HomeAction.SelectLanguagesTapped)
                    OnBoardingInfo.OnBoardingStep.SIGN_UP -> actionSender.sendAction(HomeAction.SignUpTapped)
                    OnBoardingInfo.OnBoardingStep.FILL_PROFILE -> actionSender.sendAction(HomeAction.UpsertProfileTapped)
                    OnBoardingInfo.OnBoardingStep.DOWNLOAD_CONTENT -> actionSender.sendAction(HomeAction.DownloadContentTapped)
                }
            },
            enabled = stepInfo.enabled,
        ) {
            when (stepInfo.step) {
                OnBoardingInfo.OnBoardingStep.SELECT_LANGUAGES -> Text("Select languages")
                OnBoardingInfo.OnBoardingStep.SIGN_UP -> Text("Sign up")
                OnBoardingInfo.OnBoardingStep.FILL_PROFILE -> Text("Fill profile")
                OnBoardingInfo.OnBoardingStep.DOWNLOAD_CONTENT -> Text("Download content")
            }
        }
    }
}
