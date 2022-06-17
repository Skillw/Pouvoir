package com.skillw.pouvoir.util

import org.bukkit.craftbukkit.libs.org.apache.commons.io.output.ByteArrayOutputStream
import org.bukkit.util.io.BukkitObjectInputStream
import org.bukkit.util.io.BukkitObjectOutputStream
import java.io.ByteArrayInputStream
import java.util.*

object EDCodeUtils {
    /**
     * 将对象进行 Base64 加密并输出字符串
     * @param any Any 待加密对象
     * @return String Base64 加密文本
     */
    @JvmStatic
    fun enBase64(any: Any): String? {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val bukkitObjectOutputStream = BukkitObjectOutputStream(byteArrayOutputStream)
        bukkitObjectOutputStream.writeObject(any)
        val bytes = byteArrayOutputStream.toByteArray()
        return Base64.getEncoder().encodeToString(bytes)
    }

    /**
     * 将 Base64 加密文本解析为对象
     * @param base64String String Base64 加密文本
     * @param clazz Class 类
     * @return Object 相应对象
     */
    @JvmStatic
    fun <T> deBase64(base64String: String, clazz: Class<T>): T {
        val decode = Base64.getDecoder().decode(base64String)
        val byteArrayInputStream = ByteArrayInputStream(decode)
        val bukkitObjectInputStream = BukkitObjectInputStream(byteArrayInputStream)
        val readObject = bukkitObjectInputStream.readObject()
        return clazz.cast(readObject)
    }

}