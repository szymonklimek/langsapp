package com.langsapp

import com.langsapp.config.KeyValueStorage

class AppStateRepository(
    private val keyValueStorage: KeyValueStorage,
) {

    fun hasShownWelcome(): Boolean = keyValueStorage.get(WELCOME_SCREEN_SHOWN_KEY) == "true"

    fun setHasShownWelcome(hasShown: Boolean) {
        keyValueStorage.set(WELCOME_SCREEN_SHOWN_KEY, if (hasShown) "true" else "false")
    }

    companion object {
        private const val WELCOME_SCREEN_SHOWN_KEY = "WELCOME_SCREEN_SHOWN_KEY"
    }
}
