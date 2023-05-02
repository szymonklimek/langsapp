package com.klimek.langsapp.service.user.query.storage

import arrow.core.Either
import arrow.core.right
import com.klimek.langsapp.service.user.query.UserId
import com.klimek.langsapp.service.user.query.UserName
import org.springframework.stereotype.Repository

@Repository
class UserQueryRepository {

    fun getUserIdByName(userName: UserName): Either<StorageError, UserId?> =
        UserQueryInMemoryStorage
            .users
            .entries
            .find { it.value == userName.value }
            ?.let { UserId(it.key) }
            .right()

    fun getNameById(userId: UserId): Either<StorageError, UserName?> =
        UserQueryInMemoryStorage
            .users[userId.value]
            ?.let { UserName(it) }
            .right()

    fun storeUser(userId: UserId, userName: UserName): Either<StorageError, Unit> {
        UserQueryInMemoryStorage.users[userId.value] = userName.value
        return Unit.right()
    }

    companion object {
        class StorageError
    }
}
