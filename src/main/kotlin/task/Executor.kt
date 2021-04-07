package task

import java.net.HttpURLConnection
import java.net.InetAddress
import java.net.URL

class Executor {
    private val urlsToPing: MutableList<String> = mutableListOf()

    fun addUrl(url: String): Boolean {
        return if (ping(url).code == -1) {
            false
        } else {

            urlsToPing.add(url);
            true
        }
    }

    fun getStatuses(): String {
        if (urlsToPing.size < 1) {
            return "No registered addresses"
        }
        val result: StringBuilder = StringBuilder()

        urlsToPing.forEach { item ->
            run {
                result.append("address: $item, status: ${ping((item)).getResultMessage()}").append("\n")
            }
        }

        return result.toString()
    }

    private fun ping(url: String): PingResult {
        return if (url.toLowerCase().startsWith("http")) {
            pingUrl(url)
        } else {
            pingSocket(url)
        }
    }

    private fun pingSocket(url: String): PingResult {
        return try {
            val address = InetAddress.getByName(url)
            if (address.isReachable(2000)) {
                PingResult(0)
            } else {
                PingResult(1)
            }
        } catch (e: Exception) {
            println(e.message)

            PingResult(-1, e.message)
        }
    }

    private fun pingUrl(url: String): PingResult {
        return try {
            val connection: HttpURLConnection = URL(url).openConnection() as HttpURLConnection
            connection.setRequestProperty("Connection", "close")
            connection.connectTimeout = 3000
            connection.connect()
            val response = connection.responseCode

            if (response != 404 && response < 500) {
                PingResult(0)
            } else {
                PingResult(1)
            }
        } catch (e: Exception) {
            println(e.message)

            PingResult(-1, e.message)
        }
    }
}
