package ru.tinkoff.allure.android

import ru.tinkoff.allure.AllureCommonLifecycle
import ru.tinkoff.allure.AllureLifecycle
import ru.tinkoff.allure.AllureStorage
import ru.tinkoff.allure.model.*

/**
 * @author b.mukvich on 31.05.2017.
 */
class Step {
    // todo: commonLifecycle works on Android, only 'cause steps don't call writer
    private val lifecycle: AllureLifecycle = AllureCommonLifecycle

    fun stepCompleted() =
            lifecycle.updateStep {
                if (status == null) {
                    if (warnings.isEmpty()) {
                        status = Status.PASSED
                    } else {
                        status = Status.BROKEN
                        statusDetails = StatusDetails.fromString(warnings.toString())
                    }
                }
            }

    fun stepStart(name: String, vararg params: Parameter) {
        val step = StepResult().apply {
            this.name = name
            parameters.addAll(params)
            warnings.clear()
        }
        lifecycle.startStep(step)
    }

    fun stepThrown(t: Throwable) {
        with(lifecycle) {
            updateStep {
                status = Status.FAILED
                statusDetails = StatusDetails.fromThrowable(t)
            }
        }
    }

    fun stepStop() = lifecycle.stopStep()

    companion object {
        @JvmStatic
        fun addWarning(warning: String) {
            AllureStorage.getStep(AllureStorage.getCurrentStep()).warnings.add(warning)
        }
    }
}

/**
 * Метод для вызова шага с скриншотом
 */
fun stepWithScreenShot(description: String, parameters: Parameters.Builder? = null, block: Runnable){
    return stepWithScreenShot(description,parameters) { block.run() }
}

private fun stepWithScreenShot(description: String, parameters: Parameters.Builder? = null, block: () -> Unit) {
    with(Step()) {
        if (parameters != null) {
            stepStart(description, *parameters.build().parameters)
        } else {
            stepStart(description, *emptyArray())
        }
        try {
            block.invoke()
            stepCompleted()
        } catch (t: Throwable) {
            stepThrown(t)
            throw t
        } finally {
            deviceScreenshot("screenshot")
            stepStop()
        }
    }
}

/**
 * Метод для вызова шага без скриншота
 */
fun  step(description: String, parameters: Parameters.Builder? = null, block: Runnable) {
    return step(description,parameters) { block.run() }
}

private fun step(description: String, parameters: Parameters.Builder? = null, block: () -> Unit) {
    with(Step()) {
        if (parameters != null) {
            stepStart(description, *parameters.build().parameters)
        } else {
            stepStart(description, *emptyArray())
        }
        try {
            block.invoke()
            stepCompleted()
        } catch (t: Throwable) {
            stepThrown(t)
            throw t
        } finally {
            stepStop()
        }
    }
}
