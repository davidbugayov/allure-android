package ru.tinkoff.allure.android

import android.support.test.internal.runner.listener.InstrumentationRunListener
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runner.notification.Failure
import org.junit.runner.notification.RunNotifier
import org.junit.runners.model.Statement
import ru.tinkoff.allure.AllureRunListener

@Suppress("unused")
class RetryFailShotRule(private val count: Int) : TestRule {


    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                // alure.testRunFinished()
                for (i in 1..count) {
                    val notifier =  RunNotifier();
                    //val alure = AllureAndroidListener()
                  //  notifier.addListener(alure)

                 //   alure.testRunStarted(description);
                   // alure.testRunStarted(description);
                   // alure.testStarted(description)
                    try {
                        base.evaluate()
                    } catch (t: Throwable) {
                        failshot()
                     //   alure.testRunStarted(description);
                        val failure = Failure(description,t)
                        //val uuid = "${failure.description.className}#${failure.description.methodName}"
                     //   alure.testFailure(failure)
                     //   alure.testFinished(description)
                        //если был експшен то выбрасываем тест
                        if (i == count)
                            throw t
                    }
                  //  alure.testRunFinished()
                }
            }
        }
    }

    private fun failshot() {
        deviceScreenshot("failshot")
    }
}