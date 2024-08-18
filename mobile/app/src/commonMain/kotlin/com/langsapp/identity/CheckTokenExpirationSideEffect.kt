package com.langsapp.identity

import com.langsapp.architecture.SideEffect

data class CheckTokenExpirationSideEffect(val identityState: IdentityState) : SideEffect
