package com.langsapp

import com.langsapp.learn.LearnUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class ContentRepository {
    suspend fun loadLearnUnits(count: Int): List<LearnUnit> =
        withContext(Dispatchers.IO) {
            delay(3000)
            (0 until count).map {
                LearnUnit(
                    unitId = "$it",
                    learnLanguageText = "learn1",
                    baseLanguageText = "base1",
                )
            }
        }
}
