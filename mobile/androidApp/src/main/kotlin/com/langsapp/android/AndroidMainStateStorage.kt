package com.langsapp.android

import com.langsapp.main.MainStateStorage

class AndroidMainStateStorage : MainStateStorage {
    var _welcomeScreenPassed = false
    var _identityRequired = false
    var _onBoardingRequired = false

    override fun saveWelcomeScreenPassed(passed: Boolean) {
        _welcomeScreenPassed = passed
    }

    override fun wasWelcomeScreenAlreadyPassed(): Boolean {
        return _welcomeScreenPassed
    }

    override fun isIdentityRequired(): Boolean {
        return _identityRequired
    }

    override fun setIdentityRequired(isRequired: Boolean) {

    }

    override fun isOnBoardingRequired(): Boolean = _onBoardingRequired
}
