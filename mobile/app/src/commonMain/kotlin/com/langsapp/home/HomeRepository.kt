package com.langsapp.home

import com.langsapp.config.KeyValueStorage
import com.langsapp.config.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class HomeRepository(
    private val keyValueStorage: KeyValueStorage,
) {
    suspend fun loadHomeScreen(): HomeScreenData =
        withContext(Dispatchers.IO) {
            Log.d("")
            HomeScreenData
        }

    fun hasShownWelcome(): Boolean = keyValueStorage.get(WELCOME_SCREEN_SHOWN_KEY) == "true"

    fun setHasShownWelcome(hasShown: Boolean) {
        keyValueStorage.set(WELCOME_SCREEN_SHOWN_KEY, if (hasShown) "true" else "false")
    }

    data object HomeScreenData

    companion object {
        private const val WELCOME_SCREEN_SHOWN_KEY = "WELCOME_SCREEN_SHOWN_KEY"
    }
}
