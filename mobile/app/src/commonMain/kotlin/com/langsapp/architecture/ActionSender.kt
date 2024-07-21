package com.langsapp.architecture

interface ActionSender<Action : com.langsapp.architecture.Action> {
    fun sendAction(action: Action)
}
