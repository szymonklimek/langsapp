package com.langsapp.home

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class HomeRepository {
    suspend fun loadHomeScreen(): Result<Unit> =
        withContext(Dispatchers.IO) {
            delay(1000)
            Result.success(Unit)
        }
}
