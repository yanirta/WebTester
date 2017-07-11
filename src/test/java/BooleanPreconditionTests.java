import com.applitools.Utils.Validator;
import org.junit.Test;

import java.security.InvalidParameterException;

public class BooleanPreconditionTests {
    @Test()
    public void testAllValidFlows() {
        Validator.given(true, "BoolParam 1").trueThen()
                .required("not empty", "STRParam 2")
                .notAllowed(null, "StrParam 2")
                .notAllowed("", "StrParam2");

        Validator.given(false, "Boolean parameter").falseThen()
                .required("not empty", "STRParam 2")
                .notAllowed(null, "StrParam 2")
                .notAllowed("", "StrParam2");

        Validator.given(false, "BoolParam 1").trueThen()
                .required(null, "STRParam 2")
                .required("", "STRParam 2")
                .required("not empty", "STRParam 2")
                .notAllowed(null, "StrParam 2")
                .notAllowed("", "StrParam2")
                .notAllowed("not empty", "STRParam 2");

        Validator.given(true, "BoolParam 1").falseThen()
                .required(null, "STRParam 2")
                .required("", "STRParam 2")
                .required("not empty", "STRParam 2")
                .notAllowed(null, "StrParam 2")
                .notAllowed("", "StrParam2")
                .notAllowed("not empty", "STRParam 2");
    }

    @Test(expected = InvalidParameterException.class)
    public void testPositiveRequiredNull() {
        Validator.given(true, "BoolParam 1").trueThen()
                .required(null, "STRParam 2");
    }

    @Test(expected = InvalidParameterException.class)
    public void testPositiveRequiredEmpty() {
        Validator.given(true, "BoolParam 1").trueThen()
                .required("", "STRParam 2");
    }

    @Test(expected = InvalidParameterException.class)
    public void testNegativeRequiredNull() {
        Validator.given(false, "BoolParam 1").falseThen()
                .required(null, "STRParam 2");
    }

    @Test(expected = InvalidParameterException.class)
    public void testNegativeRequiredEmpty() {
        Validator.given(false, "BoolParam 1").falseThen()
                .required("", "STRParam 2");
    }

    @Test(expected = InvalidParameterException.class)
    public void testPositiveNotAllowed() {
        Validator.given(true, "BoolParam 1").trueThen()
                .notAllowed("not empty", "STRParam 2");
    }

    @Test(expected = InvalidParameterException.class)
    public void testNegativeNotAllowed() {
        Validator.given(false, "BoolParam 1").falseThen()
                .notAllowed("not empty", "STRParam 2");
    }
}
