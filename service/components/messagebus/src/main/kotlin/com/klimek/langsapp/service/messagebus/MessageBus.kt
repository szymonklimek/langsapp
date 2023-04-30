package com.klimek.langsapp.service.messagebus

/**
 * Simple implementation of message bus with list-based publish/subscribe mechanism
 */
class MessageBus {

    init {
        println("Init EventBus: $this")
    }

    private val observers = mutableMapOf<String, MutableList<(Any) -> Unit>>()

    fun post(message: Any) {
        println("Posting message: $message")
        observers[message.javaClass.simpleName]?.forEach { it(message) }
    }

    fun register(messageClassName: Class<*>, handler: (Any) -> Unit) {
        val observersList = observers[messageClassName.simpleName] ?: mutableListOf()
        observersList.add(handler)
        observers[messageClassName.simpleName] = observersList
    }

}
