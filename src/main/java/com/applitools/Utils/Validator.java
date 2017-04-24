package com.applitools.Utils;

import com.google.common.base.Strings;

import java.security.InvalidParameterException;

public abstract class Validator {
    public static void areNotAllowedTogether(String p1, String p2, String desc1, String desc2) {
        if (!Strings.isNullOrEmpty(p1) && !Strings.isNullOrEmpty(p2))
            throw new InvalidParameterException(
                    String.format("The paremeters %s and %s are not allowed together", desc1, desc2));
    }

    public static void shouldBeSetTogether(String p1, String p2, String desc1, String desc2) {
        if (!Strings.isNullOrEmpty(p1) || !Strings.isNullOrEmpty(p1))
            throw new InvalidParameterException(
                    String.format("The paremeters %s and %s should be set together", desc1, desc2)
            );
    }

    public static void requiresSecond(String first, String required, String firstDesc, String requiredDesc) {
        if (!Strings.isNullOrEmpty(first) && Strings.isNullOrEmpty(required))
            throw new InvalidParameterException(
                    String.format("The parameter %s requres %s", firstDesc, requiredDesc)
            );
    }

    public static void anyRequired(String p1, String p2, String desc1, String desc2) {
        if (Strings.isNullOrEmpty(p1) && Strings.isNullOrEmpty(p2))
            throw new InvalidParameterException(
                    String.format("At least one of the two parameters is required: %s or %s", desc1, desc2)
            );
    }

    public static void ifFalseThenRequired(boolean p1, String p2, String desc1, String desc2) {
        if (!p1 && Strings.isNullOrEmpty(p2))
            throw new InvalidParameterException(
                    String.format("The parameter %s required when %s is set", desc2, desc1)
            );
    }

    public static void ifFalseThenNotAllowed(boolean p1, String p2, String desc1, String desc2) {
        if (p1 && !Strings.isNullOrEmpty(p2))
            throw new InvalidParameterException(
                    String.format("The parameter %s is not allowed when the parameter %s is not set", desc2, desc1)
            );
    }
}
