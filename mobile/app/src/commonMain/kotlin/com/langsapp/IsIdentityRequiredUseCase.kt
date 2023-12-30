package com.langsapp

import kotlinx.coroutines.delay

class IsIdentityRequiredUseCase(

) {

    suspend fun invoke(): Boolean {
        delay(500)

        // Required if user was logged before
        return false
    }
}
