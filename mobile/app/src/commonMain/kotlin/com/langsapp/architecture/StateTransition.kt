package com.langsapp.architecture

import com.langsapp.platform.randomUUID

/**
 * Additional information of how transition between two [State]s should be performed
 *
 * @param id Unique identifier of transition
 * @param navigationType The type of navigation
 */
data class StateTransition(
    val id: String = randomUUID(),
    val navigationType: Type,
) {

    enum class Type {
        BACKWARD,
        FORWARD,
    }
}
