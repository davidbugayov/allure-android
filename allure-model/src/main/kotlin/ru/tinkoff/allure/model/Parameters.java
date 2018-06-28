package ru.tinkoff.allure.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для передачи параметров в шаг при логировании
 *
 * @author sbt-polovkova-aa
 */

public class Parameters {
    private Parameter[] mParameters;

    public Parameters(List<Parameter> parameters) {
        mParameters = new Parameter[parameters.size()];
        parameters.toArray(mParameters);
    }

    public Parameter[] getParameters() {
        return mParameters;
    }

    public static final class Builder {
        private List<ru.tinkoff.allure.model.Parameter> mParameters = new ArrayList<>();

        public Builder addParam(String key, Object... objects) {
            StringBuilder value = new StringBuilder();
            for (Object object : objects) {
                value.append(object.toString()).append("; ");
            }
            mParameters.add(new Parameter(key, value.toString()));
            return this;
        }

        public Builder addParam(String key, Object object) {
            mParameters.add(new Parameter(key, object == null ? "null" : object.toString()));
            return this;
        }

        public Parameters build() {
            return new Parameters(mParameters);
        }
    }
}