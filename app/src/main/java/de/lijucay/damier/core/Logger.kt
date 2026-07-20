package de.lijucay.damier.core

import android.content.Context
import android.util.Log
import de.lijucay.damier.core.domain.DataUtil
import de.lijucay.damier.core.presentation.models.toDisplayableDateTimeWithMs
import java.io.File
import java.time.LocalDateTime

object Logger {
    private fun autoTag(): String {
        val stacktrace = Throwable().stackTrace
        val element = stacktrace.getOrNull(2) ?: return "Unknown"
        val fullClassName = element.className
        val simpleName = fullClassName.substringAfterLast(".").substringBefore("$")
        return simpleName
    }

    private fun write(context: Context, level: String, tag: String, message: String) {
        runCatching {
            val file = File(context.filesDir, DataUtil.LOG_FILE_NAME)
            val timestamp = LocalDateTime.now().toDisplayableDateTimeWithMs().formatted
            file.appendText("[$timestamp] - [$level]: [$tag] $message\n")
        }
    }

    fun d(context: Context, message: String) {
        val tag = autoTag()
        Log.d(tag, message)
        write(context, "DEBUG", tag, message)
    }

    fun e(context: Context, message: String, throwable: Throwable? = null) {
        val tag = autoTag()
        Log.e(tag, message, throwable)
        val fullMessage =
            if (throwable != null) "$message\n${throwable.stackTraceToString()}" else message
        write(context, "ERROR", tag, fullMessage)
    }

    fun w(context: Context, message: String) {
        val tag = autoTag()
        Log.w(tag, message)
        write(context, "WARNING", tag, message)
    }

    fun i(context: Context, message: String) {
        val tag = autoTag()
        Log.i(tag, message)
        write(context, "INFO", tag, message)
    }

    fun clear(context: Context) {
        runCatching {
            val file = File(context.filesDir, DataUtil.LOG_FILE_NAME)
            file.delete()
        }
    }
}