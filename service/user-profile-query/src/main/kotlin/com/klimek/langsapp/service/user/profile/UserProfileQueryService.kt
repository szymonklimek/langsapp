package com.klimek.langsapp.service.user.profile

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import com.klimek.langsapp.events.follow.UserFollowEvent
import com.klimek.langsapp.events.user.RemovableUserProperty
import com.klimek.langsapp.events.user.UserCreatedEvent
import com.klimek.langsapp.events.user.UserUpdatedEvent
import com.klimek.langsapp.service.user.profile.query.generated.LanguageSetting
import com.klimek.langsapp.service.user.profile.query.generated.LanguageSettings
import com.klimek.langsapp.service.user.profile.query.generated.UserProfile
import com.klimek.langsapp.service.user.profile.storage.UserProfileQueryRepository
import org.springframework.stereotype.Service

@Service
class UserProfileQueryService(
    private val repository: UserProfileQueryRepository,
) {
    fun getUserProjection(userId: String): Either<ServiceError, UserProfile?> =
        repository.getUserById(userId)
            .mapLeft { ServiceError() }
            .map {
                if (it != null) {
                    UserProfile(
                        id = it.id,
                        name = it.name,
                        avatarUrl = it.avatarUrl,
                        languageSettings = it.languageSettings?.run {
                            LanguageSettings(
                                learnLanguage = LanguageSetting(code = learnLanguage.code),
                                baseLanguage = LanguageSetting(code = baseLanguage.code),
                                supportLanguage = supportLanguage?.let {
                                    LanguageSetting(it.code)
                                },
                            )
                        },
                        followingCount = it.followingCount,
                        followersCount = it.followersCount,
                    )
                } else {
                    null
                }
            }

    fun handleUserCreatedEvent(event: UserCreatedEvent): Either<ServiceError, Unit> =
        repository
            .storeFreshUser(event.userId)
            .mapLeft { ServiceError() }

    fun handleUserUpdatedEvent(event: UserUpdatedEvent): Either<ServiceError, Unit> =
        repository
            .getUserById(userId = event.userId)
            .flatMap { it?.right() ?: ServiceError().left() }
            .flatMap { userProfileRecord ->
                repository.storeUserProfileRecord(
                    userProfileRecord
                        .copy(
                            name = event.newUserName ?: userProfileRecord.name,
                            avatarUrl = event.newAvatarUrl ?: userProfileRecord.avatarUrl,
                            languageSettings = event.newLanguageSettings ?: userProfileRecord.languageSettings,
                            lastComputedEventTimestamp = event.eventProperties.createdAt,
                        )
                        .let {
                            if (event.propertiesToRemove.contains(RemovableUserProperty.AVATAR_URL)) {
                                it.copy(avatarUrl = null)
                            } else {
                                it
                            }
                        },
                )
            }
            .mapLeft { ServiceError() }

    fun handleUserFollowEvent(event: UserFollowEvent): Either<ServiceError, Unit> =
        Either.zipOrAccumulate(
            repository.getUserById(userId = event.followerUserId),
            repository.getUserById(userId = event.userId),
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
                    repository.storeUserProfileRecords(
                        listOf(
                            follower.copy(
                                lastComputedEventTimestamp = event.eventProperties.createdAt,
                                followingCount =
                                if (event.isFollowed) {
                                    follower.followingCount + 1
                                } else {
                                    follower.followingCount - 1
                                },
                            ),
                            user.copy(
                                lastComputedEventTimestamp = event.eventProperties.createdAt,
                                followersCount =
                                if (event.isFollowed) {
                                    user.followersCount + 1
                                } else {
                                    user.followersCount - 1
                                },
                            ),
                        ),
                    )
                }
                    .mapLeft { ServiceError() }
                    .map { }
            },
        )

    companion object {
        class ServiceError
    }
}
