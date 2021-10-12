package mate.academy.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmailValidatorTest {
    private static final String EMAIL = "bchupika@mate.academy";
    private EmailValidator emailValidator;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
    }

    @Test
    void isValid_ok() {
        Assertions.assertTrue(emailValidator.isValid(EMAIL, null));
    }

    @Test
    void isValid_notOk() {
        Assertions.assertFalse(emailValidator.isValid("wrongemailcom", null));
    }

}