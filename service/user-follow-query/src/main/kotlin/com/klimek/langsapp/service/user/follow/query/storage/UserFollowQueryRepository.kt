package com.klimek.langsapp.service.user.follow.query.storage

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.klimek.langsapp.service.user.follow.query.FollowerUserId
import com.klimek.langsapp.service.user.follow.query.UserFollow
import com.klimek.langsapp.service.user.follow.query.UserId
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.springframework.stereotype.Repository
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.UUID

@Repository
class UserFollowQueryRepository(
    private val database: UserFollowQueryDatabase
) {

    fun getUserFollow(followerUserId: FollowerUserId, userId: UserId): Either<StorageError, UserFollow?> =
        runCatching {
            transaction(Database.connect(database.dataSource)) {
                getUserFollowFromDatabase(followerUserId, userId)
            }
        }
            .fold(
                onFailure = { StorageError(it.message).left() },
                onSuccess = { it.right() }
            )

    fun createOrUpdateUserFollow(userFollow: UserFollow): Either<StorageError, Unit> =
        runCatching {
            transaction(Database.connect(database.dataSource)) {
                getUserFollowFromDatabase(userFollow.followerUserId, userFollow.userId)
                    .let { currentUserFollow ->
                        if (currentUserFollow == null)
                            UserFollowQueryDbTable.insert {
                                it[id] = UUID.randomUUID()
                                it[userId] = UUID.fromString(userFollow.userId.value)
                                it[followerUserId] = UUID.fromString(userFollow.followerUserId.value)
                                it[isFollowed] = userFollow.isFollowed
                                it[updatedAt] = OffsetDateTime.now(ZoneOffset.UTC).toInstant()
                            }
                        else
                            UserFollowQueryDbTable.update({
                                (UserFollowQueryDbTable.followerUserId eq UUID.fromString(userFollow.followerUserId.value))
                                    .and(UserFollowQueryDbTable.userId eq UUID.fromString(userFollow.userId.value))
                            }) {
                                it[isFollowed] = userFollow.isFollowed
                            }
                    }
            }
        }
            .fold(
                onFailure = { StorageError(it.message).left() },
                onSuccess = { Unit.right() }
            )

    private fun getUserFollowFromDatabase(followerUserId: FollowerUserId, userId: UserId) =
        UserFollowQueryDbTable
            .select {
                (UserFollowQueryDbTable.followerUserId eq UUID.fromString(followerUserId.value))
                    .and(UserFollowQueryDbTable.userId eq UUID.fromString(userId.value))
            }
            .map { resultRow ->
                UserFollow(
                    userId = userId,
                    followerUserId = followerUserId,
                    isFollowed = resultRow[UserFollowQueryDbTable.isFollowed]
                )
            }
            .firstOrNull()


    companion object {
        class StorageError(val errorMessage: String?)
    }
}
