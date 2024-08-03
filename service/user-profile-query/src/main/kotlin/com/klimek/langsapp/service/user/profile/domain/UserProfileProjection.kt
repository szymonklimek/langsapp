package com.klimek.langsapp.service.user.profile.domain

data class UserProfileProjection(
    val id: String,
    val name: String,
    val avatarUrl: String?,
    val followersCount: Int,
    val followingCount: Int,
    val lastComputedEventTimestamp: Long,
)
