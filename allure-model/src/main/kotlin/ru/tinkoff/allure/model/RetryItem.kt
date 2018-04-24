package ru.tinkoff.allure.model

import com.google.gson.annotations.SerializedName
import java.io.PrintWriter
import java.io.StringWriter

/**
 * @author David Bugaev
 */
data class RetryItem(
        @SerializedName("uid") var uid: String? = null,
        @SerializedName("status") var status: Status? = null,
        @SerializedName("flaky") var flaky: Boolean? = null,
        @SerializedName("message") var message: String? = null,
        @SerializedName("trace") var trace: String? = null) {

    companion object {
        @JvmStatic
        fun fromThrowable(e: Throwable?): RetryItem? {
            if (e == null) return null
            return RetryItem(
                    message = e.message?.substringBefore('\n'),
                    trace = StringWriter().also { e.printStackTrace(PrintWriter(it)) }.toString()
            )
        }
    }
}
