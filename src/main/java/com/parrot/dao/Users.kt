package com.parrot.dao

import com.parrot.Main.Companion.tableName
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.logging.Level
import java.util.logging.Logger

object Users : LongIdTable(tableName) {
    val name = varchar("name", 100)
    val chatId = long("chatId").uniqueIndex()
}

class User(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<User>(Users)

    var name by Users.name
    var chatId by Users.chatId

    override fun toString(): String {
        return "User(name='$name', chatId=$chatId)"
    }
}

object UsersProvider {
    fun getUsers(): Collection<User> {
        var result: Collection<User> = emptyList()
        transaction {
            addLogger(StdOutSqlLogger)

            result = User.all().toList()
        }
        return result
    }

    fun addUser(_chatId: Long, _name: String) {
        transaction {
            addLogger(StdOutSqlLogger)
            if (!User.find { Users.chatId eq _chatId }.empty()) {
                return@transaction
            }
            User.new {
                chatId = _chatId
                name = _name
            }
        }
    }

    fun init() {
        transaction {
            addLogger(StdOutSqlLogger)

            SchemaUtils.create(Users)
        }
        Logger.getLogger(this::class.java.name).log(
            Level.INFO,
            "init with users ${getUsers().joinToString(", ")}"
        )
    }

    fun deleteUser(chatId: Long) {
        transaction {
            addLogger(StdOutSqlLogger)

            User.find { Users.chatId eq chatId }.forEach { it.delete() }
        }
    }
}
