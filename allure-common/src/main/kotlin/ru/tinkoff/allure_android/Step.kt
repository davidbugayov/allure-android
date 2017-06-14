package ru.tinkoff.allure_android

import ru.tinkoff.allure_android.model.Parameter
import ru.tinkoff.allure_android.model.Status
import ru.tinkoff.allure_android.model.StatusDetails
import ru.tinkoff.allure_android.model.StepResult

/**
 * @author Badya on 31.05.2017.
 */

class Step {
    // todo: commonLifecycle works on Android, only 'cause steps don't call writer
    val lifecycle: AllureLifecycle = AllureCommonLifecycle

    companion object {
        @JvmStatic
        inline fun <T> step(description: String, vararg params: Any = emptyArray<Any>(), block: () -> T): T {
            with(Step()) {
                val result: T
                stepStart(description, params)
                try {
                    result = block()
                    stepCompleted()
                } catch (t: Throwable) {
                    stepThrown(t)
                    throw t
                } finally {
                    stepStop()
                }
                return result
            }
        }
    }

    fun stepCompleted() =
            lifecycle.updateStep { status = Status.PASSED }

    fun stepStart(name: String, vararg params: Any) {
        fun createParam(obj: Any): Parameter {
            return Parameter(value = obj.toString())
        }

        val step = StepResult().apply {
            this.name = name
            parameters.addAll(params.map { createParam(it) })
        }
        lifecycle.startStep(step)
    }

    fun stepThrown(t: Throwable) {
        with(lifecycle) {
            updateStep {
                status = Status.fromThrowable(t)
                statusDetails = StatusDetails.fromThrowable(t)
            }
            updateTestCase {
                status = Status.fromThrowable(t)
            }
        }
    }

    fun stepStop() = lifecycle.stopStep()
}
