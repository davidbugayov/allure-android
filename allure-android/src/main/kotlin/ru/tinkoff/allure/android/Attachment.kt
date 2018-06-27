/**
 * @author Alexey Nesmelov
 */
@file:JvmName("AllureAttachment")
package ru.tinkoff.allure.android

import ru.tinkoff.allure.android.Constants.ATTACHMENT
import java.io.File
import java.io.InputStream

const val TYPE_IMAGE_PNG = "image/png"
const val TYPE_IMAGE_JPG = "image/jpg"
const val TYPE_TEXT_HTML = "text/html"
const val TYPE_TEXT_JSON = "text/json"
const val TYPE_TEXT_XML = "text/xml"
const val TYPE_VIDEO_MP4 = "video/mp4"
const val TYPE_VIDEO_TS = "video/ts"

fun attachFile(tag: String, type: String, fileExtension: String, file : File) =
    AllureAndroidLifecycle.addAttachment(tag, type, fileExtension, file)

fun attachFile(tag: String, type: String, fileExtension: String, input : InputStream) =
        attachFile(tag, type, fileExtension, getTmpFile(ATTACHMENT).apply {
        outputStream().use {
            input.copyTo(it)
            input.close()
        }
    })

fun addAttachmentToReport(name: String?, type: String?, fileName: String) =
        AllureAndroidLifecycle.addAttachmentToReport(name, type, fileName)
