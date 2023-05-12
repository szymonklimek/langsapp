package com.klimek.langsapp.service.user.follow.query.storage

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.`java-time`.timestamp

object UserFollowQueryDbTable: UUIDTable(name = "user_follow_query.follows", columnName = "id") {
    val userId = uuid("user_id")
    val followerUserId = uuid("user_id")
    val isFollowed = bool("is_followed")
    val updatedAt = timestamp("updated_at")
}
