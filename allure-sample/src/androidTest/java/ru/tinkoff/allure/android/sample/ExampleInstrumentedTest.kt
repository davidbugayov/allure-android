package ru.tinkoff.allure.android.sample

import android.content.Intent
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule

import org.junit.Assert.fail
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.tinkoff.allure.android.*
import java.util.*

@RunWith(RetryRunner::class)
class ExampleInstrumentedTest {

    @get:Rule
    val activityTestRule = ActivityTestRule<MainActivity>(MainActivity::class.java, true, false)

//    @get:Rule
//    val failshotRule = RetryFailShotRule(1)

    @get:Rule
    val retry = FailshotRule()

    @Test
    fun failedTest() {
        step("Test" , Runnable {
            activityTestRule.launchActivity(Intent(InstrumentationRegistry.getTargetContext(), MainActivity::class.java))
            onView(withId(R.id.hello_text_view)).perform(click())
            if (true) {
                    fail("fail test")

            }
        })
    }
}
