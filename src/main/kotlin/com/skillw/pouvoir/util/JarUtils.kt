package com.skillw.pouvoir.util

import org.slf4j.LoggerFactory
import sun.misc.Unsafe
import java.io.File
import java.lang.invoke.MethodHandle
import java.lang.invoke.MethodHandles
import java.lang.reflect.Method
import java.net.URL
import java.nio.file.Files
import java.nio.file.StandardCopyOption

object JarUtils {
    private val unsafe: Unsafe by lazy {
        val theUnsafeField = Unsafe::class.java.getDeclaredField("theUnsafe")
        theUnsafeField.isAccessible = true
        theUnsafeField[null] as Unsafe
    }

    private val ucp: Any? by lazy {
        val loader = Thread.currentThread().contextClassLoader
        val ucpField = try {
            loader.javaClass.getDeclaredField("ucp")
        } catch (e: NoSuchFieldException) {
            loader.javaClass.superclass.getDeclaredField("ucp")
        }
        val offset = unsafe.objectFieldOffset(ucpField)
        unsafe.getObject(loader, offset)
    }
    private val addURL: MethodHandle? by lazy {
        val lookUpField = MethodHandles.Lookup::class.java.getDeclaredField("IMPL_LOOKUP")
        val addURLMethod: Method = ucp!!.javaClass.getDeclaredMethod(
            "addURL", URL::class.java
        )
        (unsafe.getObject(
            unsafe.staticFieldBase(lookUpField),
            unsafe.staticFieldOffset(lookUpField)
        ) as MethodHandles.Lookup).unreflect(addURLMethod)
    }

    private val logger = LoggerFactory.getLogger(JarUtils::class.java.name)

    @JvmStatic
    fun loadJar(jarPath: String) {
        val url: URL = try {
            val realJarPath = jarPath.substring(jarPath.indexOf(Constants.FILE_PATH_SPLIT) + 1)
            if (jarPath.startsWith(Constants.LOCAL_FILE_PRE)) {
                val jarFile = File(realJarPath)
                jarFile.toURI().toURL()
            } else if (jarPath.startsWith(Constants.REMOTE_FILE_PRE)) {
                URL("http://$realJarPath")
            }
            null
        } catch (ignored: Exception) {
            null
        } ?: return

        loadJar(url)

    }

    fun loadJar(jarFile: File) {
        val url: URL = try {
            jarFile.toURI().toURL()
        } catch (ignored: Exception) {
            null
        } ?: return
        loadJar(url)
    }

    fun loadJar(url: URL) {
        try {
            addURL!!.invoke(ucp, url)
        } catch (e: Exception) {
            e.printStackTrace()
            throw IllegalStateException(e.message, e)
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

    fun downloadRepo(file: File, groupId: String, artifactId: String, version: String): File {
        Files.copy(
            URL(
                "https://maven.aliyun.com/repository/public/${
                    groupId.replace(
                        ".",
                        "/"
                    )
                }/$artifactId/$version/$artifactId-$version.jar"
            ).openStream(), file.toPath(), StandardCopyOption.REPLACE_EXISTING
        )
        return file
    }

    fun loadRepo(root: String, groupId: String, artifactId: String, version: String) {
        val lib = File(root, "$artifactId-$version.jar")
        if (!lib.exists()) {
            downloadRepo(lib, groupId, artifactId, version)
        }
        loadJar(lib)
    }


    class NashornLib {


        init {
            try {
                load()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        private fun load() {
            val hasNashorn = try {
                Class.forName("jdk.nashorn.api.scripting.NashornScriptEngineFactory") != null
            } catch (e: Exception) {
                false
            }
            if (!hasNashorn) {
                loadNashornLibs()
            }
        }

        private fun loadNashornLibs() {
            val libs = File("libs")
            libs.mkdirs()
            val libRoot = libs.canonicalPath
            loadRepo(libRoot, "org.ow2.asm", "asm", "9.2")
            loadRepo(libRoot, "org.ow2.asm", "asm-commons", "9.2")
            loadRepo(libRoot, "org.ow2.asm", "asm-tree", "9.2")
            loadRepo(libRoot, "org.ow2.asm", "asm-util", "9.2")
            loadRepo(libRoot, "org.openjdk.nashorn", "nashorn-core", "15.3")
        }

    }
}