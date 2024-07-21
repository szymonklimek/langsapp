package com.langsapp.identity

import com.langsapp.config.KeyValueStorage
import kotlin.test.Test
import kotlin.test.assertEquals

class IdentityRepositoryTest {

    private fun createIdentityRepository() = IdentityRepository(
        keyValueStorage = object : KeyValueStorage {
            private val map = mutableMapOf<String, String>()
            override fun get(key: String): String? = map[key]

            override fun set(key: String, value: String) = map.set(key, value)

            override fun remove(key: String) {
                map.remove(key)
            }
        },
    )

    @Test
    fun `signed in state saving retrieving and deleting correctly`() {
        val repository = createIdentityRepository()

        assertEquals(null, repository.getSignedInState())

        val signedInState = IdentityState.SignedIn(
            accessToken = "accessToken",
            refreshToken = "refreshToken",
            userId = "userId",
            accessTokenExpiresAtTimestampMs = 112,
        )
        repository.saveSignedInState(signedInState)
        assertEquals(signedInState, repository.getSignedInState())

        repository.removeSignedInState()

        assertEquals(null, repository.getSignedInState())
    }
}
