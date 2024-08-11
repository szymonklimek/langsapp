package com.langsapp.home.onboarding

import com.langsapp.content.ManageContentRepository
import com.langsapp.home.onboarding.OnBoardingInfo.Companion.finishStep

class OnBoardingRepository(
    private val userProfileRepository: UserProfileRepository,
    private val contentRepository: ManageContentRepository,
) {
    suspend fun provideOnBoardingInfo(): Result<OnBoardingInfo> {
        val profileInfo =
            userProfileRepository
                .provideProfileInfo()
                .fold(
                    onSuccess = { it },
                    onFailure = { return Result.failure(it) },
                )
        return Result.success(
            OnBoardingInfo
                .createForFreshUser()
                .let {
                    when (profileInfo) {
                        is UserProfileInfo.SignedIn ->
                            it
                                .finishStep(OnBoardingInfo.OnBoardingStep.SIGN_UP)
                                .let {
                                    if (profileInfo.username != null) {
                                        it.finishStep(OnBoardingInfo.OnBoardingStep.FILL_PROFILE)
                                    } else {
                                        it
                                    }
                                }

                        is UserProfileInfo.Anonymous -> it
                    }
                }
                .let {
                    if (profileInfo.languageSetting() != null) {
                        it.finishStep(OnBoardingInfo.OnBoardingStep.SELECT_LANGUAGES)
                    } else {
                        it
                    }
                }
                .let {
                    val languageSetting = profileInfo.languageSetting()
                    if (languageSetting != null &&
                        !contentRepository.getAllUnits(languageSetting).getOrNull().isNullOrEmpty()
                    ) {
                        it.finishStep(OnBoardingInfo.OnBoardingStep.DOWNLOAD_CONTENT)
                    } else {
                        it
                    }
                },
        )
    }
}
