package com.klimek.langsapp.service.user.query

@JvmInline
value class UserId(val value: String)

@JvmInline
value class UserName(val value: String)

data class User(val userId: UserId, val userName: UserName)
