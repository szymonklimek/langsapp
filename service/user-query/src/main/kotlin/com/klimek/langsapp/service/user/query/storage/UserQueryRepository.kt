package com.klimek.langsapp.service.user.query.storage

import arrow.core.Either
import arrow.core.right
import com.klimek.langsapp.service.user.query.User
import com.klimek.langsapp.service.user.query.UserId
import com.klimek.langsapp.service.user.query.UserName
import org.springframework.stereotype.Repository

@Repository
class UserQueryRepository {

    fun getUserIdByName(userName: UserName): Either<StorageError, UserId?> =
        UserQueryInMemoryStorage
            .users
            .entries
            .find { it.value?.userName?.value == userName.value }
            ?.let { (userId, _) -> userId }
            .right()

    fun getUserById(userId: UserId): Either<StorageError, User?> =
        UserQueryInMemoryStorage
            .users[userId]
            .right()

    fun storeUser(userId: UserId): Either<StorageError, Unit> {
        UserQueryInMemoryStorage.users[userId] = User(userId, userName = null)
        return Unit.right()
    }

    fun updateUserName(userId: UserId, userName: UserName): Either<StorageError, Unit> {
        UserQueryInMemoryStorage.users[userId] = User(userId, userName)
        return Unit.right()
    }

    companion object {
        class StorageError
    }
}
