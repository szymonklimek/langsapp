package com.klimek.langsapp.service.user.profile.storage

import com.klimek.langsapp.service.user.profile.domain.UserProfileProjection

@JvmInline
value class FollowerId(val userId: String)

@JvmInline
value class FollowId(val userId: String)

object UserProfileInMemoryStorage {
    val usersProfiles = mutableMapOf<String, UserProfileProjection>()
    val usersFollows = mutableMapOf<FollowerId, FollowId>()
}
