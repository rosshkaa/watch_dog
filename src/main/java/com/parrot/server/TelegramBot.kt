package com.parrot.server

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.TelegramFile
import com.parrot.dao.UsersProvider
import com.parrot.utils.Utils
import java.util.logging.Level
import java.util.logging.Logger

class TelegramBot private constructor() : AutoCloseable {
    companion object {
        private var currentBot: TelegramBot? = null
        private var botApiProcessor: Bot? = null

        fun getInstance(): TelegramBot {
            if (currentBot == null) {
                currentBot = TelegramBot()
            }
            return currentBot as TelegramBot
        }
    }

    fun start(telegramToken: String) {
        botApiProcessor = bot {
            token = telegramToken
            dispatch {
                command("start") {
                    try {
                        Logger.getLogger(this::class.java.name).log(
                            Level.INFO,
                            "start ${message.chat.username} ${message.chat.firstName} ${message.chat.lastName}"
                        )
                        UsersProvider.addUser(message.chat.id, message.chat.username.toString())
                        bot.sendDocument(
                            ChatId.fromId(message.chat.id),
                            TelegramFile.ByFile(Utils.getParentResourceFile("resources/hello_there.gif")),
                            "Hello there!"
                        )
                    } catch (e: Exception) {
                        Logger.getLogger(this::class.java.name).log(Level.INFO, "Bot error", e)
                        bot.sendMessage(
                            ChatId.fromId(message.chat.id), "Something went terribly wrong: ${e.message}"
                        )
                    }
                }

                command("stop") {
                    try {
                        Logger.getLogger(this::class.java.name).log(
                            Level.INFO,
                            "stop ${message.chat.username} ${message.chat.firstName} ${message.chat.lastName}"
                        )
                        UsersProvider.deleteUser(message.chat.id)
                        bot.sendDocument(
                            ChatId.fromId(message.chat.id),
                            TelegramFile.ByFile(Utils.getParentResourceFile("resources/ciao.gif")),
                            "By!"
                        )
                    } catch (e: Exception) {
                        Logger.getLogger(this::class.java.name).log(Level.INFO, "Bot error", e)
                        bot.sendMessage(
                            ChatId.fromId(message.chat.id), "Something went terribly wrong: ${e.message}"
                        )
                    }
                }
            }

        }

        botApiProcessor!!.startPolling()
    }

    fun spam(message: String) {
        UsersProvider.getUsers().forEach {
            try {
                if (botApiProcessor == null) {
                    Logger.getLogger(this::class.java.name).log(Level.INFO, "Bot if not initialized!")
                    return@forEach
                }
                botApiProcessor!!.sendMessage(
                    chatId = ChatId.fromId(it.chatId), text = message
                )
            } catch (e: java.lang.Exception) {
                Logger.getLogger(this::class.java.name).log(Level.INFO, "Telegram bot sending error", e)
            }
        }
    }

    override fun close() {
        botApiProcessor?.stopPolling()
    }
}
