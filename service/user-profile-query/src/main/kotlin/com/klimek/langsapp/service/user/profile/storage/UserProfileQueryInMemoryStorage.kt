package com.klimek.langsapp.service.user.profile.storage

@JvmInline
value class FollowerId(val userId: String)

@JvmInline
value class FollowId(val userId: String)

object UserProfileInMemoryStorage {
    val usersProfiles = mutableMapOf<String, UserProfileRecord?>()
    val usersFollows = mutableMapOf<FollowerId, FollowId>()
}
