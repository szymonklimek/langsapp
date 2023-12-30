package com.langsapp.welcome

data class WelcomeScreen(
    val slides: List<WelcomeSlide>,
    val currentSlideIndex: Int
)
