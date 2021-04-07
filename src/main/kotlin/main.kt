import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.entities.ChatId
import task.Executor

fun main(args: Array<String>) {
    val executor = Executor()
    val bot = bot {
        token = args[0]

        dispatch {
            command("add") {
                bot.sendMessage(
                    chatId = ChatId.fromId(message.chat.id),
                    text = if (executor.addUrl(this.args[0])) {
                        "Address ${this.args[0]} successfully registered"
                    } else {
                        "Error during url ${this.args[0]} check"
                    }
                )
            }

            command("get") {
                bot.sendMessage(chatId = ChatId.fromId(message.chat.id), text = executor.getStatuses())
            }
        }
    }

    bot.startPolling()
}
