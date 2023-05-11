package com.klimek.langsapp.service.user.follow.commands

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import com.klimek.langsapp.events.follow.UserFollowEvent
import com.klimek.langsapp.events.generateEventsProperties
import com.klimek.langsapp.service.user.follow.commands.event.UserFollowEventsPublisher
import com.klimek.langsapp.service.user.follow.commands.storage.UserFollowCommandsRepository
import com.klimek.langsapp.service.user.follow.query.FollowerUserId
import com.klimek.langsapp.service.user.follow.query.UserFollowQueryService
import com.klimek.langsapp.service.user.query.UserId
import com.klimek.langsapp.service.user.query.UserQueryService
import org.springframework.stereotype.Service

@Service
class UserFollowCommandsService(
    private val userFollowEventsPublisher: UserFollowEventsPublisher,
    private val userFollowCommandsRepository: UserFollowCommandsRepository,
    private val userQueryService: UserQueryService,
    private val userFollowQueryService: UserFollowQueryService
) {

    fun followUser(followerUserId: String, userId: String): Either<Error, Boolean> =
        validateUserExists(userId = userId)
            .flatMap {
                userFollowQueryService
                    .getUserFollow(
                        followerUserId = FollowerUserId(followerUserId),
                        userId = com.klimek.langsapp.service.user.follow.query.UserId(userId)
                    )
                    .mapLeft { Error.ServiceError }
                    .flatMap {
                        if (it?.isFollowed == true) false.right()
                        else insertAndSendUserFollowEvent(
                            UserFollowEvent(
                                eventProperties = generateEventsProperties(),
                                followerUserId = followerUserId,
                                userId = userId,
                                isFollowed = true
                            )
                        )
                            .map { true }
                    }
            }

    fun unfollowUser(followerUserId: String, userId: String): Either<Error, Boolean> =
        validateUserExists(userId)
            .flatMap {
                userFollowQueryService
                    .getUserFollow(
                        followerUserId = FollowerUserId(followerUserId),
                        userId = com.klimek.langsapp.service.user.follow.query.UserId(userId)
                    )
                    .mapLeft { Error.ServiceError }
                    .flatMap {
                        if (it?.isFollowed == false) false.right()
                        else
                            insertAndSendUserFollowEvent(
                                UserFollowEvent(
                                    eventProperties = generateEventsProperties(),
                                    followerUserId = followerUserId,
                                    userId = userId,
                                    isFollowed = false
                                )
                            )
                                .map { true }
                    }
            }

    fun insertAndSendUserFollowEvent(userFollowEvent: UserFollowEvent): Either<Error, Unit> =
        userFollowCommandsRepository.storeUserFollowEvent(userFollowEvent)
            .mapLeft { Error.ServiceError }
            .onRight { userFollowEventsPublisher.sendUserFollowEvent(userFollowEvent) }

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

    companion object {
        sealed class Error {
            object ServiceError : Error()
            object UserNotFound : Error()
        }
    }
}
