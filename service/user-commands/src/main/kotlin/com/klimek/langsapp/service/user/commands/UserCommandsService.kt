package com.klimek.langsapp.service.user.commands

import arrow.core.*
import com.klimek.langsapp.events.generateEventsProperties
import com.klimek.langsapp.events.user.UserCreatedEvent
import com.klimek.langsapp.events.user.UserUpdatedEvent
import com.klimek.langsapp.service.user.commands.event.UserEventsPublisher
import com.klimek.langsapp.service.user.commands.generated.UserRequest
import com.klimek.langsapp.service.user.commands.generated.UserResponse
import com.klimek.langsapp.service.user.commands.storage.UserCommandsRepository
import org.springframework.stereotype.Service

@Service
class UserCommandsService(
    private val userEventsPublisher: UserEventsPublisher,
    private val repository: UserCommandsRepository
) {

    fun createUser(userId: String, userRequest: UserRequest): Either<Error, UserResponse> =
        right()
            .flatMap {
                validateUserNotExists(userId)
            }
            .flatMap {
                validateUserNameNotSelected(userId, userRequest.name)
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
                validateUserNameNotSelected(userId, userRequest.name)
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
        repository.containsUserWithId(userId)
            .fold(
                ifLeft = { Error.ServiceError.left() },
                ifRight = { containsUserId ->
                    if (containsUserId) Error.UserAlreadyExists.left()
                    else Unit.right()
                }
            )

    private fun validateUserExists(userId: String): Either<Error, Unit> =
        repository.containsUserWithId(userId)
            .fold(
                ifLeft = { Error.ServiceError.left() },
                ifRight = { containsUserId ->
                    if (!containsUserId) Error.UserNotFound.left()
                    else Unit.right()
                }
            )

    private fun validateUserNameNotSelected(userId: String, username: String): Either<Error, Unit> =
        repository.containsUserWithName(userId, username)
            .fold(
                ifLeft = { Error.ServiceError.left() },
                ifRight = { containsUserName ->
                    if (containsUserName) Error.UserNameAlreadySelected.left()
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
