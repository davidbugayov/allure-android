package ru.tinkoff.allure.android;

import org.junit.Ignore;
import org.junit.internal.AssumptionViolatedException;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runner.notification.StoppedByUserException;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import java.io.PrintWriter;
import java.io.StringWriter;

import ru.tinkoff.allure.AllureRunListener;
import ru.tinkoff.allure.utils.ExceptionUtilsKt;


public class RetryRunner extends BlockJUnit4ClassRunner {

    /**
     * количество запусков теста
     */
    private final int retryCount = 3;
    /**
     * порог падений, который допускается для прохождения теста
     */
    private int failedAttempts = 0;

    public RetryRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }


    @Override
    public void run(final RunNotifier notifier) {
        Description description = getDescription();
        EachTestNotifier testNotifier = new EachTestNotifier(notifier,
                description);
        Statement statement = classBlock(notifier);
        try {
            statement.evaluate();
        } catch (AssumptionViolatedException e) {
            testNotifier.fireTestIgnored();
        } catch (StoppedByUserException e) {
            throw e;
        } catch (Throwable e) {
            retry(description, testNotifier, statement, e, notifier);
        }

    }

    @Override
    protected void runChild(final FrameworkMethod method, RunNotifier notifier) {
        Description description = describeChild(method);
        if (method.getAnnotation(Ignore.class) != null) {
            notifier.fireTestIgnored(description);
        } else {
            runTestUnit(methodBlock(method), description, notifier);
        }
    }

    /**
     * Runs a {@link Statement} that represents a leaf (aka atomic) test.
     */
    protected final void runTestUnit(Statement statement, Description description,
                                     RunNotifier notifier) {
        EachTestNotifier eachNotifier = new EachTestNotifier(notifier, description);
        eachNotifier.fireTestStarted();
        try {
            statement.evaluate();
        } catch (AssumptionViolatedException e) {
            eachNotifier.addFailedAssumption(e);
        } catch (Throwable e) {
            retry(description, eachNotifier, statement, e, notifier);
        } finally {
            eachNotifier.fireTestFinished();
        }
    }

    public void retry(Description description, EachTestNotifier notifier, Statement statement, Throwable currentThrowable, RunNotifier runNotifier) {
        Throwable caughtThrowable = currentThrowable;
        while (retryCount > failedAttempts) {
            AllureAndroidListener listener = new AllureAndroidListener();
            runNotifier.addListener(listener);
            try {
                listener.testStarted(description);
            } catch (Exception e) {
                System.err.println(description.getDisplayName() +
                        "Failure start test" + ExceptionUtilsKt.getStringTrace(e));
            }
            try {
                statement.evaluate();
                return;
            } catch (Throwable t) {
                caughtThrowable = t;
                try {
                    if (failedAttempts < retryCount - 1) {
                        listener.testFailure(new Failure(description, t));
                    }
                } catch (Exception e) {
                    System.err.println(description.getDisplayName() +
                            "Failure failure test" + ExceptionUtilsKt.getStringTrace(e));
                }
                failedAttempts++;
            } finally {
                try {
                    if (failedAttempts < retryCount - 1) {
                        listener.testFinished(description);
                    }
                } catch (Exception e) {
                    System.err.println(description.getDisplayName() +
                            "Failure finish test" + ExceptionUtilsKt.getStringTrace(e));
                }
                runNotifier.removeListener(listener);
            }
        }
        notifier.addFailure(caughtThrowable);

    }

//    abstract void cleanAllActivities(){
//
//    }
}