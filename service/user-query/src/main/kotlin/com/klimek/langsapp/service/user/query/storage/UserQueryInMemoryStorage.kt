package com.klimek.langsapp.service.user.query.storage

import com.klimek.langsapp.service.user.query.User
import com.klimek.langsapp.service.user.query.UserId

object UserQueryInMemoryStorage {
    val users = mutableMapOf<UserId, User?>()
}
