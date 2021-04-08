package com.parrot.server

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.TelegramFile
import com.parrot.utils.Utils
import java.util.logging.Level
import java.util.logging.Logger
import kotlin.system.exitProcess

class TelegramBot private constructor() : AutoCloseable {
    companion object {
        private var currentBot: TelegramBot? = null
        private var botApiProcessor: Bot? = null
        private val chats: MutableSet<Long> = HashSet()

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
                        chats.add(message.chat.id)
                        bot.sendDocument(
                            ChatId.fromId(message.chat.id),
                            TelegramFile.ByFile(Utils.getParentResourceFile("resources/hello_there.gif"))
                        )
                    } catch (e: Exception) {
                        Logger.getLogger(this::class.java.name).log(Level.INFO, "Bot error", e)
                        bot.sendMessage(
                            ChatId.fromId(message.chat.id),
                            "Something went terribly wrong: ${e.message}"
                        )
                    }
                }

                command("stop") {
                    try {
                        Logger.getLogger(this::class.java.name).log(
                            Level.INFO,
                            "stop ${message.chat.username} ${message.chat.firstName} ${message.chat.lastName}"
                        )
                        chats.remove(message.chat.id)
                        bot.sendDocument(
                            ChatId.fromId(message.chat.id),
                            TelegramFile.ByFile(Utils.getParentResourceFile("resources/ciao.gif"))
                        )
                    } catch (e: Exception) {
                        Logger.getLogger(this::class.java.name).log(Level.INFO, "Bot error", e)
                        bot.sendMessage(
                            ChatId.fromId(message.chat.id),
                            "Something went terribly wrong: ${e.message}"
                        )
                    }
                }
            }

        }

        botApiProcessor!!.startPolling()
    }

    public fun spam(message: String) {
        if (chats.isEmpty()) {
            Logger.getLogger(this::class.java.name).log(Level.INFO, "No chats to spam")
        }
        for (chatId in chats) {
            try {
                if (botApiProcessor == null) {
                    Logger.getLogger(this::class.java.name).log(Level.INFO, "Bot if not initialized!")
                    return
                }
                botApiProcessor!!.sendMessage(
                    chatId = ChatId.fromId(chatId),
                    text = message
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
