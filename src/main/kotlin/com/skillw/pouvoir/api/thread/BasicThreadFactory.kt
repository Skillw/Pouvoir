package com.skillw.pouvoir.api.thread

import java.lang.Thread.UncaughtExceptionHandler
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicLong

class BasicThreadFactory(builder: Builder) : ThreadFactory {
    private val threadCounter: AtomicLong
    private var wrappedFactory: ThreadFactory? = null
    private val uncaughtExceptionHandler: UncaughtExceptionHandler?
    private val namingPattern: String?
    val priority: Int?
    private val daemonFlag: Boolean?
    val threadCount: Long
        get() = threadCounter.get()

    override fun newThread(r: Runnable): Thread {
        val t = wrappedFactory!!.newThread(r)
        initializeThread(t)
        return t
    }

    private fun initializeThread(t: Thread) {
        if (namingPattern != null) {
            val count = threadCounter.incrementAndGet()
            t.name = String.format(namingPattern, count)
        }
        if (uncaughtExceptionHandler != null) {
            t.uncaughtExceptionHandler = uncaughtExceptionHandler
        }
        if (priority != null) {
            t.priority = priority
        }
        if (daemonFlag != null) {
            t.isDaemon = daemonFlag
        }
    }

    class Builder : com.skillw.pouvoir.api.thread.Builder<BasicThreadFactory> {
        var wrappedFactory: ThreadFactory? = null
        var exceptionHandler: UncaughtExceptionHandler? = null
        var namingPattern: String? = null
        var priority: Int? = null
        var daemonFlag: Boolean? = null
        fun wrappedFactory(factory: ThreadFactory?): Builder {
            return if (factory == null) {
                throw NullPointerException("Wrapped ThreadFactory must not be null!")
            } else {
                wrappedFactory = factory
                this
            }
        }

        fun namingPattern(pattern: String?): Builder {
            return if (pattern == null) {
                throw NullPointerException("Naming pattern must not be null!")
            } else {
                namingPattern = pattern
                this
            }
        }

        fun daemon(f: Boolean): Builder {
            daemonFlag = f
            return this
        }

        fun priority(prio: Int): Builder {
            priority = prio
            return this
        }

        fun uncaughtExceptionHandler(handler: UncaughtExceptionHandler?): Builder {
            return if (handler == null) {
                throw NullPointerException("Uncaught exception handler must not be null!")
            } else {
                exceptionHandler = handler
                this
            }
        }

        private fun reset() {
            wrappedFactory = null
            exceptionHandler = null
            namingPattern = null
            priority = null
            daemonFlag = null
        }

        override fun build(): BasicThreadFactory {
            val factory = BasicThreadFactory(this)
            reset()
            return factory
        }
    }

    init {
        wrappedFactory = if (builder.wrappedFactory == null) {
            Executors.defaultThreadFactory()
        } else {
            builder.wrappedFactory
        }
        namingPattern = builder.namingPattern
        priority = builder.priority
        daemonFlag = builder.daemonFlag
        uncaughtExceptionHandler = builder.exceptionHandler
        threadCounter = AtomicLong()
    }
}