package com.klimek.langsapp.service.user.follow.query

@JvmInline
value class UserId(val value: String)

@JvmInline
value class FollowerUserId(val value: String)

data class UserFollow(
    val userId: UserId,
    val followerUserId: FollowerUserId,
    val isFollowed: Boolean
)
