package de.lijucay.damier.core

import android.content.Context
import android.util.Log
import de.lijucay.damier.BuildConfig
import de.lijucay.damier.core.domain.DataUtil
import de.lijucay.damier.core.presentation.models.toDisplayableDateTimeWithMs
import java.io.File
import java.time.LocalDateTime

object Logger {

    /**
     * Automatically generates a logging tag by inspecting the stack trace to find the
     * name of the class that called the logger.
     *
     * @return The simple name of the calling class, or "Unknown" if the stack trace is unavailable.
     */
    private fun autoTag(): String {
        val stacktrace = Throwable().stackTrace
        val element = stacktrace.getOrNull(2) ?: return "Unknown"
        val fullClassName = element.className
        val simpleName = fullClassName.substringAfterLast(".").substringBefore("$")
        return simpleName
    }

    /**
     * Writes a formatted log entry to the application's internal log file.
     *
     * @param context The context used to locate the internal files' directory.
     * @param level The severity level of the log (e.g., DEBUG, INFO, ERROR).
     * @param tag The source identifier, typically the class name where the log originated.
     * @param message The descriptive text or stack trace to be recorded.
     */
    private fun write(context: Context, level: String, tag: String, message: String) {
        runCatching {
            val file = File(context.filesDir, DataUtil.LOG_FILE_NAME)
            val timestamp = LocalDateTime.now().toDisplayableDateTimeWithMs().formatted
            file.appendText("[$timestamp] - [$level]: [$tag] $message\n")
        }
    }

    /**
     * Writes logs to the log-file on level DEBUG. Only writes logs to the file,
     * when the app is a debug version
     *
     * @param context
     * @param message The log that needs to be saved into the file
     * */
    fun d(context: Context, message: String) {
        val tag = autoTag()
        Log.d(tag, message)
        if (BuildConfig.DEBUG) write(context, "DEBUG", tag, message)
    }

    /**
     * Writes logs to the log-file on level ERROR.
     *
     * @param context
     * @param message The log that needs to be saved into the file
     * @param throwable
     * */
    fun e(context: Context, message: String, throwable: Throwable? = null) {
        val tag = autoTag()
        Log.e(tag, message, throwable)
        val fullMessage =
            if (throwable != null) "$message\n${throwable.stackTraceToString()}" else message
        write(context, "ERROR", tag, fullMessage)
    }

    /**
     * Writes logs to the log-file on level WARNING.
     *
     * @param context
     * @param message The log that needs to be saved into the file
     * */
    fun w(context: Context, message: String) {
        val tag = autoTag()
        Log.w(tag, message)
        write(context, "WARNING", tag, message)
    }

    /**
     * Writes logs to the log-file on level INFO.
     *
     * @param context
     * @param message The log that needs to be saved into the file
     * */
    fun i(context: Context, message: String) {
        val tag = autoTag()
        Log.i(tag, message)
        write(context, "INFO", tag, message)
    }

    /**
     * Clears the log file completely
     *
     * @param context
     * */
    fun clear(context: Context) {
        runCatching {
            val file = File(context.filesDir, DataUtil.LOG_FILE_NAME)
            file.delete()
        }
    }
}