package com.langsapp.home

import com.langsapp.config.KeyValueStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class HomeRepository(
    private val keyValueStorage: KeyValueStorage,
) {
    suspend fun loadHomeScreen(): Result<Unit> =
        withContext(Dispatchers.IO) {
            delay(1000)
            Result.success(Unit)
        }

    fun hasShownWelcome(): Boolean = keyValueStorage.get(WELCOME_SCREEN_SHOWN_KEY) == "true"

    fun setHasShownWelcome(hasShown: Boolean) {
        keyValueStorage.set(WELCOME_SCREEN_SHOWN_KEY, if (hasShown) "true" else "false")
    }

    companion object {
        private const val WELCOME_SCREEN_SHOWN_KEY = "WELCOME_SCREEN_SHOWN_KEY"
    }
}
