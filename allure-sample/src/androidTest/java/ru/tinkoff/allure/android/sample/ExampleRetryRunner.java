package ru.tinkoff.allure.android.sample;

import org.junit.runners.model.InitializationError;

import ru.tinkoff.allure.android.RetryRunner;

public class ExampleRetryRunner extends RetryRunner {
    public ExampleRetryRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected void clearAllActivities() {

    }
}