package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class EmailValidatorTest {
    private static EmailValidator emailValidator;
    private final String emailValid = "denys12@gmail.com";
    private final String emailIsNotValid = "gmail.com";

    @BeforeAll
    static void beforeAll() {
        emailValidator = new EmailValidator();
    }

    @Test
    void isValid_Email_Ok() {
        boolean valid = emailValidator.isValid(emailValid, null);
        assertTrue(valid);
    }

    @Test
    void isValid_notPresentEmail_notOk() {
        boolean isNotValid = emailValidator.isValid(emailIsNotValid, null);
        boolean isNotValidEmailNull = emailValidator.isValid(null, null);
        assertFalse(isNotValid);
        assertFalse(isNotValidEmailNull);
    }
}
