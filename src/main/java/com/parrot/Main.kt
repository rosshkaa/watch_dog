package com.parrot

import com.parrot.server.Server
import com.parrot.utils.Utils
import org.apache.commons.daemon.Daemon
import org.apache.commons.daemon.DaemonContext
import java.io.File
import kotlin.system.exitProcess

class Main : Daemon {
    companion object {
        private const val KEY_RESOURCE_FILE = "tg_key.txt"
        private const val port = 1337
        private const val webhookAddress = "/webhook"
        const val dbName = "webhook_users"
        const val tableName = "webhook_users"

        private val server = Server.getInstance()

        @JvmStatic
        fun main(args: Array<String>) {
            server.start(File("src/main/resources/$KEY_RESOURCE_FILE").readText(), port, webhookAddress)
        }

        @JvmStatic
        fun start(args: Array<String>) {
            server.start(
                Utils.readParentResourceFile(KEY_RESOURCE_FILE),
                port,
                webhookAddress
            )
        }

        @JvmStatic
        fun stop(args: Array<String>) {
            server.stop()
            exitProcess(0)
        }
    }

    override fun start() {
        Companion.start(emptyArray())
    }

    override fun stop() {
        Companion.stop(emptyArray())
    }

    override fun destroy() {
        stop()
    }

    override fun init(context: DaemonContext?) {
    }
}
