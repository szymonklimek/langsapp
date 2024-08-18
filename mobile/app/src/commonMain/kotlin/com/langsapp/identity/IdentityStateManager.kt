package com.langsapp.identity

import com.langsapp.architecture.ActionResult
import com.langsapp.architecture.StateManager
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock

class IdentityStateManager(
    anonymousUserId: String,
    tokenRefreshFunction: (IdentityState.SignedIn) -> Result<IdentityAction.TokensRefreshed>,
    identityRepository: IdentityRepository,
    clock: Clock = Clock.System,
    refreshIntervalMs: Long = 30000,
) : StateManager<IdentityState, IdentityAction>(
    initialState = identityRepository
        .getState()
        ?: IdentityState.AnonymousUser(userId = anonymousUserId)
            .also {
                identityRepository.saveState(it)
            },
    handleAction = { currentState, identityAction ->
        when (identityAction) {
            is IdentityAction.UserSignedIn -> {
                IdentityState.SignedIn(
                    accessToken = identityAction.accessToken,
                    refreshToken = identityAction.refreshToken,
                    userId = identityAction.userId,
                    accessTokenExpiresAtTimestampMs = identityAction.accessTokenExpiresAtTimestampMs,
                ).let {
                    ActionResult(it, sideEffects = listOf(CheckTokenExpirationSideEffect(it)))
                }
            }

            is IdentityAction.TokensRefreshed -> {
                if (currentState is IdentityState.SignedIn) {
                    currentState.copy(
                        accessToken = identityAction.accessToken,
                        refreshToken = identityAction.refreshToken,
                        accessTokenExpiresAtTimestampMs = identityAction.accessTokenExpiresAtTimestampMs,
                    ).let {
                        ActionResult(it, sideEffects = listOf(CheckTokenExpirationSideEffect(it)))
                    }
                } else {
                    ActionResult(currentState)
                }
            }

            IdentityAction.UserSignedOut -> {
                ActionResult(IdentityState.AnonymousUser(userId = anonymousUserId))
            }

            IdentityAction.TokenValid ->
                if (currentState is IdentityState.SignedIn) {
                    ActionResult(sideEffects = listOf(CheckTokenExpirationSideEffect(currentState)))
                } else {
                    ActionResult()
                }

            IdentityAction.TokenExpired -> {
                if (currentState is IdentityState.SignedIn) {
                    ActionResult(IdentityState.TokenExpired(currentState.userId))
                } else {
                    ActionResult()
                }
            }
        }
            .apply {
                newState?.let { identityRepository.saveState(it) }
            } as ActionResult<IdentityState>
    },
    handleSideEffect = { sideEffect ->
        sideEffect
            .takeIf { it is CheckTokenExpirationSideEffect && it.identityState is IdentityState.SignedIn }
            ?.let {
                delay(refreshIntervalMs)
                val signedInState = (it as CheckTokenExpirationSideEffect).identityState as IdentityState.SignedIn
                if (signedInState.accessTokenExpiresAtTimestampMs < clock.now().toEpochMilliseconds()) {
                    tokenRefreshFunction(signedInState)
                        .fold(
                            onSuccess = { action: IdentityAction.TokensRefreshed -> action },
                            onFailure = {
                                // TODO Implement proper actions for token refresh failures
                                IdentityAction.TokenExpired
                            },
                        )
                } else {
                    IdentityAction.TokenValid
                }
            }
    },
)
