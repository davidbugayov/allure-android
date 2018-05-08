package ru.tinkoff.allure.model

import com.google.gson.annotations.SerializedName

/**
 * @author Badya on 14.04.2017.
 */
enum class Status(val s: String) {
    @SerializedName("failed")
    FAILED("failed"),
    @SerializedName("broken")
    BROKEN("broken"),
    @SerializedName("passed")
    PASSED("passed"),
    @SerializedName("skipped")
    SKIPPED("skipped"),
    @SerializedName("unknown")
    UNKNOWN("unknown");
}

