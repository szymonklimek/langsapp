package com.klimek.langsapp.service.user.follow.query.storage

import arrow.core.Either
import arrow.core.right
import com.klimek.langsapp.service.user.follow.query.FollowerUserId
import com.klimek.langsapp.service.user.follow.query.UserFollow
import com.klimek.langsapp.service.user.follow.query.UserId
import org.springframework.stereotype.Repository

@Repository
class UserFollowQueryRepository {

    fun getUserFollow(followerUserId: FollowerUserId, userId: UserId): Either<StorageError, UserFollow?> =
        UserFollowQueryInMemoryStorage
            .userFollows[followerUserId to userId]
            .right()
            .map { isFollowed: Boolean? ->
                isFollowed?.let {
                    UserFollow(
                        userId = userId,
                        followerUserId = followerUserId,
                        isFollowed = it,
                    )
                }
            }

    fun createOrUpdateUserFollow(userFollow: UserFollow): Either<StorageError, Unit> {
        UserFollowQueryInMemoryStorage.userFollows[userFollow.followerUserId to userFollow.userId] =
            userFollow.isFollowed
        return Unit.right()
    }

    companion object {
        class StorageError
    }
}
