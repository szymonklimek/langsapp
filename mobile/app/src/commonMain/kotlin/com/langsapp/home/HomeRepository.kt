package com.langsapp.home

import com.langsapp.common.CommonResult
import com.langsapp.common.mapToCommonResult
import com.langsapp.config.KeyValueStorage
import com.langsapp.config.Log
import com.langsapp.home.onboarding.OnBoardingInfo
import com.langsapp.home.onboarding.OnBoardingRepository
import com.langsapp.home.onboarding.UserProfileInfo
import com.langsapp.home.onboarding.UserProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class HomeRepository(
    private val keyValueStorage: KeyValueStorage,
    private val userProfileRepository: UserProfileRepository,
    private val onBoardingRepository: OnBoardingRepository,
) {
    suspend fun loadHomeScreen(): HomeScreenData =
        withContext(Dispatchers.IO) {
            Log.d("")
            HomeScreenData(
                userProfileInfo =
                userProfileRepository
                    .provideProfileInfo()
                    .mapToCommonResult { },
                onBoardingInfo = onBoardingRepository
                    .provideOnBoardingInfo()
                    .mapToCommonResult { },
            )
        }

    fun hasShownWelcome(): Boolean = keyValueStorage.get(WELCOME_SCREEN_SHOWN_KEY) == "true"

    fun setHasShownWelcome(hasShown: Boolean) {
        keyValueStorage.set(WELCOME_SCREEN_SHOWN_KEY, if (hasShown) "true" else "false")
    }

    data class HomeScreenData(
        val userProfileInfo: CommonResult<UserProfileInfo, Unit>,
        val onBoardingInfo: CommonResult<OnBoardingInfo, Unit>,
    )

    companion object {
        private const val WELCOME_SCREEN_SHOWN_KEY = "WELCOME_SCREEN_SHOWN_KEY"
    }
}
