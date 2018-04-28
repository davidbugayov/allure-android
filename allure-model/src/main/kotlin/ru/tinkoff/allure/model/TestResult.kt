package ru.tinkoff.allure.model

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * @author Badya on 14.04.2017.
 */
class TestResult(
        @SerializedName("uuid") val uuid: String = UUID.randomUUID().toString(),
        name: String? = null,
        @SerializedName("historyId") var historyId: String? = null,
        @SerializedName("testCaseId") var testCaseId: String? = null,
        @SerializedName("rerunOf") var rerunOf: String? = null,
        @SerializedName("fullName") var fullName: String? = null,
        @SerializedName("labels") var labels: List<Label> = ArrayList(),
        @SerializedName("links") var links: List<Link> = ArrayList()
) : ExecutableItem(name = name)