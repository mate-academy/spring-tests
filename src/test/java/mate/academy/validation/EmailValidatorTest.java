package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class EmailValidatorTest {
    private static final String INVALID_EMAIL_ONE_DOMAIN = "test@a";
    private static final String INVALID_EMAIL_UNFINISHED = "test@a.";
    private static final String VALID_EMAIL = "test@gmail.com";
    private static EmailValidator validator;

    @BeforeAll
    static void beforeAll() {
        validator = new EmailValidator();
    }

    @Test
    void isValid_nullValue_notOk() {
        assertFalse(validator.isValid(null, null),
                "Validator must return false in case passed through args email is null");
    }

    @Test
    void isValid_invalidEmailDomain_notOk() {
        assertFalse(validator.isValid(INVALID_EMAIL_ONE_DOMAIN, null),
                "Expected email with invalid domain to return false");
        assertFalse(validator.isValid(INVALID_EMAIL_UNFINISHED, null),
                "Expected email with unfinished domain to return false");
    }

    @Test
    void isValid_validEmail_ok() {
        assertTrue(validator.isValid(VALID_EMAIL, null),
                "Expected for valid email return true");
    }
}
