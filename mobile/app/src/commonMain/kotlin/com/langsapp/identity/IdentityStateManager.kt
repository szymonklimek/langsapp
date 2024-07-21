package com.langsapp.identity

import com.langsapp.architecture.ActionResult
import com.langsapp.architecture.StateManager

class IdentityStateManager(
    anonymousUserId: String,
    identityRepository: IdentityRepository,
) : StateManager<IdentityState, IdentityAction>(
    initialState = identityRepository.getSignedInState() ?: IdentityState.AnonymousUser(userId = anonymousUserId),
    handleAction = { _, identityAction ->
        when (identityAction) {
            is IdentityAction.UserSignedIn -> {
                val newState = IdentityState.SignedIn(
                    accessToken = identityAction.accessToken,
                    refreshToken = identityAction.refreshToken,
                    userId = identityAction.userId,
                    accessTokenExpiresAtTimestampMs = identityAction.accessTokenExpiresAtTimestampMs,
                )
                identityRepository.saveSignedInState(newState)
                ActionResult(newState)
            }

            IdentityAction.UserSignedOut -> {
                identityRepository.removeSignedInState()
                ActionResult(IdentityState.AnonymousUser(userId = anonymousUserId))
            }
        }
    },
)
