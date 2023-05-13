package com.klimek.langsapp.service.user.commands

import arrow.core.*
import com.klimek.langsapp.events.generateEventsProperties
import com.klimek.langsapp.service.events.store.EventsStoreRepository
import com.klimek.langsapp.events.user.UserCreatedEvent
import com.klimek.langsapp.events.user.UserUpdatedEvent
import com.klimek.langsapp.service.user.commands.event.UserEventsPublisher
import com.klimek.langsapp.service.user.commands.generated.UserRequest
import com.klimek.langsapp.service.user.commands.generated.UserResponse
import com.klimek.langsapp.service.user.query.UserId
import com.klimek.langsapp.service.user.query.UserName
import com.klimek.langsapp.service.user.query.UserQueryService
import org.springframework.stereotype.Service

@Service
class UserCommandsService(
    private val userEventsPublisher: UserEventsPublisher,
    private val userQueryService: UserQueryService,
    private val repository: EventsStoreRepository
) {

    fun createUser(userId: String, userRequest: UserRequest): Either<Error, UserResponse> =
        right()
            .flatMap {
                validateUserNotExists(userId)
            }
            .flatMap {
                validateUserNameNotSelectedByOthers(userId, userRequest.name)
            }
            .map {
                UserCreatedEvent(
                    eventProperties = generateEventsProperties(),
                    userId = userId,
                    userName = userRequest.name,
                    avatarUrl = userRequest.avatarUrl
                )
            }
            .flatMap { userCreatedEvent ->
                repository.storeUserCreatedEvent(userCreatedEvent)
                    .mapLeft { Error.ServiceError }
                    .map { userCreatedEvent }
            }
            .onRight { userCreatedEvent ->
                userEventsPublisher.sendUserCreatedEvent(userCreatedEvent)
            }
            .map { userCreatedEvent ->
                UserResponse(
                    id = userCreatedEvent.userId,
                    name = userCreatedEvent.userName,
                    avatarUrl = userCreatedEvent.avatarUrl
                )
            }

    fun updateUser(userId: String, userRequest: UserRequest): Either<Error, UserResponse> =
        right()
            .flatMap {
                validateUserExists(userId)
            }
            .flatMap {
                validateUserNameNotSelectedByOthers(userId, userRequest.name)
            }
            .map {
                UserUpdatedEvent(
                    eventProperties = generateEventsProperties(),
                    userId = userId,
                    userName = userRequest.name,
                    avatarUrl = userRequest.avatarUrl
                )
            }
            .flatMap { userUpdatedEvent ->
                repository.storeUserUpdatedEvent(userUpdatedEvent)
                    .mapLeft { Error.ServiceError }
                    .map { userUpdatedEvent }
            }
            .onRight { userUpdatedEvent ->
                userEventsPublisher.sendUserUpdatedEvent(userUpdatedEvent)
            }
            .map { userUpdatedEvent ->
                UserResponse(
                    id = userUpdatedEvent.userId,
                    name = userUpdatedEvent.userName!!,
                    avatarUrl = userUpdatedEvent.avatarUrl
                )
            }

    private fun validateUserNotExists(userId: String): Either<Error, Unit> =
        userQueryService
            .getUserById(userId = UserId(userId))
            .fold(
                ifLeft = { Error.ServiceError.left() },
                ifRight = { user ->
                    if (user != null) Error.UserAlreadyExists.left()
                    else Unit.right()
                }
            )

    private fun validateUserExists(userId: String): Either<Error, Unit> =
        userQueryService
            .getUserById(userId = UserId(userId))
            .fold(
                ifLeft = { Error.ServiceError.left() },
                ifRight = { user ->
                    if (user == null) Error.UserNotFound.left()
                    else Unit.right()
                }
            )

    private fun validateUserNameNotSelectedByOthers(requestingUserId: String, username: String): Either<Error, Unit> =
        userQueryService
            .getUserByName(userName = UserName(username))
            .fold(
                ifLeft = { Error.ServiceError.left() },
                ifRight = { user ->
                    if (user != null && user.userId != UserId(requestingUserId)) Error.UserNameAlreadySelected.left()
                    else Unit.right()
                }
            )

    companion object {
        sealed class Error {
            object UserAlreadyExists : Error()
            object UserNotFound : Error()
            object UserNameAlreadySelected : Error()
            object ServiceError : Error()
        }
    }
}
