package com.klimek.langsapp.service.user.query

import arrow.core.Either
import com.klimek.langsapp.service.user.query.storage.UserQueryRepository
import org.springframework.stereotype.Service

@Service
class UserQueryService(
    private val repository: UserQueryRepository
) {

    fun getUserById(userId: UserId): Either<ServiceError, User?> =
        repository
            .getNameById(userId)
            .mapLeft { ServiceError() }
            .map { userName ->
                userName?.let {
                    User(
                        userId = userId,
                        userName = it
                    )
                }
            }

    fun getUserByName(userName: UserName): Either<ServiceError, User?> =
        repository
            .getUserIdByName(userName)
            .mapLeft { ServiceError() }
            .map { userId ->
                userId?.let {
                    User(
                        userId = it,
                        userName = userName
                    )
                }
            }

    companion object {
        class ServiceError
    }
}
