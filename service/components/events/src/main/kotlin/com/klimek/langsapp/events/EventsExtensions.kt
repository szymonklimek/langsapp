package com.klimek.langsapp.events

import com.klimek.langsapp.events.common.EventProperties
import java.util.UUID

fun generateEventsProperties() = EventProperties(
    id = UUID.randomUUID().toString(),
    createdAt = System.currentTimeMillis()
)
