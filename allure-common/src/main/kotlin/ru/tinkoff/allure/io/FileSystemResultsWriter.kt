package ru.tinkoff.allure.io

import ru.tinkoff.allure.model.TestResult
import ru.tinkoff.allure.model.TestResultContainer
import ru.tinkoff.allure.serialization.SerializationProcessor
import ru.tinkoff.allure.serialization.gson.GsonSerializationProcessor
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

/**
 * @author Badya on 18.04.2017.
 */
open class FileSystemResultsWriter(val resultsDir: ResultDirFactory = lazy { FileSystemResultsWriter.getDefaultResultsDir().also { it.mkdirs() } },
                                   val serializationProcessor: SerializationProcessor = GsonSerializationProcessor) : AllureResultsWriter {
    companion object {
        @JvmStatic
        fun getDefaultResultsDir() = File(System.getProperty("allure.results.directory", "build/allure-results"))
    }

    override fun write(testResult: TestResult) =
            serializationProcessor.serialize(
                    File(resultsDir.value, generateTestResultName(testResult.uuid)),
                    testResult)


    override fun write(testResultContainer: TestResultContainer) =
            serializationProcessor.serialize(
                    File(resultsDir.value, generateTestResultContainerName(testResultContainer.uuid)),
                    testResultContainer)


    override fun write(dest: String, attachment: InputStream) = write(File(resultsDir.value, dest), attachment)

    private fun write(dest: File, attachment: InputStream) {
        try {
            FileOutputStream(dest).use { output ->
                attachment.use { input ->
                    input.copyTo(output)
                }
            }
        } catch (e: IOException) {
            throw AllureResultsWriteException("Could not write attachment to ${dest.absolutePath}", e)
        }
    }

    override fun copy(src: File, dest: File) {
        try {
            src.copyTo(resultsDir.value.resolve(dest), true)
        } catch (e: IOException) {
            throw AllureResultsWriteException("Could not move attachment from ${src.absolutePath} to ${dest.absolutePath}", e)
        }
    }

    override fun move(src: File, dest: File) {
        copy(src, dest)
        src.delete()
    }

}

class AllureResultsWriteException(message: String?, cause: Throwable) : RuntimeException(message, cause)
