package com.klimek.langsapp.service.events.store

import arrow.core.Either
import arrow.core.right
import com.fasterxml.jackson.databind.ObjectMapper
import com.klimek.langsapp.events.follow.UserFollowEvent
import com.klimek.langsapp.events.user.UserCreatedEvent
import com.klimek.langsapp.events.user.UserUpdatedEvent
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.UUID

@Repository
class EventsStoreRepository(
    val objectMapper: ObjectMapper,
    val database: EventsStoreDatabase
) {
    fun storeUserCreatedEvent(userCreatedEvent: UserCreatedEvent): Either<StorageError, Unit> =
        transaction(Database.connect(database.dataSource)) {
            EventsStoreDbTable.insert {
                it[id] = UUID.fromString(userCreatedEvent.eventProperties.id)
                it[EventsStoreDbTable.eventType] = userCreatedEvent.javaClass.toEventType()
                it[EventsStoreDbTable.userId] = UUID.fromString(userCreatedEvent.userId)
                it[EventsStoreDbTable.createdAt] = Instant.ofEpochMilli(userCreatedEvent.eventProperties.createdAt)
                it[EventsStoreDbTable.eventData] = objectMapper.writeValueAsString(userCreatedEvent)
            }
        }
            .right()
            .map { }

    fun storeUserUpdatedEvent(userUpdatedEvent: UserUpdatedEvent): Either<StorageError, Unit> =
        transaction(Database.connect(database.dataSource)) {
            EventsStoreDbTable.insert {
                it[id] = UUID.fromString(userUpdatedEvent.eventProperties.id)
                it[EventsStoreDbTable.eventType] = userUpdatedEvent.javaClass.toEventType()
                it[EventsStoreDbTable.userId] = UUID.fromString(userUpdatedEvent.userId)
                it[EventsStoreDbTable.createdAt] = Instant.ofEpochMilli(userUpdatedEvent.eventProperties.createdAt)
                it[EventsStoreDbTable.eventData] = objectMapper.writeValueAsString(userUpdatedEvent)
            }
        }
            .right()
            .map { }

    fun storeUserFollowEvent(userFollowEvent: UserFollowEvent): Either<StorageError, Unit> =
        transaction(Database.connect(database.dataSource)) {
            EventsStoreDbTable.insert {
                it[id] = UUID.fromString(userFollowEvent.eventProperties.id)
                it[EventsStoreDbTable.eventType] = userFollowEvent.javaClass.toEventType()
                it[EventsStoreDbTable.userId] = UUID.fromString(userFollowEvent.followerUserId)
                it[EventsStoreDbTable.createdAt] = Instant.ofEpochMilli(userFollowEvent.eventProperties.createdAt)
                it[EventsStoreDbTable.eventData] = objectMapper.writeValueAsString(userFollowEvent)
            }
        }
            .right()
            .map { }

    private fun Class<*>.toEventType() = simpleName

    companion object {
        class StorageError
    }
}