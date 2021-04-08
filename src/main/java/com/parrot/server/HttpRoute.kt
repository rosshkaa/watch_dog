package com.parrot.server

import com.google.gson.Gson
import com.parrot.dto.RequestDTO
import spark.Request
import spark.Response
import spark.Route
import java.util.*
import java.util.logging.Level

class HttpRoute constructor(private val bot: TelegramBot) : Route {

    override fun handle(request: Request, response: Response): Any {
        try {
            val bodyObj = Gson().fromJson(request.body(), RequestDTO::class.java)
            bot.spam(formatMessage(bodyObj))
            response.status(200)
        } catch (e: Exception) {
            java.util.logging.Logger.getLogger(this::class.java.name).log(Level.INFO, "Server start error", e)
            response.status(500)
        }
        return response;
    }

    private fun formatMessage(messageObj: RequestDTO): String {
        val sb: StringBuilder = StringBuilder()
        messageObj.alerts.stream()
            .forEach { item -> sb.append("${item.status.uppercase(Locale.getDefault())} ${item.labels.alertname}: ${item.annotations.summary}") }
        return sb.toString()
    }
}