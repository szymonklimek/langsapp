package com.klimek.langsapp.service.user.follow.query

import arrow.core.Either
import com.klimek.langsapp.service.user.follow.query.storage.UserFollowQueryRepository
import org.springframework.stereotype.Service

@Service
class UserFollowQueryService(
    private val repository: UserFollowQueryRepository
) {

    fun getUserFollow(followerUserId: FollowerUserId, userId: UserId): Either<ServiceError, UserFollow?> =
        repository
            .getUserFollow(followerUserId, userId)
            .mapLeft { ServiceError() }

    companion object {
        class ServiceError
    }
}
