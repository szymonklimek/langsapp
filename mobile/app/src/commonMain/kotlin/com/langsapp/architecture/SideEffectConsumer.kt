package com.langsapp.architecture

interface SideEffectConsumer<SideEffect : com.langsapp.architecture.SideEffect> {
    fun onSideEffect(sideEffect: SideEffect)
}
