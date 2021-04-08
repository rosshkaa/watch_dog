package com.parrot.utils

import com.parrot.Main
import java.io.File
import java.net.URLDecoder


class Utils {
    companion object {
        @JvmStatic
        public fun getParentResourceFile(path: String): File {
            val mainPath: String = Main::class.java.protectionDomain.codeSource.location.path
            val decodedPath = URLDecoder.decode(mainPath, "UTF-8")
            val parentFile = File(decodedPath).parentFile.parentFile
            return parentFile.toPath().resolve(path).toFile()
        }

        @JvmStatic
       public fun readParentResourceFile(path: String): String {
            return getParentResourceFile(path).readText().trim()
        }
    }
}