package mate.academy.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmailValidatorTest {
    private static final String USER_EMAIL_VALID = "bob@i.ua";
    private static final String USER_EMAIL_INVALID = "bob$$i$$ua";
    private EmailValidator emailValidator;

    @BeforeEach
    public void setUp() {
        emailValidator = new EmailValidator();
    }

    @Test
    public void isValid_ok() {
        String validEmail = USER_EMAIL_VALID;
        Assertions.assertTrue(emailValidator.isValid(validEmail, null));
    }

    @Test
    public void isValid_notOk() {
        String invalidEmail = USER_EMAIL_INVALID;
        Assertions.assertFalse(emailValidator.isValid(invalidEmail, null));
    }
}
