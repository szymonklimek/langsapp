package com.klimek.langsapp.service.events.store

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.`java-time`.timestamp

object EventsStoreDbTable: UUIDTable(name = "events_store.events", columnName = "id") {
    val eventType = text("event_type")
    val createdAt = timestamp("created_at")
    val userId = uuid("user_id").nullable()
    val eventData = text("event_data")
}