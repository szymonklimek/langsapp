package com.langsapp.identity

import com.langsapp.config.KeyValueStorage
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class IdentityRepository(private val keyValueStorage: KeyValueStorage) {

    fun getState(): IdentityState? =
        keyValueStorage
            .get(KEY_IDENTITY_STATE)
            ?.let {
                Json.decodeFromString(it)
            }

    fun saveState(state: IdentityState) {
        keyValueStorage.set(KEY_IDENTITY_STATE, Json.encodeToString(state))
    }

    companion object {
        private const val KEY_IDENTITY_STATE = "identity-state"
    }
}
