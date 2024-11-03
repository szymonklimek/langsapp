package com.klimek.langsapp.service.user.commands

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import com.klimek.langsapp.domain.Language
import com.klimek.langsapp.domain.LanguageSettings
import com.klimek.langsapp.events.generateEventsProperties
import com.klimek.langsapp.events.user.RemovableUserProperty
import com.klimek.langsapp.events.user.UserCreatedEvent
import com.klimek.langsapp.events.user.UserUpdatedEvent
import com.klimek.langsapp.service.user.commands.event.UserEventsPublisher
import com.klimek.langsapp.service.user.commands.generated.CreateUserRequest
import com.klimek.langsapp.service.user.commands.generated.UpdateUserRequest
import com.klimek.langsapp.service.user.commands.storage.UserCommandsRepository
import com.klimek.langsapp.service.user.query.UserId
import com.klimek.langsapp.service.user.query.UserName
import com.klimek.langsapp.service.user.query.UserQueryService
import org.springframework.stereotype.Service

@Service
class UserCommandsService(
    private val userEventsPublisher: UserEventsPublisher,
    private val userQueryService: UserQueryService,
    private val repository: UserCommandsRepository,
) {

    fun createUser(userId: String, userRequest: CreateUserRequest): Either<Error, Unit> =
        right()
            .flatMap {
                validateUserNotExists(userId)
            }
            .map {
                UserCreatedEvent(
                    eventProperties = generateEventsProperties(),
                    userId = userId,
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
            .map { }

    fun updateUser(userId: String, updateUserRequest: UpdateUserRequest): Either<Error, Unit> =
        right()
            .flatMap {
                validateUserExists(userId)
            }
            .flatMap {
                validateUserPropertiesToRemove(updateUserRequest)
            }
            .flatMap {
                if (updateUserRequest.username != null) {
                    validateUserNameNotSelectedByOthers(userId, updateUserRequest.username)
                } else {
                    Either.right()
                }
            }
            .map {
                UserUpdatedEvent(
                    eventProperties = generateEventsProperties(),
                    userId = userId,
                    newUserName = updateUserRequest.username,
                    newAvatarUrl = updateUserRequest.avatarUrl,
                    newLanguageSettings = updateUserRequest.languageSettings?.let {
                        LanguageSettings(
                            learnLanguage = Language(it.learnLanguage.code),
                            baseLanguage = Language(it.baseLanguage.code),
                            supportLanguage = it.supportLanguage?.let { supportLanguage ->
                                Language(supportLanguage.code)
                            },
                        )
                    },
                    propertiesToRemove = emptyList(),
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
            .map { }

    private fun validateUserNotExists(userId: String): Either<Error, Unit> =
        userQueryService
            .getUserById(userId = UserId(userId))
            .fold(
                ifLeft = { Error.ServiceError.left() },
                ifRight = { user ->
                    if (user != null) {
                        Error.UserAlreadyExists.left()
                    } else {
                        Unit.right()
                    }
                },
            )

    private fun validateUserExists(userId: String): Either<Error, Unit> =
        userQueryService
            .getUserById(userId = UserId(userId))
            .fold(
                ifLeft = { Error.ServiceError.left() },
                ifRight = { user ->
                    if (user == null) {
                        Error.UserNotFound.left()
                    } else {
                        Unit.right()
                    }
                },
            )

    private fun validateUserPropertiesToRemove(updateUserRequest: UpdateUserRequest): Either<Error, Unit> =
        if (
            updateUserRequest.propertiesToDelete?.all {
                runCatching { RemovableUserProperty.valueOf(it.value.uppercase()) }
                    .map { true }
                    .getOrDefault(false)
            } != false
        ) {
            Unit.right()
        } else {
            Error.InvalidProperties.left()
        }

    private fun validateUserNameNotSelectedByOthers(requestingUserId: String, username: String): Either<Error, Unit> =
        userQueryService
            .getUserByName(userName = UserName(username))
            .fold(
                ifLeft = { Error.ServiceError.left() },
                ifRight = { user ->
                    if (user != null && user.userId != UserId(requestingUserId)) {
                        Error.UserNameAlreadySelected.left()
                    } else {
                        Unit.right()
                    }
                },
            )

    companion object {
        sealed class Error {
            object UserAlreadyExists : Error()
            object UserNotFound : Error()
            object UserNameAlreadySelected : Error()
            object InvalidProperties : Error()
            object ServiceError : Error()
        }
    }
}
