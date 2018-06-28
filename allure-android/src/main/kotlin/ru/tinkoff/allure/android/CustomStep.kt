package ru.tinkoff.allure.android

import ru.tinkoff.allure.Step
import ru.tinkoff.allure.model.Parameters

/**
 * Результат шага при тестировании с скриншотом и безскриншота
 */
inline fun <T : Any?> stepWithResult(description: String, block: () -> T): T {
    with(Step()) {
        val result: T
        stepStart(description, *emptyArray())
        try {
            result = block()
            stepCompleted()
        } catch (t: Throwable) {
            stepThrown(t)
            throw t
        } finally {
            deviceScreenshot("screenshot")
            stepStop()
        }
        return result
    }
}

fun step(description: String, block: Runnable): Unit {
    with(Step()) {
        stepStart(description, *emptyArray())
        try {
            block.run()
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

fun step(description: String, parameters: Parameters.Builder, block: Runnable): Unit {
    with(Step()) {
        stepStart(description, *parameters.build().parameters)
        try {
            block.run()
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

fun stepWithoutScreenshot(description: String, block: Runnable): Unit {
    with(Step()) {
        stepStart(description, *emptyArray())
        try {
            block.run()
            stepCompleted()
        } catch (t: Throwable) {
            stepThrown(t)
            throw t
        } finally {
            stepStop()
        }
    }
}

fun stepWithoutScreenshot(description: String, parameters: Parameters.Builder, block: Runnable): Unit {
    with(Step()) {
        stepStart(description, *parameters.build().parameters)
        try {
            block.run()
            stepCompleted()
        } catch (t: Throwable) {
            stepThrown(t)
            throw t
        } finally {
            stepStop()
        }
    }
}
