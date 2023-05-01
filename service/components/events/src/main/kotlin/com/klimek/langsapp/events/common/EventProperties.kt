package com.klimek.langsapp.events.common

/**
 * Default properties of event expected to be included in every event
 *
 * @param id Unique identifier of the event
 * @param createdAt Unix time (in milliseconds) of the moment when event was created
 */
data class EventProperties(
    val id: String,
    val createdAt: Long
)
