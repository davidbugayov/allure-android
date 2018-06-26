package ru.tinkoff.allure.android

import android.os.Environment
import java.io.File
import java.io.IOException

/**
 * Returns a file with defined prefix and generated name.
 *
 * @param prefix
 */
fun getTmpFile(prefix: String): File {
    return try {
        createTempDir(prefix, null, Environment.getExternalStorageDirectory())
    } catch (e: IOException) {
        val context = ContextHolder.getTargetAppContext()
        val applicationInfo = context.applicationInfo
        File(applicationInfo.dataDir + File.separator + Constants.RESULTS_FOLDER, prefix)
    }
}