package com.klimek.langsapp.service.user.follow.commands

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.right
import com.klimek.langsapp.events.follow.UserFollowEvent
import com.klimek.langsapp.events.generateEventsProperties
import com.klimek.langsapp.service.user.follow.commands.event.UserFollowEventsPublisher
import com.klimek.langsapp.service.user.follow.commands.storage.UserFollowCommandsRepository
import org.springframework.stereotype.Service

@Service
class UserFollowCommandsService(
    private val userFollowEventsPublisher: UserFollowEventsPublisher,
    private val userFollowCommandsRepository: UserFollowCommandsRepository
) {

    fun followUser(followerUserId: String, userId: String): Either<ServiceError, Boolean> =
        userFollowCommandsRepository.getLastStoredEvent(followerUserId, userId)
            .mapLeft { ServiceError() }
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

    fun unfollowUser(followerUserId: String, userId: String): Either<ServiceError, Boolean> =
        userFollowCommandsRepository.getLastStoredEvent(followerUserId, userId)
            .mapLeft { ServiceError() }
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

    fun insertAndSendUserFollowEvent(userFollowEvent: UserFollowEvent): Either<ServiceError, Unit> =
        userFollowCommandsRepository.storeUserFollowEvent(userFollowEvent)
            .mapLeft { ServiceError() }
            .onRight { userFollowEventsPublisher.sendUserFollowEvent(userFollowEvent) }

    companion object {
        class ServiceError
    }
}
