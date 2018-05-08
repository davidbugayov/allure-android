package ru.tinkoff.allure.android;

import org.junit.Ignore;
import org.junit.internal.AssumptionViolatedException;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runner.notification.StoppedByUserException;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;


public class RetryRunner extends BlockJUnit4ClassRunner {

    private final int retryCount = 3;
    private int failedAttempts = 0;

    public RetryRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }


    @Override
    public void run(final RunNotifier notifier) {
        EachTestNotifier testNotifier = new EachTestNotifier(notifier,
                getDescription());
        Statement statement = classBlock(notifier);
        AllureAndroidListener listener = new AllureAndroidListener();

        try {
            notifier.addListener(listener);
            listener.testRunStarted(getDescription());
            statement.evaluate();
        } catch (AssumptionViolatedException e) {
            testNotifier.fireTestIgnored();
        } catch (StoppedByUserException e) {
            throw e;
        } catch (Throwable e) {
            try {
                listener.testFailure(new Failure(getDescription(), e));
                notifier.removeListener(listener);
            } catch (Exception e1) {
                e1.printStackTrace();
            }

           // retry(notifier, testNotifier, statement, e, getDescription());
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
            retry(notifier, eachNotifier, statement, e, description);
        } finally {
            eachNotifier.fireTestFinished();
        }
    }

    public void retry(RunNotifier runNotifier, EachTestNotifier notifier, Statement statement, Throwable currentThrowable, Description des) {
        Throwable caughtThrowable = currentThrowable;


        while (retryCount > failedAttempts) {
            AllureAndroidListener listener = new AllureAndroidListener();

            try {
                runNotifier.addListener(listener);
                listener.testStarted(des);
                statement.evaluate();
                return;
            } catch (Throwable t) {
                failedAttempts++;
                caughtThrowable = t;
                try {
                    listener.testFailure(new Failure(des, t));
                    runNotifier.removeListener(listener);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }


            }
        }
        notifier.addFailure(caughtThrowable);
    }
}