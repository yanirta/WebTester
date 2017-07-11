import com.applitools.Utils.Validator;
import org.junit.Test;

import java.security.InvalidParameterException;

public class StringPreconditionTests {

    @Test()
    public void testAllValidFlows() {
        Validator.given("not empty", "StrParam 1").isSetThen()
                .required("not empty", "StrParam 2")
                .notAllowed("", "StrParam 2")
                .notAllowed(null, "StrParam 2");

        Validator.given("", "StrParam 1").isNotSetThen()
                .required("not empty", "StrParam 2")
                .notAllowed("", "StrParam 2")
                .notAllowed(null, "StrParam 2");

        Validator.given(null, "StrParam 1").isNotSetThen()
                .required("not empty", "StrParam 2")
                .notAllowed("", "StrParam 2")
                .notAllowed(null, "StrParam 2");

        Validator.given(null, "StrParam 1").isSetThen()
                .required("", "StrParam 2")
                .required(null, "StrParam 2")
                .required("not empty", "StrParam 2")
                .notAllowed("not empty", "StrParam 2")
                .notAllowed("", "StrParam 2")
                .notAllowed(null, "StrParam 2");

        Validator.given("", "StrParam 1").isSetThen()
                .required("", "StrParam 2")
                .required(null, "StrParam 2")
                .required("not empty", "StrParam 2")
                .notAllowed("not empty", "StrParam 2")
                .notAllowed("", "StrParam 2")
                .notAllowed(null, "StrParam 2");

        Validator.given("not empty", "StrParam 1").isNotSetThen()
                .required("", "StrParam 2")
                .required(null, "StrParam 2")
                .required("not empty", "StrParam 2")
                .notAllowed("not empty", "StrParam 2")
                .notAllowed("", "StrParam 2")
                .notAllowed(null, "StrParam 2");
    }

    @Test(expected = InvalidParameterException.class)
    public void testPositiveRequiresEmpty() {
        Validator.given("not empty", "StrParam 1").isSetThen()
                .required("", "StrParam 2");
    }

    @Test(expected = InvalidParameterException.class)
    public void testPositiveRequiresNull() {
        Validator.given("not empty", "StrParam 1").isSetThen()
                .required("", "StrParam 2");
    }

    @Test(expected = InvalidParameterException.class)
    public void testNegativeEmptyRequiresEmpty() {
        Validator.given("", "StrParam 1").isNotSetThen()
                .required("", "StrParam 2");
    }

    @Test(expected = InvalidParameterException.class)
    public void testNegativeEmptyRequiresNull() {
        Validator.given("", "StrParam 1").isNotSetThen()
                .required(null, "StrParam 2");
    }

    @Test(expected = InvalidParameterException.class)
    public void testNegativeNullRequiresEmpty() {
        Validator.given(null, "StrParam 1").isNotSetThen()
                .required("", "StrParam 2");
    }

    @Test(expected = InvalidParameterException.class)
    public void testNegativeNullRequiresNull() {
        Validator.given(null, "StrParam 1").isNotSetThen()
                .required(null, "StrParam 2");
    }

    @Test(expected = InvalidParameterException.class)
    public void testPositiveNotAllowed() {
        Validator.given("not empty", "StrParam 1").isSetThen()
                .notAllowed("not empty", "StrParam 2");
    }

    @Test(expected = InvalidParameterException.class)
    public void testNegativeEmptyNotAllowed() {
        Validator.given("", "StrParam 1").isNotSetThen()
                .notAllowed("not empty", "StrParam 2");
    }

    @Test(expected = InvalidParameterException.class)
    public void testNegativeNullNotAllowed() {
        Validator.given(null, "StrParam 1").isNotSetThen()
                .notAllowed("not empty", "StrParam 2");
    }
}
