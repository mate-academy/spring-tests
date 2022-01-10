package mate.academy.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EmailValidatorTest {
    private EmailValidator emailValidator;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
    }

    @Test
    void isValid_True() {
        assertTrue(emailValidator.isValid("maksym@gmail.com", null));
        assertTrue(emailValidator.isValid("maksym@gmail.co", null));
        assertTrue(emailValidator.isValid("maksym@mate.academy", null));
    }

    @Test
    void isValid_False() {
        assertFalse(emailValidator.isValid("maksym@.com", null));
        assertFalse(emailValidator.isValid("maksym@gmail1", null));
        assertFalse(emailValidator.isValid("123", null));
        assertFalse(emailValidator.isValid(null, null));
    }
}
