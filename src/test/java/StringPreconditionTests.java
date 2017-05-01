import com.applitools.Utils.Validator;
import org.junit.Test;

import java.security.InvalidParameterException;

public class StringPreconditionTests {

    @Test()
    public void testAllValidFlows() {
        Validator.givenString("not empty", "StrParam 1").isSetThen()
                .required("not empty", "StrParam 2")
                .notAllowed("", "StrParam 2")
                .notAllowed(null, "StrParam 2");

        Validator.givenString("", "StrParam 1").isNotSetThen()
                .required("not empty", "StrParam 2")
                .notAllowed("", "StrParam 2")
                .notAllowed(null, "StrParam 2");

        Validator.givenString(null, "StrParam 1").isNotSetThen()
                .required("not empty", "StrParam 2")
                .notAllowed("", "StrParam 2")
                .notAllowed(null, "StrParam 2");

        Validator.givenString(null, "StrParam 1").isSetThen()
                .required("", "StrParam 2")
                .required(null, "StrParam 2")
                .required("not empty", "StrParam 2")
                .notAllowed("not empty", "StrParam 2")
                .notAllowed("", "StrParam 2")
                .notAllowed(null, "StrParam 2");

        Validator.givenString("", "StrParam 1").isSetThen()
                .required("", "StrParam 2")
                .required(null, "StrParam 2")
                .required("not empty", "StrParam 2")
                .notAllowed("not empty", "StrParam 2")
                .notAllowed("", "StrParam 2")
                .notAllowed(null, "StrParam 2");

        Validator.givenString("not empty", "StrParam 1").isNotSetThen()
                .required("", "StrParam 2")
                .required(null, "StrParam 2")
                .required("not empty", "StrParam 2")
                .notAllowed("not empty", "StrParam 2")
                .notAllowed("", "StrParam 2")
                .notAllowed(null, "StrParam 2");
    }

    @Test(expected = InvalidParameterException.class)
    public void testPositiveRequiresEmpty() {
        Validator.givenString("not empty", "StrParam 1").isSetThen()
                .required("", "StrParam 2");
    }

    @Test(expected = InvalidParameterException.class)
    public void testPositiveRequiresNull() {
        Validator.givenString("not empty", "StrParam 1").isSetThen()
                .required("", "StrParam 2");
    }

    @Test(expected = InvalidParameterException.class)
    public void testNegativeEmptyRequiresEmpty() {
        Validator.givenString("", "StrParam 1").isNotSetThen()
                .required("", "StrParam 2");
    }

    @Test(expected = InvalidParameterException.class)
    public void testNegativeEmptyRequiresNull() {
        Validator.givenString("", "StrParam 1").isNotSetThen()
                .required(null, "StrParam 2");
    }

    @Test(expected = InvalidParameterException.class)
    public void testNegativeNullRequiresEmpty() {
        Validator.givenString(null, "StrParam 1").isNotSetThen()
                .required("", "StrParam 2");
    }

    @Test(expected = InvalidParameterException.class)
    public void testNegativeNullRequiresNull() {
        Validator.givenString(null, "StrParam 1").isNotSetThen()
                .required(null, "StrParam 2");
    }

    @Test(expected = InvalidParameterException.class)
    public void testPositiveNotAllowed() {
        Validator.givenString("not empty", "StrParam 1").isSetThen()
                .notAllowed("not empty", "StrParam 2");
    }

    @Test(expected = InvalidParameterException.class)
    public void testNegativeEmptyNotAllowed() {
        Validator.givenString("", "StrParam 1").isNotSetThen()
                .notAllowed("not empty", "StrParam 2");
    }

    @Test(expected = InvalidParameterException.class)
    public void testNegativeNullNotAllowed() {
        Validator.givenString(null, "StrParam 1").isNotSetThen()
                .notAllowed("not empty", "StrParam 2");
    }
}
