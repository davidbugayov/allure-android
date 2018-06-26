/**
 * @author Alexey Nesmelov
 */
@file:JvmName("AllureAttachment")
package ru.tinkoff.allure.android

import ru.tinkoff.allure.android.Constants.ATTACHMENT
import java.io.File
import java.io.InputStream

fun attachFile(tag: String, type: String, fileExtension: String, file : File) =
    AllureAndroidLifecycle.addAttachment(tag, type, fileExtension, file)

fun attachFile(tag: String, type: String, fileExtension: String, input : InputStream) =
        attachFile(tag, type, fileExtension, getTmpFile(ATTACHMENT).apply {
        outputStream().use {
            input.copyTo(it)
            input.close()
        }
    })

fun addAttachmentToReport(name: String?, type: String?, fileExtension: String?, fileName: String) =
        AllureAndroidLifecycle.addAttachmentToReport(name, type, fileExtension, fileName)





