package com.skillw.pouvoir.util

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.regex.Pattern

/**
 * ClassName : com.skillw.classsystem.util.MessageUtils
 * Created by Glom_ on 2021-03-25 20:25:13
 * Copyright  2021 user. All rights reserved.
 */
object MessageUtils {
    @JvmStatic
    fun message(sender: CommandSender, msg: String) {
        sender.sendMessage(ColorUtils.color(msg))
    }

    @JvmStatic
    fun wrong(msg: String) {
        Bukkit.getLogger().warning(msg)
    }

    @JvmStatic
    fun info(msg: String) {
        Bukkit.getLogger().info(msg)
    }

    @JvmStatic
    fun debug(msg: String) {
        info("§9[§eDebug§9] §e$msg")
    }

    @JvmStatic
    fun post(url: String, params: String): String {
        var out: PrintWriter? = null
        var `in`: BufferedReader? = null
        val result = StringBuilder()
        try {
            val realUrl = URL(url)
            val conn = realUrl.openConnection()
            conn.setRequestProperty("accept", "*/*")
            conn.setRequestProperty("connection", "Keep-Alive")
            conn.setRequestProperty(
                "user-agent",
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)"
            )
            conn.doOutput = true
            conn.doInput = true
            out = PrintWriter(conn.getOutputStream())
            out.print(params)
            out.flush()
            `in` = BufferedReader(InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))
            var line: String?
            while (`in`.readLine().also { line = it } != null) {
                result.append(line)
            }
        } catch (e: Exception) {
            info("Connect was failed!")
            return "-114514"
        } finally {
            try {
                out?.close()
                `in`?.close()
            } catch (ex: IOException) {
                return "-114514"
            }
        }
        return result.toString()
    }

    @JvmStatic
    val ip: String
        get() {
            var ip = ""
            val link = "https://ip.chinaz.com"
            val inputLine = StringBuilder()
            var read: String
            val url: URL
            val urlConnection: HttpURLConnection
            var `in`: BufferedReader? = null
            try {
                url = URL(link)
                urlConnection = url.openConnection() as HttpURLConnection
                `in` = BufferedReader(InputStreamReader(urlConnection.inputStream, "UTF-8"))
                while (`in`.readLine().also { read = it } != null) {
                    inputLine.append(
                        """
    $read
    
    """.trimIndent()
                    )
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                if (`in` != null) {
                    try {
                        `in`.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
            val p = Pattern.compile("<dd class=\"fz24\">(.*?)</dd>")
            val m = p.matcher(inputLine.toString())
            if (m.find()) {
                ip = m.group(1)
            }
            return ip
        }
}