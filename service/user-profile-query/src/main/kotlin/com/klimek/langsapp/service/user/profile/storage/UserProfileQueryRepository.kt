package com.klimek.langsapp.service.user.profile.storage

import arrow.core.Either
import arrow.core.right
import com.klimek.langsapp.service.user.profile.domain.UserProfileProjection
import org.springframework.stereotype.Repository

@Repository
class UserProfileQueryRepository {

    fun getUserById(userId: String): Either<StorageError, UserProfileProjection?> =
        UserProfileInMemoryStorage.usersProfiles[userId].right()

    fun storeUserProjection(user: UserProfileProjection): Either<StorageError, Unit> {
        UserProfileInMemoryStorage.usersProfiles[user.id] = user
        return Unit.right()
    }

    fun storeUserProjections(users: List<UserProfileProjection>): Either<StorageError, Unit> {
        users.forEach {
            UserProfileInMemoryStorage.usersProfiles[it.id] = it
        }
        return Unit.right()
    }

    companion object {
        class StorageError
    }
}
