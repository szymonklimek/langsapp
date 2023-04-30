package com.klimek.langsapp.service.user.profile

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import com.klimek.langsapp.events.follow.UserFollowEvent
import com.klimek.langsapp.events.user.UserCreatedEvent
import com.klimek.langsapp.events.user.UserUpdatedEvent
import com.klimek.langsapp.service.user.profile.domain.UserProfileProjection
import com.klimek.langsapp.service.user.profile.storage.UserProfileQueryRepository
import org.springframework.stereotype.Service

@Service
class UserProfileQueryService(
    private val repository: UserProfileQueryRepository
) {
    fun getUserProjection(userId: String): Either<ServiceError, UserProfileProjection?> =
        repository.getUserById(userId)
            .mapLeft { ServiceError() }

    fun handleUserCreatedEvent(event: UserCreatedEvent): Either<ServiceError, Unit> =
        repository
            .storeUserProjection(
                UserProfileProjection(
                    id = event.userId,
                    name = event.userName,
                    avatarUrl = event.avatarUrl,
                    followersCount = 0,
                    followingCount = 0,
                    lastComputedEventTimestamp = event.eventProperties.createdAt
                )
            )
            .mapLeft { ServiceError() }

    fun handleUserUpdatedEvent(event: UserUpdatedEvent): Either<ServiceError, Unit> =
        repository
            .getUserById(userId = event.userId)
            .flatMap { it?.right() ?: ServiceError().left() }
            .flatMap { userProfileProjection ->
                repository.storeUserProjection(
                    userProfileProjection.copy(
                        name = event.userName,
                        avatarUrl = event.avatarUrl,
                        lastComputedEventTimestamp = event.eventProperties.createdAt
                    )
                )
            }
            .mapLeft { ServiceError() }

    fun handleUserFollowEvent(event: UserFollowEvent): Either<ServiceError, Unit> =
        Either.zipOrAccumulate(
            repository.getUserById(userId = event.followerUserId),
            repository.getUserById(userId = event.userId)
        ) { follower, user ->
            when {
                follower == null -> ServiceError().left()
                user == null -> ServiceError().left()
                else -> (follower to user).right()
            }
        }.fold(
            ifLeft = { ServiceError().left() },
            ifRight = {
                it.flatMap { (follower, user) ->
                    repository.storeUserProjections(
                        listOf(
                            follower.copy(
                                lastComputedEventTimestamp = event.eventProperties.createdAt,
                                followingCount =
                                if (event.isFollowed) follower.followingCount + 1
                                else follower.followingCount - 1
                            ),
                            user.copy(
                                lastComputedEventTimestamp = event.eventProperties.createdAt,
                                followersCount =
                                if (event.isFollowed) user.followersCount + 1
                                else user.followersCount - 1
                            )
                        )
                    )
                }
                    .mapLeft { ServiceError() }
                    .map { }
            }
        )

    companion object {
        class ServiceError
    }
}
