package com.klimek.langsapp.service.user.follow.commands.storage

import arrow.core.Either
import arrow.core.right
import com.klimek.langsapp.events.follow.UserFollowEvent
import org.springframework.stereotype.Repository

@Repository
class UserFollowCommandsRepository {
    private val storage = UserFollowCommandsInMemoryStorage

    fun getLastStoredEvent(followerUserId: String, userId: String): Either<StorageError, UserFollowEvent?> =
        storage.getLastEventForUser(followerUserId, userId).right()

    fun storeUserFollowEvent(event: UserFollowEvent): Either<StorageError, Unit> =
        storage.storeUserFollowEvent(event).right()

    companion object {
        class StorageError
    }
}
