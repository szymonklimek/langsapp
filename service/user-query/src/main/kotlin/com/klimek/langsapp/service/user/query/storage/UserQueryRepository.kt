package com.klimek.langsapp.service.user.query.storage

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.klimek.langsapp.service.user.query.UserId
import com.klimek.langsapp.service.user.query.UserName
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.springframework.stereotype.Repository
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.UUID

@Repository
class UserQueryRepository(
    private val database: UserQueryDatabase
) {

    fun getUserIdByName(userName: UserName): Either<StorageError, UserId?> =
        runCatching {
            transaction(Database.connect(database.dataSource)) {
                UserQueryDbTable.select { UserQueryDbTable.userName eq userName.value }
            }
                .map { it[UserQueryDbTable.id] }
                .firstOrNull()
        }.fold(
            onFailure = { StorageError(it.message).left() },
            onSuccess = {
                if (it == null) null.right()
                else UserId(value = it.toString()).right()

            }
        )

    fun getNameById(userId: UserId): Either<StorageError, UserName?> =
        runCatching {
            transaction(Database.connect(database.dataSource)) {
                UserQueryDbTable.select { UserQueryDbTable.id eq UUID.fromString(userId.value) }
            }
                .map { it[UserQueryDbTable.userName] }
                .firstOrNull()
        }.fold(
            onFailure = { StorageError(it.message).left() },
            onSuccess = {
                if (it == null) null.right()
                else UserName(value = it).right()
            }
        )

    fun storeUser(userId: UserId, userName: UserName): Either<StorageError, Unit> =
        runCatching {
            transaction(Database.connect(database.dataSource)) {
                UserQueryDbTable
                    .select { UserQueryDbTable.id eq UUID.fromString(userId.value) }
                    .map { resultRow ->
                        resultRow[UserQueryDbTable.userName]
                    }
                    .firstOrNull()
                    .let { currentUserFollow ->
                        if (currentUserFollow == null)
                            UserQueryDbTable
                                .insert {
                                    it[id] = UUID.randomUUID()
                                    it[UserQueryDbTable.userName] = userName.value
                                    it[updatedAt] = OffsetDateTime.now(ZoneOffset.UTC).toInstant()
                                }
                        else
                            UserQueryDbTable
                                .update({ UserQueryDbTable.id eq UUID.fromString(userId.value) }) {
                                    it[UserQueryDbTable.userName] = userName.value
                                    it[updatedAt] = OffsetDateTime.now(ZoneOffset.UTC).toInstant()
                                }
                    }
            }
        }
            .fold(
                onFailure = { StorageError(it.message).left() },
                onSuccess = { Unit.right() }
            )

    companion object {
        class StorageError(val errorMessage: String?)
    }
}
