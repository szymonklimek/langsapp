package com.langsapp.main

interface MainStateStorage {
    fun saveWelcomeScreenPassed(passed: Boolean = true)
    fun wasWelcomeScreenAlreadyPassed(): Boolean

    fun isIdentityRequired(): Boolean
    fun setIdentityRequired(isRequired: Boolean)

    fun isOnBoardingRequired(): Boolean

}
