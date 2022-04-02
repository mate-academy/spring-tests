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
        assertTrue(emailValidator.isValid("alise@i.ua", null));
        assertTrue(emailValidator.isValid("alice@g.co", null));
        assertTrue(emailValidator.isValid("alice@mate.academy", null));
    }

    @Test
    void isValid_False() {
        assertFalse(emailValidator.isValid("alice@.ua", null));
        assertFalse(emailValidator.isValid("alice@g.", null));
        assertFalse(emailValidator.isValid("alice", null));
        assertFalse(emailValidator.isValid("alice#i.ua", null));
        assertFalse(emailValidator.isValid(null, null));
    }
}