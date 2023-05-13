package com.klimek.langsapp.service.user.query.storage

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.`java-time`.timestamp

object UserQueryDbTable: UUIDTable(name = "user_query.users", columnName = "id") {
    val userName = text("user_name")
    val updatedAt = timestamp("updated_at")
}
