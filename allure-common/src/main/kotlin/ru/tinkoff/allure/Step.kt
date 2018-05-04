package ru.tinkoff.allure

import ru.tinkoff.allure.model.*

/**
 * @author b.mukvich on 31.05.2017.
 */

inline fun <T : Any?> step(description: String, vararg params: Parameter, block: () -> T): T {
    with(Step()) {
        val result: T
        stepStart(description, *params)
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
                status = Status.fromThrowable(t)
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
