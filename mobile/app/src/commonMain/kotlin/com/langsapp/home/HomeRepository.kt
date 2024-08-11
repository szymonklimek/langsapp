package com.langsapp.home

import com.langsapp.config.KeyValueStorage
import com.langsapp.config.Log
import com.langsapp.home.onboarding.OnBoardingInfo
import com.langsapp.home.onboarding.OnBoardingRepository
import com.langsapp.home.onboarding.UserProfileInfo
import com.langsapp.home.onboarding.UserProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
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
                userProfileInfo = userProfileRepository.provideProfileInfo(),
                onBoardingInfo = onBoardingRepository.provideOnBoardingInfo(),
            )
        }

    fun hasShownWelcome(): Boolean = keyValueStorage.get(WELCOME_SCREEN_SHOWN_KEY) == "true"

    fun setHasShownWelcome(hasShown: Boolean) {
        keyValueStorage.set(WELCOME_SCREEN_SHOWN_KEY, if (hasShown) "true" else "false")
    }

    data class HomeScreenData(
        val userProfileInfo: Result<UserProfileInfo>,
        val onBoardingInfo: Result<OnBoardingInfo>,
    )

    companion object {
        private const val WELCOME_SCREEN_SHOWN_KEY = "WELCOME_SCREEN_SHOWN_KEY"
    }
}
