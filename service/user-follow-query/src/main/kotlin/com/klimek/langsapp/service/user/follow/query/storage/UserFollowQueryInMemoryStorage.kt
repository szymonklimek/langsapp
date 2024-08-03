package com.klimek.langsapp.service.user.follow.query.storage

import com.klimek.langsapp.service.user.follow.query.FollowerUserId
import com.klimek.langsapp.service.user.follow.query.UserId

object UserFollowQueryInMemoryStorage {
    val userFollows = mutableMapOf<Pair<FollowerUserId, UserId>, Boolean?>()
}
