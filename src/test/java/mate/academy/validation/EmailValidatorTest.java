package mate.academy.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmailValidatorTest {
    private static final String VALID_EMAIL = "bob@gmail.com";
    private static final String INVALID_EMAIL = "bob.";
    private EmailValidator emailValidator;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
    }

    @Test
    void emailValid_Ok() {
        boolean isValid = emailValidator.isValid(VALID_EMAIL, null);
        Assertions.assertTrue(isValid);
    }

    @Test
    void emailValid_notOK() {
        boolean isInvalid = emailValidator.isValid(INVALID_EMAIL, null);
        Assertions.assertFalse(isInvalid);
    }
}
