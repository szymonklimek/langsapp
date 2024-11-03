package com.klimek.langsapp.service.user.query

import arrow.core.Either
import com.klimek.langsapp.events.user.UserCreatedEvent
import com.klimek.langsapp.events.user.UserUpdatedEvent
import com.klimek.langsapp.service.user.query.storage.UserQueryRepository
import org.springframework.stereotype.Service

@Service
class UserQueryService(
    private val repository: UserQueryRepository,
) {

    fun getUserById(userId: UserId): Either<ServiceError, User?> =
        repository
            .getUserById(userId)
            .mapLeft { ServiceError() }

    fun getUserByName(userName: UserName): Either<ServiceError, User?> =
        repository
            .getUserIdByName(userName)
            .mapLeft { ServiceError() }
            .map { userId ->
                userId?.let {
                    User(
                        userId = it,
                        userName = userName,
                    )
                }
            }

    fun handleUserCreatedEvent(userCreatedEvent: UserCreatedEvent) {
        repository.storeUser(UserId(userCreatedEvent.userId))
    }

    fun handleUserUpdatedEvent(userUpdatedEvent: UserUpdatedEvent) {
        userUpdatedEvent.newUserName?.let {
            repository.updateUserName(
                userId = UserId(userUpdatedEvent.userId),
                userName = UserName(it),
            )
        }
    }

    companion object {
        class ServiceError
    }
}
