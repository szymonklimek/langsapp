package com.langsapp.identity

import com.langsapp.BaseTest
import com.langsapp.InMemoryKeyValueStorage
import com.langsapp.config.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlin.test.Test
import kotlin.test.assertEquals

class IdentityStateManagerTest : BaseTest() {

    @Test
    fun `signing in and out`() = runBlocking {
        val identityStateManager = IdentityStateManager(
            anonymousUserId = "test_id",
            tokenRefreshFunction = {
                Result.success(
                    IdentityAction.TokensRefreshed(
                        accessToken = "new_access_token",
                        refreshToken = "new_refresh_token",
                        accessTokenExpiresAtTimestampMs = Clock.System.now().toEpochMilliseconds() + 1000L,
                    ),
                )
            },
            identityRepository = IdentityRepository(
                keyValueStorage = InMemoryKeyValueStorage(),
            ),
            clock = Clock.System,
        )
        assertEquals("AnonymousUser(userId=test_id)", identityStateManager.currentState.toString())
        identityStateManager.sendAction(
            IdentityAction.UserSignedIn(
                accessToken = "access_token",
                refreshToken = "refresh_token",
                userId = "user_id",
                accessTokenExpiresAtTimestampMs = Clock.System.now().toEpochMilliseconds() + 1000L,
            ),
        )
        with(identityStateManager.currentState as IdentityState.SignedIn) {
            assertEquals("access_token", accessToken)
            assertEquals("refresh_token", refreshToken)
            assertEquals("user_id", userId)
        }
        identityStateManager.sendAction(IdentityAction.UserSignedOut)

        with(identityStateManager.currentState as IdentityState.AnonymousUser) {
            assertEquals("test_id", userId)
        }
    }

    @Test
    fun `signing in and token refreshing`() = runBlocking {
        val identityStateManager = IdentityStateManager(
            anonymousUserId = "test_id",
            tokenRefreshFunction = {
                Log.d("Refreshing token")
                Result.success(
                    IdentityAction.TokensRefreshed(
                        accessToken = "new_access_token",
                        refreshToken = "new_refresh_token",
                        accessTokenExpiresAtTimestampMs = Clock.System.now().toEpochMilliseconds() + 100L,
                    ),
                )
            },
            identityRepository = IdentityRepository(
                keyValueStorage = InMemoryKeyValueStorage(),
            ),
            clock = Clock.System,
            refreshIntervalMs = 100,
        )
        identityStateManager.sendAction(
            IdentityAction.UserSignedIn(
                accessToken = "access_token",
                refreshToken = "refresh_token",
                userId = "user_id",
                accessTokenExpiresAtTimestampMs = Clock.System.now().toEpochMilliseconds() + 100L,
            ),
        )
        delay(110)

        with(identityStateManager.currentState as IdentityState.SignedIn) {
            assertEquals("new_access_token", accessToken)
            assertEquals("new_refresh_token", refreshToken)
            assertEquals("user_id", userId)
        }
    }

    @Test
    fun `signing out stops token refreshing`() = runBlocking {
        var counter = 0
        val manager = IdentityStateManager(
            anonymousUserId = "test_id",
            tokenRefreshFunction = {
                counter++
                Result.success(
                    IdentityAction.TokensRefreshed(
                        accessToken = "new_access_token$counter",
                        refreshToken = "new_refresh_token",
                        accessTokenExpiresAtTimestampMs = Clock.System.now().toEpochMilliseconds() + 100L,
                    ),
                )
            },
            identityRepository = IdentityRepository(
                keyValueStorage = InMemoryKeyValueStorage(),
            ),
            clock = Clock.System,
            refreshIntervalMs = 50,
        ).apply {
            sendAction(
                IdentityAction.UserSignedIn(
                    accessToken = "access_token",
                    refreshToken = "refresh_token",
                    userId = "user_id",
                    accessTokenExpiresAtTimestampMs = Clock.System.now().toEpochMilliseconds() + 100L,
                ),
            )
        }
        delay(250)
        assertEquals("new_access_token2", (manager.currentState as IdentityState.SignedIn).accessToken)
        manager.sendAction(IdentityAction.UserSignedOut)
        delay(100)
        assertEquals(2, counter)
    }
}
