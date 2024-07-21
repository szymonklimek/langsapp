package com.langsapp.identity

import com.langsapp.config.KeyValueStorage
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class IdentityRepository(private val keyValueStorage: KeyValueStorage) {

    fun getSignedInState(): IdentityState.SignedIn? = keyValueStorage
        .get(KEY_SIGNED_IN_STATE)
        ?.let {
            Json.decodeFromString(it)
        }

    fun saveSignedInState(identityState: IdentityState.SignedIn) =
        keyValueStorage.set(KEY_SIGNED_IN_STATE, Json.encodeToString(identityState))

    fun removeSignedInState() = keyValueStorage.remove(KEY_SIGNED_IN_STATE)

    companion object {
        private const val KEY_SIGNED_IN_STATE = "signed_in_state"
    }
}
