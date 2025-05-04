package mate.academy.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmailValidatorTest {
    private EmailValidator emailValidator;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
    }

    @Test
    void isValid_validEmail_ok() {
        String email = "bob@gmail.com";
        boolean actual = emailValidator.isValid(email, null);
        Assertions.assertTrue(actual, "Method should return true for email " + email);
    }

    @Test
    void isValid_nullValueEmail_notOk() {
        boolean actual = emailValidator.isValid(null, null);
        Assertions.assertFalse(actual, "Method should return false null value");
    }

    @Test
    void isValid_emptyFirstPartEmail_notOk() {
        String email = "@gmail.com";
        boolean actual = emailValidator.isValid(email, null);
        Assertions.assertFalse(actual, "Method should return false for value " + email);
    }

    @Test
    void isValid_withoutSnail_notOk() {
        String email = "bobgmail.com";
        boolean actual = emailValidator.isValid(email, null);
        Assertions.assertFalse(actual, "Method should return false for value " + email);
    }

    @Test
    void isValid_withoutDot_notOk() {
        String email = "bob@gmailcom";
        boolean actual = emailValidator.isValid(email, null);
        Assertions.assertFalse(actual, "Method should return false for value " + email);
    }

    @Test
    void isValid_lastPart_notOk() {
        String email = "bob@gmail.";
        boolean actual = emailValidator.isValid(email, null);
        Assertions.assertFalse(actual, "Method should return false for value " + email);
    }
}
