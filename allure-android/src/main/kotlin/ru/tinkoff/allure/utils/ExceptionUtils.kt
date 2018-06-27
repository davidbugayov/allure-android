package ru.tinkoff.allure.utils

import java.io.PrintWriter
import java.io.StringWriter

fun getStringTrace(e: Exception): String {
    val sw = StringWriter()
    val pw = PrintWriter(sw)
    e.printStackTrace(pw)
    return sw.toString()
}