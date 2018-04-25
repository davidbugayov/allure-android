package ru.tinkoff.allure.model

import com.google.gson.annotations.SerializedName
import java.io.PrintWriter
import java.io.StringWriter

/**
 * @author David Bugaev
 */
data class Extra(
        @SerializedName("severity") val severity:String = "normal",//пока оставим таким
        // образом если как то повлияет на отчет, то добавим обработку
        @SerializedName("retries") var retries: RetryItem? = null) {

    companion object {
        @JvmStatic
        fun fromThrowable(e: Throwable?): StatusDetails? {
            if (e == null) return null
            return StatusDetails(
                    message = e.message?.substringBefore('\n'),
                    trace = StringWriter().also { e.printStackTrace(PrintWriter(it)) }.toString()
            )
        }
    }
}