package com.skillw.pouvoir.util

import org.slf4j.LoggerFactory
import java.io.File
import java.net.URL
import java.net.URLClassLoader

object JarUtils {
    private val logger = LoggerFactory.getLogger(JarUtils::class.java.name)

    @JvmStatic
    fun loadJar(jarPath: String) {
        var url: URL? = null
        // 获取jar的真实路径，过滤掉标志前缀(本地路径以"local|"标志，远程路径以"remote|"标志，s3路径以"s3|"标志)
        val realJarPath = jarPath.substring(jarPath.indexOf(Constants.FILE_PATH_SPLIT) + 1)
        try {
            // 根据jarPath前缀获取url
            if (jarPath.startsWith(Constants.LOCAL_FILE_PRE)) {
                // 加载本地jar，比如：local|/User/xxx/test/demo.jar
                val jarFile = File(realJarPath)
                url = jarFile.toURI().toURL()
            } else if (jarPath.startsWith(Constants.REMOTE_FILE_PRE)) {
                // 加载远程jar，比如：remote|http://192.168.xx.xxx/test/demo.jar
                url = URL("http://$realJarPath")
            }
        } catch (ignored: Exception) {
        }
        if (null != url) {
            // 从URLClassLoader类中获取类所在文件夹的方法，jar也可以认为是一个文件夹
            val classLoader: Any = ClassLoader.getSystemClassLoader()
            // 获取方法的访问权限以便写回
            if (classLoader is URLClassLoader) {
                val systemClassLoader = ClassLoader.getSystemClassLoader() as URLClassLoader
                val urlClassLoader: Class<*> = URLClassLoader::class.java
                try {
                    val method = urlClassLoader.getDeclaredMethod("addURL", URL::class.java)
                    method.isAccessible = true
                    method.invoke(systemClassLoader, url)
                } catch (var5: Exception) {
                    var5.printStackTrace()
                    throw IllegalStateException(var5.message, var5)
                }
            } else {
                try {
                    val field = classLoader.javaClass.getDeclaredField("ucp")
                    field.isAccessible = true
                    val ucp = field[classLoader]
                    val method = ucp.javaClass.getDeclaredMethod("addURL", URL::class.java)
                    method.isAccessible = true
                    method.invoke(ucp, url)
                } catch (exception: Exception) {
                    exception.printStackTrace()
                    throw IllegalStateException(exception.message, exception)
                }
            }
        }
    }

    object Constants {
        /**
         * 文件分隔前缀
         */
        const val FILE_PATH_SPLIT = "|"

        /**
         * 本地文件
         */
        const val LOCAL_FILE_PRE = "local"

        /**
         * 远程文件
         */
        const val REMOTE_FILE_PRE = "remote"

        /**
         * s3文件
         */
        const val AWS_FILE_PRE = "aws"
    }
}