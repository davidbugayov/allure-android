package ru.tinkoff.allure.android;

import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.test.runner.AndroidJUnitRunner;

/**
 * Patch multi dex app for older APIs and keeps reference to
 * target context.
 *
 * @author Shackih Pavel
 */
@SuppressWarnings("unused")
public class MultiDexAllureAndroidRunner extends AndroidJUnitRunner {

    private static final String LISTENER_KEY = "listener";
    private static final String COMMA = ",";

    @Override
    public void onCreate(Bundle arguments) {
        MultiDex.installInstrumentation(getContext(), getTargetContext());
        ContextHolder.setTargetAppContext(getTargetContext());
        super.onCreate(arguments);
    }
}