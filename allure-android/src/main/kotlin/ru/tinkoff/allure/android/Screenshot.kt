package ru.tinkoff.allure.android

import android.os.Environment
import android.support.test.InstrumentationRegistry.getInstrumentation
import android.support.test.uiautomator.UiDevice
import ru.tinkoff.allure.android.Constants.ATTACHMENT
import ru.tinkoff.allure.android.Constants.RESULTS_FOLDER
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * @author Badya on 31.05.2017.
 */
fun deviceScreenshot(tag: String) {
    val file = getTmpFile(ATTACHMENT)
    with(UiDevice.getInstance(getInstrumentation())) {
        waitForIdle(TimeUnit.SECONDS.toMillis(5))
        takeScreenshot(file)
    }
    AllureAndroidLifecycle.addAttachment(name = tag, type = "image/png", fileExtension = ".png", file = file)
}

private fun getTmpFile(prefix: String): File {
    return try {
        createTempDir(prefix, null, Environment.getExternalStorageDirectory())
    } catch (e: IOException) {
        val context = ContextHolder.getTargetAppContext()
        val applicationInfo = context.applicationInfo
        File(applicationInfo.dataDir + File.separator + RESULTS_FOLDER, prefix)
    }
}