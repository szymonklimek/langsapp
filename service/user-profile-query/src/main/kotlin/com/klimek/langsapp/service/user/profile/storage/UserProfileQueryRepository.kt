package com.klimek.langsapp.service.user.profile.storage

import arrow.core.Either
import arrow.core.right
import org.springframework.stereotype.Repository

@Repository
class UserProfileQueryRepository {

    fun getUserById(userId: String): Either<StorageError, UserProfileRecord?> =
        UserProfileInMemoryStorage.usersProfiles[userId].right()

    fun storeFreshUser(userId: String): Either<StorageError, Unit> {
        UserProfileInMemoryStorage.usersProfiles[userId] = UserProfileRecord(id = userId)
        return Unit.right()
    }

    fun storeUserProfileRecord(userProfileRecord: UserProfileRecord): Either<StorageError, Unit> {
        UserProfileInMemoryStorage.usersProfiles[userProfileRecord.id] = userProfileRecord
        return Unit.right()
    }

    fun storeUserProfileRecords(users: List<UserProfileRecord>): Either<StorageError, Unit> {
        users.forEach {
            UserProfileInMemoryStorage.usersProfiles[it.id] = it
        }
        return Unit.right()
    }

    companion object {
        class StorageError
    }
}
