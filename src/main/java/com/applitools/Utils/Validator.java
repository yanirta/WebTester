package com.applitools.Utils;

import com.google.common.base.Strings;

import java.security.InvalidParameterException;

public abstract class Validator {

    public static class BooleanPrecondition {
        private final String boolName_;
        private final boolean bool_;
        private boolean expected_ = true;

        public BooleanPrecondition(boolean param, String paramName) {
            bool_ = param;
            boolName_ = paramName;
        }

        public BooleanPrecondition(boolean param) {
            this(param, "boolean");
        }

        public BooleanPrecondition falseThen() {
            expected_ = false;
            return this;
        }

        public BooleanPrecondition trueThen() {
            expected_ = true;
            return this;
        }

        public BooleanPrecondition notAllowed(String param, String paramName) {
            return assertOn(param, paramName, false);
        }

        public BooleanPrecondition notAllowed(String param) {
            return notAllowed(param, "String");
        }

        public BooleanPrecondition required(String param, String paramName) {
            return assertOn(param, paramName, true);
        }

        private BooleanPrecondition assertOn(String param, String paramName, boolean condition) {
            if (bool_ == expected_ && Strings.isNullOrEmpty(param) == condition)
                throw new InvalidParameterException(
                        String.format("%s parameter is %s if %s is %s",
                                paramName,
                                condition ? "required" : "not allowed",
                                boolName_,
                                bool_));
            return this;
        }
    }

    public static class StringPrecondition {
        private final String paramName_;
        private final String param_;
        private boolean isNotNullOrEmpty_;

        public StringPrecondition(String param, String paramName) {
            param_ = param;
            paramName_ = paramName;
        }

        public StringPrecondition isSetThen() {
            isNotNullOrEmpty_ = true;
            return this;
        }

        public StringPrecondition isNotSetThen() {
            isNotNullOrEmpty_ = false;
            return this;
        }

        public StringPrecondition required() {
            if (Strings.isNullOrEmpty(param_))
                throw new InvalidParameterException(
                        String.format("Parameter %s is %s",
                                paramName_,
                                "required"
                        ));

            return this;
        }

        public StringPrecondition required(String param, String paramName) {
            return assertOnParam(param, paramName, true);
        }

        public StringPrecondition notAllowed(String param, String paramName) {
            return assertOnParam(param, paramName, false);
        }

        private StringPrecondition assertOnParam(String param, String paramName, boolean isNullOrEmpty) {
            if (!Strings.isNullOrEmpty(param_) == isNotNullOrEmpty_ && Strings.isNullOrEmpty(param) == isNullOrEmpty)
                throw new InvalidParameterException(
                        String.format("Parameter %s is %s, when %s is %s",
                                paramName,
                                isNullOrEmpty ? "required" : "not allowed",
                                paramName_,
                                isNotNullOrEmpty_ ? "set" : "not set"
                        ));
            return this;
        }
    }

    public static BooleanPrecondition given(boolean param, String paramName) {
        return new BooleanPrecondition(param, paramName);
    }

    public static StringPrecondition given(String param, String paramName) {
        return new StringPrecondition(param, paramName);
    }
}
