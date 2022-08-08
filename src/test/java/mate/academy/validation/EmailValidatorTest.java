package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmailValidatorTest {
    private EmailValidator emailValidator;
    private final String emailValid = "denys12@gmail.com";
    private final String emailIsNotValid = "gmail.com";

    @BeforeEach
    void setUp() {
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
