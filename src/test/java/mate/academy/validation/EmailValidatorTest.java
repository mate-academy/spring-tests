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
    void emailValidator_isValid_Ok() {
        Assertions.assertTrue(emailValidator.isValid("valid@gmail.com", null));
    }

    @Test
    void emailValidator_isValidEmailNull_Ok() {
        Assertions.assertFalse(emailValidator.isValid(null, null));
    }

    @Test
    void emailValidator_isValidEmailNotValid_Ok() {
        Assertions.assertFalse(emailValidator.isValid("notValidGmail", null));
    }
}
