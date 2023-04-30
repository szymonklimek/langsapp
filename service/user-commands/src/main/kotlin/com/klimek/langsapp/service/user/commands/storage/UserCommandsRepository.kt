package com.klimek.langsapp.service.user.commands.storage

import arrow.core.Either
import arrow.core.right
import com.klimek.langsapp.events.user.UserCreatedEvent
import com.klimek.langsapp.events.user.UserUpdatedEvent
import org.springframework.stereotype.Repository

@Repository
class UserCommandsRepository {
    private val storage = UserCommandsInMemoryStorage

    fun containsUserWithName(currentUserId: String, userName: String): Either<StorageError, Boolean> =
        storage
            .userNamesToIds[userName]
            .let {
                it != null && it != currentUserId
            }
            .right()

    fun containsUserWithId(currentUserId: String): Either<StorageError, Boolean> =
        storage
            .userIdsToNames
            .containsKey(currentUserId)
            .right()

    fun storeUserCreatedEvent(userCreatedEvent: UserCreatedEvent): Either<StorageError, Unit> =
        storage
            .storeUserCreatedEvent(userCreatedEvent)
            .right()


    fun storeUserUpdatedEvent(userUpdatedEvent: UserUpdatedEvent): Either<StorageError, Unit> =
        storage
            .storeUserUpdatedEvent(userUpdatedEvent)
            .right()


    companion object {
        class StorageError
    }
}
